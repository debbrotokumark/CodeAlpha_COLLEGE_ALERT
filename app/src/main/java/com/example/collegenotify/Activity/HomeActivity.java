package com.example.collegenotify.Activity;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegenotify.Fragment.EventFragment;
import com.example.collegenotify.Fragment.NotificationFragment;
import com.example.collegenotify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomnevigaionview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        askNotificationPermission();
        bottomnevigaionview = findViewById(R.id.bottomnevigaionview);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventFragment(), "EventFragment").commit();
        bottomnevigaionview.setOnNavigationItemSelectedListener(navListener);
    }
    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Notification Permission")
                        .setMessage("Enable notifications to receive important notices, essential information, and the latest updates.")
                        .setPositiveButton("Yes, Enable", (dialogInterface, i) -> requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS))
                        .setNegativeButton("Not Now", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .create()
                        .show();

            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String tag = null;

                if (item.getItemId() == R.id.mevent) {
                    tag = "EventFragment";
                    selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (selectedFragment == null) {
                        selectedFragment = new EventFragment();
                    }
                } else if (item.getItemId() == R.id.mNotification) {
                    tag = "NotificationFragment";
                    selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (selectedFragment == null) {
                        selectedFragment = new NotificationFragment();
                    }
                }


                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment == null || !Objects.equals(currentFragment.getTag(), tag)) {
                    if (selectedFragment != null) {

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, selectedFragment, tag);
                        transaction.commit();
                    }
                }

                return true;
            };

}