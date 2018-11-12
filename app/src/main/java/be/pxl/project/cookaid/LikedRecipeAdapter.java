package be.pxl.project.cookaid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LikedRecipeAdapter extends RecyclerView.Adapter<LikedRecipeHolder>{

    Context context;
    ArrayList<Recipe> recipes;
    FragmentManager fragmentManager;
    final long ONE_MEGABYTE = 1024 * 1024;

    public LikedRecipeAdapter(Context context, ArrayList<Recipe> recipes, FragmentManager fragmentManager) {
        this.context = context;
        this.recipes = recipes;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public LikedRecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);
        return new LikedRecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LikedRecipeHolder likedRecipeHolder, int i) {
        final Recipe currentRecipe = recipes.get(i);


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(currentRecipe.getUri()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                likedRecipeHolder.recipeImage.setBackground(drawable);
            }
        });


        likedRecipeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = currentRecipe.getName();
                final String category = currentRecipe.getCategory();
                final String level = currentRecipe.getLevel();
                final String recipe = currentRecipe.getRecipe();
                final String uri = currentRecipe.getUri();
                openDialogFragment(name, category, level, recipe, uri);
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }
        });

        likedRecipeHolder.recipeLevel.setText(currentRecipe.getLevel());
        likedRecipeHolder.recipeName.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private void openDialogFragment(String name, String category, String level, String recipe, String uri){
        Bundle bundle= new Bundle();
        bundle.putString("NAME_KEY",name);
        bundle.putString("CATEGORY_KEY",category);
        bundle.putString("LEVEL_KEY",level);
        bundle.putString("RECIPE_KEY",recipe);
        bundle.putString("URI_KEY", uri);

        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);
        recipeFragment.show(fragmentManager,"mTag");
    }
}
