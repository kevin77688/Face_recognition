FRAME_RATE = 10

import cv2
import time
import faceRecognition

# camera
cap = cv2.VideoCapture(0)
prev = 0

while True:
    # count time elpase
    timeElapse = time.time() - prev
    _, img = cap.read()

    if timeElapse > 1./FRAME_RATE:
        prev = time.time()
        img = faceRecognition.detectFace(img)
        cv2.imshow('camera capture', img)

    # wait for esc
    key = cv2.waitKey(1)
    if key == 27 or key == ord('q') or key == ord('Q'):
        break

cap.release()
cv2.destroyAllWindows()