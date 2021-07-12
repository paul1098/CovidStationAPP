package com.example.covidstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registro extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    EditText    correoET,contrasenaET,nombreET;
    Button      btnRegistrar;
    TextView    textRegistro;

    FirebaseAuth    auth; //FIREBASE AUTENTICACIÓN
    ProgressDialog  dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //CONEXION CON LA VISTA
        nombreET = findViewById(R.id.nombreET);
        correoET = findViewById(R.id.correoET);
        contrasenaET = findViewById(R.id.contrasenaET);
        btnRegistrar = findViewById(R.id.btn_registrar);

        /* PARA INICIAR FIREBASE */
        auth =  FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creando Cuenta!");
        dialog.setCancelable(false);


         btnRegistrar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String userCorreo = correoET.getText().toString();
                 String userContrasena = contrasenaET.getText().toString();

                 //VALIDACION PARA CORREO
                 if (!Patterns.EMAIL_ADDRESS.matcher(userCorreo).matches()){

                    correoET.setError("Correo no valido");
                    correoET.setFocusable(true);

                    //VALIDACION PARA CONTRASEÑA
                 } else if(userContrasena.length()<6){
                     contrasenaET.setError("La contraseña debe tener mas de 6 caracteres");
                     contrasenaET.setFocusable(true);
                 } else {
                     RegistrarJugador(userCorreo,userContrasena);
                 }
             }
         });


         textRegistro = findViewById(R.id.textRegistro);
        textRegistro.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 startActivity(new Intent(registro.this, MainActivity.class));
                    finish();
             }
         });


    }

    //METODO PARA REGISTRAR UN NUEVO USUARIO
    private void RegistrarJugador(String userCorreo, String userContrasena) {
        auth.createUserWithEmailAndPassword(userCorreo,userContrasena)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            int contador = 0;


                            assert user != null;
                            String uidString = user.getUid();
                            String correoString = correoET.getText().toString();
                            String contrasenaString = contrasenaET.getText().toString();
                            String nombreString = nombreET.getText().toString();

                            HashMap<Object,Object> DatosJugador = new HashMap<>();

                            DatosJugador.put("Uid",uidString);
                            DatosJugador.put("Nombre",nombreString);
                            DatosJugador.put("Correo",correoString);
                            DatosJugador.put("Contraseña",contrasenaString);
                            DatosJugador.put("Kills",contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance(); //PARA INSTANCIAR LA BASE DE DATOS
                            DatabaseReference reference = database.getReference("Jugadores"); //NOMBRE DE LA BASE DE DATOS
                            reference.child(uidString).setValue(DatosJugador);
                            startActivity(new Intent(registro.this,central.class));
                            Toast.makeText(registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                //SI FALLA EL REGISTRO SE EJECUTA ESTO
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registro.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}