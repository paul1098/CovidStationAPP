package com.example.covidstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    //declaracion de variables
    EditText    login_usuario,login_contrasena;
    Button      btn_login;

    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //conexión con la vista
        login_usuario = findViewById(R.id.login_usuario);
        login_contrasena = findViewById(R.id.login_contrasena);
        btn_login = findViewById(R.id.btn_login);

        /* para instanciar firebase */

        auth =  FirebaseAuth.getInstance();


        dialog = new ProgressDialog(this);
        dialog.setMessage("Iniciando Sesion!");
        dialog.setCancelable(false);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usercorreo2 = login_usuario.getText().toString();
                String usercontrasena2 = login_contrasena.getText().toString();

                //VALIDACIÓN PARA CORREO
                if (!Patterns.EMAIL_ADDRESS.matcher(usercorreo2).matches()){

                    login_usuario.setError("Correo no valido");
                    login_usuario.setFocusable(true);

                    //VALIDACIÓN PARA CONTRASEÑA
                } else if(usercontrasena2.length()<6){
                    login_contrasena.setError("La contraseña debe tener mas de 6 caracteres");
                    login_contrasena.setFocusable(true);
                } else {
                    LogeoDeJugador(usercorreo2,usercontrasena2);
                }
            }

        });


    }

    /*METODO PARA LOGEAR AL JUGADOR*/
    private void LogeoDeJugador(String usercorreo2, String usercontrasena2) {
        auth.signInWithEmailAndPassword(usercorreo2,usercontrasena2)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this,central.class));
                            assert user !=null; //afirmamos que el usuario no es nulo
                            Toast.makeText(MainActivity.this,"Bienvenido "+user.getEmail(),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    //SI FALLA EL LOGEO NOS MUESTRA UN MENSAJE
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void irRegistrarse(View view){
        Intent i = new Intent(this,registro.class);
        startActivity(i);
    }

}