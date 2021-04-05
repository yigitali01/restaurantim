package com.example.restorant;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restorant.model.Basket;
import com.example.restorant.model.Table;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//============================
// BasketFragment.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 22.11.2020
// Purpose: The activity that displays the items in the basket on the screen.
//
// --------------------------------------
public class BasketFragment extends Fragment implements View.OnClickListener, BasketAdapter.BasketListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    BasketAdapter basketAdapter;
    CollectionReference colref = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("basket");
    Spinner spinner;
    CollectionReference spinnerRef = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("tables");
    List<String> tableList;
    ArrayAdapter<String> tablespinnerAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
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


    // -------------------------------------------------
    // Function name: onCreateView
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 22.11.2020
    // Date of last modification: 22.11.2020
    // Arguments: LayoutInflater, Viewgroup, Bundle
    // Description: This function works when the activity is first turned on. Table spinner was implemented here.
    // --------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_basket, container, false);
        RecyclerView basketrecyclerView = v.findViewById(R.id.basketrecyclerView);
        spinner = v.findViewById(R.id.basketSpinner);
       /* basketListFragment.add(new Basket("mehmet",5,10));
        basketListFragment.add(new Basket("Kebap",1,10));
        basketListFragment.add(new Basket("Su",2,10));*/

        Query query = colref.orderBy("product", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Basket> options = new FirestoreRecyclerOptions.Builder<Basket>()
                .setQuery(query, Basket.class)
                .build();
//        for (int i = 0; i < Singleton.getInstance().getBasketList().size(); i++) {
//            System.out.println(Singleton.getInstance().getBasketList().get(i));
//        }
//        basketListFragment = Singleton.getInstance().getBasketList();

        basketAdapter = new BasketAdapter(options,this);
        basketrecyclerView.setHasFixedSize(true);
        basketrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        basketrecyclerView.setAdapter(basketAdapter);

        spinner = v.findViewById(R.id.basketSpinner);
        tableList = new ArrayList<>();
        tablespinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tableList);
        tablespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(tablespinnerAdapter);
        final String selStr = "Select a table";

        Task<QuerySnapshot> querySnapshotTask = spinnerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tableList.add(selStr);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("no");
                        tableList.add(subject);
                    }
                    tablespinnerAdapter.notifyDataSetChanged();
                }
            }
        });

        //Create your buttons and set their onClickListener to "this"
        Button b1 =  v.findViewById(R.id.acceptBtn);
        b1.setOnClickListener(this);


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        basketAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        basketAdapter.stopListening();
    }

    // -------------------------------------------------
    // Function name: onClick
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 25.11.2020
    // Date of last modification: 25.11.2020
    // Arguments: LayoutInflater, View
    // Description: Implementation of BasketFragment accept button.
    // --------------------------------------------------
    @Override
    //implement the onClick method here
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.acceptBtn:
                spinnerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String selectedTable = spinner.getSelectedItem().toString();
                            for (final QueryDocumentSnapshot documentTable : task.getResult()) {
                                String subject = documentTable.getString("no");
                                Table table = documentTable.toObject(Table.class);
                                if (subject.matches(selectedTable)){
                                    table.setTableStatus(true);
                                    spinnerRef.document(documentTable.getId()).set(table).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    colref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            CollectionReference newBasketRef =  firebaseFirestore.collection("users").
                                                    document(firebaseAuth.getUid()).collection("tables")
                                                    .document(documentTable.getId())
                                                    .collection("basket");
                                            Basket basket = new Basket();
                                            if (task.isSuccessful()){
                                                for (QueryDocumentSnapshot documentBasket : task.getResult()) {
                                                    basket = documentBasket.toObject(Basket.class);
                                                    newBasketRef.document(basket.getProduct()).set(basket);
                                                    colref.document(documentBasket.getId()).delete();
                                                    TableAdapter tableAdapter;
                                                    Toast.makeText(getContext(),"Products added successfully",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                });

            //-----------------------------------------------------------//

                break;

                //...

        }
    }

    @Override
    public void handleUpdate(final DocumentSnapshot snapshot) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

//        final TextView basketamountText = new TextView(getActivity());
//        basketamountText.setText(":");
//        basketamountText.setTextSize(18);
//        basketamountText.setTypeface(Typeface.DEFAULT_BOLD);
//        basketamountText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        basketamountText.setTextColor(0xFF444444);
//        layout.addView(basketamountText);

        final EditText basketAmount = new EditText(getActivity());
        basketAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        basketAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(basketAmount);
        final Basket basket = snapshot.toObject(Basket.class);
        basketAmount.setText(String.valueOf(basket.getAmount()));
        basketAmount.setSelection(String.valueOf(basket.getAmount()).length());

        new AlertDialog.Builder(getActivity()).setTitle("Edit Amount")
                .setView(layout)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newproductAmount = basketAmount.getText().toString();

                        basket.setAmount(Integer.parseInt(newproductAmount));

                        snapshot.getReference().set(basket).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }).setNegativeButton("CANCEL",null).show();

    }
}

class BasketAdapter extends FirestoreRecyclerAdapter<Basket, BasketAdapter.basketviewHolder> {
    CollectionReference colRef;
    BasketListener basketListener;
    public static ArrayList<Basket> basketList = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BasketAdapter(@NonNull FirestoreRecyclerOptions<Basket> options,BasketListener basketListener) {
        super(options);
        this.basketListener = basketListener;
    }

    @NonNull
    @Override
    public basketviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_row_list, parent, false);
        return new basketviewHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull final basketviewHolder holder, int position, @NonNull final Basket model) {

        holder.basketrowName.setText(model.getProduct());
        holder.basketrowPrice.setText(String.valueOf(model.getTotalPrice()));
        holder.basketselectedAmount.setText(String.valueOf(model.getAmount()));

        holder.deleteproductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSnapshots().getSnapshot(holder.getAdapterPosition()).getReference().delete();
            }
        });
//        System.out.println(model.toString());

    }

    public class basketviewHolder extends RecyclerView.ViewHolder {
        TextView basketrowName,basketrowPrice,basketselectedAmount;
        ImageView deleteproductButton;

        public basketviewHolder(@NonNull View itemView) {
            super(itemView);
            basketrowName = itemView.findViewById(R.id.basket_product_name);
            basketrowPrice = itemView.findViewById(R.id.basket_product_price);
            basketselectedAmount = itemView.findViewById(R.id.basketselectAmount);
            deleteproductButton = itemView.findViewById(R.id.deletfromCart);
            basketselectedAmount.setInputType(InputType.TYPE_NULL);
            basketselectedAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                   basketListener.handleUpdate(snapshot);
                }
            });
        }


    }
    public interface BasketListener{
        public void handleUpdate(DocumentSnapshot snapshot);

    }
}