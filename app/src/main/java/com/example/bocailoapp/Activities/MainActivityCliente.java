package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.FragmentsAdmin.InicioAdmin;
import com.example.bocailoapp.FragmentsCliente.InicioCliente;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivityCliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    ArrayList<Plato> platos = new ArrayList<>();
    ArrayList<Plato> platosPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        SharedPreferences pedido =this.getSharedPreferences("pedido", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawer_layout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.pagercliente);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadData();

        if(platosPedido == null || platosPedido.isEmpty() || platosPedido.size()<=0 ){
            platosPedido = new ArrayList<>();
            saveData();
        }

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(), getLifecycle()));

        //Para pasar de una pestaña a otra deslizando
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.InicioCliente:
                Intent intentInicio = new Intent(MainActivityCliente.this, MainActivityCliente.class);
                startActivity(intentInicio);
                break;
            case R.id.CarritoPedido:
                Intent intentCarrito = new Intent(MainActivityCliente.this, Carrito.class);
                startActivity(intentCarrito);
                break;
            case R.id.Facebook:
                Uri uriUrl= Uri.parse("https://www.facebook.com/www.bocailo.es");
                Intent intentFacebook= new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intentFacebook);
                break;
            case R.id.Instagram:
                Uri uriInsta= Uri.parse("https://instagram.com/_u/bocailo");
                Intent intentInsta = new Intent(Intent.ACTION_VIEW, uriInsta);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                    intentInsta.setPackage("com.instagram.android");
                }
                try{
                    startActivity(intentInsta);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/bocailo")));
                }
                break;
            case R.id.Whatsapp:
                Intent intentwhatsapp = new Intent();
                intentwhatsapp.setAction(Intent.ACTION_VIEW);
                String uri= "whatsapp://send?phone=608055815";
                intentwhatsapp.setData(Uri.parse(uri));
                startActivity(intentwhatsapp);
                break;
            case R.id.DondeEncontrarnos:
                String map = "http://maps.google.com/maps?q=C. de Lucio Benito, 14, 28750 San Agustín del Guadalix, Madrid";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
                break;
            case R.id.Email:
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"sahida.dam18@gmail.com"});
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "consulta de "+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
                intentEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(intentEmail, "Elije un cliente de correo"));
                break;
            case R.id.Telefono:
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + 916222002));
                startActivity(intentCall);
                break;
            case R.id.CerrarSesion:
                firebaseAuth.signOut();
                Toast.makeText(this, "Cerraste sesión correctamente", Toast.LENGTH_SHORT).show();
                Intent intentSalir = new Intent(MainActivityCliente.this, LoginActivity.class);
                startActivity(intentSalir);
                break;
        }

        return false;
    }

    class AdaptadorFragment extends FragmentStateAdapter {

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

    public void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("pedido", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(platosPedido);
        editor.putString("array", json);
        editor.apply();
    }

    public void loadData()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("pedido", MODE_PRIVATE);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = sharedPreferences.getString("array", null);


        Type type = new TypeToken<ArrayList<Plato>>()
        {
        }.getType();

        platosPedido = gson.fromJson(json, type);

    }
}
