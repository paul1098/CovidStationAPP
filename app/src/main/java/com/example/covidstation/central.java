package com.example.covidstation;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class central extends AppCompatActivity {

    Button      jugarBtn,puntuacionesBtn,acercaDeBtn,CerrarSesion;
    TextView    miPuntuaciónTxt,virus,uid,correo,nombre,menuTxt;

    FirebaseAuth  auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference(  "Jugadores");

        miPuntuaciónTxt = findViewById( R.id.miPuntuaciónTxt);
        virus = findViewById(R.id.virus);
        uid = findViewById( R.id.uid);
        correo = findViewById( R.id.correo);
        nombre = findViewById( R.id.nombre);
        menuTxt = findViewById( R.id.menuTxt);

        jugarBtn = findViewById( R.id.jugarBtn);
        puntuacionesBtn = findViewById( R.id.puntuacionesBtn);
        acercaDeBtn = findViewById( R.id.acercaDeBtn);
        CerrarSesion = findViewById( R.id.CerrarSesion);


        jugarBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(central.this, "JUGAR", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(central.this,EscenarioJuego.class);

                String UidS = uid.getText().toString();
                String NombreS = nombre.getText().toString();
                String VirusS = virus.getText().toString();

                intent.putExtra("UID",UidS);
                intent.putExtra("NOMBRE",NombreS);
                intent.putExtra("VIRUS",VirusS);

                startActivity(intent);
                Toast.makeText(central.this, "ENVIANDO PARAMETROS",Toast.LENGTH_SHORT).show();
            }
        }));
        
        puntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(central.this, "PUNTUACIONES", Toast.LENGTH_SHORT).show();
            }
        });

        acercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(central.this, "ACERCA DE", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });

    }

    //EL METODO SE INICIA CUANDO SE ABRE EL JUEGO
    @Override
    protected void onStart() {
        usuarioLogueado();
        super.onStart();
    }

    //METODO PARA MANTENER LA SESION INICIADA
    private void usuarioLogueado(){
        if (user != null){
            Consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(central.this,MainActivity.class));
            finish();
        }
    }

    //METODO PARA CERRAR SESION
    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(central.this,MainActivity.class));
        Toast.makeText(this, "Sesion cerrada exitosamente", Toast.LENGTH_SHORT).show();
    }

    //METODO PARA REALIZAR CONSULTA
    private void Consulta(){
        /*CONSULTA*/
        Query query = JUGADORES.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren ()){

                    /*OBTENCION DE DATOS*/
                    String virusString = ""+ds.child("Kills").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Correo").getValue();
                    String nombreString = ""+ds.child("Nombre").getValue();

                    /*SETEO DE DATOS EN LOS TXTVIEW*/
                    virus.setText(virusString);
                    uid.setText(uidString);
                    correo.setText(emailString);
                    nombre.setText(nombreString);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {

            }
        });


    }


}