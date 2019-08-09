package be.pxl.project.cookaid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Objects;

public class SearchRecipeActivity extends AppCompatActivity {
    private ArrayList<Recipe> mRecipeList;
    private SearchRecipeAdapter mArrayAdapter;
    private DatabaseReference mDatabase;
    private TextView mErrorTextView;
    private Button mLikeBtn, mDislikeBtn;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);

        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        mRecipeList = new ArrayList<>();
        Button backBtn = findViewById(R.id.search_recipes_back_btn);
        mLikeBtn = findViewById(R.id.like_btn);
        mDislikeBtn = findViewById(R.id.dislike_btn);
        mErrorTextView = findViewById(R.id.error_text_view);

        PushDownAnim.setPushDownAnimTo(backBtn, mLikeBtn, mDislikeBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        mDislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mArrayAdapter = new SearchRecipeAdapter(this, mRecipeList);
        flingContainer.setAdapter(mArrayAdapter);

        refreshRecipeList();

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mRecipeList.remove(0);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Recipe recipe = (Recipe) o;
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase.child("users").child(userId).child("dislikedRecipeIds").child(recipe.getId()).setValue(recipe.getId());


                refreshRecipeList();
                Toast.makeText(SearchRecipeActivity.this, "Dislike!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                Recipe recipe = (Recipe) o;
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase.child("users").child(userId).child("savedRecipeIds").child(recipe.getId()).setValue(recipe.getId());


                refreshRecipeList();
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

    private void refreshRecipeList() {
        mRecipeList.clear();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("recipes").getChildren()) {
                    boolean recipeSaved = false;
                    boolean recipeDisliked = false;

                    for (DataSnapshot savedRecipeId : dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("savedRecipeIds").getChildren()) {
                        if (Objects.equals(savedRecipeId.getValue(), Objects.requireNonNull(ds.getValue(Recipe.class)).getId())) {
                            recipeSaved = true;
                        }
                    }

                    for (DataSnapshot dislikedRecipeId : dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dislikedRecipeIds").getChildren()) {
                        if (Objects.equals(dislikedRecipeId.getValue(), Objects.requireNonNull(ds.getValue(Recipe.class)).getId())) {
                            recipeDisliked = true;
                        }
                    }

                    if (!(recipeSaved || recipeDisliked)) {
                        final Recipe recipe = ds.getValue(Recipe.class);
                        mRecipeList.add(recipe);
                        mArrayAdapter.notifyDataSetChanged();
                    }
                }

                if (mRecipeList.isEmpty()) {
                    mErrorTextView.setVisibility(View.VISIBLE);
                    mLikeBtn.setEnabled(false);
                    mLikeBtn.setAlpha(0.5F);
                    mDislikeBtn.setEnabled(false);
                    mDislikeBtn.setAlpha(0.5F);
                } else {
                    mErrorTextView.setVisibility(View.INVISIBLE);
                    mLikeBtn.setEnabled(true);
                    mLikeBtn.setAlpha(1);
                    mDislikeBtn.setEnabled(true);
                    mDislikeBtn.setAlpha(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}