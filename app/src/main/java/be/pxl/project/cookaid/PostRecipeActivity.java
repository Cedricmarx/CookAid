package be.pxl.project.cookaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostRecipeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private List<String> mLevelList, mCategoryList;
    private EditText mRecipeNameEditText, mRecipeEditText, mCategoryEditText, mLevelEditText;
    private Bitmap mPreviewImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        mPreviewImage = getIntent().getParcelableExtra("BITMAP_IMAGE");
        mLevelList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        Button postButton = findViewById(R.id.post_image_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecipeNameEditText = findViewById(R.id.recipe_name_edit_text);
        mRecipeEditText = findViewById(R.id.recipe_edit_text);
        ImageView previewImageView = findViewById(R.id.preview_image_view);
        mCategoryEditText = findViewById(R.id.category_edit_text);
        mLevelEditText = findViewById(R.id.level_edit_text);

        ValueEventListener levelsListener = getValueEventListener("levels");
        ValueEventListener categoryListener = getValueEventListener("categories");

        mDatabase.addValueEventListener(levelsListener);
        mDatabase.addValueEventListener(categoryListener);
        previewImageView.setImageBitmap(mPreviewImage);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRecipeToDatabase();
            }
        });
    }

    private void postRecipeToDatabase() {
        final String id = UUID.randomUUID().toString();
        final String name = mRecipeNameEditText.getText().toString();
        final String category = mCategoryEditText.getText().toString();
        final String level = mLevelEditText.getText().toString();
        final String recipe = mRecipeEditText.getText().toString();

        // Photo to Byte[]
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + name + UUID.randomUUID().toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mPreviewImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Recipe tempRecipe = new Recipe(id, name, category, level, recipe, taskSnapshot.getMetadata().getPath());
                String key = mDatabase.child("recipes").push().getKey();
                mDatabase.child("recipes").child(key).setValue(tempRecipe);
                startActivity(new Intent(PostRecipeActivity.this, HomeActivity.class));
            }
        });
    }

    private ValueEventListener getValueEventListener(final String value) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child(value).getChildren()) {
                    if (ds.getValue() != null) {
                        if (value == "levels") {
                            mLevelList.add(ds.getValue().toString());
                        } else {
                            mCategoryList.add(ds.getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
