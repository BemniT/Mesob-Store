package com.example.ethiostore;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.Prevalent.Continity;
import com.example.ethiostore.View_Holder.Game_VerticalAdapter;
import com.example.ethiostore.View_Holder.SavedItemsAdapter;
import com.example.ethiostore.View_Holder.VerticalAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Saved_content_Activity extends AppCompatActivity {

    private ImageView backArrow;
    private RecyclerView recyclerView;
    private DatabaseReference savedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_content);

        backArrow = (ImageView) findViewById(R.id.close_saved);
        recyclerView = (RecyclerView) findViewById(R.id.saved_recycler_view);
        savedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Continity.currentOnlineUser.getPhone())
                        .child("Saved");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {


                List<Object> savedItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                     String type = dataSnapshot.child("type").getValue(String.class);
                     if (type != null)
                     {
                         if (type.equals("Apps")) {
                             Apps apps = dataSnapshot.getValue(Apps.class);
                             savedItems.add(apps);
                         }else if (type.equals("Games")) {
                             Games games = dataSnapshot.getValue(Games.class);
                             savedItems.add(games);
                         }
                     }

                }
                SavedItemsAdapter savedItemsAdapter = new SavedItemsAdapter(savedItems,Saved_content_Activity.this);
                recyclerView.setAdapter(savedItemsAdapter);

                savedItemsAdapter.setOnItemClickListener(new SavedItemsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item) {
                        if (item instanceof Apps)
                        {
                            Apps apps = (Apps) item;
                            Intent intent = new Intent(Saved_content_Activity.this, App_Game_DetailActivity.class);
                            intent.putExtra("type","Apps");
                            intent.putExtra("sid",apps.getApp_id());
                            startActivity(intent);
                        } else if (item instanceof Games) {
                            Games games =(Games) item;
                            Intent intent = new Intent(Saved_content_Activity.this, App_Game_DetailActivity.class);
                            intent.putExtra("type","Games");
                            intent.putExtra("sid",games.getApp_id());
                            startActivity(intent);

                        }
                    }
                });

                GridLayoutManager gridLayoutManager = new GridLayoutManager(Saved_content_Activity.this, 1);
                recyclerView.setLayoutManager(gridLayoutManager);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        VerticalAdapter verticalAdapter = new VerticalAdapter(verticalItems, getActivity()) ;
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
//        verticalRecyclerView.setLayoutManager(gridLayoutManager);
//        verticalRecyclerView.setAdapter(verticalAdapter);
    }
}