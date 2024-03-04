package com.example.ethiostore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.Prevalent.LanguageManager;
import com.example.ethiostore.View_Holder.Game_HorizontalAdapter;
import com.example.ethiostore.View_Holder.Game_VerticalAdapter;
import com.example.ethiostore.View_Holder.HorizontalAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Game_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference gameRef;
    private RecyclerView verticalRecyclerGame, HorizontalRecyclerGame;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout horizontalRecyclerViewContainer;
    private TextView for_you, topChart, news, categories;

    public Game_Fragment() {
        // Required empty public constructor
    }

    public static Game_Fragment newInstance(String param1, String param2) {
        Game_Fragment fragment = new Game_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        LanguageManager.loadLocale(getActivity());
        verticalRecyclerGame = view.findViewById(R.id.Vertical_recyclerGame);
        HorizontalRecyclerGame = view.findViewById(R.id.Horizontal_recyclerGame);
        horizontalRecyclerViewContainer = view.findViewById(R.id.gameHorizontalRecyclerViewContainer);
        horizontalRecyclerViewContainer.setVisibility(View.VISIBLE);
        for_you = view.findViewById(R.id.game_foryou);
        topChart = view.findViewById(R.id.game_top_chart);
        news = view.findViewById(R.id.gameNew_chart);
        categories = view.findViewById(R.id.game_categories_chart);

        fetchHorizontalData();

        int base = ContextCompat.getColor(getActivity(),R.color.base);
        int top_text = ContextCompat.getColor(getActivity(),R.color.top_text);
        for_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerViewContainer.setVisibility(View.VISIBLE);
                verticalRecyclerGame.setVisibility(View.GONE);
                for_you.setTextColor(base);
                topChart.setTextColor(top_text);
                news.setTextColor(top_text);
                fetchHorizontalData();
            }
        });

        topChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerViewContainer.setVisibility(View.GONE);
                verticalRecyclerGame.setVisibility(View.VISIBLE);
                for_you.setTextColor(top_text);
                topChart.setTextColor(base);
                news.setTextColor(top_text);
                fetchVerticalData();
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerViewContainer.setVisibility(View.GONE);
                verticalRecyclerGame.setVisibility(View.VISIBLE);
                for_you.setTextColor(top_text);
                topChart.setTextColor(top_text);
                news.setTextColor(base);
                fetchVerticalData();
            }
        });
        return view;
    }
    private void fetchHorizontalData()
    {
        gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Games> allItems = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Games games = snapshot1.getValue(Games.class);
                    allItems.add(games);
                }
                int itemsPerRow = 5;
                int totalItems = allItems.size();
                int numHorizontalRecyclerViews = (int) Math.ceil((double) totalItems / itemsPerRow);

                // LinearLayout horizontalRecyclerViewContainer = getView().findViewById(R.id.horizontalRecyclerViewContainer);

                horizontalRecyclerViewContainer.removeAllViews();

                for (int i = 0; i < numHorizontalRecyclerViews; i++) {
                    List<Games> horizontalItems = new ArrayList<>();
                    int start = i * itemsPerRow;
                    int end = Math.min(start + itemsPerRow, totalItems);
                    horizontalItems.addAll(allItems.subList(start, end));

//                    HorizontalAdapter horizontalAdapter = new HorizontalAdapter(horizontalItems, getActivity());
//                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                    Game_HorizontalAdapter gameHorizontalAdapter = new Game_HorizontalAdapter(horizontalItems, getActivity());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                    RecyclerView horizontalRecyclerView = new RecyclerView(getContext());
                    RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    horizontalRecyclerView.setLayoutParams(layoutParams);
                    horizontalRecyclerView.setLayoutManager(linearLayoutManager);
                    horizontalRecyclerView.setAdapter(gameHorizontalAdapter);

                    horizontalRecyclerViewContainer.addView(horizontalRecyclerView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchVerticalData(){

        gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<Games> gameVerticalItems = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Games games = dataSnapshot.getValue(Games.class);
                    gameVerticalItems.add(games);
                }

                Game_VerticalAdapter gameVerticalAdapter = new Game_VerticalAdapter(gameVerticalItems, getActivity());
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                verticalRecyclerGame.setLayoutManager(gridLayoutManager);
                verticalRecyclerGame.setAdapter(gameVerticalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}