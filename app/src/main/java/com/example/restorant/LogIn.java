package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


//============================
// LogIn.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 28.10.2020
// Purpose: The activity that enables the log-in process.
//
// --------------------------------------


public class LogIn extends AppCompatActivity {
    EditText loginmail;
    EditText password;
    FirebaseAuth loginauth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginmail = findViewById(R.id.entermail);
        password = findViewById(R.id.loginPassword);

        loginauth = FirebaseAuth.getInstance();

        System.out.println(loginauth);

    }

    // -------------------------------------------------
    // Function name: loginUser
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 28.10.2020
    // Date of last modification: 28.10.2020
    // Arguments: View
    // Description: Function that provides log in process.
    // --------------------------------------------------
    public void loginUser(View view){

        String loginmailText = loginmail.getText().toString();
        String loginpasswordText = password.getText().toString();
        if((loginmailText.matches(""))||(loginpasswordText.matches(""))){
            Toast logInErrorToast = Toast.makeText(getApplicationContext(),"Please enter the informations correctly for login.",Toast.LENGTH_LONG);
            logInErrorToast.show();
        }
        else{
            loginauth.signInWithEmailAndPassword(loginmailText,loginpasswordText).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(),"Successful Login",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LogIn.this,MainMenuActivity.class);
                    // System.out.println("ffffff"+loginauth.getUid());
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}