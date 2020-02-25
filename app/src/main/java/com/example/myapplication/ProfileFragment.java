package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    TextView name;
    CollectionReference collectionReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        name = v.findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        db.collection("user").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                name.setText("Hi "+document.get("name").toString()+"!");
                            } else {
                            }
                        } else {
                            System.out.println("Get failed with "+task.getException());
                        }
                    }
                });


        return  v;
    }
}
