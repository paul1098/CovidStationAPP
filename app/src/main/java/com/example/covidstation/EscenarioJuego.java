package com.example.covidstation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);


        IvVirus = findViewById(R.id.IvVirus);
        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);

        miDialog = new Dialog(EscenarioJuego.this);

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
            }
        });
        IRMENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "MENU", Toast.LENGTH_SHORT).show();
            }
        });
        PUNTAJES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "PUNTAJES", Toast.LENGTH_SHORT).show();
            }
        });

        miDialog.show();
    }
}