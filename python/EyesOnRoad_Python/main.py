"""EyesOnRoad - protótipo de monitoramento de sinais de sonolência.

A webcam é processada localmente. O vídeo não é gravado nem enviado para a internet.
Os registros de eventos são salvos em dados/alertas.csv.
"""
from __future__ import annotations

import argparse
import csv
import math
import sys
from collections import deque
import threading
import time
import urllib.request
from datetime import datetime
from pathlib import Path

try:
    import cv2
    import mediapipe as mp
    import numpy as np
    from mediapipe.tasks import python
    from mediapipe.tasks.python import vision
except ImportError as exc:
    print("\nDependência ausente:", exc)
    print("Execute primeiro configurar.bat ou instale requirements.txt.")
    raise SystemExit(1)

BASE_DIR = Path(__file__).resolve().parent
MODEL_DIR = BASE_DIR / "modelos"
MODEL_PATH = MODEL_DIR / "face_landmarker.task"
LOG_DIR = BASE_DIR / "dados"
LOG_PATH = LOG_DIR / "alertas.csv"
ALARM_WAV = BASE_DIR / "sons" / "alarme_forte.wav"
MODEL_URL = (
    "https://storage.googleapis.com/mediapipe-models/face_landmarker/"
    "face_landmarker/float16/1/face_landmarker.task"
)

# Índices faciais do modelo Face Landmarker do MediaPipe.
LEFT_EYE = [33, 160, 158, 133, 153, 144]
RIGHT_EYE = [362, 385, 387, 263, 373, 380]
MOUTH_TOP_BOTTOM = (13, 14)
MOUTH_LEFT_RIGHT = (78, 308)

# Valores iniciais de referência. Eles podem precisar de ajustes conforme câmera,
# iluminação, posição do rosto e características de cada pessoa.
EYE_CLOSED_THRESHOLD = 0.20
MOUTH_OPEN_THRESHOLD = 0.35
EYES_CLOSED_SECONDS = 2.5

# O programa calibra a inclinação lateral nos primeiros 2 segundos com o rosto
# visível. Depois, só considera uma inclinação lateral acentuada e persistente.
# O alerta é único por episódio e só reativa após a cabeça voltar à posição neutra.
HEAD_ROLL_CALIBRATION_SECONDS = 2.0
HEAD_ROLL_SMOOTHING_FRAMES = 9
HEAD_SIDE_TILT_DEGREES = 35.0       # início da condição de inclinação lateral
HEAD_SIDE_HYSTERESIS_DEGREES = 25.0  # evita oscilações junto ao limite de início
HEAD_SIDE_NEUTRAL_DEGREES = 18.0    # limite para considerar a cabeça de volta ao neutro
HEAD_SIDE_HOLD_SECONDS = 1.0        # movimento breve não dispara o alerta
HEAD_SIDE_REARM_SECONDS = 1.0       # tempo neutro necessário para rearmar
YAWN_COOLDOWN_SECONDS = 4.0

# Cores BGR para o OpenCV.
NAVY = (79, 11, 2)
CYAN = (174, 174, 0)
WHITE = (255, 255, 255)
GREEN = (65, 180, 80)
YELLOW = (0, 210, 255)
RED = (40, 40, 235)
GRAY = (180, 180, 180)


def ensure_model() -> None:
    """Baixa o modelo oficial na primeira execução, se ele ainda não existir."""
    MODEL_DIR.mkdir(parents=True, exist_ok=True)
    if MODEL_PATH.exists() and MODEL_PATH.stat().st_size > 1_000_000:
        return

    temporary_path = MODEL_PATH.with_suffix('.download')
    print("Baixando o modelo de detecção facial do MediaPipe (necessário só na primeira vez)...")
    try:
        request = urllib.request.Request(
            MODEL_URL,
            headers={"User-Agent": "EyesOnRoad/1.0"},
        )
        with urllib.request.urlopen(request, timeout=45) as response, temporary_path.open('wb') as out:
            while True:
                chunk = response.read(1024 * 1024)
                if not chunk:
                    break
                out.write(chunk)
        if temporary_path.stat().st_size < 1_000_000:
            raise RuntimeError("O arquivo recebido parece incompleto.")
        temporary_path.replace(MODEL_PATH)
        print("Modelo salvo em:", MODEL_PATH)
    except Exception as exc:
        try:
            temporary_path.unlink(missing_ok=True)
        except OSError:
            pass
        raise RuntimeError(
            "Não foi possível baixar o modelo do MediaPipe. Verifique a conexão com a internet "
            "e execute novamente."
        ) from exc


