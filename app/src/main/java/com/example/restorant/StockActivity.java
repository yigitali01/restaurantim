package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restorant.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

//============================
// StockActivity.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 18.12.2020
// Purpose: Through this activity, the user can edit or delete the items in stock.
//
// --------------------------------------
public class StockActivity extends AppCompatActivity implements StockAdapter.stockListener  {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getUid();
    private CollectionReference productRef = firebaseFirestore.collection("users").document(userId).collection("products");
    StockAdapter stockAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        setUpRecyclerView();
    }


    // -------------------------------------------------
    // Function name: onMove
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 18.12.2020
    // Date of last modification: 18.12.2020
    // Arguments: RecyclerView, ViewHolder, ViewHolder
    // Description: Function that adds swipe to delete functionality to rows.
    // --------------------------------------------------
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT){
                    Toast.makeText(getApplicationContext(),"Deleting...",Toast.LENGTH_SHORT);
                    StockAdapter.stockviewHolder stockviewHolder = (StockAdapter.stockviewHolder) viewHolder;
                    stockviewHolder.DeleteItem(viewHolder.getAdapterPosition());
//                    viewHolder.getOldPosition();
//                    MenuAdapter.MenuviewHolder menuviewHolder = (MenuAdapter.MenuviewHolder) viewHolder;
//                    menuviewHolder.DeleteItem(viewHolder.getAdapterPosition());

                }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(StockActivity.this, R.color.design_default_color_error))
                    .addActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    // -------------------------------------------------
    // Function name: setUpRecyclerView
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 19.12.2020
    // Date of last modification: 19.12.2020
    // Arguments:
    // Description: Function that make connection between recyclerviewAdapter class and Activty.
    // --------------------------------------------------
   private void setUpRecyclerView(){
        Query query = productRef.orderBy("productName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();

        stockAdapter = new StockAdapter(options,this);
        RecyclerView stockrecyclerView = findViewById(R.id.stockrecyclerView);
        stockrecyclerView.setHasFixedSize(true);
        stockrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockrecyclerView.setAdapter(stockAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(stockrecyclerView);

    }




    public void searchData(String s){

    }

    // -------------------------------------------------
    // Function name: handleUpdate
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 19.12.2020
    // Date of last modification: 19.12.2020
    // Arguments: DocumentSnapshot
    // Description: Function that provides product data update.
    // --------------------------------------------------
    @Override
    public void handleUpdate(final DocumentSnapshot snapshot) {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView productnameText = new TextView(this);
        productnameText.setText("Product Name:");
        productnameText.setTextSize(18);
        productnameText.setTypeface(Typeface.DEFAULT_BOLD);
        productnameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        productnameText.setTextColor(0xFF444444);
        layout.addView(productnameText);

        final EditText productName = new EditText(this);
        productName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(productName);

        final TextView productamountText = new TextView(this);
        productamountText.setText("Product Amount:");
        productamountText.setTextSize(18);
        productamountText.setTypeface(Typeface.DEFAULT_BOLD);
        productamountText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        productamountText.setTextColor(0xFF444444);
        layout.addView(productamountText);

        final EditText productAmount = new EditText(this);
        productAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(productAmount);

        final TextView productpriceText = new TextView(this);
        productpriceText.setText("Product Price:");
        productpriceText.setTextSize(18);
        productpriceText.setTypeface(Typeface.DEFAULT_BOLD);
        productpriceText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        productpriceText.setTextColor(0xFF444444);
        layout.addView(productpriceText);

        final EditText productPrice = new EditText(this);
        productPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(productPrice);

        final Product product = snapshot.toObject(Product.class);
        productName.setText(product.getProductName());
        productName.setSelection(product.getProductName().length());

        productAmount.setText(String.valueOf(product.getProductAmount()));
        productAmount.setSelection(String.valueOf(product.getProductAmount()).length());

        productPrice.setText(String.valueOf(product.getProductPrice()));
        productPrice.setSelection(String.valueOf(product.getProductPrice()).length());
        new AlertDialog.Builder(this).setTitle("Edit Product")
                .setView(layout)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newproductName = productName.getText().toString();
                        String newproductAmount = productAmount.getText().toString();
                        String newproductPrice = productPrice.getText().toString();

                        product.setProductName(newproductName);
                        product.setProductAmount(Integer.parseInt(newproductAmount));
                        product.setProductPrice(Integer.parseInt(newproductPrice));

                        snapshot.getReference().set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }).setNegativeButton("CANCEL",null).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        stockAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stockAdapter.stopListening();
    }



}

class StockAdapter extends FirestoreRecyclerAdapter<Product, StockAdapter.stockviewHolder>{
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getUid();
    private CollectionReference productRef = firebaseFirestore.collection("users").document(userId).collection("products");
    stockListener stockListener;
    public StockAdapter(@NonNull FirestoreRecyclerOptions<Product> options,stockListener stockListener) {
        super(options);
        this.stockListener =  stockListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull stockviewHolder holder, int position, @NonNull Product model) {
        holder.stockproductname.setText(model.getProductName());
        holder.stockproductAmount.setText(String.valueOf(model.getProductAmount()));
        holder.stockproductPrice.setText(String.valueOf(model.getProductPrice()));


    }

    @NonNull
    @Override
    public stockviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row_list,parent,false);
        return new stockviewHolder(v);
    }


    class stockviewHolder extends RecyclerView.ViewHolder{
        TextView stockproductname;
        TextView stockproductAmount;
        TextView stockproductPrice;
        public stockviewHolder(@NonNull View itemView) {
            super(itemView);

            stockproductname = itemView.findViewById(R.id.stock_product_name);
            stockproductAmount = itemView.findViewById(R.id.stock_product_number);
            stockproductPrice = itemView.findViewById(R.id.stock_product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    stockListener.handleUpdate(snapshot);
                }
            });

        }
        public void DeleteItem(int position){
            getSnapshots().getSnapshot(position).getReference().delete();
        }

        /*public void  UpdateItem(int position){
            getSnapshots().getSnapshot(position).getReference().update("productName",stockproductname,productRef.document(productId));
            getSnapshots().getSnapshot(position).getReference().update("productAmount",stockproductAmount,productRef.document(productId));
            getSnapshots().getSnapshot(position).getReference().update("productPrice",stockproductPrice,productRef.document(productId));
        }*/


    }
    interface stockListener{
        public void handleUpdate(DocumentSnapshot snapshot);

    }

}
