package com.example.bocailoapp.FragmentsAdmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bocailoapp.Activities.MainActivityAdministrador;
import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegistroAdmin extends Fragment {

    TextInputEditText Correo, Password, Nombre, Apellidos, Dni;
    Button Registrar;

    //autenticación con firebase
    FirebaseAuth auth;

    //Animación cuando le demos click al botón registrar
    ProgressDialog progressDialog;

    //private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro_admin, container, false);

        //Inicializamos base de datos
        auth= FirebaseAuth.getInstance();


        //Inicializamos las variables
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombre = view.findViewById(R.id.Nombre);
        Apellidos = view.findViewById(R.id.Apellidos);
        Dni = view.findViewById(R.id.Dni);



        //Inicializamos el botón
        Registrar= view.findViewById(R.id.Registrar);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //convertimos a string correo y contraseña
                String correo = Objects.requireNonNull(Correo.getText()).toString();
                String pass = Objects.requireNonNull(Password.getText()).toString();
                String nombre = Objects.requireNonNull(Nombre.getText()).toString();
                String apellidos = Objects.requireNonNull(Apellidos.getText()).toString();
                String dni= Objects.requireNonNull(Dni.getText()).toString();


                if(correo.equals("") || pass.equals("") || nombre.equals("") || apellidos.equals("") || dni.equals("")){
                    Toast.makeText(getActivity(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    //validación de correo electronico
                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        Correo.setError("Correo inválido");
                        Correo.setFocusable(true);
                    }
                    else if(pass.length()<6){
                        Password.setError("La contraseña debe ser mayor o igual a 6");
                        Password.setFocusable(true);
                    }else{
                        RegistroAdministradores(correo, pass);
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        //el setcancelable es para que al hacer click no desaparezca y esté ahí hasta que finalice el registro
        progressDialog.setCancelable(false);

        return  view;
    }

    //método para registrar administradores
    private void RegistroAdministradores(String email, String pass) {

        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si el admin se ha creado correctamente
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null; //confirmar que no es nulo

                            //Convertir a cadena los datos de los admin
                            String UID = user.getUid();
                            String correo = Objects.requireNonNull(Correo.getText()).toString();
                            String pass = Objects.requireNonNull(Password.getText()).toString();
                            String nombre = Objects.requireNonNull(Nombre.getText()).toString();
                            String apellidos= Objects.requireNonNull(Apellidos.getText()).toString();
                            String dni= Objects.requireNonNull(Dni.getText()).toString();

                            HashMap<Object, Object> Administradores = new HashMap<>();

                            Administradores.put("UID", UID);
                            Administradores.put("CORREO", correo);
                            Administradores.put("PASSWORD", pass);
                            Administradores.put("NOMBRE", nombre);
                            Administradores.put("APELLIDOS", apellidos);
                            Administradores.put("DNI", dni);
                            Administradores.put("ISADMIN", "SI");

                            //Inicializar Firebase Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("USUARIOS");
                            reference.child(UID).setValue(Administradores);

                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro completado", Toast.LENGTH_SHORT).show();
                            requireActivity().finish();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}