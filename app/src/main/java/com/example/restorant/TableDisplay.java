package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restorant.model.Basket;
import com.example.restorant.model.Product;
import com.example.restorant.model.Table;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


//============================
// TableDisplay.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 03.01.2021
// Purpose: It allows the products on the tables to be displayed.
//
// --------------------------------------
public class TableDisplay extends AppCompatActivity implements TableDisplayAdapter.TableDisplayListener {
    RecyclerView tabledisplayRecyclerview;
    TableDisplayAdapter tableDisplayAdapter;
    TextView receipt;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference productRef = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("products");
    CollectionReference tableRef = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("tables");
    StringBuilder orderStr = new StringBuilder();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_display);

        Intent intent = getIntent();
        String docId = intent.getStringExtra("username");
        CollectionReference basketref = tableRef.document(docId).collection("basket");

        tabledisplayRecyclerview = findViewById(R.id.tabledisplayRecyclerview);
        receipt = findViewById(R.id.receipt);

        Query query = basketref.orderBy("product", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Basket> options = new FirestoreRecyclerOptions.Builder<Basket>()
                .setQuery(query, Basket.class)
                .build();
        tableDisplayAdapter = new TableDisplayAdapter(options,this);
        tabledisplayRecyclerview.setHasFixedSize(true);
        tabledisplayRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tabledisplayRecyclerview.setAdapter(tableDisplayAdapter);
        checkPrice(basketref);
    }


    // -------------------------------------------------
    // Function name: onResume
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 04.01.2021
    // Date of last modification: 04.01.2021
    // Arguments:
    // Description: Function that gets clicked table information from database.
    // --------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String docId = intent.getStringExtra("username");
        final CollectionReference basketref = tableRef.document(docId).collection("basket");
        final DocumentReference ref = tableRef.document(docId);
        basketref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (basketref.get().isSuccessful()){
                    if (queryDocumentSnapshots.isEmpty()){
                        Table table = ref.get().getResult().toObject(Table.class);
                        table.setTableStatus(false);
                        ref.set(table);
                    }

                }

            }
        });

    }

    @Override
    public void handleUpdate(final DocumentSnapshot snapshot) {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

//        final TextView basketamountText = new TextView(getActivity());
//        basketamountText.setText(":");
//        basketamountText.setTextSize(18);
//        basketamountText.setTypeface(Typeface.DEFAULT_BOLD);
//        basketamountText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        basketamountText.setTextColor(0xFF444444);
//        layout.addView(basketamountText);

        final EditText basketAmount = new EditText(getApplicationContext());
        basketAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        basketAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(basketAmount);
        final Basket basket = snapshot.toObject(Basket.class);
        basketAmount.setText(String.valueOf(basket.getAmount()));
        basketAmount.setSelection(String.valueOf(basket.getAmount()).length());

        new AlertDialog.Builder(this).setTitle("Edit Amount")
                .setView(layout)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newproductAmount = basketAmount.getText().toString();

                        basket.setAmount(Integer.parseInt(newproductAmount));

                        snapshot.getReference().set(basket).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = getIntent();
                                String docId = intent.getStringExtra("username");

                                CollectionReference basketref = tableRef.document(docId).collection("basket");
                                checkPrice(basketref);
                            }
                        });
                    }
                }).setNegativeButton("CANCEL",null).show();


    }

    @Override
    public void handleDelete(DocumentSnapshot snapshot) {
        snapshot.getReference().delete();
        Intent intent = getIntent();
        String docId = intent.getStringExtra("username");

        CollectionReference basketref = tableRef.document(docId).collection("basket");
        final DocumentReference tableDoc = tableRef.document(docId);
        basketref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    tableDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final Table table = documentSnapshot.toObject(Table.class);
                            table.setTableStatus(false);
                            tableDoc.set(table);
                        }
                    });
                }
            }
        });

        checkPrice(basketref);
    }


    // -------------------------------------------------
    // Function name: checkPrice
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 03.01.2021
    // Date of last modification: 03.01.2021
    // Arguments: CollectionReferance
    // Description: Function that calculates table bill
    // --------------------------------------------------
    public void checkPrice(CollectionReference collectionReference){

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    int tableCheck = 0;
                    for (DocumentSnapshot snapshot:task.getResult()){
                        String totalpriceField = snapshot.get("totalPrice").toString();
                        tableCheck += Integer.parseInt(totalpriceField);
                    }
                    receipt.setText("Total Price:  "+ String.valueOf(tableCheck)+" ₺");

                }
            }
        });

    }
    public String getDateTime() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        Date tarih = new Date();
        return String.valueOf(format.format(tarih));

    }
    // -------------------------------------------------
    // Function name: pay
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 05.01.2021
    // Date of last modification: 05.01.2021
    // Arguments: View
    // Description: OnClick implementation of TableDisplay activity check button.
    //             This function provides to reduce table items to the database.
    //             It also provides to add bill to the recentlyOrders
    // --------------------------------------------------
    public void pay(View view){

        Intent intent = getIntent();
        String docId = intent.getStringExtra("username");
        final CollectionReference basketref = tableRef.document(docId).collection("basket");
        final DocumentReference tableDoc = tableRef.document(docId);
        tableDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                final Table table = documentSnapshot.toObject(Table.class);
                basketref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int recipePrice = 0;
                            //    final Table table = tableDoc.get().getResult().toObject(Table.class);
                            final List<Basket>[] recentlyList = new List[]{new ArrayList<>()};
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                final Basket tempBasket = documentSnapshot.toObject(Basket.class);
                                int temp = tempBasket.getTotalPrice();
                                ;
                                recentlyList[0].add(tempBasket);
                                orderStr.append(tempBasket.toString());
                                productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot documentSnapshot1 : task.getResult()){
                                                Product tempProduct = documentSnapshot1.toObject(Product.class);
                                                if (tempBasket.getProduct().matches(tempProduct.getProductName())){
                                                    tempProduct.setProductAmount(tempProduct.getProductAmount() - tempBasket.getAmount());
                                                    productRef.document(documentSnapshot1.getId()).set(tempProduct);

                                                }

                                            }
                                        }

                                    }
                                });
                                recipePrice += temp;
                                basketref.document(documentSnapshot.getId()).delete();
                            }
                            String recentList = orderStr.toString();
                            checkPrice(basketref);
                            RecentlyOrders recentlyOrders = new RecentlyOrders(getDateTime(), recentlyList[0],table.getNo(),recipePrice);
                            firebaseFirestore.collection("users").document(firebaseAuth.getUid())
                                    .collection("recentlyorders").document().set(recentlyOrders);


                            productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (DocumentSnapshot productCheck: task.getResult()){
                                            Product tempProduct = productCheck.toObject(Product.class);
                                            if (tempProduct.getProductAmount()<=0){
                                                AlertDialog.Builder alert = new AlertDialog.Builder(TableDisplay.this);
                                                alert.setTitle("Stock Reduction");
                                                alert.setMessage("There are "+tempProduct.getProductAmount()+" "+tempProduct.getProductName()+"s"+" left.");
                                                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                alert.show();
                                            }
                                        }
                                    }
                                }
                            });
                            table.setTableStatus(false);
                            tableDoc.set(table);

                        }
                    }
                });

            }
        });