def initialize_log() -> None:
    LOG_DIR.mkdir(parents=True, exist_ok=True)
    if not LOG_PATH.exists():
        with LOG_PATH.open('w', newline='', encoding='utf-8-sig') as file:
            writer = csv.writer(file, delimiter=';')
            writer.writerow(["data_hora", "evento", "ear_medio", "abertura_boca"])


def log_event(event: str, ear: float | None, mouth_ratio: float | None) -> None:
    initialize_log()
    timestamp = datetime.now().astimezone().isoformat(timespec="seconds")
    with LOG_PATH.open('a', newline='', encoding='utf-8-sig') as file:
        writer = csv.writer(file, delimiter=';')
        writer.writerow([
            timestamp,
            event,
            f"{ear:.3f}" if ear is not None else "",
            f"{mouth_ratio:.3f}" if mouth_ratio is not None else "",
        ])


def play_alarm() -> None:
    """Toca o alarme WAV pulsante em segundo plano, sem travar a câmera.

    O arquivo foi normalizado para um sinal forte, com tons alternados e harmônicos.
    O volume final ainda depende das configurações do Windows e dos alto-falantes.
    """
    if sys.platform.startswith("win"):
        try:
            import winsound
            if ALARM_WAV.exists():
                winsound.PlaySound(
                    str(ALARM_WAV),
                    winsound.SND_FILENAME | winsound.SND_NODEFAULT,
                )
                return
        except Exception as exc:
            print(f"Não foi possível reproduzir o arquivo de alarme: {exc}")

        # Alternativa caso o WAV esteja ausente ou não possa ser reproduzido.
        try:
            import winsound
            for frequency, duration_ms in ((900, 450), (1500, 550), (900, 450), (1500, 550)):
                winsound.Beep(frequency, duration_ms)
                time.sleep(0.08)
            return
        except Exception:
            pass

    # Alternativa simples para sistemas sem winsound. Depende do terminal.
    for _ in range(4):
        print("\a", end="", flush=True)
        time.sleep(0.15)


def distance(p1: tuple[int, int], p2: tuple[int, int]) -> float:
    return math.hypot(p1[0] - p2[0], p1[1] - p2[1])


def landmark_points(face_landmarks, indexes: list[int], width: int, height: int) -> list[tuple[int, int]]:
    points: list[tuple[int, int]] = []
    for index in indexes:
        landmark = face_landmarks[index]
        x = max(0, min(width - 1, int(landmark.x * width)))
        y = max(0, min(height - 1, int(landmark.y * height)))
        points.append((x, y))
    return points


def eye_aspect_ratio(points: list[tuple[int, int]]) -> float:
    """Calcula EAR: valor baixo geralmente corresponde a olhos mais fechados."""
    if len(points) != 6:
        return 0.0
    horizontal = distance(points[0], points[3])
    if horizontal <= 1e-6:
        return 0.0
    vertical_1 = distance(points[1], points[5])
    vertical_2 = distance(points[2], points[4])
    return (vertical_1 + vertical_2) / (2.0 * horizontal)


def mouth_open_ratio(face_landmarks, width: int, height: int) -> float:
    top, bottom = landmark_points(face_landmarks, list(MOUTH_TOP_BOTTOM), width, height)
    left, right = landmark_points(face_landmarks, list(MOUTH_LEFT_RIGHT), width, height)
    mouth_width = distance(left, right)
    if mouth_width <= 1e-6:
        return 0.0
    return distance(top, bottom) / mouth_width


def estimate_head_pose(face_landmarks, width: int, height: int) -> tuple[float, float] | None:
    """Estima pitch (cabeça para cima/baixo) e roll (inclinação lateral).

    O MediaPipe fornece os pontos faciais; o OpenCV usa esses pontos para estimar
    a orientação aproximada da cabeça com solvePnP. Os ângulos são heurísticos e
    podem variar com câmera, iluminação, distância e características individuais.
    """
    # Pontos: ponta do nariz, queixo, cantos externos dos olhos e cantos da boca.
    landmark_ids = [1, 152, 33, 263, 61, 291]
    image_points = np.array([
        (face_landmarks[i].x * width, face_landmarks[i].y * height)
        for i in landmark_ids
    ], dtype=np.float64)

    # Modelo 3D aproximado usado somente para estimar a orientação facial.
    model_points = np.array([
        (0.0, 0.0, 0.0),
        (0.0, 330.0, -65.0),
        (-225.0, -170.0, -135.0),
        (225.0, -170.0, -135.0),
        (-150.0, 150.0, -125.0),
        (150.0, 150.0, -125.0),
    ], dtype=np.float64)

    focal_length = float(width)
    camera_matrix = np.array([
        [focal_length, 0.0, width / 2.0],
        [0.0, focal_length, height / 2.0],
        [0.0, 0.0, 1.0],
    ], dtype=np.float64)
    distortion = np.zeros((4, 1), dtype=np.float64)

    success, rotation_vector, _ = cv2.solvePnP(
        model_points, image_points, camera_matrix, distortion,
        flags=cv2.SOLVEPNP_ITERATIVE,
    )
    if not success:
        return None

    rotation_matrix, _ = cv2.Rodrigues(rotation_vector)
    euler_angles = cv2.RQDecomp3x3(rotation_matrix)[0]
    pitch = float(euler_angles[0])
    roll = float(euler_angles[2])
    return pitch, roll


