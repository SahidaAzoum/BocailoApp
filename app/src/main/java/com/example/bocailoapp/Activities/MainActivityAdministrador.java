package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bocailoapp.FragmentsAdmin.InicioAdmin;
import com.example.bocailoapp.FragmentsAdmin.RegistroAdmin;
import com.example.bocailoapp.FragmentsCompartidos.Costillar;
import com.example.bocailoapp.FragmentsCompartidos.Ensaladas;
import com.example.bocailoapp.FragmentsCompartidos.Entrantes;
import com.example.bocailoapp.FragmentsCompartidos.Hamburguesas;
import com.example.bocailoapp.FragmentsCompartidos.Perritos;
import com.example.bocailoapp.FragmentsCompartidos.Postres;
import com.example.bocailoapp.FragmentsCompartidos.Sandwiches;
import com.example.bocailoapp.FragmentsCompartidos.TexMex;
import com.example.bocailoapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityAdministrador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawer_layout_Admin);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.pageradmin);

        NavigationView navigationView = findViewById(R.id.nav_view_Admin);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //esto es para cuando seleccionas la pestaña
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        /*if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.pageradmin,new InicioAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioAdmin);
        }*/

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.InicioAdmin:

                break;
            case R.id.PerfilAdmin:

                break;
            case R.id.RegistrarAdmin:
                getSupportFragmentManager().beginTransaction().replace(R.id.pageradmin,new RegistroAdmin()).commit();
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

    class AdaptadorFragment extends FragmentStateAdapter
    {

        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new Entrantes();
                case 1:
                    return new Hamburguesas();
                case 2:
                    return new Sandwiches();
                case 3:
                    return new Perritos();
                case 4:
                    return new Costillar();
                case 5:
                    return new Ensaladas();
                case 6:
                    return new TexMex();
                default:
                    return new Postres();

            }
        }

        @Override
        public int getItemCount() {
            return 8;
        }
    }
}