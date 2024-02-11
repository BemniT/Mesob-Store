package com.example.ethiostore;

import static android.view.View.GONE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.View_Holder.HorizontalAdapter;
//import com.example.ethiostore.View_Holder.VerticalAdapter;
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
    LinearLayout horizontalRecyclerViewContainer;
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

        //fetchHorizontalData();
//        fetchVerticalData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app, container, false);

        horizontalRecyclerViewContainer = view.findViewById(R.id.horizontalRecyclerViewContainer);
        horizontalRecyclerViewContainer.setVisibility(View.VISIBLE);
         horizontalRecyclerView = view.findViewById(R.id.Horizontal_recyclerApp);
         verticalRecyclerView = view.findViewById(R.id.Vertical_recycler_app);
        forYou = view.findViewById(R.id.app_foryou);
        Top = view.findViewById(R.id.app_top_chart);
        news = view.findViewById(R.id.new_chart);
        forYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerViewContainer.setVisibility(View.VISIBLE);
                verticalRecyclerView.setVisibility(GONE);
                fetchHorizontalData();
            }
        });

        Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerViewContainer.setVisibility(View.GONE);
                verticalRecyclerView.setVisibility(View.VISIBLE);
                fetchVerticalData();
            }
        });
        fetchHorizontalData();
        return view;
    }
    private void fetchHorizontalData() {
        appRef = FirebaseDatabase.getInstance().getReference().child("Apps");

        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Apps> allItems = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Apps apps = snapshot1.getValue(Apps.class);
                    allItems.add(apps);
                }
                int itemsPerRow = 5;
                int totalItems = allItems.size();
                int numHorizontalRecyclerViews = (int) Math.ceil((double) totalItems / itemsPerRow);

               // LinearLayout horizontalRecyclerViewContainer = getView().findViewById(R.id.horizontalRecyclerViewContainer);

                horizontalRecyclerViewContainer.removeAllViews();

                for (int i = 0; i < numHorizontalRecyclerViews; i++) {
                    List<Apps> horizontalItems = new ArrayList<>();
                    int start = i * itemsPerRow;
                    int end = Math.min(start + itemsPerRow, totalItems);
                    horizontalItems.addAll(allItems.subList(start, end));

                    HorizontalAdapter horizontalAdapter = new HorizontalAdapter(horizontalItems, getActivity());
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                    RecyclerView horizontalRecyclerView = new RecyclerView(getContext());
                    RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    horizontalRecyclerView.setLayoutParams(layoutParams);
                    horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
                    horizontalRecyclerView.setAdapter(horizontalAdapter);

                    horizontalRecyclerViewContainer.addView(horizontalRecyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("App_fragment", "Database Error: " + error.getMessage());
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