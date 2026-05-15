import cv2
import time

# Carrega classificadores Haar Cascade
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

cap = cv2.VideoCapture(0)

eye_closed_start = None
ALERT_TIME = 2  # segundos

while True:
    ret, frame = cap.read()
    if not ret:
        break

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    faces = face_cascade.detectMultiScale(gray, 1.3, 5)
    eyes_detected = False

    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x,y), (x+w,y+h), (255,0,0), 2)

        roi_gray = gray[y:y+h, x:x+w]
        roi_color = frame[y:y+h, x:x+w]

        eyes = eye_cascade.detectMultiScale(roi_gray)

        if len(eyes) > 0:
            eyes_detected = True
            for (ex, ey, ew, eh) in eyes:
                cv2.rectangle(roi_color, (ex,ey), (ex+ew,ey+eh), (0,255,0), 2)

    # 👁️ Lógica de sonolência
    if not eyes_detected:
        if eye_closed_start is None:
            eye_closed_start = time.time()
        else:
            elapsed = time.time() - eye_closed_start
            if elapsed > ALERT_TIME:
                cv2.putText(frame, "SONOLENCIA!", (30,50),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0,0,255), 3)
    else:
        eye_closed_start = None

    cv2.imshow("Monitoramento OpenCV", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()