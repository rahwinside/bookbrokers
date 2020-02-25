package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFragment extends Fragment {
    Spinner deptSpinner;
    Spinner semSpinner;
    String dept;
    int sem;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> topicList = new ArrayList<>();
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);
        deptSpinner = (Spinner) v.findViewById(R.id.spinner1);
        semSpinner = (Spinner) v.findViewById(R.id.spinner);
        listView = (ListView) v.findViewById(R.id.list1);

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                dept = getResources().getStringArray(R.array.dept_arrays)[position];
//                if(dept.equals("SELECT ONE") ||sem==0){
////                    Snackbar snackbar = Snackbar
////                            .make(getView(), "Please select valid options", Snackbar.LENGTH_LONG);
////                    snackbar.show();
////                }
                if(!(dept.equals("SELECT ONE") ||sem==0)){
                    db.collection("books").document(dept.toLowerCase()).collection(Integer.toString(sem))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                       if(!document.getId().toString().equals("empty"))
                                           topicList.add(document.getId().toString());
                                   }
                                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_selectable_list_item,topicList);
                                   adapter.notifyDataSetChanged();
                                   listView.setAdapter(adapter);
                               } else {
                                   AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

                                   builder1.setTitle("Error retrieving from server");
                                   builder1.setMessage("Please check your internet connectivity or try again later.");
                                   builder1.setCancelable(true);

                                   builder1.setPositiveButton(
                                           "Okay",
                                           new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int id) {
                                                   dialog.cancel();
                                               }
                                           });

                                   AlertDialog alert11 = builder1.create();
                                   alert11.show();
                               }
                           }
                           }
                        );
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sem = position;
                if(!(dept.equals("SELECT ONE") ||sem==0)){
                    db.collection("books").document(dept.toLowerCase()).collection(Integer.toString(sem))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                       if(!document.getId().toString().equals("empty"))
                                           topicList.add(document.getId().toString());
                                   }
                                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_selectable_list_item,topicList);
                                   adapter.notifyDataSetChanged();
                                   listView.setAdapter(adapter);
                               } else {
                                   AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

                                   builder1.setTitle("Error retrieving from server");
                                   builder1.setMessage("Please check your internet connectivity or try again later.");
                                   builder1.setCancelable(true);

                                   builder1.setPositiveButton(
                                           "Okay",
                                           new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int id) {
                                                   dialog.cancel();
                                               }
                                           });

                                   AlertDialog alert11 = builder1.create();
                                   alert11.show();
                               }
                           }
                       }
                    );
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getContext(), AddBookActivity.class);
                i.putExtra("DEPT", dept);
                i.putExtra("SEM", Integer.toString(sem));
                i.putExtra("SUBJECT", topicList.get(position));
                startActivity(i);
            }
        });

        return v;
    }

}
