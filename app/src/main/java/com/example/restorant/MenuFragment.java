package com.example.restorant;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.restorant.model.Basket;
import com.example.restorant.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//============================
// MenuFragment.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 26.11.2020
// Purpose: It allows the products in stock to be added to the basket.
//
// --------------------------------------
public class MenuFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getUid();
    private CollectionReference menuRef =  firebaseFirestore.collection("users").document(Objects.requireNonNull(userId)).collection("products");
    private CollectionReference productRef;
    //String productId = productRef.getId();

    MenuAdapter menuAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addproduct, container, false);

        //productRef = firebaseFirestore.collection("users").document(userId).collection("products");
        //firebaseFirestore.collection("users").document(userId).collection("menu").document().set(menu);
        Query query = menuRef.orderBy("productName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();

        menuAdapter = new MenuAdapter(options);
        RecyclerView menurecyclerView = v.findViewById(R.id.addProductRecyclerView);
        menurecyclerView.setHasFixedSize(true);
        menurecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        menurecyclerView.setAdapter(menuAdapter);


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        menuAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        menuAdapter.stopListening();
    }

}



class MenuAdapter extends FirestoreRecyclerAdapter<Product, MenuAdapter.MenuviewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private String modelproductName;
    //private int modelProductPrice;
    private String modelProductPrice;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference colRef;
    DocumentReference userRef;
    Basket basket;

    public MenuAdapter(@NonNull FirestoreRecyclerOptions<Product> options/*,List<ChoosedAmount> amounts*/) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MenuviewHolder holder, final int position, @NonNull final Product model) {
        modelproductName = model.getProductName();
        modelProductPrice = String.valueOf(model.getProductPrice());
        holder.menurowName.setText(modelproductName);
        holder.menurowPrice.setText(modelProductPrice);

        holder.addproductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.show();

                // --------------------- //
                colRef = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("basket");
                Task<QuerySnapshot> querySnapshotTask = colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int tmp = 0;
                            Basket tempBasket = new Basket();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (model.getProductName().matches(document.toObject(Basket.class).getProduct())) {
                                    tempBasket = document.toObject(Basket.class);
                                    tmp++;
                                }
                            }
                            if (tmp == 0) {
                                basket = new Basket(model.getProductName(),
                                        model.getChoosedAmount() + 1, model.getProductPrice());
//                                model.setProductAmount(model.getChoosedAmount()+1);
//                                firebaseFirestore.collection("users").
//                                        document(firebaseAuth.getUid()).collection("products").
//                                        document(getSnapshots().getSnapshot(hol)).set(model);
                                firebaseFirestore.collection("users").
                                        document(firebaseAuth.getUid()).collection("basket").
                                        document(holder.menurowName.getText().toString()).set(basket);

                            } else {
                                basket = tempBasket;
                                basket.setAmount(basket.getAmount() + 1);

                                firebaseFirestore.collection("users").
                                        document(firebaseAuth.getUid()).collection("basket").
                                        document(holder.menurowName.getText().toString()).set(basket);

                            }
                            dialog.dismiss();


                        }
                    }
                });
                // --------------------- //

            }
        });
    }

    /*@NonNull
        colref.



        @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }*/

    //    @Override
//    public void onClick(View v) {
//        // delete and reset adapter process:
//        // NewsFragment.product_adapter(getPosition());
////        Intent intent = new Intent(mContext, NewsDetailActivity.class);
////        intent.putExtra(NewsDetailActivity.NEWS_DETAIL_ID,String.valueOf(getPosition()));
////        mContext.startActivity(intent);
//    }


    @NonNull
    @Override
    public MenuviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_row_list, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        MenuviewHolder holder = new MenuviewHolder(v);
        return holder;
    }

    // btn id = addtoCart
    class MenuviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView menurowName, menurowPrice;
        EditText selectedAmount;
        ImageButton addproductButton;
        Button button;

        public MenuviewHolder(View itemView) {
            super(itemView);
//            System.out.println("QWEQWEASDASDASD");
            menurowName = itemView.findViewById(R.id.menu_product_name);
            menurowPrice = itemView.findViewById(R.id.menu_product_price);
//            selectedAmount = itemView.findViewById(R.id.menuselectAmount);
            addproductButton = itemView.findViewById(R.id.addtoCart);

//            System.out.println("GGGG:" + addproductButton);
            addproductButton.setOnClickListener(this);

        }

        public void DeleteItem(int position) {
            getSnapshots().getSnapshot(position).getReference();
        }

        @Override
        public void onClick(View v) {

        }
    }

    interface menuListener {
        public void handleUpdate(DocumentSnapshot snapshot);

    }
}
