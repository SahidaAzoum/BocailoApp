package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bocailoapp.FragmentsAdmin.InicioAdmin;
import com.example.bocailoapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityCliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InicioAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioCliente);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return false;
    }
}
