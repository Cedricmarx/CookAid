package be.pxl.project.cookaid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class ShowCaptureActivity extends AppCompatActivity {

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] b = extras.getByteArray("capture");

        if (b != null) {
            ImageView image = findViewById(R.id.imageCaptured);

            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            rotateBitmap = rotate(decodeBitmap);


            image.setImageBitmap(rotateBitmap);

        }

        Uid = FirebaseAuth.getInstance().getUid();

        Button mPost = findViewById(R.id.post);

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToPosts();
            }
        });
    }

    private void saveToPosts() {

        final DatabaseReference postDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("post");
        final String key = postDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] dateToUpload = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = filePath.putBytes(dateToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                finish();
                return;
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });

    }

    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap, 0, 0, width, height, matrix, true);
    }
}
