package be.pxl.project.cookaid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LikedRecipeAdapter extends RecyclerView.Adapter<LikedRecipeAdapter.LikedRecipeViewHolder> {

    private ArrayList<Recipe> mLikedRecipeList;
    private StorageReference mStorageReference;
    private Resources res;
    final long ONE_MEGABYTE = 1024 * 1024;

    public static class LikedRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;


        public LikedRecipeViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.recipe_image);
            mTextView = itemView.findViewById(R.id.recipe_recipe);
        }
    }


    public LikedRecipeAdapter(ArrayList<Recipe> likedRecipeList){
            mLikedRecipeList = likedRecipeList;
    }


    @NonNull
    @Override
    public LikedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);
        LikedRecipeViewHolder likedRecipeViewHolder = new LikedRecipeViewHolder(view);
        res = viewGroup.getContext().getResources();
        return likedRecipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LikedRecipeViewHolder likedRecipeViewHolder, int i) {
       Recipe currentRecipe = mLikedRecipeList.get(i);

/*
       mStorageReference.child(currentRecipe.getUri()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {
               Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
               Drawable drawable = new BitmapDrawable(bitmap);
               likedRecipeViewHolder.mImageView.setBackground(res, drawable);
               Glid
           }
       });
*/

       likedRecipeViewHolder.mTextView.setText(currentRecipe.getName());

    }

    @Override
    public int getItemCount() {
        return mLikedRecipeList.size();
    }
}
