package com.grobo.messscanner.show;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.grobo.messscanner.R;
import com.grobo.messscanner.database.MessModel;
import com.grobo.messscanner.database.MessViewModel;

import java.util.Calendar;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private TextView total;
    private TextView breakfastCancelled;
    private TextView breakfastRemaining;
    private TextView lunchCancelled;
    private TextView lunchRemaining;
    private TextView snacksCancelled;
    private TextView snacksRemaining;
    private TextView dinnerCancelled;
    private TextView dinnerRemaining;

    private int nTotal = 0;

    private MessViewModel messViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        messViewModel = ViewModelProviders.of(this).get(MessViewModel.class);

        total = findViewById(R.id.tv_total_students);
        breakfastCancelled = findViewById(R.id.tv_breakfast_cancelled);
        breakfastRemaining = findViewById(R.id.tv_breakfast_remaining);
        lunchCancelled = findViewById(R.id.tv_lunch_cancelled);
        lunchRemaining = findViewById(R.id.tv_lunch_remaining);
        snacksCancelled = findViewById(R.id.tv_snacks_cancelled);
        snacksRemaining = findViewById(R.id.tv_snacks_remaining);
        dinnerCancelled = findViewById(R.id.tv_dinner_cancelled);
        dinnerRemaining = findViewById(R.id.tv_dinner_remaining);


        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        Log.e(getClass().getSimpleName(), calendar1.getTime().toString() + String.valueOf(calendar1.getTime().getTime()));

//        Query query = FirebaseFirestore.getInstance().collectionGroup("cancel").whereArrayContains("days", calendar1.getTime());
//        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
//            if (e == null) {
//                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
//                    List<TempModel> documentSnapshots = queryDocumentSnapshots.toObjects(TempModel.class);
//                    Log.e(getClass().getSimpleName(), documentSnapshots.toString());
//                    parseData(documentSnapshots);
//                }
//            } else if (e.getMessage() != null)
//                Log.e(getClass().getSimpleName(), e.getMessage());
//        });

        nTotal = messViewModel.getUserCount();
        total.setText(String.valueOf(nTotal));

        parseData();

    }

    private void parseData() {
        int nBreakfast = 0;
        int nLunch = 0;
        int nSnacks = 0;
        int nDinner = 0;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        long checkTime = calendar1.getTimeInMillis();

        List<MessModel> allUsers = messViewModel.getAllUsers();

//        for (MessModel d : allUsers) {
//            if (d.getFullDay().contains(checkTime)) {
//                nBreakfast++;
//                nLunch++;
//                nSnacks++;
//                nDinner++;
//            } else {
//                if (d.getBreakfast().contains(checkTime))
//                    nBreakfast++;
//                if (d.getLunch().contains(checkTime))
//                    nLunch++;
//                if (d.getSnacks().contains(checkTime))
//                    nSnacks++;
//                if (d.getDinner().contains(checkTime))
//                    nDinner++;
//            }
//        }
//
//        breakfastCancelled.setText(String.valueOf(nBreakfast));
//        lunchCancelled.setText(String.valueOf(nLunch));
//        snacksCancelled.setText(String.valueOf(nSnacks));
//        dinnerCancelled.setText(String.valueOf(nDinner));
//
//        breakfastRemaining.setText(String.valueOf(nTotal - nBreakfast));
//        lunchRemaining.setText(String.valueOf(nTotal - nLunch));
//        snacksRemaining.setText(String.valueOf(nTotal - nSnacks));
//        dinnerRemaining.setText(String.valueOf(nTotal - nDinner));

    }
}