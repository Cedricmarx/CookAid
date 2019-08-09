package be.pxl.project.cookaid;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchRecipeAdapter extends ArrayAdapter<Recipe> {
    private Context mContext;
    private List<Recipe> mRecipeList;

    SearchRecipeAdapter(@NonNull Context context, @NonNull List<Recipe> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mRecipeList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull final ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        }

        final Recipe recipe = mRecipeList.get(position);

        if (recipe != null) {
            final TextView cardTextView = listItem.findViewById(R.id.helloText);
            cardTextView.setText(recipe.getName());

            final ImageView recipeImageView = listItem.findViewById(R.id.recipe_image_view);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(recipe.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(recipeImageView);
                }
            });
        }
        return listItem;
    }
}