//        DocumentReference testDoc = firebaseFirestore.collection("users")
//                .document(firebaseAuth.getUid()).collection("recentlyorders")
//                .document("NBv7dSGBCzZeMx71EFVF");
//        RecentlyOrders test = testDoc.get().getResult().toObject(RecentlyOrders.class);
//        System.out.println("abccc:"+test.getBasket());
    }

    @Override
    public void onStart() {
        super.onStart();
        tableDisplayAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        tableDisplayAdapter.stopListening();
    }
}

class TableDisplayAdapter extends FirestoreRecyclerAdapter<Basket, TableDisplayAdapter.TableDisplayViewHolder>{
    TableDisplayListener tableDisplayListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TableDisplayAdapter(@NonNull FirestoreRecyclerOptions<Basket> options,TableDisplayListener tableDisplayListener) {
        super(options);
        this.tableDisplayListener = tableDisplayListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final TableDisplayViewHolder holder, int position, @NonNull Basket model) {

        holder.basketrownameTable.setText(model.getProduct());
        holder.basketrowpriceTable.setText(String.valueOf(model.getTotalPrice()));
        holder.basketselectedamountTable.setText(String.valueOf(model.getAmount()));


    }

    @NonNull
    @Override
    public TableDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_display_row_list, parent, false);
        return new TableDisplayViewHolder(v);
    }

    public class TableDisplayViewHolder extends RecyclerView.ViewHolder{

        TextView basketrownameTable,basketrowpriceTable,basketselectedamountTable;
        ImageView deleteproductbuttonTable;

      public TableDisplayViewHolder(@NonNull View itemView) {
          super(itemView);
          basketrownameTable = itemView.findViewById(R.id.basket_product_name_table);
          basketrowpriceTable = itemView.findViewById(R.id.basket_product_price_table);
          basketselectedamountTable = itemView.findViewById(R.id.basketselectAmount_table);
          deleteproductbuttonTable = itemView.findViewById(R.id.deletfromcart_table);
          basketselectedamountTable.setInputType(InputType.TYPE_NULL);
          basketselectedamountTable.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                  tableDisplayListener.handleUpdate(snapshot);
              }
          });
          deleteproductbuttonTable.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                  tableDisplayListener.handleDelete(snapshot);
              }
          });
      }
  }
  public interface TableDisplayListener{
      public void handleUpdate(DocumentSnapshot snapshot);
      public void handleDelete(DocumentSnapshot snapshot);
  }
}