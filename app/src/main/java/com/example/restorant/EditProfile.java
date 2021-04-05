package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.restorant.model.UserID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//============================
// EditProfile.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 23.12.2020
// Purpose: It is the activity that provides profile editing.
//
// --------------------------------------
public class EditProfile extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText new_name_editText,new_phone_editText;
    DocumentReference userRef = firebaseFirestore.collection("users").document(firebaseAuth.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        new_name_editText = findViewById(R.id.editprofileName);
        new_phone_editText = findViewById(R.id.editprofileNumber);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()){
                    new_name_editText.setText(task.getResult().getString("name"));
                    new_phone_editText.setText(task.getResult().getString("phone"));
                }
            }
        });

    }

    // -------------------------------------------------
    // Function name: savenewname
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 23.12.2020
    // Date of last modification: 23.12.2020
    // Arguments: View
    // Description: OnClick implementation of EditProfile activity save button.
    //              This function provides to update profile information.
    // --------------------------------------------------
    public void savenewName(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
        alert.setTitle("New profile name");
        alert.setMessage("Do you accept the profile name change?");
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference colref = firebaseFirestore.collection("users").document(firebaseAuth.getUid());
                UserID newname = new UserID(firebaseAuth.getUid(),new_name_editText.getText().toString(),new_phone_editText.getText().toString());
                colref.set(newname);
                Intent intent = new Intent(EditProfile.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
}