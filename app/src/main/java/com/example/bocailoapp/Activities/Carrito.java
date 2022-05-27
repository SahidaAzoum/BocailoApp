package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.example.bocailoapp.Utils.AdaptadorCarrito;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class Carrito extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{

    FirebaseAuth firebaseAuth;

    Button btnEnviarPedido, btnVolverCarrito;
    ListView lvCarrito;

    ArrayList<Plato> platosPedido;

    AdaptadorCarrito adaptador;
    DrawerLayout drawerLayout;

    static TextView tvTotal;
    static double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        drawerLayout= findViewById(R.id.drawerCarrito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_viewcarrito);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        btnEnviarPedido = findViewById(R.id.btnEnviarPedido);
        btnVolverCarrito = findViewById(R.id.btnvolvercarrito);
        tvTotal = findViewById(R.id.tvTotalCarrito);

        lvCarrito = findViewById(R.id.lvcarrito);

        loadData();

        adaptador= new AdaptadorCarrito(getApplicationContext(), platosPedido);

        btnEnviarPedido.setOnClickListener(this);
        btnVolverCarrito.setOnClickListener(this);

        lvCarrito.setAdapter(adaptador);

        calcularPrecio(platosPedido);


    }


    @Override
    public void onClick(View view)
    {

        switch(view.getId())
            {
                case R.id.btnEnviarPedido:
                    saveData();
                    if(platosPedido.isEmpty())
                        {
                            Toast.makeText(this, "Debe tener platos para poder realizar el pedido.", Toast.LENGTH_SHORT).show();
                        }
                    else
                        {
                            Intent intentEnviar = new Intent(Carrito.this, TotalizarPedido.class);
                            startActivity(intentEnviar);
                        }

                    break;
                case R.id.btnvolvercarrito:
                    saveData();
                    Intent intentVolver = new Intent(Carrito.this, MainActivityCliente.class);
                    startActivity(intentVolver);
                    break;
            }

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

    public static void calcularPrecio(ArrayList<Plato> array)
    {
        total = 0;

        for (int i=0; i<array.size(); i++ )
            {
                total += array.get(i).getPrecio();
            }

        tvTotal.setText(total + "€");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.InicioCliente:
                Intent intentInicio = new Intent(Carrito.this, MainActivityCliente.class);
                startActivity(intentInicio);
                break;
            case R.id.CarritoPedido:
                Intent intentCarrito = new Intent(Carrito.this, Carrito.class);
                startActivity(intentCarrito);
                break;
            case R.id.MisDatos:
                Intent intentDatos = new Intent(Carrito.this, DatosPersonales.class);
                startActivity(intentDatos);
                break;
            case R.id.MisPedidos:
                Intent intentPedidos = new Intent(Carrito.this, HistoricoPedidosCliente.class);
                startActivity(intentPedidos);
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
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "consulta de "+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
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
                Intent intentSalir = new Intent(Carrito.this, LoginActivity.class);
                startActivity(intentSalir);
                break;
        }

        return false;
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

    public static void saveData(ArrayList<Plato> array, Context contexto)
    {
        SharedPreferences sharedPreferences = contexto.getSharedPreferences("pedido", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(array);
        editor.putString("array", json);
        editor.apply();
    }


}