def draw_text(frame, text: str, position: tuple[int, int], color=WHITE, scale=0.65, thickness=2) -> None:
    cv2.putText(frame, text, position, cv2.FONT_HERSHEY_SIMPLEX, scale, (0, 0, 0), thickness + 2, cv2.LINE_AA)
    cv2.putText(frame, text, position, cv2.FONT_HERSHEY_SIMPLEX, scale, color, thickness, cv2.LINE_AA)


def create_landmarker():
    base_options = python.BaseOptions(model_asset_path=str(MODEL_PATH))
    options = vision.FaceLandmarkerOptions(
        base_options=base_options,
        running_mode=vision.RunningMode.VIDEO,
        num_faces=1,
        min_face_detection_confidence=0.5,
        min_face_presence_confidence=0.5,
        min_tracking_confidence=0.5,
        output_face_blendshapes=False,
        output_facial_transformation_matrixes=False,
    )
    return vision.FaceLandmarker.create_from_options(options)


def run(camera_index: int) -> None:
    ensure_model()
    initialize_log()

    # CAP_DSHOW costuma inicializar webcams de forma mais previsível no Windows.
    if sys.platform.startswith("win"):
        camera = cv2.VideoCapture(camera_index, cv2.CAP_DSHOW)
    else:
        camera = cv2.VideoCapture(camera_index)

    if not camera.isOpened():
        camera.release()
        raise RuntimeError(
            f"Não foi possível abrir a câmera de índice {camera_index}. "
            "Feche outros programas que estejam usando a webcam e tente --camera 1."
        )

    camera.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
    camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)

    eye_closed_since: float | None = None
    eye_alert_sent = False
    # Filtro e calibração da inclinação lateral da cabeça.
    roll_history: deque[float] = deque(maxlen=HEAD_ROLL_SMOOTHING_FRAMES)
    roll_calibration_values: list[float] = []
    roll_calibration_started: float | None = None
    head_roll_baseline: float | None = None
    side_tilt_since: float | None = None
    head_side_alert_sent = False
    side_returned_since: float | None = None
    head_pitch: float | None = None
    head_roll: float | None = None
    relative_head_roll: float | None = None
    mouth_was_open = False
    last_yawn_log = 0.0
    total_alerts = 0
    start_time = time.perf_counter()
    last_timestamp_ms = 0

    print("EyesOnRoad iniciado. Pressione Q ou ESC na janela do vídeo para encerrar.")
    print("Alarme dos olhos: fechamento continuo por 2.5 segundos.")
    print("Alarme lateral: inclinacao acentuada por 1 segundo; sem alerta por inclinacao para frente.")
    print("Mantenha a cabeca reta durante os primeiros 2 segundos para calibracao.")
    print("Registros de eventos:", LOG_PATH)

    try:
        with create_landmarker() as landmarker:
            while True:
                ok, frame = camera.read()
                if not ok or frame is None:
                    print("Não foi possível ler um quadro da câmera.")
                    break

                frame = cv2.flip(frame, 1)  # efeito espelho para facilitar o uso
                height, width = frame.shape[:2]
                rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                mp_image = mp.Image(image_format=mp.ImageFormat.SRGB, data=np.ascontiguousarray(rgb))
                timestamp_ms = max(last_timestamp_ms + 1, int((time.perf_counter() - start_time) * 1000))
                last_timestamp_ms = timestamp_ms
                result = landmarker.detect_for_video(mp_image, timestamp_ms)
                now = time.monotonic()

                status = "Aguardando detecção do rosto"
                status_color = YELLOW
                ear_value: float | None = None
                mouth_value: float | None = None
                head_pitch = None
                head_roll = None
                relative_head_roll = None
                closed_seconds = 0.0
                head_pose_seconds = 0.0

                if result.face_landmarks:
                    face = result.face_landmarks[0]
                    pose = estimate_head_pose(face, width, height)
                    if pose is not None:
                        head_pitch, head_roll = pose
                        roll_history.append(head_roll)
                        smoothed_roll = float(np.median(np.asarray(roll_history, dtype=np.float64)))

                        # Calibração inicial: usa a mediana dos ângulos enquanto a pessoa
                        # mantém a cabeça na posição normal. A decisão usa a mudança
                        # relativa à postura inicial, e não o ângulo absoluto da câmera.
                        if head_roll_baseline is None:
                            if roll_calibration_started is None:
                                roll_calibration_started = now
                            roll_calibration_values.append(smoothed_roll)
                            if (now - roll_calibration_started >= HEAD_ROLL_CALIBRATION_SECONDS
                                    and len(roll_calibration_values) >= 15):
                                head_roll_baseline = float(np.median(
                                    np.asarray(roll_calibration_values, dtype=np.float64)
                                ))
                                print(f"Calibracao lateral concluida (referencia: {head_roll_baseline:.1f} graus).")
                        else:
                            relative_head_roll = smoothed_roll - head_roll_baseline

                    left_eye_points = landmark_points(face, LEFT_EYE, width, height)
                    right_eye_points = landmark_points(face, RIGHT_EYE, width, height)
                    ear_left = eye_aspect_ratio(left_eye_points)
                    ear_right = eye_aspect_ratio(right_eye_points)
                    ear_value = (ear_left + ear_right) / 2.0
                    mouth_value = mouth_open_ratio(face, width, height)

                    cv2.polylines(frame, [np.array(left_eye_points, dtype=np.int32)], True, CYAN, 2, cv2.LINE_AA)
                    cv2.polylines(frame, [np.array(right_eye_points, dtype=np.int32)], True, CYAN, 2, cv2.LINE_AA)
                    for point in (left_eye_points + right_eye_points):
                        cv2.circle(frame, point, 2, WHITE, -1, cv2.LINE_AA)

                    eyes_closed = ear_value < EYE_CLOSED_THRESHOLD
                    if eyes_closed:
                        if eye_closed_since is None:
                            eye_closed_since = now
                        closed_seconds = now - eye_closed_since
                        if closed_seconds >= EYES_CLOSED_SECONDS and not eye_alert_sent:
                            eye_alert_sent = True
                            total_alerts += 1
                            log_event("ALERTA: olhos fechados por 2.5 segundos", ear_value, mouth_value)
                            threading.Thread(target=play_alarm, daemon=True).start()
                        if eye_alert_sent:
                            status = "ALERTA: POSSIVEL SONOLENCIA"
                            status_color = RED
                        else:
                            status = f"Olhos fechados: {closed_seconds:.1f}s / 2.5s"
                            status_color = YELLOW
                    else:
                        eye_closed_since = None
                        eye_alert_sent = False
                        status = "Monitoramento ativo"
                        status_color = GREEN

                    # Alerta apenas para uma inclinação lateral forte, não para movimentos
                    # curtos nem para a cabeça abaixada/para frente. O desvio é suavizado e
                    # medido em relação à posição neutra calibrada no início.
                    if head_roll_baseline is None:
                        if status == "Monitoramento ativo":
                            status = "Calibrando cabeca: mantenha-a reta"
                            status_color = YELLOW
                    elif relative_head_roll is not None:
                        lateral_deviation = abs(relative_head_roll)

                        if not head_side_alert_sent:
                            if lateral_deviation >= HEAD_SIDE_TILT_DEGREES:
                                if side_tilt_since is None:
                                    side_tilt_since = now
                                head_pose_seconds = now - side_tilt_since
                            elif lateral_deviation < HEAD_SIDE_HYSTERESIS_DEGREES:
                                # Só zera a contagem se voltar bem abaixo do limite; isso
                                # evita que ruídos próximos ao limiar iniciem alertas repetidos.
                                side_tilt_since = None
                                head_pose_seconds = 0.0

                            # Enquanto a inclinação não voltar abaixo da faixa de histerese,
                            # a contagem continua mesmo que oscile ligeiramente perto do limite.
                            if side_tilt_since is not None:
                                head_pose_seconds = now - side_tilt_since

                            if (side_tilt_since is not None
                                    and head_pose_seconds >= HEAD_SIDE_HOLD_SECONDS
                                    and not head_side_alert_sent):
                                head_side_alert_sent = True
                                side_returned_since = None
                                total_alerts += 1
                                event = (
                                    "ALERTA: inclinacao lateral persistente "
                                    f"(desvio={relative_head_roll:.1f} graus)"
                                )
                                log_event(event, ear_value, mouth_value)
                                threading.Thread(target=play_alarm, daemon=True).start()
                        else:
                            # Um único alerta por episódio. Só rearma após retornar ao
                            # neutro por 1 segundo, não ao cruzar repetidamente um limiar.
                            if lateral_deviation <= HEAD_SIDE_NEUTRAL_DEGREES:
                                if side_returned_since is None:
                                    side_returned_since = now
                                elif now - side_returned_since >= HEAD_SIDE_REARM_SECONDS:
                                    head_side_alert_sent = False
                                    side_tilt_since = None
                                    side_returned_since = None
                            else:
                                side_returned_since = None

                        if status == "Monitoramento ativo":
                            if head_side_alert_sent:
                                status = "ALERTA: CABECA CAIDA PARA O LADO"
                                status_color = RED
                            elif side_tilt_since is not None:
                                status = f"Inclinacao lateral: {head_pose_seconds:.1f}s / {HEAD_SIDE_HOLD_SECONDS:.1f}s"
                                status_color = YELLOW

                    # Se os pontos faciais ficaram momentaneamente indisponíveis, não
                    # contar esse intervalo como postura inclinada. Se já alertou, mantém
                    # o alerta travado até observar a cabeça voltar ao neutro.
                    mouth_open = mouth_value >= MOUTH_OPEN_THRESHOLD
                    if mouth_open and not mouth_was_open and (now - last_yawn_log) >= YAWN_COOLDOWN_SECONDS:
                        log_event("Bocejo/sinal de boca aberta detectado", ear_value, mouth_value)
                        last_yawn_log = now
                    mouth_was_open = mouth_open
                    if mouth_open and status == "Monitoramento ativo":
                        status = "Boca aberta - verificando bocejo"
                        status_color = YELLOW
                else:
                    eye_closed_since = None
                    eye_alert_sent = False
                    side_tilt_since = None
                    side_returned_since = None
                    roll_history.clear()
                    mouth_was_open = False

                # Cabeçalho visual
                cv2.rectangle(frame, (0, 0), (width, 94), NAVY, -1)
                cv2.rectangle(frame, (0, 91), (width, 95), CYAN, -1)
                draw_text(frame, "EyesOnRoad | Monitoramento", (22, 34), WHITE, 0.8, 2)
                draw_text(frame, status, (22, 70), status_color, 0.68, 2)
                if ear_value is not None:
                    draw_text(frame, f"EAR: {ear_value:.3f}", (width - 210, 34), WHITE, 0.55, 1)
                if mouth_value is not None:
                    draw_text(frame, f"Boca: {mouth_value:.3f}", (width - 210, 63), WHITE, 0.55, 1)
                if relative_head_roll is not None:
                    draw_text(frame, f"Inclinacao lateral: {relative_head_roll:+.1f} graus", (width - 350, 88), WHITE, 0.48, 1)
                elif head_roll_baseline is None:
                    draw_text(frame, "Calibrando postura...", (width - 260, 88), WHITE, 0.48, 1)

                # Rodapé e instruções de uso
                cv2.rectangle(frame, (0, height - 42), (width, height), NAVY, -1)
                draw_text(frame, f"Alertas nesta sessao: {total_alerts}", (20, height - 14), WHITE, 0.52, 1)
                draw_text(frame, "Q/ESC: sair", (width - 150, height - 14), WHITE, 0.52, 1)

                cv2.imshow("EyesOnRoad - Monitoramento", frame)
                key = cv2.waitKey(1) & 0xFF
                if key in (ord('q'), ord('Q'), 27):
                    break
    finally:
        camera.release()
        cv2.destroyAllWindows()

    print("Monitoramento encerrado. Arquivo de alertas:", LOG_PATH)


def main() -> None:
    parser = argparse.ArgumentParser(description="Monitor de sinais de sonolência do EyesOnRoad")
    parser.add_argument("--camera", type=int, default=0, help="Índice da câmera (padrão: 0; tente 1 para outra webcam)")
    args = parser.parse_args()
    try:
        run(args.camera)
    except KeyboardInterrupt:
        print("\nExecução interrompida pelo usuário.")
    except Exception as exc:
        print("\nERRO:", exc)
        print("\nVerifique se a webcam está conectada, se a conexão com a internet funciona na primeira execução e se os pacotes foram instalados.")
        raise SystemExit(1)


if __name__ == "__main__":
    main()
