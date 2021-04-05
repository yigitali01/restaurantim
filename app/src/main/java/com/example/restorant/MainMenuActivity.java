package com.example.restorant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


//============================
// MainMenuActivity.java (-2)
// Creator Name: Yiğit Ali Delibudak
// Date of Creation: 09.11.2020
// Purpose: It is the screen that opens after logging in.
//         It provides to the display of fragments and add functionality to bottom navigation bar.
//
// --------------------------------------
public class MainMenuActivity extends AppCompatActivity {


    // -------------------------------------------------
    // Function name: onCreate
    // Creator Name: Yiğit Ali Delibudak
    // Date of Creation: 09.11.2020
    // Date of last modification: 09.11.2020
    // Arguments: Bundle
    // Description: Function that adds functionality to bottom navigation bar clicking actions.
    // --------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        BottomNavigationView btnNav = findViewById(R.id.bottom_navigation_view);
        btnNav.setOnNavigationItemSelectedListener(navListener);

        //Setting starting fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout,new TablesFragment()).commit();
    }
    public BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.productsItem:
                    selectedFragment = new MenuFragment();
                    break;
                case R.id.tablesItem:
                    selectedFragment = new TablesFragment();
                    break;
                case R.id.basketItem:
                    selectedFragment = new BasketFragment();
                    break;
                case R.id.optionsItem:
                    selectedFragment = new OptionsFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout,selectedFragment)
                    .commit();
            return true;
        }
    };

}