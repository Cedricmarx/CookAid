package be.pxl.project.cookaid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private final ArrayList<Recipe> mLikedRecipeList = new ArrayList<>();
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for( DataSnapshot recipeIdDs: dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("savedRecipeIds").getChildren()) {
                        String recipeId = recipeIdDs.getValue().toString();

                        for (DataSnapshot recipeDs : dataSnapshot.child("recipes").getChildren()) {
                            Recipe recipe = recipeDs.getValue(Recipe.class);

                            if (recipe.getId().equals(recipeId)) {
                                mLikedRecipeList.add(recipe);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new SimpleItemRecyclerViewAdapter(this, mLikedRecipeList, mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final ArrayList<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, recipe.getId());
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_LEVEL, recipe.getLevel());
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_RECIPE, recipe.getRecipe());
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_URI, recipe.getUri());
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_CATEGORY, recipe.getCategory());
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_NAME, recipe.getName());
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.getId());
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_NAME, recipe.getName());
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_LEVEL, recipe.getLevel());
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_CATEGORY, recipe.getCategory());
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_URI, recipe.getUri());
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_RECIPE, recipe.getRecipe());
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      ArrayList<Recipe> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(mValues.get(position).getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(holder.mImageView);
                }
            });

            PushDownAnim.setPushDownAnimTo(holder.itemView);

            holder.mNameView.setText(mValues.get(position).getName());
            holder.mLevelView.setText(mValues.get(position).getLevel());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mImageView;
            final TextView mNameView;
            final TextView mLevelView;

            ViewHolder(View view) {
                super(view);
                mImageView = view.findViewById(R.id.recipe_image);
                mNameView = view.findViewById(R.id.recipe_name);
                mLevelView = view.findViewById(R.id.recipe_level);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
