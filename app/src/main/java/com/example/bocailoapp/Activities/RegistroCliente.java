package com.example.bocailoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroCliente extends AppCompatActivity {

    EditText etEmail, etPassword, etRepitaPass, editTextPhone;

    Button btnRegistrarCliente;

    //autenticación con firebase
    FirebaseAuth auth;

    //Animación cuando le demos click al botón registrar
    ProgressDialog progressDialog;

    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        //Inicializamos base de datos
        auth= FirebaseAuth.getInstance();


        //Inicializamos las variables
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRepitaPass = (EditText) findViewById(R.id.etRepitaPass);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        btnRegistrarCliente =(Button) findViewById(R.id.btnRegistrarCliente);

        btnRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo=etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String reppass= etRepitaPass.getText().toString();
                String telefono = editTextPhone.getText().toString();

                if(correo.equals("") || password.equals("") || reppass.equals("") || telefono.equals("")){
                    Toast.makeText(RegistroCliente.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    //validación de correo electronico
                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        etEmail.setError("Correo inválido");
                        etEmail.setFocusable(true);
                    }
                    else if(!password.equals(reppass)){
                        etRepitaPass.setError("La contraseña no coincide");
                        etRepitaPass.setFocusable(true);
                    }else{
                        RegistroCliente(correo, password);
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(RegistroCliente.this);
        progressDialog.setMessage("Registrando, espere por favor");
        //el setcancelable es para que al hacer click no desaparezca y esté ahí hasta que finalice el registro
        progressDialog.setCancelable(false);


    }

    //método para registrar administradores
    private void RegistroCliente(String email, String pass) {

        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si el cliente se ha creado correctamente
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null; //confirmar que no es nulo

                            //Convertir a cadena los datos de los admin
                            String UID = user.getUid();
                            String correo = etEmail.getText().toString();
                            String pass = etPassword.getText().toString();
                            String telefono = editTextPhone.getText().toString();


                            HashMap<Object, Object> Clientes = new HashMap<>();

                            Clientes.put("UID", UID);
                            Clientes.put("CORREO", correo);
                            Clientes.put("PASSWORD", pass);
                            Clientes.put("TELEFONO", telefono);
                            Clientes.put("ISADMIN", "NO");

                            //Inicializar Firebase Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("USUARIOS");
                            reference.child(UID).setValue(Clientes);

                            startActivity(new Intent(RegistroCliente.this, MainActivityCliente.class));
                            Toast.makeText(RegistroCliente.this, "Registro completado", Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(RegistroCliente.this, MainActivityCliente.class);
                            startActivity(intent);
                            finish();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(RegistroCliente.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroCliente.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}