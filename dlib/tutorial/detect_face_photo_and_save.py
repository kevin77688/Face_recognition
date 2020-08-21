import cv2
import time
capture = cv2.VideoCapture(0) # 打開攝像頭

face = cv2.CascadeClassifier('haarcascade_frontalface_default.xml') # 導入人臉模型
# face.load('D:/opencv/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml')
cv2.namedWindow('攝像頭') # 獲取攝像頭畫面

count = 0

while True:

    ret, frame = capture.read() # 讀取視頻圖片

    gray = cv2.cvtColor(frame, cv2.COLOR_RGB2GRAY) # 灰度

    faces = face.detectMultiScale(gray,1.1,3,0,(100,100))
    boarden_x =0
    boarden_y =0
    to_x=0
    to_y =0
    for (x, y, w, h) in faces: # 5個參數，一個參數圖片 ，2 坐標原點，3 識別大小，4，顏色5，線寬

        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
        boarden_x = x
        boarden_y = y
        to_x = w
        to_y = h
        cv2.imshow('window', frame) # 顯示
    if(boarden_x!=0 and boarden_y!=0 and to_x!=0):
        crop_img = frame[boarden_y:boarden_y+to_x, boarden_x:boarden_x+to_x]
        cv2.imshow('My Image',crop_img)
        time.sleep( 1.5 )
        cv2.imwrite(str(count)+'.jpg', crop_img)
        count +=1
    # 存成3張圖片
    if(count>4):
        break
    # cv2.waitKey(0)
    # cv2.imwrite(str(count)+'.jpg', img)
    if cv2.waitKey(5) & 0xFF == ord('q'):

        break

capture.release() # 釋放資源

cv2.destroyAllWindows() # 關閉窗口
