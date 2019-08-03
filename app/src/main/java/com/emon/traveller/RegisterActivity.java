package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmailEdit, mPasswordEdit, mNameET;
    private CircleImageView profileCIv;
    private Button mRegister;
    private Button mLogin;
    private TextView gotoLogin;
    private FirebaseAuth mAuth;

    String name;
    private static final int PICK_IMAGE_REQUEST = 21;
    Uri filepath;
    // String sdownload_url;

    ProgressDialog pd;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEdit = (EditText) findViewById(R.id.email);
        mPasswordEdit = (EditText) findViewById(R.id.password);
        mNameET = findViewById(R.id.name);
        profileCIv = findViewById(R.id.profileCIv);

        mRegister = (Button) findViewById(R.id.register);
        mLogin = (Button) findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setTitle("Please wait");
                pd.setMessage("Loading....");
                pd.show();
                name = mNameET.getText().toString().trim();
                String email = mEmailEdit.getText().toString().trim();
                String password = mPasswordEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if (filepath != null) {
                                    StorageReference reference = storageReference.child("ProfileImages/" + UUID.randomUUID().toString());
                                    reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!urlTask.isSuccessful()) ;
                                            Uri downloadUrl = urlTask.getResult();

                                            final String sdownload_url = String.valueOf(downloadUrl);
                                            //  Toast.makeText(RegisterActivity.this, String.valueOf(downloadUrl), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("name", name);
                                            intent.putExtra("profilepic", sdownload_url);
                                            startActivity(intent);
                                            finish();
                                            pd.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        profileCIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(RegisterActivity.this);
            }
        });

    }


    public void forget(View view) {
        startActivity(new Intent(RegisterActivity.this, ForgetPassActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                filepath = resultUri;
                profileCIv.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
