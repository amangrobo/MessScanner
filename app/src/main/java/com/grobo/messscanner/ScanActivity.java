package com.grobo.messscanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.grobo.messscanner.database.AppDatabase;
import com.grobo.messscanner.database.TempModel;
import com.grobo.messscanner.database.UserDao;
import com.grobo.messscanner.database.UserModel;
import com.grobo.messscanner.database.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    private UserDao userDao;
    private AlertDialog alertDialog;
    private ListenerRegistration registration;
    private List<DocumentSnapshot> documentSnapshots;
    private String currentDate;
    private int mealNo;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        qrCodeReaderView = findViewById(R.id.qr_reader_view);
        ViewGroup.LayoutParams params = qrCodeReaderView.getLayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenWidth();
        qrCodeReaderView.setLayoutParams(params);

        spinner = findViewById(R.id.meal_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                mealNo = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userDao = AppDatabase.getDatabase(this).userDao();

        checkPermission();

        alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (dialog != null) dialog.dismiss();
                })
                .setOnDismissListener(dialog -> {
                    if (qrCodeReaderView != null)
                        qrCodeReaderView.startCamera();
                }).create();

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1);

        Log.e(getClass().getSimpleName(), calendar1.getTime().toString() + String.valueOf(calendar1.getTime().getTime()));

        Query query = FirebaseFirestore.getInstance().collectionGroup("cancel").whereArrayContains("days", calendar1.getTime());
        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    documentSnapshots = queryDocumentSnapshots.getDocuments();
                    Log.e(getClass().getSimpleName(), documentSnapshots.toString());
                }
            } else if (e.getMessage() != null)
                Log.e(getClass().getSimpleName(), e.getMessage());
        });
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

    private void showDialog(String email) {

        if (alertDialog != null && !alertDialog.isShowing()) {

            alertDialog.setTitle(email);

            String currentTakenFoodData = currentDate + "_" + mealNo + "_1";
            int status = 0;

            UserModel currentUser = null;
            Utils.LoadUserById task = new Utils.LoadUserById(userDao);

            try {
                currentUser = task.execute(email).get();
            } catch (Exception e) {
                if (e.getMessage() != null)
                    Log.e(getClass().getSimpleName(), e.getMessage());
                alertDialog.setMessage("User does not exist !");
            }


            if (currentUser != null) {

                alertDialog.setTitle(currentUser.getName() == null ? email : currentUser.getName());

                if (documentSnapshots != null) {

                    for (DocumentSnapshot d : documentSnapshots) {
                        String[] split = d.getReference().getPath().split("/");

                        TempModel curr = d.toObject(TempModel.class);

                        if (email.equals(split[1])) {

                            if (curr.isFull() || curr.getMeals().contains(mealNo)) {
                                alertDialog.setMessage("Meal cancelled...");
                                status = -1;
                            }

                        }

                        if (status != -1 && currentUser.getFoodData().contains(currentTakenFoodData)) {
                            alertDialog.setMessage("Food already taken...");
                            status = 1;
                        }
                        if (status == 0) {
                            alertDialog.setMessage("Welcome");

                            Utils.InsertUser newTask = new Utils.InsertUser(userDao);
                            List<String> foodData = currentUser.getFoodData();
                            if (foodData == null) foodData = new ArrayList<>();
                            foodData.add(currentTakenFoodData);
                            currentUser.setFoodData(foodData);
                            newTask.execute(currentUser);
                        }
                    }

                } else {
                    if (currentUser.getFoodData().contains(currentTakenFoodData)) {
                        alertDialog.setMessage("Food already taken...");
                        status = 1;
                    }
                    if (status == 0) {
                        alertDialog.setMessage("Welcome");

                        Utils.InsertUser newTask = new Utils.InsertUser(userDao);
                        List<String> foodData = currentUser.getFoodData();
                        if (foodData == null) foodData = new ArrayList<>();
                        foodData.add(currentTakenFoodData);
                        currentUser.setFoodData(foodData);
                        newTask.execute(currentUser);
                    }
                }

            } else {
                alertDialog.setMessage("User does not exist !");
            }

            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();

        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        currentDate = dateFormat.format(calendar.getTime());

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 7 && hour <= 10) {
            spinner.setSelection(0);
        } else if (hour >= 12 && hour <= 14) {
            spinner.setSelection(1);
        } else if (hour >= 16 && hour <= 18) {
            spinner.setSelection(2);
        } else if (hour >= 19) {
            spinner.setSelection(3);
        } else {
            spinner.setSelection(4);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null)
            registration.remove();
    }

    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
