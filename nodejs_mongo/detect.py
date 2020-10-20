import os
import face_recognition
import sys 
import json
from PIL import Image

def cropImage(imageWaitForDetectionPath, userId):
    originalImage = face_recognition.load_image_file(imageWaitForDetectionPath)
    face_locations = face_recognition.face_locations(originalImage)
    if (len(face_locations) != 1):
        # detect multiple faces
        return "false";
    top, right, bottom, left = face_locations[0]
    cropImage = Image.open(imageWaitForDetectionPath).crop((left, top, right, bottom))
    cropImage.save("./data/" + userId + ".png")
    return "true";
    
userId = sys.argv[1];
print(cropImage("./uploads/" + userId + ".png", userId));
