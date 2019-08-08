package be.pxl.project.cookaid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import org.w3c.dom.Text;

import be.pxl.project.cookaid.dummy.DummyContent;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    public static final String ARG_ITEM_LEVEL = "item_level";
    public static final String ARG_ITEM_URI = "item_uri";
    public static final String ARG_ITEM_CATEGORY = "item_category";
    public static final String ARG_ITEM_RECIPE = "item_recipe";

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            String id = getArguments().getString(ARG_ITEM_ID);
            String name = getArguments().getString(ARG_ITEM_NAME);
            String category = getArguments().getString(ARG_ITEM_CATEGORY);
            String level = getArguments().getString(ARG_ITEM_LEVEL);
            String recipe = getArguments().getString(ARG_ITEM_RECIPE);
            String uri = getArguments().getString(ARG_ITEM_URI);

            mItem = new Recipe(id, name, category, level, recipe, uri);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(mItem.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into((ImageView) rootView.findViewById(R.id.recipe_detail_image));
                }
            });

            ((TextView) rootView.findViewById(R.id.recipe_detail_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.recipe_detail_recipe)).setText(mItem.getRecipe());
            ((TextView) rootView.findViewById(R.id.recipe_detail_category)).setText(mItem.getCategory());
            ((TextView) rootView.findViewById(R.id.recipe_detail_level)).setText(mItem.getLevel());
        }

        return rootView;
    }
}
