package com.grobo.messscanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.grobo.messscanner.database.MessModel;
import com.grobo.messscanner.database.MessViewModel;

public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    private MessViewModel messViewModel;
    private AlertDialog alertDialog;
    private int mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        messViewModel = ViewModelProviders.of(this).get(MessViewModel.class);
        mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);

        qrCodeReaderView = findViewById(R.id.qr_reader_view);
        ViewGroup.LayoutParams params = qrCodeReaderView.getLayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenWidth();
        qrCodeReaderView.setLayoutParams(params);

        checkPermission();

        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (dialog != null) dialog.dismiss();
                })
                .setOnDismissListener(dialog -> {
                    if (qrCodeReaderView != null)
                        qrCodeReaderView.startCamera();
                }).create();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    10101);
        } else {
            initializeCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkPermission();
    }

    private void initializeCamera() {
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(500L);
        qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        qrCodeReaderView.stopCamera();
        showDialog(text.trim());
    }

    private void showDialog(String token) {
        if (alertDialog != null && !alertDialog.isShowing()) {

            alertDialog.setTitle(token);

            MessModel currentModel = messViewModel.getUserByToken(token);

            if (currentModel != null) {

                alertDialog.setTitle(currentModel.getName() == null ? token : currentModel.getName());

                if (currentModel.getMess() != mess) {
                    SpannableStringBuilder spn = new SpannableStringBuilder("User enrolled in mess : " + currentModel.getMess());
                    spn.setSpan(new ForegroundColorSpan(Color.RED), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    alertDialog.setMessage(spn);
                } else if (currentModel.isTaken()) {
                    SpannableStringBuilder spn = new SpannableStringBuilder("Coupon already taken\nRoll : " + currentModel.getRoll());
                    spn.setSpan(new ForegroundColorSpan(Color.RED), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    alertDialog.setMessage(spn);
                } else {
                    SpannableStringBuilder spn = new SpannableStringBuilder("Welcome\nRoll : " + currentModel.getRoll());
                    spn.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    alertDialog.setMessage(spn);

                    currentModel.setTaken(true);
                    messViewModel.insert(currentModel);
                }
            }

        } else {
            SpannableStringBuilder spn = new SpannableStringBuilder("User doesn't exist.");
            spn.setSpan(new ForegroundColorSpan(Color.RED), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            alertDialog.setMessage(spn);
        }

        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
