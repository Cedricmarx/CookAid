package be.pxl.project.cookaid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikedRecipeActivity extends AppCompatActivity {

    DatabaseReference ref;
    private LikedRecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_recipe);

        final ArrayList<Recipe> likedRecipeList = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot recipeIdDs: dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("savedRecipeIds").getChildren()) {

                    String recipeId = recipeIdDs.getValue().toString();

                    for (DataSnapshot recipeDs : dataSnapshot.child("recipes").getChildren()) {

                        Recipe recipe = recipeDs.getValue(Recipe.class);

                        if (recipe.getId().equals(recipeId)) {
                            likedRecipeList.add(recipe);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RecyclerView recyclerView= findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LikedRecipeAdapter(this,likedRecipeList,this.getSupportFragmentManager());
        recyclerView.setAdapter(mAdapter);
    }
}
