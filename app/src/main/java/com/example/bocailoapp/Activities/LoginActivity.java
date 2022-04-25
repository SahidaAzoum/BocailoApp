package com.example.bocailoapp.Activities;

import static com.google.firebase.auth.FirebaseAuth.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bocailoapp.FragmentsAdmin.RegistroAdmin;
import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnIniciarSesion,btnRegistro;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    boolean isAdmin;
    String admin1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword=(EditText) findViewById(R.id.etPassword);
        btnIniciarSesion =(Button) findViewById(R.id.btnIniciarSesion);
        btnRegistro =(Button) findViewById(R.id.btnRegistro);

        firebaseAuth= getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Accediendo, espere por favor");
        progressDialog.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("USUARIOS");

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //convertimos a string correo y contraseña
                String correo = etEmail.getText().toString();
                String pass = etPassword.getText().toString();


                //validación de correo electronico
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    etEmail.setError("Correo inválido");
                    etEmail.setFocusable(true);
                }
                else if(pass.length()<6){
                    etPassword.setError("La contraseña debe ser mayor o igual a 6");
                    etPassword.setFocusable(true);
                }else{
                    Login(correo, pass);
                }


            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegistroCliente.class);
                startActivity(intent);

            }
        });


    }

    private void Login(String correo, String pass) {

        progressDialog.show();
        progressDialog.setCancelable(false);

        firebaseAuth.signInWithEmailAndPassword(correo, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Loggin correcto", Toast.LENGTH_SHORT).show();

                databaseReference= FirebaseDatabase.getInstance().getReference().child("USUARIOS").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("ISADMIN");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        admin1= String.valueOf(snapshot.getValue());

                        if(admin1!= null){
                            if(admin1.equalsIgnoreCase("SI")){
                                Intent intent = new Intent(LoginActivity.this, MainActivityAdministrador.class);
                                startActivity(intent);
                            }else if(admin1.equalsIgnoreCase("NO")){
                                Intent intentCliente = new Intent(LoginActivity.this, MainActivityCliente.class);
                                startActivity(intentCliente);
                            }
                        }else{
                            System.out.println("Es nulo");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

}