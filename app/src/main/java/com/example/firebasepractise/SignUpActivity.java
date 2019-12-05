package com.example.firebasepractise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "test";
    EditText email_edittext, password_edittext, confirm_passwrd_edittext;
    Button signUp_btn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_edittext = findViewById(R.id.email);
        password_edittext = findViewById(R.id.password);
        confirm_passwrd_edittext = findViewById(R.id.confirm_password);
        signUp_btn = findViewById(R.id.signUp_btn);

        firebaseAuth=FirebaseAuth.getInstance();


        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edittext.getText().toString();
                String password = password_edittext.getText().toString();
                String confirm_password = confirm_passwrd_edittext.getText().toString();


                if (!isEmpty(email) && !isEmpty(password) && !isEmpty(confirm_password)) {
                    if (password.equals(confirm_password)) {
                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, authResult.getUser().getUid());
                                sendVerificationEmial();
                                firebaseAuth.signOut();
                                sendUserToSignInActivity();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(SignUpActivity.this, "Both the password should match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "All the fields are compulsory", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationEmial() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SignUpActivity.this, "Verifiaction mail has been send", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void sendUserToSignInActivity() {
        Intent intent=new Intent(SignUpActivity.this,SignInAcitvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public boolean isEmpty(String mystring) {
        return mystring.equals("");
    }
}
