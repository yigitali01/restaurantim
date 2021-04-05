package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restorant.model.UserID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


//============================
// SignUp.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 01.11.2020
// Purpose: The activity that makes the process of becoming a member.
//
// --------------------------------------
public class SignUp extends AppCompatActivity {
    EditText eMail,emailRep,name,password,passwordRep,phone;
    CheckBox checkbox;
    FirebaseAuth firebaseAuth;
    UserID userID;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        eMail = findViewById(R.id.signupemail);
        emailRep = findViewById(R.id.signupemailrep);
        name = findViewById(R.id.signupusername);
        password = findViewById(R.id.signupPassword);
        passwordRep = findViewById(R.id.passwordrep);
        checkbox = findViewById(R.id.signupcheckBox);
        phone = findViewById(R.id.signupusernumber);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();
    }

    // -------------------------------------------------
    // Function name: signUpUser
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 01.11.2020
    // Date of last modification: 01.11.2020
    // Arguments: View
    // Description: Function that provides Sign up process.
    // --------------------------------------------------
    public void signupUser(View view){
       String emailText = eMail.getText().toString();
       String emailrepText = emailRep.getText().toString();
       String passwordText = password.getText().toString();
       String passwordrepText = passwordRep.getText().toString();
       String nameText = name.getText().toString();
       String userPhone = phone.getText().toString();

        if ((emailText.matches(""))||(emailrepText.matches(""))||
                (nameText.matches(""))||(passwordText.matches(""))||
                (passwordrepText.matches(""))|| (userPhone.matches("") ||
                (checkbox.isChecked()==false))){
            Toast error = Toast.makeText(getApplicationContext(),"Please enter the values properly.",Toast.LENGTH_LONG);
            error.show();
        }
        else if (!emailText.matches(emailrepText)){
            Toast error = Toast.makeText(getApplicationContext(),"E-mails aren't matched.",Toast.LENGTH_LONG);
            error.show();
        }
        else if (!passwordText.matches(passwordrepText)){
            Toast error = Toast.makeText(getApplicationContext(),"Passwords aren't matched.",Toast.LENGTH_LONG);
            error.show();
        }
        else if (passwordText.length()<8||passwordText.length()>21){
            Toast error = Toast.makeText(getApplicationContext(),"Your password must be at least 8 and at most 21 characters.",Toast.LENGTH_LONG);
            error.show();
        }
        else if(nameText.length()>35){
            Toast error = Toast.makeText(getApplicationContext(),"Your name can be up to 35 characters.",Toast.LENGTH_LONG);
            error.show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(),"The membership process is successful",Toast.LENGTH_LONG).show();
                    firebaseAuth.signInWithEmailAndPassword(eMail.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(),"Successful Login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this,MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                    userID = new UserID(firebaseAuth.getUid(),name.getText().toString(),phone.getText().toString());
                    firebaseFirestore.collection("users").document(firebaseAuth.getUid()).set(userID);

                }
            });

        }

    }


}