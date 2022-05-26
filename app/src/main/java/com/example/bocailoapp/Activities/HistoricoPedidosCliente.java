package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bocailoapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HistoricoPedidosCliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{

    DrawerLayout drawerLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button btnVolver;

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pedidos_cliente);

        drawerLayout= findViewById(R.id.drawerDatosPedido);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_viewmispedidos);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btnVolver = findViewById(R.id.btnVolverMisPedidos);
        listview  = findViewById(R.id.listviewPedidos);

        btnVolver.setOnClickListener(this);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
            {
                case R.id.InicioCliente:
                    Intent intentInicio = new Intent(HistoricoPedidosCliente.this, MainActivityCliente.class);
                    startActivity(intentInicio);
                    break;
                case R.id.CarritoPedido:
                    Intent intentCarrito = new Intent(HistoricoPedidosCliente.this, Carrito.class);
                    startActivity(intentCarrito);
                    break;
                case R.id.MisDatos:
                    Intent intentDatos = new Intent(HistoricoPedidosCliente.this, DatosPersonales.class);
                    startActivity(intentDatos);
                    break;
                case R.id.Facebook:
                    Uri uriUrl = Uri.parse("https://www.facebook.com/www.bocailo.es");
                    Intent intentFacebook = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(intentFacebook);
                    break;
                case R.id.Instagram:
                    Uri uriInsta = Uri.parse("https://instagram.com/_u/bocailo");
                    Intent intentInsta = new Intent(Intent.ACTION_VIEW, uriInsta);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT)
                        {
                            intentInsta.setPackage("com.instagram.android");
                        }
                    try
                        {
                            startActivity(intentInsta);
                        } catch (ActivityNotFoundException e)
                        {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/bocailo")));
                        }
                    break;
                case R.id.Whatsapp:
                    Intent intentwhatsapp = new Intent();
                    intentwhatsapp.setAction(Intent.ACTION_VIEW);
                    String uri = "whatsapp://send?phone=608055815";
                    intentwhatsapp.setData(Uri.parse(uri));
                    //intentwhatsapp.setPackage("com.whatsapp");
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
                    intentEmail.putExtra(Intent.EXTRA_SUBJECT, "consulta de " + Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
                    intentEmail.setType("message/rfc822");
                    startActivity(Intent.createChooser(intentEmail, "Elije un cliente de correo"));
                    break;
                case R.id.Telefono:
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + 916222002));
                    startActivity(intentCall);
                    break;
                case R.id.Salir:
                    firebaseAuth.signOut();
                    Toast.makeText(this, "Cerraste sesión correctamente", Toast.LENGTH_SHORT).show();
                    Intent intentSalir = new Intent(HistoricoPedidosCliente.this, LoginActivity.class);
                    startActivity(intentSalir);
                    break;
            }

        return false;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
            {
                case R.id.btnVolverMisPedidos:
                    Intent intentVolver = new Intent(HistoricoPedidosCliente.this, MainActivityCliente.class);
                    startActivity(intentVolver);
                    break;
            }
    }

    public void CargarPedidos(){

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("PEDIDOS");

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}