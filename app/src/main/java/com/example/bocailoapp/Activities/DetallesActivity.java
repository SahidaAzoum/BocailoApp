package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class DetallesActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth firebaseAuth;
    FirebaseUser user;



    ArrayList<Plato> platos = new ArrayList<>();
    TextView tvNombre, tvDescripcion, tvPrecio;
    ImageView ivImagen;
    EditText etObservaciones;
    DrawerLayout drawerLayout;


    String nombre, descripcion, imagen, tipo, observaciones;
    double precio;
    int id, index;


    ArrayList<Plato> platosPedido;

    Button btnVolverCarta, btnAnadirPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        drawerLayout= findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadData();

        tvNombre = findViewById(R.id.tvPlatoNombre);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvPrecio = findViewById(R.id.tvPrecio);
        ivImagen = findViewById(R.id.ivImagenPlato);
        etObservaciones = findViewById(R.id.etObservaciones);

        btnVolverCarta = findViewById(R.id.btnVolverCarta);
        btnAnadirPedido = findViewById(R.id.btnAnadirPedido);

        btnVolverCarta.setOnClickListener(this);
        btnAnadirPedido.setOnClickListener(this);

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Bundle datos = this.getIntent().getExtras();

        if(datos.getBoolean("modificar"))
            {
                btnAnadirPedido.setText("MODIFICAR");
                nombre = datos.getString("nombre");
                descripcion = datos.getString("descripcion");
                imagen = datos.getString("imagen", "");
                precio = datos.getDouble("precio");
                id = datos.getInt("id");
                tipo = datos.getString("tipo");
                index= datos.getInt("posicion");
                observaciones = datos.getString("observaciones");
                tvNombre.setText(nombre);
                tvDescripcion.setText(descripcion);
                tvPrecio.setText(String.valueOf(precio));
                etObservaciones.setText(observaciones);

            }
        else{

            if(datos != null)
                {
                    nombre = datos.getString("nombre");
                    descripcion = datos.getString("descripcion");
                    imagen = datos.getString("imagen", "");
                    precio = datos.getDouble("precio");
                    id = datos.getInt("id");
                    tipo = datos.getString("tipo");

                }
            platos = new ArrayList<Plato>();
            tvNombre.setText(nombre);
            tvDescripcion.setText(descripcion);
            tvPrecio.setText(String.valueOf(precio));
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("plato");


        if(imagen != null && imagen.length() != 0 && imagen.isEmpty()== false){

            storageReference = storage.getReferenceFromUrl("gs://bocailoapp.appspot.com/ImagenesPlatos").child(imagen);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Picasso.with(getApplicationContext()).load(uri.toString()).into(ivImagen);
                }
            });

        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
            {
                case R.id.btnVolverCarta:
                    Intent intentCarta = new Intent(this, MainActivityCliente.class);
                    startActivity(intentCarta);
                    break;
                case R.id.btnAnadirPedido:

                    if(btnAnadirPedido.getText().toString().equals("MODIFICAR")){
                        platosPedido.get(index).setObservaciones(etObservaciones.getText().toString());
                        saveData();
                        Intent intentCarrito = new Intent(this, Carrito.class);
                        startActivity(intentCarrito);
                        break;
                    }
                    if(btnAnadirPedido.getText().equals("AÑADIR A PEDIDO")){
                        observaciones = etObservaciones.getText().toString();
                        Plato p = new Plato(id,nombre,descripcion,tipo,precio,imagen,observaciones);
                        platosPedido.add(p);
                        saveData();
                        Intent intentPedido = new Intent(this, MainActivityCliente.class);
                        startActivity(intentPedido);
                        break;
                    }
                    {

                    }

            }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.InicioCliente:
                Intent intentInicio = new Intent(DetallesActivity.this, MainActivityCliente.class);
                startActivity(intentInicio);
                break;
            case R.id.CarritoPedido:
                Intent intentCarrito = new Intent(DetallesActivity.this, Carrito.class);
                startActivity(intentCarrito);
                break;
            case R.id.MisDatos:
                Intent intentDatos = new Intent(DetallesActivity.this, DatosPersonales.class);
                startActivity(intentDatos);
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
                Intent intentSalir = new Intent(DetallesActivity.this, LoginActivity.class);
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