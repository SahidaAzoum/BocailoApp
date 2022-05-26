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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DatosPersonales extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    EditText etnombre, etapellidos,etemail,ettelefono,etcalle,etcp;
    Button btnModificar, btnVolver;
    AutoCompleteTextView etpoblacion;

    DrawerLayout drawerLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String[] poblaciones = {"San Agustín del Guadalix", "El Molar", "Ciudad de Santo Domingo", "Ciudalcampo" +
            "Valdelagua", "Punta Galea"};

    String tel, nombre, apellidos, email, calle, poblacion,cp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        drawerLayout= findViewById(R.id.drawerDatosPedido);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_viewdatospersonales);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.navigation_drawer_close,R.string.navigation_drawer_open);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        etnombre = findViewById(R.id.etNombreDatos);
        etapellidos = findViewById(R.id.etApellidosDatos);
        etemail = findViewById(R.id.etEmailDatos);
        ettelefono = findViewById(R.id.etTelefonoDatos);
        etcalle = findViewById(R.id.etCalleDatos);
        etpoblacion = findViewById(R.id.etPoblacionDatos);
        etcp = findViewById(R.id.etCpPedido);

        btnModificar = findViewById(R.id.btnModificar);
        btnVolver = findViewById(R.id.btnVolverDatos);

        etnombre.setEnabled(false);
        etapellidos.setEnabled(false);
        etemail.setEnabled(false);
        ettelefono.setEnabled(false);
        etcalle.setEnabled(false);
        etpoblacion.setEnabled(false);
        etcp.setEnabled(false);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, poblaciones);
        etpoblacion.setAdapter(adaptador);

        CargarDatosPersonales();



        btnModificar.setOnClickListener(this);
        btnVolver.setOnClickListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch(item.getItemId())
            {
                case R.id.InicioCliente:
                    Intent intentInicio = new Intent(DatosPersonales.this, MainActivityCliente.class);
                    startActivity(intentInicio);
                    break;
                case R.id.CarritoPedido:
                    Intent intentCarrito = new Intent(DatosPersonales.this, Carrito.class);
                    startActivity(intentCarrito);
                    break;
                case R.id.MisDatos:
                    Intent intentDatos = new Intent(DatosPersonales.this, DatosPersonales.class);
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
                    Intent intentSalir = new Intent(DatosPersonales.this, LoginActivity.class);
                    startActivity(intentSalir);
                    break;
            }
        return false;
    }

    public void CargarDatosPersonales()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("USUARIOS").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                nombre = String.valueOf(snapshot.child("NOMBRE").getValue());
                apellidos = String.valueOf(snapshot.child("APELLIDOS").getValue());
                tel = String.valueOf(snapshot.child("TELEFONO").getValue());
                email = user.getEmail();
                calle = String.valueOf(snapshot.child("CALLE").getValue());
                poblacion = String.valueOf(snapshot.child("POBLACION").getValue());
                cp = String.valueOf(snapshot.child("CP").getValue());

                etnombre.setText(nombre);
                etapellidos.setText(apellidos);
                ettelefono.setText(tel);
                etemail.setText(email);
                etcalle.setText(calle);
                etpoblacion.setText(poblacion);
                etcp.setText(cp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }

    public void ModificarDatosPersonales()
    {
        nombre = etnombre.getText().toString();
        apellidos = etapellidos.getText().toString();
        email = etemail.getText().toString();
        tel = ettelefono.getText().toString();
        calle = etcalle.getText().toString();
        poblacion = etpoblacion.getText().toString();
        cp = etcp.getText().toString();

        String UID = firebaseAuth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("USUARIOS");
        reference.child(UID).child("NOMBRE").setValue(nombre);
        reference.child(UID).child("APELLIDOS").setValue(apellidos);
        reference.child(UID).child("CORREO").setValue(email);
        reference.child(UID).child("TELEFONO").setValue(tel);
        reference.child(UID).child("CALLE").setValue(calle);
        reference.child(UID).child("POBLACION").setValue(poblacion);
        reference.child(UID).child("CP").setValue(cp);

        Toast.makeText(this, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
            {
                case R.id.btnModificar:

                    if(btnModificar.getText().toString().equals("MODIFICAR"))
                        {
                            etnombre.setEnabled(true);
                            etapellidos.setEnabled(true);
                            etemail.setEnabled(true);
                            ettelefono.setEnabled(true);
                            etcalle.setEnabled(true);
                            etpoblacion.setEnabled(true);
                            etcp.setEnabled(true);

                            btnModificar.setText("GUARDAR");

                        }
                    else 
                        {
                            ModificarDatosPersonales();
                            etnombre.setEnabled(false);
                            etapellidos.setEnabled(false);
                            etemail.setEnabled(false);
                            ettelefono.setEnabled(false);
                            etcalle.setEnabled(false);
                            etpoblacion.setEnabled(false);
                            etcp.setEnabled(false);

                            btnModificar.setText("MODIFICAR");
                            Intent intentInicio = new Intent(DatosPersonales.this, MainActivityCliente.class);
                            startActivity(intentInicio);
                        }
                    break;
                case R.id.btnVolverDatos:
                    Intent intentVolver = new Intent(DatosPersonales.this, MainActivityCliente.class);
                    startActivity(intentVolver);
                    break;
            }
    }
}