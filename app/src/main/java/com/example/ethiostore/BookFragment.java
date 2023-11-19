package com.example.ethiostore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ethiostore.Interface.ItemClickListener;
import com.example.ethiostore.Model.Books;
import com.example.ethiostore.View_Holder.Book_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class BookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference bookRef;

    public BookFragment() {
        // Required empty public constructor
    }

    public static App_fragment newInstance(String param1, String param2) {
        App_fragment fragment = new App_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);


        recyclerView =  view.findViewById(R.id.book_recyclerView);
        recyclerView.setHasFixedSize(false);
        int numberOfColumns = 2; // You can change this to the desired number of columns
        layoutManager = new GridLayoutManager(getContext(), numberOfColumns);

        recyclerView.setLayoutManager(layoutManager);


        return view;
    }



    @Override
    public void onStart()
    {
        super.onStart();

        bookRef = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Books> options = new FirebaseRecyclerOptions.Builder<Books>()
                .setQuery(bookRef.child("Books"), Books.class)
                .build();


        FirebaseRecyclerAdapter<Books, Book_ViewHolder> adapter = new FirebaseRecyclerAdapter<Books, Book_ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Book_ViewHolder holder, int position, @NonNull Books model)
            {
                holder.book_name.setText(model.getSname());
                Picasso.get().load(model.getImage()).into(holder.book_images);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(), Software_Detail_Activity.class);
                        intent.putExtra("sid", model.getSid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public Book_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.book_card_view, parent, false);
                Book_ViewHolder holder = new Book_ViewHolder(view);
                return holder;
            }
        } ;
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

}