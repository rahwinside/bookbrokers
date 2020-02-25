package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class BookListActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String dept;
    int sem;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        sem = Integer.parseInt(getIntent().getStringExtra("SEM"));
        dept = getIntent().getStringExtra("DEPT");
        subject = getIntent().getStringExtra("SUBJECT");
        ButterKnife.bind(this);
        init();
        getBookList();
    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getBookList(){
        Query query = db.collection("books").document(dept.toLowerCase()).collection(Integer.toString(sem)).document(subject).collection("collection");

        FirestoreRecyclerOptions<BookResponse> response = new FirestoreRecyclerOptions.Builder<BookResponse>()
                .setQuery(query, BookResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BookResponse, BookHolder>(response) {
            @Override
            public void onBindViewHolder(BookHolder holder, int position, BookResponse model) {
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
            public BookHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item, group, false);

                return new BookHolder(view);
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
        @BindView(R.id.user)
        TextView textUser;
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
