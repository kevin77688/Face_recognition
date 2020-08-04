import face_recognition
import os

allFileList = os.listdir("./images")
for inputName in allFileList:
    image = face_recognition.load_image_file("./images/" + inputName)
    face_locations = face_recognition.face_locations(image)
    print(face_locations)
    outputName = "./Data/" + inputName[0:inputName.find('.')] + ".xml"
    fp = open(outputName,"w")
    fp.write("<annotation>\n")
    fp.write("<filename>" + outputName + "</filename>\n")
    for _ in range(len(face_locations)):
        fp.write("<object>\n")
        fp.write("<bndbox>\n")
        fp.write("<xmin>" + str(face_locations[_][3]) + "</xmin>\n")
        fp.write("<ymin>" + str(face_locations[_][0]) + "</ymin>\n")
        fp.write("<xmax>" + str(face_locations[_][1]) + "</xmax>\n")
        fp.write("<ymax>" + str(face_locations[_][2]) + "</ymax>\n")
        fp.write("</bndbox>\n")
        fp.write("</object>\n")
    fp.write("</annotation>")
    fp.close()
