package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.abdularis.civ.AvatarImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    @BindView(R.id.recycler_view_profile)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar_profile)
    ProgressBar progressBar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    TextView name;
    CollectionReference collectionReference;

    AvatarImageView avatarImageView;

    String uid;

    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        name = v.findViewById(R.id.name);
        avatarImageView = v.findViewById(R.id.profile);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        ButterKnife.bind(this, v);

        init();
        getBookList();

        db.collection("user").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.get("name").toString();
                                name.setText("Hi "+username+"!");
                                avatarImageView.setText(username.substring(0,1).toUpperCase());
                            } else {
                            }
                        } else {
                            System.out.println("Get failed with "+task.getException());
                        }
                    }
                });


        return  v;
    }
    private void init(){
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getBookList(){
        Query query = db.collection("user").document(uid).collection("myads");

        FirestoreRecyclerOptions<BookProfResponse> response = new FirestoreRecyclerOptions.Builder<BookProfResponse>()
                .setQuery(query, BookProfResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BookProfResponse, ProfileFragment.BookHolder>(response) {
            @Override
            public void onBindViewHolder(ProfileFragment.BookHolder holder, int position, BookProfResponse model) {
                progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getName());
                holder.textAuthor.setText(model.getAuthor());
                holder.textPrice.setText(model.getPrice());
//                Glide.with(getApplicationContext())
//                        .load(model.getImage())
//                        .into(holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(recyclerView, model.getName()+", "+model.getAuthor()+" at "+model.getPrice(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            }

            @Override
            public ProfileFragment.BookHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.book_item, group, false);

                return new ProfileFragment.BookHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView textName;
        @BindView(R.id.image)
        CircleImageView imageView;
        @BindView(R.id.author)
        TextView textAuthor;
        @BindView(R.id.price)
        TextView textPrice;

        public BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
