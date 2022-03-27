package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bocailoapp.FragmentsAdmin.InicioAdmin;
import com.example.bocailoapp.FragmentsAdmin.RegistroAdmin;
import com.example.bocailoapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityAdministrador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawer_layout_Admin);

        NavigationView navigationView = findViewById(R.id.nav_view_Admin);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_Admin,new InicioAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioAdmin);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.InicioAdmin:

                break;
            case R.id.PerfilAdmin:

                break;
            case R.id.RegistrarAdmin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_Admin,new RegistroAdmin()).commit();
                break;
            case R.id.ListarAdmin:

                break;
            case R.id.Cocina:

                break;
            case R.id.Pedido:

                break;
            case R.id.HistoricoPedidos:

                break;
            case R.id.Salir:
                CerrarSesion();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //cambiar el activity al que va
    private void CerrarSesion(){
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivityAdministrador.this, MainActivityCliente.class));
        Toast.makeText(this, "Cerraste sesion exitosamente", Toast.LENGTH_SHORT).show();
    }
}