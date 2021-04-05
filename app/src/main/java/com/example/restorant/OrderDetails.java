package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

//============================
// OrderDetails.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 27.12.2020
// Purpose: Provides viewing of previously placed orders.
//
// --------------------------------------
public class OrderDetails extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference colref = firebaseFirestore.collection("users")
            .document(firebaseAuth.getUid()).collection("recentlyorders");
    RecyclerView ordersrecyclerView;
    OrderDetailsAdapter orderDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ordersrecyclerView = findViewById(R.id.orderdetailsrecyclerView);
        Query query = colref.orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<RecentlyOrders> options = new FirestoreRecyclerOptions.Builder<RecentlyOrders>()
                .setQuery(query, RecentlyOrders.class)
                .build();
        orderDetailsAdapter = new OrderDetailsAdapter(options);
        ordersrecyclerView.setHasFixedSize(true);
        ordersrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ordersrecyclerView.setAdapter(orderDetailsAdapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        orderDetailsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderDetailsAdapter.stopListening();
    }

}

class OrderDetailsAdapter extends FirestoreRecyclerAdapter<RecentlyOrders, OrderDetailsAdapter.OrderDetailsViewHolder>{

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrderDetailsAdapter(@NonNull FirestoreRecyclerOptions<RecentlyOrders> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position, @NonNull RecentlyOrders model) {
    holder.table.setText(model.getTable());
    String s = "";
    for (int i = 0; i<model.getBasket().size();i++){
        String product = String.valueOf(model.getBasket().get(i).getProduct());
        String price = String.valueOf(model.getBasket().get(i).getPrice());
        String total = String.valueOf(model.getBasket().get(i).getTotalPrice());
        String amount = String.valueOf(model.getBasket().get(i).getAmount());
        s+=     "--------------------"+"\n"
                +"Product: "+ product+"\n"
                +"Price: "+price+"\n"
                +"Total: "+total+"\n"
                +"Amount: "+amount+"pcs."+"\n";
    }
    holder.basket.setText(s);


    holder.date.setText(""+model.getDate());
    holder.bill.setText("Bill: "+String.valueOf(model.getTotalPrice()));

    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_row_list, parent, false);
        return new OrderDetailsViewHolder(v);
    }

    public class OrderDetailsViewHolder extends RecyclerView.ViewHolder{
        TextView date,basket,table,bill;
        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.orderDate);
            basket = itemView.findViewById(R.id.orderdetailsBasket);
            table = itemView.findViewById(R.id.orderdetailsTable);
            bill = itemView.findViewById(R.id.orderdetailsPrice);
        }
    }
}