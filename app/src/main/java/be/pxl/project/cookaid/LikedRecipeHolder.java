package be.pxl.project.cookaid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LikedRecipeHolder extends RecyclerView.ViewHolder {

    TextView recipeName;
    TextView recipeLevel;
    ImageView recipeImage;

    public LikedRecipeHolder(View itemView) {
        super(itemView);
        recipeName = itemView.findViewById(R.id.recipe_name);
        recipeLevel = itemView.findViewById(R.id.recipe_level);
        recipeImage = itemView.findViewById(R.id.recipe_image);

    }
}
