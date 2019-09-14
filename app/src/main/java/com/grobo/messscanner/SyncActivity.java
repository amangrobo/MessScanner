package com.grobo.messscanner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.grobo.messscanner.database.AppDatabase;
import com.grobo.messscanner.database.UserDao;
import com.grobo.messscanner.database.UserModel;
import com.grobo.messscanner.database.Utils;

import java.util.List;

public class SyncActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDao userDao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        findViewById(R.id.button_sync_daily).setOnClickListener(this);
        findViewById(R.id.button_sync_monthly).setOnClickListener(this);

        userDao = AppDatabase.getDatabase(this).userDao();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_sync_monthly) {

            progressDialog.setMessage("Loading data... Please wait.");
            progressDialog.show();

            syncMonthly();

        }

    }

    private void syncMonthly() {

        int mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);

        FirebaseAuth.getInstance().signInWithEmailAndPassword("mess1@notifications-grobo.web.app", "messone").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Query query = FirebaseFirestore.getInstance().collection("mess").whereEqualTo("mess", mess);
                query.get().addOnCompleteListener(task1 -> {

                    if (task1.isSuccessful() && task1.getResult() != null) {
                        List<UserModel> documents = task1.getResult().toObjects(UserModel.class);

                        Log.e(getClass().getSimpleName(), task1.getResult().getDocuments().toString());

                        new Utils.DeleteAllUsersTask(userDao).execute();

                        new Handler().postDelayed(() -> {
                            for (UserModel userModel : documents) {
                                Utils.InsertUser insertUser = new Utils.InsertUser(userDao);
                                insertUser.execute(userModel);
                            }
                            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                        }, 100);

                    } else {
                        Toast.makeText(SyncActivity.this, "Sync failed " + new String(Character.toChars(0x1F641)) + " , please try again", Toast.LENGTH_SHORT).show();
                        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                    }

                });

            } else {
                Toast.makeText(this, "Sync Failed !!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
