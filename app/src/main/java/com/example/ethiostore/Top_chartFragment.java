package com.example.ethiostore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.View_Holder.VerticalAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Top_chartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Top_chartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView horizontalRecyclerView, verticalRecyclerView ;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference appRef;

    public Top_chartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Top_chartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Top_chartFragment newInstance(String param1, String param2) {
        Top_chartFragment fragment = new Top_chartFragment();
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
        View view = inflater.inflate(R.layout.fragment_app, container, false);


//        horizontalRecyclerView = view.findViewById(R.id.Horizontal_recyclerApp);
        verticalRecyclerView = view.findViewById(R.id.Vertical_recycler_app);
        fetchVerticalData();
        return view;
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