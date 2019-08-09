package be.pxl.project.cookaid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RecipeDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    public static final String ARG_ITEM_LEVEL = "item_level";
    public static final String ARG_ITEM_URI = "item_uri";
    public static final String ARG_ITEM_CATEGORY = "item_category";
    public static final String ARG_ITEM_RECIPE = "item_recipe";

    private Recipe mRecipe;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            String id = getArguments().getString(ARG_ITEM_ID);
            String name = getArguments().getString(ARG_ITEM_NAME);
            String category = getArguments().getString(ARG_ITEM_CATEGORY);
            String level = getArguments().getString(ARG_ITEM_LEVEL);
            String recipe = getArguments().getString(ARG_ITEM_RECIPE);
            String uri = getArguments().getString(ARG_ITEM_URI);

            mRecipe = new Recipe(id, name, category, level, recipe, uri);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (mRecipe != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(mRecipe.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into((ImageView) rootView.findViewById(R.id.recipe_detail_image));
                }
            });

            ((TextView) rootView.findViewById(R.id.recipe_detail_name)).setText(mRecipe.getName());
            ((TextView) rootView.findViewById(R.id.recipe_detail_recipe)).setText(mRecipe.getRecipe());
            ((TextView) rootView.findViewById(R.id.recipe_detail_category)).setText(mRecipe.getCategory());
            ((TextView) rootView.findViewById(R.id.recipe_detail_level)).setText(mRecipe.getLevel());
        }

        return rootView;
    }
}
