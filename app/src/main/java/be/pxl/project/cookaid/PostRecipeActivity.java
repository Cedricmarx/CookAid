package be.pxl.project.cookaid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostRecipeActivity extends Activity {
    private DatabaseReference mDatabase;
    private List<String> mLevelList, mCategoryList;
    private Spinner mLevelSpinner, mCategorySpinner;
    private ImageView mPreviewImageView;
    private Button mPostButton;
    private EditText mRecipeNameEditText, mRecipeEditText;
    private Bitmap mPreviewImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        mPreviewImage = getIntent().getParcelableExtra("BITMAP_IMAGE");
        mLevelList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mPostButton = findViewById(R.id.post_image_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mLevelSpinner = findViewById(R.id.level_spinner);
        mCategorySpinner = findViewById(R.id.category_spinner);
        mRecipeNameEditText = findViewById(R.id.recipe_name_edit_text);
        mRecipeEditText = findViewById(R.id.recipe_edit_text);
        mPreviewImageView = findViewById(R.id.preview_image_view);

        ValueEventListener levelsListener = getValueEventListener("levels");
        ValueEventListener categoryListener = getValueEventListener("categories");

        mDatabase.addValueEventListener(levelsListener);
        mDatabase.addValueEventListener(categoryListener);

        setAdapterToSpinner(mLevelList, mLevelSpinner);
        setAdapterToSpinner(mCategoryList, mCategorySpinner);

        mPreviewImageView.setImageBitmap(mPreviewImage);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRecipeToDatabase();
            }
        });
    }

    private void postRecipeToDatabase() {
        String name = mRecipeNameEditText.getText().toString();
        String category = mCategorySpinner.getSelectedItem().toString();
        String level = mLevelSpinner.getSelectedItem().toString();
        String recipe = mRecipeEditText.getText().toString();

        // Photo to Byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mPreviewImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        Recipe tempRecipe = new Recipe(name, category, level, recipe, data);
        mDatabase.child("recipes").child(name).setValue(tempRecipe);
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

    private void setAdapterToSpinner(List<String> list, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
