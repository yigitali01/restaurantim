package com.example.restorant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restorant.model.Table;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//============================
// TablesFragment.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 15.11.2020
// Purpose: This activity provides to display tables.
//
// --------------------------------------
public class TablesFragment extends Fragment implements TableAdapter.tableListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getUid();
    private CollectionReference tableRef = firebaseFirestore.collection("users").document(userId).collection("tables");
    TableAdapter tableAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TablesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TablesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TablesFragment newInstance(String param1, String param2) {
        TablesFragment fragment = new TablesFragment();
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
        View v = inflater.inflate(R.layout.fragment_tables, container, false);
        Query query = tableRef.orderBy("no", Query.Direction.ASCENDING);
       // where(FieldPath.documentId(), "==", this.editSemId)

        FirestoreRecyclerOptions<Table> options = new FirestoreRecyclerOptions.Builder<Table>()
                .setQuery(query,Table.class)
                .build();

        tableAdapter = new TableAdapter(options,this);
        RecyclerView tablerecyclerView = v.findViewById(R.id.tablesrecyclerView);
        tablerecyclerView.setHasFixedSize(true);
        tablerecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        tablerecyclerView.setAdapter(tableAdapter);
        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        tableAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        tableAdapter.stopListening();
    }

    // -------------------------------------------------
    // Function name: handleUpdate
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 15.11.2020
    // Date of last modification: 15.11.2020
    // Arguments: DocumentSnapshot
    // Description: This function provides to update table name.
    // --------------------------------------------------
    @Override
    public void handleUpdate(final DocumentSnapshot snapshot) {
        final Table table = snapshot.toObject(Table.class);
        final EditText tablenumber = new EditText(getActivity());
        tablenumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tablenumber.setText(table.getNo());
        AlertDialog.Builder updatetable = new AlertDialog.Builder(getActivity());
        updatetable.setTitle("Update");
        updatetable.setMessage("Please enter the new table value");
        updatetable.setView(tablenumber);
        updatetable.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newtableNumber = tablenumber.getText().toString();
                table.setNo(newtableNumber);
                snapshot.getReference().set(table);
            }
        }).setNegativeButton("CANCEL",null);
        updatetable.show();

    }


    // -------------------------------------------------
    // Function name: handleDelete
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 16.11.2020
    // Date of last modification: 16.11.2020
    // Arguments: DocumentSnapshot
    // Description: This function provides to delte table.
    // --------------------------------------------------
    @Override
    public void handleDelete(DocumentSnapshot snapshot) {
        snapshot.getReference().delete();
    }


    // -------------------------------------------------
    // Function name: handleopenTable
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 17.11.2020
    // Date of last modification: 17.11.2020
    // Arguments: DocumentSnapshot
    // Description: This function provides to open selected table products.
    // --------------------------------------------------
    @Override
    public void handleopenTable(DocumentSnapshot snapshot) {
        Intent intent = new Intent(getContext(),TableDisplay.class);
        startActivity(intent);
//       TextView textView= getView().findViewById(R.id.tablenameDisplayer);
//       textView.setText(snapshot.getString("no"));
    }



}


//============================
// TablesFragment.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 18.11.2020
// Purpose: This class provides to make connection between database, row design and recyclerview.
//
// --------------------------------------
class TableAdapter extends FirestoreRecyclerAdapter<Table, TableAdapter.tableviewHolder> {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    tableListener tableListener;
    public TableAdapter(@NonNull FirestoreRecyclerOptions<Table> options, tableListener tableListener) {
        super(options);
        this.tableListener = tableListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final TableAdapter.tableviewHolder holder, int position, @NonNull Table model) {
        holder.tableNo.setText(model.getNo());
        if (model.getTableStatus().equals(true)){
            holder.itemView.setBackgroundColor(Color.RED);

        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#16AB1C"));
        }

    }

    @NonNull
    @Override
    public tableviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_list,parent,false);
        return new tableviewHolder(v);
    }


    class tableviewHolder extends RecyclerView.ViewHolder  {

        CollectionReference tableRef = firebaseFirestore.collection("users")
                .document(firebaseAuth.getUid()).collection("tables");
        TextView tableNo;
        public tableviewHolder(@NonNull final View itemView) {
            super(itemView);
            tableNo = itemView.findViewById(R.id.tableNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    Table table = snapshot.toObject(Table.class);
                    if (table.getTableStatus().equals(true)){
                        CollectionReference ref = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("tables");
                        String no =  snapshot.getId();
                        //Table ismini veritabanından çekerek yazdırdık şimdi ise collection ın id sini çekeceğiz.
                        Intent i = new Intent (itemView.getContext(), TableDisplay.class);
                        i.putExtra("username", no);
                        itemView.getContext().startActivity(i);
                    }
                   else {
                       Toast.makeText(v.getContext(),"This table is empty",Toast.LENGTH_LONG).show();
                    }

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    Table table = snapshot.toObject(Table.class);
                    if (table.getTableStatus().equals(false)){
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.inflate(R.menu.table_popup_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.popup_updateItem:
                                        tableListener.handleUpdate(snapshot);
                                        return true;
                                    case R.id.popup_deleteItem:
                                        DeleteItem();
                                        return true;
                                    default:
                                        return false;
                                }

                            }
                        });
                        popupMenu.show();



                    }else {

                        Toast.makeText(v.getContext(),"You cannot edit the table while it's filled.",Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }

            });
        }

        public void DeleteItem(){
            tableListener.handleDelete(getSnapshots().getSnapshot(getAdapterPosition()));
        }




    }
    interface tableListener{
        public void handleUpdate(DocumentSnapshot snapshot);
        public void handleDelete(DocumentSnapshot snapshot);
        public void handleopenTable(DocumentSnapshot snapshot);
    }
}