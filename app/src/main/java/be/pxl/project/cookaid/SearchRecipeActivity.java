package be.pxl.project.cookaid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class SearchRecipeActivity extends AppCompatActivity {
    private ArrayList<Recipe> mRecipeList;
    private SearchRecipeAdapter mArrayAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_recipes);
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        mRecipeList = new ArrayList<>();
        Button backBtn = findViewById(R.id.search_recipes_back_btn);
        Button likeBtn = findViewById(R.id.like_btn);
        Button dislikeBtn = findViewById(R.id.dislike_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mArrayAdapter = new SearchRecipeAdapter(this, R.layout.item, R.id.helloText, mRecipeList);
        flingContainer.setAdapter(mArrayAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("recipes").getChildren()) {
                    final Recipe recipe = ds.getValue(Recipe.class);
                    mRecipeList.add(recipe);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mRecipeList.remove(0);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(SearchRecipeActivity.this, "Dislike!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                if (!mRecipeList.isEmpty()) {
                    Recipe recipe = (Recipe) o;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabase.child("users").child(userId).child("savedRecipeIds").child(recipe.getId()).setValue(recipe.getId());
                }

                Toast.makeText(SearchRecipeActivity.this, "Like!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });
    }
}