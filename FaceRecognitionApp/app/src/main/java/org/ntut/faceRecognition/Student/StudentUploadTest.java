package org.ntut.faceRecognition.Student;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.ntut.faceRecognition.Camera.CameraCapture;
import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Utility.ImageSaver;
import org.ntut.faceRecognition.Utility.Utils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class StudentUploadTest extends AppCompatActivity {
    public static final String KEY_User_Document1 = "doc1";
    ImageView imageView;
    Button uploadButton, returnButton;
    TextView title;

    public static final int READ_PERMISSION_CODE = 1000;
    public static final int CAMERA_AND_WRITE_PERMISSION_CODE = 1001;

    private final String Document_img1 = "";
    private String username, userId;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IMyService iMyService;
    private int uploadCate = 0;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_test);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        getExtras();
        findView();
        setImageView();
        uploadButton();
        returnButton();
        setTitle();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = getIntent().getStringExtra("username");
            userId = getIntent().getStringExtra("userId");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void findView() {
        imageView = findViewById(R.id.IdProf);
        uploadButton = findViewById(R.id.deleterButton);
        returnButton = findViewById(R.id.return_button);
        title = findViewById(R.id.title_text);
    }

    private void setImageView() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void uploadButton(){
        uploadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                File f;
                if (uploadCate == 0){
                    return;
                }
                else if (uploadCate == 1){
                    f = new ImageSaver(StudentUploadTest.this).
                            setFileName("captureImage.png").
                            setDirectoryName("images").
                            createFile();
                }
                else {
                    f = new File(picturePath);
                }
                uploadCate = 0;
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", userId, requestFile);
                RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
                compositeDisposable.add(iMyService.studentUpload(fullName, body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                    returnButton.callOnClick();
                            }
                        }));
            }
        });
    }

    private void returnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(StudentUploadTest.this));
    }

    private void setTitle() {
        title.setText("\n歡迎" + username + "學生");
    }

    private void openCamera(){
        Intent intent = new Intent();
        intent.setClass(StudentUploadTest.this, CameraCapture.class);
        startActivityForResult(intent, 1);
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentUploadTest.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, CAMERA_AND_WRITE_PERMISSION_CODE);
                        } else
                            //system os < marshmallow or permission already granted
                            openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, READ_PERMISSION_CODE);
                        } else
                            //system os < marshmallow or permission already granted
                            openGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
            case CAMERA_AND_WRITE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                Bitmap bitmap = new ImageSaver(this).
                        setFileName("captureImage.png").
                        setDirectoryName("images").
                        load();
                bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                imageView.setImageBitmap(bitmap);
                imageView.invalidate();
                uploadCate = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 2){
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //thumbnail = getResizedBitmap(thumbnail, 400);
                Log.w("path of image:", picturePath + "");
                imageView.setImageBitmap(thumbnail);
                uploadCate = 2;
            }
        }
    }
}