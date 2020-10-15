package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.ntut.faceRecognition.Camera.CameraPhotoSelection;

public class MainActivity extends AppCompatActivity {

    private Button login_button, contact_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setLoginButton();
        setContactButton();
    }

    private void findView() {
        login_button = findViewById(R.id.login_button);
        contact_button = findViewById(R.id.contact_button);
    }

    private void setLoginButton() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(Login.class);
            }
        });
    }

    private void setContactButton() {
        // TODO remember to delete this
        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage(CameraPhotoSelection.class);
            }
        });
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        startActivity(intent);
    }
}