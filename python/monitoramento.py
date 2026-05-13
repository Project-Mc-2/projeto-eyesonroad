import cv2
import winsound
import time

# Carrega classificadores
rosto_cascade = cv2.CascadeClassifier(
    cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
)

olho_cascade = cv2.CascadeClassifier(
    cv2.data.haarcascades + 'haarcascade_eye.xml'
)

# Inicia câmera
camera = cv2.VideoCapture(0)

# Controle de tempo
inicio_sem_olhos = None
TEMPO_ALERTA = 2  # segundos

while True:
    sucesso, frame = camera.read()

    if not sucesso:
        break

    # Espelha imagem
    frame = cv2.flip(frame, 1)

    # Escala de cinza
    cinza = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detecta rosto
    rostos = rosto_cascade.detectMultiScale(
        cinza,
        scaleFactor=1.3,
        minNeighbors=5
    )

    for (x, y, w, h) in rostos:

        # Desenha rosto
        cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 0, 0), 2)

        # Região do rosto
        rosto_gray = cinza[y:y+h, x:x+w]
        rosto_color = frame[y:y+h, x:x+w]

        # Detecta olhos
        olhos = olho_cascade.detectMultiScale(rosto_gray)

        # Se encontrou olhos
        if len(olhos) >= 2:

            inicio_sem_olhos = None

            cv2.putText(frame, "OLHOS ABERTOS", (20, 40),
                        cv2.FONT_HERSHEY_SIMPLEX,
                        1, (0, 255, 0), 2)

            # Desenha olhos
            for (ox, oy, ow, oh) in olhos:
                cv2.rectangle(rosto_color,
                              (ox, oy),
                              (ox+ow, oy+oh),
                              (0, 255, 0), 2)

        else:
            cv2.putText(frame, "OLHOS FECHADOS", (20, 40),
                        cv2.FONT_HERSHEY_SIMPLEX,
                        1, (0, 0, 255), 2)

            # Conta tempo
            if inicio_sem_olhos is None:
                inicio_sem_olhos = time.time()

            tempo = time.time() - inicio_sem_olhos

            # Alerta
            if tempo >= TEMPO_ALERTA:

                cv2.putText(frame, "ALERTA DE SONO!", (20, 80),
                            cv2.FONT_HERSHEY_SIMPLEX,
                            1, (0, 0, 255), 3)

                winsound.Beep(2000, 500)

    # Mostra tela
    cv2.imshow("Monitoramento", frame)

    # Fecha com Q
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

camera.release()
cv2.destroyAllWindows()