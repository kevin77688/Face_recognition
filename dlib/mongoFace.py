import os
import face_recognition

# command line : 
#   python -c ' from mongoFace import *; detectFace("path_to_all_faces", "check_faces.png")'
def detectFace(dataFace_dir, detectPicture):
    known_faces = []
    fileNames = []
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
    results = face_recognition.compare_faces(known_faces, unknownFace_encoding, 0.15)

    # print result
    for index in range(len(results)):
        print("Check {} : {}".format(fileNames[index], results[index]))
