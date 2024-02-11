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

import com.example.ethiostore.Model.Games;
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
        verticalRecyclerGame = view.findViewById(R.id.Vertical_recyclerGame);
        HorizontalRecyclerGame = view.findViewById(R.id.Horizontal_recyclerGame);
        return view;
    }
    private void fetchHorizontalData()
    {
        gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        gameRef.limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<Games> gameHorizontalItems = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Games games = dataSnapshot.getValue(Games.class);
                    gameHorizontalItems.add(games);
                }

                Game_HorizontalAdapter gameHorizontalAdapter = new Game_HorizontalAdapter(gameHorizontalItems, getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                HorizontalRecyclerGame.setLayoutManager(linearLayoutManager);
                HorizontalRecyclerGame.setAdapter(gameHorizontalAdapter);
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