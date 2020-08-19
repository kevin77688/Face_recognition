package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class faceDetector implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CascadeClassifier faceDetector;
    private Mat mRgba, mGray;
    private AppCompatActivity mActivity;
    private BaseLoaderCallback baseCallback = new BaseLoaderCallback(mActivity) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    try {
                        InputStream is = mActivity.getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
                        File cascadeDir = mActivity.getDir("cascade", Context.MODE_PRIVATE);
                        File cascFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
                        FileOutputStream fs = new FileOutputStream(cascFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = is.read(buffer)) != -1)
                            fs.write(buffer, 0, bytesRead);

                        is.close();
                        fs.close();

                        faceDetector = new CascadeClassifier(cascFile.getAbsolutePath());

                        if (faceDetector.empty())
                            faceDetector = null;
                        else
                            cascadeDir.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.onManagerConnected(status);
            }

        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    public faceDetector(AppCompatActivity activity) {
        mActivity = activity;

        if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, mActivity, baseCallback);
        else
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Imgproc.cvtColor(inputFrame.rgba(), mRgba, Imgproc.COLOR_BGR2RGB);
        mGray = inputFrame.gray();

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(mGray, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(
                    mRgba,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 0, 0));
        }
        return mRgba;
    }
}
