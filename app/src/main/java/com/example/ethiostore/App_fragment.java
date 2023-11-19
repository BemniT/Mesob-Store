package com.example.ethiostore;

import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.View_Holder.App_ViewHolder;
import com.example.ethiostore.View_Holder.Book_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class App_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView forYou, Top, categories, news;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference appRef;
    public App_fragment() {
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




        //news = news.findViewById(R.id.app)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app, container, false);

        recyclerView = view.findViewById(R.id.recycler_app_view);
        recyclerView.hasFixedSize();
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

//        forYou = forYou.findViewById(R.id.app_foryou);
//        Top = Top.findViewById(R.id.app_top_chart);
//        categories = categories.findViewById(R.id.app_categories_chart);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appRef = FirebaseDatabase.getInstance().getReference().child("Apps");


        FirebaseRecyclerOptions<Apps> options = new FirebaseRecyclerOptions.Builder<Apps>()
                .setQuery(appRef, Apps.class).build();

        FirebaseRecyclerAdapter<Apps, App_ViewHolder> adapter = new FirebaseRecyclerAdapter<Apps, App_ViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull App_ViewHolder holder, int position, @NonNull Apps model)
            {
                holder.app_name.setText(model.getSname());
                Picasso.get().load(model.getImage()).into(holder.app_image);
            }

            @NonNull
            @Override
            public App_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_view,parent , false);
                App_ViewHolder holder = new App_ViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}