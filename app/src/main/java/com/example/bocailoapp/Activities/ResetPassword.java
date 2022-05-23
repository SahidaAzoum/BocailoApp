package com.example.bocailoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity
{

    private EditText etEmailRecu;
    private Button btnRecuperarPass, btnVolverRecu;

    private String email = "";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etEmailRecu = findViewById(R.id.etEmailRecu);
        btnRecuperarPass = findViewById(R.id.btnRecuperarPass);
        btnVolverRecu = findViewById(R.id.btnVolverRecu);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRecuperarPass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = etEmailRecu.getText().toString();

                if (!email.isEmpty())
                    {
                        resetPassword();
                    } else
                    {
                        Toast.makeText(ResetPassword.this, "Introduzca un email válido", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        btnVolverRecu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intentVolver = new Intent(ResetPassword.this, LoginActivity.class);
                startActivity(intentVolver);
            }
        });
    }

    private void resetPassword()
    {
        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPassword.this, "Se ha enviado un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(ResetPassword.this, "No se pudo enviar el correo para restablecer contraseña", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }
}