package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        editText = findViewById(R.id.emailforget);
        button = findViewById(R.id.reset_btn);
        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPassActivity.this, "Password reset link send. check your mail", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String messege = task.getException().getMessage();
                                        Toast.makeText(ForgetPassActivity.this, "Error:" + messege, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ForgetPassActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
