package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restorant.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//============================
// AddProduct.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 03.12.2020
// Purpose: Product adding operation is performed in this activity.
//
// --------------------------------------
@IgnoreExtraProperties
public class AddProduct extends AppCompatActivity {
    EditText editnameProduct;
    EditText editnumberProduct;
    EditText editpriceProduct;
    Product product;
    ProgressDialog progressDialog;
    String userid;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        editnameProduct = findViewById(R.id.additemName);
        editnumberProduct = findViewById(R.id.additemNumber);
        editpriceProduct = findViewById(R.id.additemPrice);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getUid();
        progressDialog = new ProgressDialog(this);

    }

    // -------------------------------------------------
    // Function name: getDateTime
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 05.12.2020
    // Date of last modification: 05.12.2020
    // Arguments:
    // Description: Function that gets current date
    // --------------------------------------------------
    public String getDateTime() {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        Date tarih = new Date();
        return String.valueOf(format.format(tarih));
    }

    // -------------------------------------------------
    // Function name: additemData
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 07.12.2020
    // Date of last modification: 07.12.2020
    // Arguments: View
    // Description: OnClick implementation of AddProduct activity save button.
    //              This function provides to add product to the database.
    // --------------------------------------------------
    public void additemData(View view) {

        if (editnameProduct.getText().toString().matches("") ||
                editnumberProduct.getText().toString().matches("")
                || editpriceProduct.getText().toString().matches("")) {
            Toast additemError = Toast.makeText(getApplicationContext(), "Lütfen Değerlerin Tamamını Doldurunuz.", Toast.LENGTH_LONG);
            additemError.show();

        } else {
            AlertDialog.Builder validateitemAlert = new AlertDialog.Builder(AddProduct.this);
            validateitemAlert.setTitle("Confirmation");
            validateitemAlert.setMessage("Do you approve the new product entry?");
            validateitemAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    progressDialog.setTitle("Please Wait.");
                    progressDialog.show();

                    CollectionReference col = firebaseFirestore.collection("users").document(userid).collection("products");
                    final String productid = col.document().getId();
                    product = new Product(editnameProduct.getText().toString(), Integer.parseInt(editnumberProduct.getText().toString()), Integer.parseInt(editpriceProduct.getText().toString()),0,productid );
                    col.document(productid).set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            System.out.println("Date : "+getDateTime());

                            Intent intent = new Intent(AddProduct.this, MainMenuActivity.class);

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
            validateitemAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            validateitemAlert.show();
        }
    }
}