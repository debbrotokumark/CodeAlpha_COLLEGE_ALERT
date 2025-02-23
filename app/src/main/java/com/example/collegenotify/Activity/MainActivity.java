package com.example.collegenotify.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegenotify.R;
import com.example.collegenotify.Services.SharePreferenceManager;

public class MainActivity extends AppCompatActivity {
    private SharePreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new SharePreferenceManager(this);

        // Check if user has seen welcome screen
        if (!preferenceManager.isFirstTime()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        Button getStartedButton = findViewById(R.id.btnStart);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update SharedPreferences to mark welcome screen as seen
                preferenceManager.setFirstTime(false);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });

    }


}