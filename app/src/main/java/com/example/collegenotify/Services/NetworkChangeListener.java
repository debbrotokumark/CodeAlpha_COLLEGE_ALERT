package com.example.collegenotify.Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.collegenotify.R;

import java.util.Objects;

public class NetworkChangeListener extends BroadcastReceiver {
    private AlertDialog dialog;

    @SuppressLint({"UnsafeProtectedBroadcastReceiver", "ResourceAsColor"})
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;

                if (dialog == null || !dialog.isShowing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    View layout_dialog = LayoutInflater.from(activity).inflate(R.layout.no_internet_dialog, null);
                    builder.setView(layout_dialog);

                    TextView btnRetry = layout_dialog.findViewById(R.id.btnRetry);

                    dialog = builder.create();
                    dialog.show();

                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                        dialog.getWindow().setGravity(Gravity.CENTER);
                    }

                    dialog.setCancelable(false);

                    btnRetry.setOnClickListener(view -> {
                        dialog.dismiss();
                        if (Common.isConnectedToInternet(context)) {
                            dialog.dismiss();
                        } else {
                            dialog.show();
                        }
                    });

                    dialog.setOnKeyListener((dialogInterface, keyCode, event) -> {
                        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                            dialog.dismiss();
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                            return true;
                        }
                        return false;
                    });
                }
            }
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
