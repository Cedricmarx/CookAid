package be.pxl.project.cookaid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SearchRecipeAdapter extends ArrayAdapter<Recipe> {
    public SearchRecipeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Recipe> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull final ViewGroup parent) {
        final Recipe recipe = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        final TextView cardTextView = convertView.findViewById(R.id.helloText);
        cardTextView.setText(recipe.getName());

        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(recipe.getUri()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable drawable = new BitmapDrawable(parent.getResources(), bitmap);
                cardTextView.setBackground(drawable);
            }
        });

        return convertView;
    }
}
