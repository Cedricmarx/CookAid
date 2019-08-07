package be.pxl.project.cookaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
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
import java.io.IOException;
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

        String bitmapUri = getIntent().getStringExtra("BITMAP_IMAGE_PATH");
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapUri);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(bitmapUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);
        int deg = rotationInDegrees;
        Matrix matrix = new Matrix();
        if (rotation != 0f) {
            matrix.preRotate(rotationInDegrees);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int newWidth = size.x;

//Get actual width and height of image
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

// Calculate the ratio between height and width of Original Image
        float ratio = (float) height / (float) width;
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int newHeight = (int) ((int) (width * ratio)/scale);


        mPreviewImage = bitmap;
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
//        final double viewWidthToBitmapWidthRatio = (double)previewImageView.getWidth() / (double)bitmap.getWidth();
//        previewImageView.getLayoutParams().height = (int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);

        previewImageView.setImageBitmap(mPreviewImage);



        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRecipeToDatabase();
            }
        });
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
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
        mPreviewImage.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
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
                finish();
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
