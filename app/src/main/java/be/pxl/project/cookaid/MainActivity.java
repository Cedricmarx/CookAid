package be.pxl.project.cookaid;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,}, 1);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        Button loginPageBtn = findViewById(R.id.login_page_button);
        Button signUpPageBtn = findViewById(R.id.sign_up_page_button);

        PushDownAnim.setPushDownAnimTo(loginPageBtn, signUpPageBtn);
    }

    public void loginPageButton_Clicked(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void signUpPageButton_Clicked(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
}
