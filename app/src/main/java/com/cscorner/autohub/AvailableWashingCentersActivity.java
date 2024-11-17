package com.cscorner.autohub;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscorner.autohub.WashingCenterAdapter;
import com.cscorner.autohub.WashingCenterOwner;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AvailableWashingCentersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private WashingCenterAdapter adapter;
    private List<WashingCenterOwner> ownerList;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_washing_centre_rcview);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ownerList = new ArrayList<>();
        adapter = new WashingCenterAdapter(ownerList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        fetchWashingCenterOwners();
    }

    private void fetchWashingCenterOwners() {
        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("WashingCenterOwners")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String name = document.getString("name");
                                String username = document.getString("username");
                                String mobileNo = document.getString("mobileNo");

                                WashingCenterOwner owner = new WashingCenterOwner(name, username, mobileNo);
                                ownerList.add(owner);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle error
                        task.getException().printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }
}