import os
import face_recognition
import sys 
import json
from PIL import Image

def detectFace(dataFace_dir, detectPicture):
    known_faces = []
    fileNames = []
    distance = []
    # import knownFaces to dlib
    for filename in os.listdir(dataFace_dir):
        if filename.endswith(".jpg") or filename.endswith(".png"):
            currentFile = os.path.join(dataFace_dir, filename)
            fileNames.append(filename)
            knownFace = face_recognition.load_image_file(currentFile)
            knownFace_encoding = face_recognition.face_encodings(knownFace)[0]
            known_faces.append(knownFace_encoding)
        else:
            continue
    # import test picture
    unknownFace = face_recognition.load_image_file(detectPicture)
    unknownFace_encoding = face_recognition.face_encodings(unknownFace)[0]
    
    # compare with dataset
    # distance = face_recognition.face_distance(known_faces, unknownFace_encoding)
    # for index in range(len(fileNames)):
        # print("{} : {}".format(fileNames[index], distance
    results = face_recognition.compare_faces(known_faces, unknownFace_encoding, 0.45)
    resultStack = []
    # print result
    for index in range(len(results)):
        if results[index]:
            resultStack.append(fileNames[index][0:-4])
    return resultStack

def main():
    array = sys.argv[2].split(',')
    jsonParsed = json.dumps(array)
    resultStack = detectFace("./uploads/", "./teacherUploads/" + sys.argv[1] + ".png")
    print(json.dumps(resultStack))
    sys.stdout.flush()

if __name__ == '__main__':
    main()
