package be.pxl.project.cookaid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class RecipeFragment extends DialogFragment {

    TextView recipeName;
    TextView recipeCategory;
    TextView recipeLevel;
    TextView recipeRecipe;
    ImageView recipeImage;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView=inflater.inflate(R.layout.recipe_detail, container, false);

        recipeName=rootView.findViewById(R.id.recipe_detail_name);
        recipeCategory=rootView.findViewById(R.id.recipe_detail_category);
        recipeLevel=rootView.findViewById(R.id.recipe_detail_level);
        recipeRecipe=rootView.findViewById(R.id.recipe_detail_recipe);
        recipeImage=rootView.findViewById(R.id.recipe_detail_image);


        String name = this.getArguments().getString("NAME_KEY");
        String category = this.getArguments().getString("CATEGORY_KEY");
        String level = this.getArguments().getString("LEVEL_KEY");
        String recipe = this.getArguments().getString("RECIPE_KEY");
        String uri = this.getArguments().getString("URI_KEY");


        recipeName.setText(name);
        recipeCategory.setText(category);
        recipeLevel.setText(level);
        recipeRecipe.setText(recipe);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(uri).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable drawable = new BitmapDrawable(rootView.getResources(), bitmap);
                recipeImage.setBackground(drawable);
            }
        });

        return rootView;
    }

}
