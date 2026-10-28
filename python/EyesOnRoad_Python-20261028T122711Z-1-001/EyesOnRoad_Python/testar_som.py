"""Testa o alarme sonoro do EyesOnRoad sem abrir a webcam."""
import sys
import time
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent
ALARM_WAV = BASE_DIR / "sons" / "alarme_forte.wav"


def main() -> None:
    print("Teste do alarme forte do EyesOnRoad...")
    if sys.platform.startswith("win"):
        try:
            import winsound
            if ALARM_WAV.exists():
                print("Reproduzindo alarme WAV. Ajuste o volume do Windows se necessário.")
                winsound.PlaySound(str(ALARM_WAV), winsound.SND_FILENAME | winsound.SND_NODEFAULT)
                print("Teste concluído.")
                return
            print("Arquivo WAV não encontrado:", ALARM_WAV)
        except Exception as exc:
            print(f"Não foi possível reproduzir o WAV: {exc}")
            try:
                import winsound
                for frequency, duration_ms in ((900, 450), (1500, 550), (900, 450), (1500, 550)):
                    winsound.Beep(frequency, duration_ms)
                    time.sleep(0.08)
                print("Teste concluído com bipes alternativos.")
                return
            except Exception as fallback_exc:
                print("Falha no teste de áudio:", fallback_exc)
    else:
        for _ in range(4):
            print("\\a", end="", flush=True)
            time.sleep(0.15)
        print("Teste concluído. Em outros sistemas, o som depende do terminal.")


if __name__ == "__main__":
    main()
