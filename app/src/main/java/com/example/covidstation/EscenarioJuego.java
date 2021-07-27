package com.example.covidstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {
    String UIDS, NOMBRES, VIRUS ;
    TextView TvContador,TvNombre,TvTiempo;
    ImageView IvVirus;

    int AnchoPantalla;
    int AltoPantalla;
    Random aleatorio;

    boolean GameOver = false;
    Dialog miDialog;

    int contador = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);


        IvVirus = findViewById(R.id.IvVirus);
        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);

        miDialog = new Dialog(EscenarioJuego.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("Jugadores");


        Bundle intent = getIntent().getExtras();
        UIDS = intent.getString("UID");
        NOMBRES = intent.getString("NOMBRE");
        VIRUS = intent.getString("VIRUS");

        TvNombre.setText(NOMBRES);
        TvContador.setText(VIRUS);
        Pantalla();
        CuentaAtras();

        //AL HACER CLICK A LA IMAGEN
        IvVirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GameOver) {
                    contador++;
                    TvContador.setText(String.valueOf(contador));
                    //CAMBIO DE IMAGEN
                    IvVirus.setImageResource(R.drawable.virusaplastado);
                    //PARA VOLVER AL ESTADO ORIGINAL
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        //PARA EL DELAY Y LA EJECUCION
                        public void run() {
                            IvVirus.setImageResource(R.drawable.virus);
                            Movimiento();
                        }
                    }, 500);
                }
            }
        });

    }
    //PARA EL MOVIMIENTO DEL ENEMIGO (EL AREA) ----PANTALLA---
    private void Pantalla(){
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        AnchoPantalla = point.x;
        AltoPantalla = point.y;
        aleatorio = new Random();
    }

    // MOVIMIENTO
    private void Movimiento(){
        int min = 0 ;
        int maximoX = AnchoPantalla -  IvVirus.getWidth();
        int maximoY = AltoPantalla -  IvVirus.getHeight()-100;


        int randomX = aleatorio.nextInt(((maximoX-min)+1)+min);
        int randomY = aleatorio.nextInt(((maximoY-min)+1)+min);
        IvVirus.setX(randomX);
        IvVirus.setY(randomY);
    }

    //PARA RETROCEDER EL TIEMPO DEL CONTADOR
    private void CuentaAtras(){
        new CountDownTimer(10000, 1000) {

            //SE EJECUTA CADA SEGUNDO
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+"S"); }

            //CUANDO SE ACABA EL TIEMPO
            public void onFinish() {
                TvTiempo.setText("0S");
                GameOver = true;
                MensajeGameOver();
                GuardarResultados("Kills",contador);
            }
        }.start();
    }

    private void MensajeGameOver() {

        TextView SeacaboTXT,HasmatadoTXT, NumeroTXT;
        Button  JUGARDENUEVO,IRMENU,PUNTAJES;

        miDialog.setContentView(R.layout.gameover);

        SeacaboTXT = miDialog.findViewById(R.id.SeacaboTXT);
        HasmatadoTXT = miDialog.findViewById(R.id.HasmatadoTXT);
        NumeroTXT = miDialog.findViewById(R.id.NumeroTXT);

        JUGARDENUEVO = miDialog.findViewById(R.id.JUGARDENUEVO);
        IRMENU = miDialog.findViewById(R.id.IRMENU);
        PUNTAJES = miDialog.findViewById(R.id.PUNTAJES);

        String virus = String.valueOf(contador);
        NumeroTXT.setText(virus);

        JUGARDENUEVO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "JUGAR DE NUEVO", Toast.LENGTH_SHORT).show();
                contador = 0;
                miDialog.dismiss();;
                TvContador.setText("0");
                GameOver = false;
                CuentaAtras();
                Movimiento();
            }
        });
        IRMENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "MENU", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EscenarioJuego.this,central.class));
            }
        });

        PUNTAJES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EscenarioJuego.this,Puntajes.class);
                startActivity(intent);

            }
        });
        miDialog.show();
    }

    private void GuardarResultados(String key,int Kills ) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put(key,Kills);
        JUGADORES.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EscenarioJuego.this, "El puntaje a sido actulizado",Toast.LENGTH_SHORT).show();


            }
        });



    }
}