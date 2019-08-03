package be.pxl.project.cookaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }

        Button takePictureBtn = findViewById(R.id.take_picture_btn);
        Button searchRecipesBtn = findViewById(R.id.search_recipes_btn);
        Button savedRecipesBtn = findViewById(R.id.saved_recipes_btn);

        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dispatchTakePictureIntent();
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
            }
        });

        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchRecipeActivity.class));
            }
        });

        savedRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LikedRecipeActivity.class));
            }
        });


    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Intent intent = new Intent(HomeActivity.this, PostRecipeActivity.class);
//            intent.putExtra("BITMAP_IMAGE", imageBitmap);
//            startActivity(intent);
//        }
//    }
//
//    public void logoutTextView_clicked(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(HomeActivity.this, MainActivity.class));
//    }
}
