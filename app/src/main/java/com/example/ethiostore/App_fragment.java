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
import android.widget.TextView;


import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.View_Holder.HorizontalAdapter;
import com.example.ethiostore.View_Holder.VerticalAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class App_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView forYou, Top, categories, news;
    private RecyclerView horizontalRecyclerView, verticalRecyclerView ;
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

        fetchHorizontalData();
        fetchVerticalData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app, container, false);


         horizontalRecyclerView = view.findViewById(R.id.Horizontal_recyclerApp);
         verticalRecyclerView = view.findViewById(R.id.Vertical_recycler_app);
//        forYou = forYou.findViewById(R.id.app_foryou);
//        Top = Top.findViewById(R.id.app_top_chart);
//        categories = categories.findViewById(R.id.app_categories_chart);
        return view;
    }
    private void fetchHorizontalData()
    {
        appRef = FirebaseDatabase.getInstance().getReference().child("Apps");

        appRef.limitToFirst(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<Apps> horizontalItem = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Apps apps = snapshot1.getValue(Apps.class);
                    horizontalItem.add(apps);
                }
                HorizontalAdapter horizontalAdapter = new HorizontalAdapter(horizontalItem,  getActivity());
                LinearLayoutManager horizontalLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

                horizontalRecyclerView.setLayoutManager(horizontalLayoutManger);
                horizontalRecyclerView.setAdapter(horizontalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                
            }
        });
    }

    private void fetchVerticalData()
    {
        appRef = FirebaseDatabase.getInstance().getReference().child("Apps");
        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Apps> verticalItems = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Apps apps = dataSnapshot.getValue(Apps.class);
                    verticalItems.add(apps);
                }
                VerticalAdapter verticalAdapter = new VerticalAdapter(verticalItems, getActivity()) ;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                verticalRecyclerView.setLayoutManager(gridLayoutManager);
                verticalRecyclerView.setAdapter(verticalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}