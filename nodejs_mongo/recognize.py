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
    unknownFaceLocation = face_recognition.face_locations(unknownFace)
    unknownFaces_encoding = face_recognition.face_encodings(unknownFace, unknownFaceLocation)
    
    # compare with dataset
    resultStack = []
    name = null
    for face_encoding in face_encodings:
        matches = face_recognition.compare_faces(known_faces, face_encoding, 0.55)
        face_distances = face_recognition.face_distance(known_faces, face_encoding)
            best_match_index = np.argmin(face_distances)
            if matches[best_match_index]:
                name = fileNames[best_match_index][7:-4]
        if name is not null:
            resultStack.append(name)

def main():
    array = sys.argv[2].split(',')
    jsonParsed = json.dumps(array)
    resultStack = detectFace(array, "./teacherUploads/" + sys.argv[1] + ".png")
    print(json.dumps(resultStack))
    sys.stdout.flush()

if __name__ == '__main__':
    main()
