package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NewUserActivity extends AppCompatActivity {

    CollectionReference collectionReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText name;
    Spinner dept, sem;
    String semStr, deptStr, phone, uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        collectionReference = db.collection("user");
        name = findViewById(R.id.name);
        dept = findViewById(R.id.spinner);
        sem = findViewById(R.id.spinner1);

        phone = getIntent().getStringExtra("phone");
        uid = getIntent().getStringExtra("uid");

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                semStr = Integer.toString(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                semStr = Integer.toString(0);
            }
        });
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                deptStr = getResources().getStringArray(R.array.dept_arrays)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                deptStr = "Alien";
            }
        });
    }

    public void registerUser(View view) {
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", name.getText().toString());
        newUser.put("sem", semStr);
        newUser.put("dept", deptStr);
        newUser.put("phone", phone);
        collectionReference.document(uid).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(view, "Registered successfully!", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", null).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, "Error registering, please try again later!", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", null).show();
            }
        });
    }
}
