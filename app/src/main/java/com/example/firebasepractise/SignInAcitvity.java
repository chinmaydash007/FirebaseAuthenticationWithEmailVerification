package com.example.firebasepractise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInAcitvity extends AppCompatActivity {
    private static final String TAG = "test";
    EditText email_editext, password_edittext;
    Button signin_btn, signUp_btn;
    FirebaseAuth firebaseAuth;
    TextView verification_email_textview;


    FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitvity);

        setupAuthStateListerner();


        email_editext = findViewById(R.id.email);
        password_edittext = findViewById(R.id.password);
        signin_btn = findViewById(R.id.signIn_btn);
        signUp_btn = findViewById(R.id.signUp_btn);

        verification_email_textview = findViewById(R.id.verification_email_textview);

        firebaseAuth = FirebaseAuth.getInstance();

        setupAuthStateListerner();

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_editext.getText().toString();
                String password = password_edittext.getText().toString();

                if (!isEmpty(email) && !isEmpty(password)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.d(TAG, authResult.getUser().getUid());
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                sendUserToMainActivity();
                            } else {
                                Toast.makeText(SignInAcitvity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignInAcitvity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SignInAcitvity.this, "All  feilds are necessary", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInAcitvity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        verification_email_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationEmail resendVerificationEmail = new ResendVerificationEmail();
                resendVerificationEmail.show(getSupportFragmentManager(), "Resend Verification Email Dialog");
            }
        });
    }

    private void setupAuthStateListerner() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, firebaseUser.getUid());
                    if (!firebaseUser.isEmailVerified()) {
                        Toast.makeText(SignInAcitvity.this, "Check your email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    private void sendUserToMainActivity() {

        Intent intent = new Intent(SignInAcitvity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public boolean isEmpty(String text) {
        return text.equals("");

    }

}
