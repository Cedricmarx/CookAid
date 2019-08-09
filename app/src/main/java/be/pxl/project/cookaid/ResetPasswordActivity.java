package be.pxl.project.cookaid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mInputEmail;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mInputEmail = findViewById(R.id.email);
        Button btnReset = findViewById(R.id.btn_reset_password);
        mProgressBar = findViewById(R.id.progressBar);

        PushDownAnim.setPushDownAnimTo(btnReset);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mInputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}
