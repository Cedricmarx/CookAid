package be.pxl.project.cookaid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecipeFragment extends Fragment {

    public static RecipeFragment newInstance(){
        RecipeFragment fragment = new RecipeFragment();
        return fragment;
    }


    DatabaseReference ref;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_liked_recipe, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ArrayList<Recipe> likedRecipeList = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/recipes/");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot ds: dataSnapshot.getChildren()){
                    Recipe recipe = ds.getValue(Recipe.class);
                    likedRecipeList.add(recipe);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LikedRecipeAdapter likedRecipeAdapter = new LikedRecipeAdapter(likedRecipeList);

        recyclerView.setAdapter(likedRecipeAdapter);

        return view;

    }
}
