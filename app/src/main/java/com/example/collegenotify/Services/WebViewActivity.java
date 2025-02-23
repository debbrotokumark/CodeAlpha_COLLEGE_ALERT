package com.example.collegenotify.Services;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegenotify.R;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private boolean isReceiverRegistered = false;  // for theme chaneg app crash issue
    PermissionHandler mPermissionHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        mPermissionHandler=new PermissionHandler(WebViewActivity.this);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        String tile = getIntent().getStringExtra("title");

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(tile);
        // Check Storage Permission Granted or not
        if (!mPermissionHandler.checkStoragePermission()) {
            mPermissionHandler.requestPermissions();
        }
        ImageView imgback = findViewById(R.id.imgBack);
        imgback.setOnClickListener(v -> finish());



            setupWebView();

    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("facebook.com") || url.contains("fb://group.com")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setPackage("com.facebook.katana"); // Facebook app package name
                        startActivity(intent);
                        return true; // Prevent WebView from loading the URL
                    } catch (Exception e) {
                        // Facebook app is not installed, open in browser
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }
                return false; // Load other URLs in WebView
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {

            // Check Storage Permission Granted or not
            if (mPermissionHandler.checkStoragePermission()) {
                // Show Toast when permission is granted
                downloadFile(url);
            } else {
                mPermissionHandler.requestPermissions();
            }
        });

        // Handle long press to download images
        webView.setOnLongClickListener(v -> {
            HitTestResult result = webView.getHitTestResult();
            if (result.getType() == HitTestResult.IMAGE_TYPE || result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                final String imageUrl = result.getExtra();
                showDownloadImageDialog(imageUrl);
                return true;
            }
            return false;
        });

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");


        if (url != null) {
            webView.loadUrl(url);
        }
    }

    private void showDownloadImageDialog(final String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download Image")
                .setMessage("Do you want to download this image?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    // Check Storage Permission Granted or not
                    if (mPermissionHandler.checkStoragePermission()) {
                        // Show Toast when permission is granted
                        downloadFile(imageUrl);
                    } else {
                        mPermissionHandler.requestPermissions();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(arg0 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));

        });


        dialog.show();
    }

    private void downloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        request.setDestinationInExternalPublicDir("Download", fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Downloading file...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // Write this code where onCreate Bundle is end...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && permissions.length == grantResults.length) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (!allPermissionsGranted) {
                mPermissionHandler.showDialog(permissions, requestCode);
            } else {
                Toast.makeText(WebViewActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkChangeListener, filter);
            isReceiverRegistered = true; // Update flag
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeListener);
            isReceiverRegistered = false; // Update flag


        }
    }

    @Override
    protected void onDestroy() {
        // Unregister the receiver only if it was registered to avoid IllegalArgumentException
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeListener);
            isReceiverRegistered = false; // Update flag
        }
        super.onDestroy();
    }


}
