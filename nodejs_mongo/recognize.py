import os
import face_recognition
import sys 
import json
import pickle
from PIL import Image

def detectFace(courseListPhoto_dir, detectPicture):
    fileNames = courseListPhoto_dir
    known_faces = []
    distance = []
    # import knownFaces to dlib
    for filename in fileNames:
        studentId = filename[7:-4];
        try:
            with open('./encoding_data/'+studentId+'.dat', 'rb') as f:
                knownFace_encoding = pickle.load(f)
        except Exception as e: 
            knownFace = face_recognition.load_image_file(filename)
            knownFace_encoding = face_recognition.face_encodings(knownFace)[0]
            with open('./encoding_data/'+studentId+'.dat', 'wb') as f:
                pickle.dump(knownFace_encoding, f)
        known_faces.append(knownFace_encoding)
    # import test picture
    unknownFace = face_recognition.load_image_file(detectPicture)
    unknownFace_encoding = face_recognition.face_encodings(unknownFace)[0]
    
    # compare with dataset
    # distance = face_recognition.face_distance(known_faces, unknownFace_encoding)
    # for index in range(len(fileNames)):
        # print("{} : {}".format(fileNames[index], distance
    results = face_recognition.compare_faces(known_faces, unknownFace_encoding, 0.55)
    resultStack = []
    # print result
    for index in range(len(results)):
        if results[index]:
            resultStack.append(fileNames[index][7:-4])
    return resultStack

def main():
    array = sys.argv[2].split(',')
    jsonParsed = json.dumps(array)
    resultStack = detectFace(array, "./teacherUploads/" + sys.argv[1] + ".png")
    print(json.dumps(resultStack))
    sys.stdout.flush()

if __name__ == '__main__':
    main()
