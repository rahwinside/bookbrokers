package com.example.myapplication;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    CollectionReference collectionReference;
    EditText bName, author, price;

    String dept;
    int sem;
    String subject, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        bName = findViewById(R.id.editText2);
        author = findViewById(R.id.editText3);
        price = findViewById(R.id.editText4);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        sem = Integer.parseInt(getIntent().getStringExtra("SEM"));
        dept = getIntent().getStringExtra("DEPT");
        subject = getIntent().getStringExtra("SUBJECT");
        collectionReference = db.collection("books").document(dept.toLowerCase()).collection(Integer.toString(sem)).document(subject).collection("collection");


    }

    public void addBook(View view) {


        Map<String, Object> bookInfo = new HashMap<>();
        bookInfo.put("name", bName.getText().toString());
        bookInfo.put("author", author.getText().toString());
        bookInfo.put("price", price.getText().toString());
        bookInfo.put("user", uid);
        collectionReference.add(bookInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        String docID = documentReference.getId();
                        db.collection("user").document(uid).collection("myads").document(docID).set(bookInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(view, "Book has been posted successfully!", Snackbar.LENGTH_LONG)
                                        .setAction("Dismiss", null).show();
                                finish();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view, "Error posting book, please try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", null).show();
                    }
                });

//                .document().set(bookInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Snackbar.make(view, "Book has been posted successfully!", Snackbar.LENGTH_LONG)
//                        .setAction("Dismiss", null).show();
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Snackbar.make(view, "Error posting book, please try again later.", Snackbar.LENGTH_LONG)
//                        .setAction("Dismiss", null).show();
//            }
//        });




    }
}
