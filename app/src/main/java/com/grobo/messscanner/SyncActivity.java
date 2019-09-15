package com.grobo.messscanner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.grobo.messscanner.database.MessModel;
import com.grobo.messscanner.database.MessViewModel;
import com.grobo.messscanner.network.GetDataService;
import com.grobo.messscanner.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private MessViewModel messViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        messViewModel = ViewModelProviders.of(this).get(MessViewModel.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);


        findViewById(R.id.button_sync).setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Alert !!")
                    .setMessage("Are you sure you want to sync ??\nYou need to do this only once a day in the morning.")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        sync();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        if (dialogInterface != null) dialogInterface.dismiss();
                    }).show();
        });

    }

    private void sync() {

        progressDialog.setMessage("Loading data... Please wait.");
        progressDialog.show();

        int mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<MessModel.MessSuper> call = service.getAllUsersOfThisMess(mess);
        call.enqueue(new Callback<MessModel.MessSuper>() {
            @Override
            public void onResponse(@NonNull Call<MessModel.MessSuper> call, @NonNull Response<MessModel.MessSuper> response) {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getMessModels() != null) {

                        List<MessModel> messModels = response.body().getMessModels();

                        for (MessModel messModel : messModels)
                            messViewModel.insert(messModel);

                    }

                    Toast.makeText(SyncActivity.this, "Sync Successful " + new String(Character.toChars(0x1F642)), Toast.LENGTH_LONG).show();


                } else {
                    Log.e(getClass().getSimpleName(), "sync failed");
                    Toast.makeText(SyncActivity.this, "Sync failed " + new String(Character.toChars(0x1F641)) + " , please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessModel.MessSuper> call, @NonNull Throwable t) {
                if (t.getMessage() != null)
                    Log.e("failure", t.getMessage());
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "Sync failed " + new String(Character.toChars(0x1F641)) + " , please try again", Toast.LENGTH_LONG).show();
            }
        });


    }

//    private void syncMonthly() {
//        progressDialog.setMessage("Loading data... Please wait.");
//        progressDialog.show();
//
//        int mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);
//
//        FirebaseAuth.getInstance().signInWithEmailAndPassword("mess1@notifications-grobo.web.app", "messone").addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Query query = FirebaseFirestore.getInstance().collection("mess").whereEqualTo("mess", mess);
//                query.get().addOnCompleteListener(task1 -> {
//
//                    if (task1.isSuccessful() && task1.getResult() != null) {
//                        List<MessModel> documents = task1.getResult().toObjects(MessModel.class);
//
//                        new Utils.DeleteAllUsersTask(messDao).execute();
//
//                        new Handler().postDelayed(() -> {
//                            for (MessModel messModel : documents) {
//                                Utils.InsertUser insertUser = new Utils.InsertUser(messDao);
//                                insertUser.execute(messModel);
//                            }
//                            if (progressDialog != null && progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            Toast.makeText(this, "Successfully synced.", Toast.LENGTH_SHORT).show();
//                        }, 100);
//
//                    } else {
//                        Toast.makeText(SyncActivity.this, "Sync failed " + new String(Character.toChars(0x1F641)) + " , please try again", Toast.LENGTH_LONG).show();
//                        if (progressDialog != null && progressDialog.isShowing())
//                            progressDialog.dismiss();
//                    }
//
//                });
//
//            } else {
//                Toast.makeText(this, "Sync failed " + new String(Character.toChars(0x1F641)) + " , please try again", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
