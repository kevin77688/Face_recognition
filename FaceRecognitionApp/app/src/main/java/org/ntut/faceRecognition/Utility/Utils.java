package org.ntut.faceRecognition.Utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class Utils {
    public static void showToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static View.OnClickListener setReturnButton(final Activity activity) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        };
        return listener;
    }
}