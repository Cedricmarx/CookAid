package be.pxl.project.cookaid;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        TextView tx = findViewById(R.id.logoTextView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lovelo_Black.otf");
        tx.setTypeface(custom_font);
    }

    public void loginPageButton_Clicked(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void signUpPageButton_Clicked(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}
