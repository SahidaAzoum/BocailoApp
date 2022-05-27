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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TotalizarPedido extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText etnombre, etapellidos,etemail,ettelefono,etcalle,etcp,etobservaciones;
    Button btnEnviar;
    AutoCompleteTextView etpoblacion;

    ArrayList<Plato> platosPedido;

    int i = 1;

    DrawerLayout drawerLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String[] poblaciones = {"San Agustín del Guadalix", "El Molar", "Ciudad de Santo Domingo", "Ciudalcampo" +
            "Valdelagua", "Punta Galea"};

    String tel, nombre, apellidos, email, calle, poblacion,cp, observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalizar_pedido);

        drawerLayout= findViewById(R.id.drawerDatosPedido);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_viewdatosPedido);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        etnombre = findViewById(R.id.etNombrePedido);
        etapellidos = findViewById(R.id.etApellidosPedido);
        etemail = findViewById(R.id.etEmailPedidos);
        ettelefono = findViewById(R.id.etTelefonoPedidos);
        etcalle = findViewById(R.id.etCallePedido);
        etpoblacion = findViewById(R.id.etPoblacionPedido);
        etcp = findViewById(R.id.etCpPedido);
        etobservaciones = findViewById(R.id.etObservacionesPedido);
        btnEnviar = findViewById(R.id.btnEnviar);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, poblaciones);
        etpoblacion.setAdapter(adaptador);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("USUARIOS").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                nombre = String.valueOf(snapshot.child("NOMBRE").getValue());
                if (nombre.equals("null"))
                    {
                        etnombre.setText("");
                    }
                else
                    {
                        etnombre.setText(nombre);
                    }
                apellidos = String.valueOf(snapshot.child("APELLIDOS").getValue());
                if(!apellidos.equals("null"))
                    {
                        etapellidos.setText(apellidos);
                    }
                email = String.valueOf(snapshot.child("CORREO").getValue());
                if(!email.equals("null"))
                    {
                        etemail.setText(email);
                    }
                tel = String.valueOf(snapshot.child("TELEFONO").getValue());
                if(!tel.equals("null"))
                    {
                        ettelefono.setText(tel);
                    }
                calle = String.valueOf(snapshot.child("CALLE").getValue());
                if(!calle.equals("null"))
                    {
                        etcalle.setText(calle);
                    }
                poblacion = String.valueOf(snapshot.child("POBLACION").getValue());
                if (!poblacion.equals("null"))
                    {
                        etpoblacion.setText(poblacion);
                    }
                cp = String.valueOf(snapshot.child("CP").getValue());
                if (!cp.equals("null"))
                    {
                        etcp.setText(cp);
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etnombre.getText().length()<=0 || etapellidos.getText().length()<=0 || etemail.getText().length()<=0 || etcalle.getText().length()<=0
                ||etpoblacion.getText().length()<=0 || etcp.getText().length()<=0)
                    {
                        Toast.makeText(TotalizarPedido.this, "Debe rellenar todos los campos marcados con *", Toast.LENGTH_SHORT).show();
                    }
                else
                    {
                        nombre = etnombre.getText().toString();
                        apellidos = etapellidos.getText().toString();
                        email = user.getEmail();
                        calle = etcalle.getText().toString();
                        poblacion = etpoblacion.getText().toString();
                        cp = etcp.getText().toString();
                        observaciones = etobservaciones.getText().toString();
                    }

                if(poblacion.equals("San Agustín del Guadalix") || poblacion.equalsIgnoreCase("Ciudad de Santo Domingo") ||
                poblacion.equalsIgnoreCase("El Molar") || poblacion.equalsIgnoreCase("Ciudalcampo") || poblacion.equalsIgnoreCase("Valdelagua")
                || poblacion.equalsIgnoreCase("Punta Galea"))
                    {
                        EnviarPedido(nombre, apellidos,email, tel,calle,poblacion,cp,observaciones);
                    }
                else
                    {
                        Toast.makeText(TotalizarPedido.this, "Lo sentimos, pero no servimos en esta localidad", Toast.LENGTH_SHORT).show();

                    }
            }
        });


    }

    public void EnviarPedido(String nombre, String apellidos, String email, String telefono, String calle, String poblacion, String cp, String observaciones)
    {
        loadData();
        String UID = firebaseAuth.getCurrentUser().getUid();
        String numPedido = "";
        LocalDateTime fecha = null;
        LocalDate fecha2 =  null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                fecha = LocalDateTime.now();
                fecha2 = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
                numPedido+=formatter.format(fecha2);
            }

        numPedido+="/";
        numPedido+= i;
        String date = fecha.toString();

        HashMap<String, String> pedido = new HashMap<>();
        pedido.put("nombre", nombre);
        pedido.put("apellidos", apellidos);
        pedido.put("email", email);
        pedido.put("telefono", telefono);
        pedido.put("calle", calle);
        pedido.put("poblacion",poblacion);
        pedido.put("cp", cp);
        pedido.put("observaciones", observaciones);
        pedido.put("UID", UID);
        pedido.put("fecha",date);
        //pedido.put("platos", String.valueOf(platosPedido));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PEDIDOS");
        reference.child(numPedido).setValue(pedido);
        reference.child(numPedido).child("platos").setValue(platosPedido);
        platosPedido.clear();
        i++;
        saveData();
        Intent intentInicio = new Intent(TotalizarPedido.this, MainActivityCliente.class);
        startActivity(intentInicio);


    }

    public void loadData()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("pedido", MODE_PRIVATE);
        i = sharedPreferences.getInt("i", 1);


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = sharedPreferences.getString("array", null);

        Type type = new TypeToken<ArrayList<Plato>>()
        {
        }.getType();

        platosPedido = gson.fromJson(json, type);

    }

    public void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("pedido", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(platosPedido);
        editor.putString("array", json);
        editor.putInt("i", i);
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.InicioCliente:
                Intent intentInicio = new Intent(TotalizarPedido.this, MainActivityCliente.class);
                startActivity(intentInicio);
                break;
            case R.id.CarritoPedido:
                Intent intentCarrito = new Intent(TotalizarPedido.this, Carrito.class);
                startActivity(intentCarrito);
                break;
            case R.id.MisDatos:
                Intent intentDatos = new Intent(TotalizarPedido.this, DatosPersonales.class);
                startActivity(intentDatos);
                break;
            case R.id.MisPedidos:
                Intent intentPedidos = new Intent(TotalizarPedido.this, HistoricoPedidosCliente.class);
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
                Intent intentSalir = new Intent(TotalizarPedido.this, LoginActivity.class);
                startActivity(intentSalir);
                break;
        }

        return false;
    }
}