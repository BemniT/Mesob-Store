package com.example.ethiostore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ethiostore.Model.Books;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.View_Holder.Book_HorizontalAdapter;
import com.example.ethiostore.View_Holder.Book_VerticalAdapter;
import com.example.ethiostore.View_Holder.Game_HorizontalAdapter;
import com.example.ethiostore.View_Holder.Game_VerticalAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class BookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mParam1;
    private String mParam2;

    private RecyclerView horizontalRecyclerBook, verticalRecyclerBook;
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

        fetchHorizontalData();
        fetchVerticalData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        horizontalRecyclerBook = view.findViewById(R.id.Horizontal_recyclerBook);
        verticalRecyclerBook = view.findViewById(R.id.Vertical_recycler_Book);

        return view;
    }

    private void fetchVerticalData()
    {
        bookRef = FirebaseDatabase.getInstance().getReference().child("Books");

       bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<Books> booksVerticalItems = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Books books = dataSnapshot.getValue(Books.class);
                   booksVerticalItems.add(books);
                }

                Book_VerticalAdapter bookVerticalAdapter = new Book_VerticalAdapter(booksVerticalItems, getActivity());
                LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(getContext());
                verticalRecyclerBook.setLayoutManager(linearLayoutManager);
                verticalRecyclerBook.setAdapter(bookVerticalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchHorizontalData() {
        bookRef = FirebaseDatabase.getInstance().getReference().child("Books");
        bookRef.limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Books> bookHorizontalItems = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Books books = dataSnapshot.getValue(Books.class);
                    bookHorizontalItems.add(books);
                }

                Book_HorizontalAdapter bookHorizontalAdapter = new Book_HorizontalAdapter(bookHorizontalItems, getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                horizontalRecyclerBook.setLayoutManager(linearLayoutManager);
                horizontalRecyclerBook.setAdapter(bookHorizontalAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}