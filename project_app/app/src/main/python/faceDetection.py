CASCADE_FILE = 'weight/haarcascade_frontalface_default.xml'
RECTANGLE_COLOR = (0, 255, 0)

import cv2

# detect weight loader
face_cascade = cv2.CascadeClassifier(CASCADE_FILE)

def detectFace(img):
    grayImage = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(
        grayImage, scaleFactor = 1.12, minNeighbors = 4
    )

    for (x, y, w, h) in faces:
        cv2.rectangle(img, (x, y), (x+w, y+h), RECTANGLE_COLOR, 2)

    img = cv2.flip(img, 1)
    return img