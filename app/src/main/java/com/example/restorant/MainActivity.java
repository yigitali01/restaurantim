package com.example.restorant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


//============================
// MainActivity.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 20.10.2020
// Purpose: It is the home screen of the application.
//
// --------------------------------------



public class MainActivity extends AppCompatActivity {
    Button signUp, logIn;
    FirebaseAuth firebaseAuth;
    LogIn loginInfo;

    // -------------------------------------------------
    // Function name: onCreate
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 20.10.2020
    // Date of last modification: 20.10.2020
    // Arguments: Bundle
    // Description: This is the default function of android studio AppCompatActivity.
    //             The functions that run when the activity is first opened are written here.
    // --------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.firstscreensignUp);
        logIn = findViewById(R.id.firstscreensignIn);
        loginInfo = new LogIn();
        FirebaseUser firebaseUser = loginInfo.loginauth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

    }


    // -------------------------------------------------
    // Function name: logInPage
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 21.10.2020
    // Date of last modification: 21.10.2020
    // Arguments: View
    // Description: Function that opens log in page after clicking log-in button.
    // --------------------------------------------------

    public void logInPage(View view) {
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
        //finish();
    }

    // -------------------------------------------------
    // Function name: signUpPage
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 21.10.2020
    // Date of last modification: 21.10.2020
    // Arguments: View
    // Description: Function that opens sign up page after clicking sign up button.
    // --------------------------------------------------

    public void signUpPage(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
        //finish();
    }
}