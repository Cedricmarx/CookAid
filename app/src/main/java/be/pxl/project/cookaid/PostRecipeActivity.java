package be.pxl.project.cookaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PostRecipeActivity extends AppCompatActivity implements Validator.ValidationListener {
    private DatabaseReference mDatabase;
    private Bitmap mPreviewImage;
    private Validator mValidator;

    @NotEmpty
    private EditText mRecipeNameEditText;

    @NotEmpty
    private EditText mRecipeEditText;

    @NotEmpty
    private EditText mCategoryEditText;

    @NotEmpty
    private EditText mLevelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        String bitmapUri = getIntent().getStringExtra("BITMAP_IMAGE_PATH");
        mPreviewImage = BitmapFactory.decodeFile(bitmapUri);
        mPreviewImage = rotateBitmapByScreenOrientation(bitmapUri, mPreviewImage);
        Button postButton = findViewById(R.id.post_image_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecipeNameEditText = findViewById(R.id.recipe_name_edit_text);
        mRecipeEditText = findViewById(R.id.recipe_edit_text);
        ImageView previewImageView = findViewById(R.id.preview_image_view);
        mCategoryEditText = findViewById(R.id.category_edit_text);
        mLevelEditText = findViewById(R.id.level_edit_text);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        PushDownAnim.setPushDownAnimTo(postButton);

        previewImageView.setImageBitmap(mPreviewImage);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postButton_onClick(view);
            }
        });
    }

    private void postButton_onClick(View view) {
        mValidator.validate();
    }


    private Bitmap rotateBitmapByScreenOrientation(String bitmapUri, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(bitmapUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);
        Matrix matrix = new Matrix();
        if (rotation != 0f) {
            matrix.preRotate(rotationInDegrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return null;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void postRecipeToDatabase() {
        final String id = UUID.randomUUID().toString();
        final String name = mRecipeNameEditText.getText().toString();
        final String category = mCategoryEditText.getText().toString();
        final String level = mLevelEditText.getText().toString();
        final String recipe = mRecipeEditText.getText().toString();

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

    @Override
    public void onValidationSucceeded() {
        postRecipeToDatabase();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
