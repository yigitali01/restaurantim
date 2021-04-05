package com.example.restorant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//============================
// OptionsFragment.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 01.12.2020
// Purpose: The options screen is implemented in this class.
//
// --------------------------------------
public class OptionsFragment extends Fragment {
    ListView optionslistView;
    ArrayList<String> optionsarrayList;
    ArrayAdapter<String> optionsarrayAdapter;
    FirebaseAuth firebaseAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OptionsFragment newInstance(String param1, String param2) {
        OptionsFragment fragment = new OptionsFragment();
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
    // Date of Creation: 01.12.2020
    // Date of last modification: 01.12.2020
    // Arguments: LayoutInflater, Viewgroup, Bundle
    // Description: This function works when the activity is first turned on. List view onClick operations
    //              was implemented here.
    // --------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        optionslistView = v.findViewById(R.id.optionsList);
        optionsarrayList = new ArrayList<>();
        optionsarrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.
                                        simple_list_item_activated_1,optionsarrayList);
        optionslistView.setAdapter(optionsarrayAdapter);

        optionsarrayList.add("Add Table");
        optionsarrayList.add("Add Product");
        optionsarrayList.add("Stock Informations");
        optionsarrayList.add("Recently Orders");
        optionsarrayList.add("Edit Profile");
        optionsarrayList.add("Log Out");

        optionslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (optionsarrayList.get(i).toString() == "Add Table"){
                    Intent intent = new Intent(getActivity(), AddTable.class);
                    startActivity(intent);
                }
                else if (optionsarrayList.get(i).toString() == "Add Product"){
                    Intent intent = new Intent(getActivity(), AddProduct.class);
                    startActivity(intent);
                }
                else if (optionsarrayList.get(i).toString() == "Stock Informations"){
                    Intent intent = new Intent(getActivity(),StockActivity.class);
                    startActivity(intent);

                }
                else if (optionsarrayList.get(i).toString() == "Recently Orders"){
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    startActivity(intent);
                }
                else if (optionsarrayList.get(i).toString() == "Edit Profile"){
                    Intent intent = new Intent(getActivity(), EditProfile.class);
                    startActivity(intent);
                }
                else if (optionsarrayList.get(i).toString() == "Log Out"){
                    AlertDialog.Builder logoutAlert = new AlertDialog.Builder(getActivity());
                    logoutAlert.setTitle("Log Out");
                    logoutAlert.setMessage("Do you want to log out?");
                    logoutAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseAuth.signOut();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            Toast.makeText(getContext(),"Log-Out is successful.",Toast.LENGTH_LONG).show();
                        }

                    });
                    logoutAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    logoutAlert.show();
                     }
            }
        });

        return v;
    }


}