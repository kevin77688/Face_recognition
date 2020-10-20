import os
import face_recognition
import sys 
import json
from PIL import Image

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
    resultJson = "{"
    # print result
    for index in range(len(results) - 1):
        resultJson += fileNames[index] + ":" + results[index] + ","
    resultJson += fileNames[len(results) - 1] + ":" +　results[len(results) - 1]
    return resultJson

def main():
    print("t")
    array = sys.argv[2].split(',')
    jsonParsed = json.dumps(array)

    img = Image.open("./uploads/" + sys.argv[1] + ".png")
    img.show()

    try:
        result = detectFace("./uploads/", "./uploads/" + sys.argv[1] + ".png")
        print("1")
    except Exception as e:
        print(json.dumps[e])
    sys.stdout.flush()
    
def cropImage(imageWaitForDetectionPath, userId):
    originalImage = face_recognition.load_image_file(imageWaitForDetectionPath)
    face_location = face_recognition.face_location(originalImage)
    if (len(face_location) != 1):
        # detect multiple faces
        originalImage.save(imageWaitForDetectionPath)
        return
    top, right, bottom, left = face_location[0]
    cropImage = originalImage.crop(top, left, top - bottom, right - left)
    
    cropImage.save("./data/" + userId + ".png")

if __name__ == '__main__':
    main()

