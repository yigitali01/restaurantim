package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restorant.model.Table;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

//============================
// AddTable.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 12.12.2020
// Purpose: Table adding operation is performed in this activity.
//
// --------------------------------------
public class AddTable extends AppCompatActivity {
    EditText edittableNumber;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String userid;
    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);
        edittableNumber = findViewById(R.id.addtableNumber);
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getUid();
    }


    // -------------------------------------------------
    // Function name: addtableData
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 13.12.2020
    // Date of last modification: 13.12.2020
    // Arguments: View
    // Description: OnClick implementation of AddTable activity save button.
    //              This function provides to add table to the database.
    // --------------------------------------------------
    public void addtableData(View view) {
        CollectionReference reference = firebaseFirestore.collection("users").document(firebaseAuth.getUid())
                .collection("table");
    
        if (edittableNumber.getText().toString().matches("")) {
            Toast addtableError = Toast.makeText(getApplicationContext(), "Please enter valid table number.", Toast.LENGTH_LONG);
            addtableError.show();
        } else {
            AlertDialog.Builder validateaddTable = new AlertDialog.Builder(AddTable.this);
            validateaddTable.setTitle("New Table");
            validateaddTable.setMessage("Do you accept the new table entry?");
            validateaddTable.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    /*Map<String,Object> tables = new HashMap<>();
                   tables.put("userId",userid);
                    tables.put("Product Name",edittableNumber.getText().toString());*/

                    table = new Table(edittableNumber.getText().toString(), userid,false);

                    // -------------------------------- //
                    // table.addProductList();
                    // table.getProductList();
                    // -------------------------------- //
                    // System.out.println(table.toString());
                    // -------------------------------- //
                    progressDialog.setTitle("Please Wait.");
                    progressDialog.show();

                    firebaseFirestore.collection("users").document(userid).collection("tables")
                                     .document().set(table).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();

                            Intent intent = new Intent(AddTable.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //this will be called if there is any error while updating data.
                        }
                    });


                }
            });
            validateaddTable.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            validateaddTable.show();

        }
    }
}