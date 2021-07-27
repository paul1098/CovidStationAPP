package com.example.covidstation;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class central extends AppCompatActivity {

    Button      jugarBtn,editarBtn,CambiarPassBtn,puntuacionesBtn,acercaDeBtn,CerrarSesion;
    TextView    miPuntuaciónTxt,virus,uid,correo,nombre,edad,pais,menuTxt;
    CircleImageView imagenPerfil;

    FirebaseAuth  auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference(  "Jugadores");

        dialog = new Dialog(central.this);

        miPuntuaciónTxt = findViewById( R.id.miPuntuaciónTxt);

        //PERFIL
        imagenPerfil = findViewById(R.id.imagenPerfil);
        virus = findViewById(R.id.virus);
        uid = findViewById( R.id.uid);
        correo = findViewById( R.id.correo);
        nombre = findViewById( R.id.nombre);
        edad = findViewById( R.id.edad);
        pais = findViewById( R.id.pais);

        menuTxt = findViewById( R.id.menuTxt);

        //OPCIONES DEL JUEGO
        jugarBtn = findViewById( R.id.jugarBtn);
        editarBtn = findViewById(R.id.editarBtn);
       // CambiarPassBtn = findViewById(R.id.CambiarPassBtn);
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

            }
        }));

        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(central.this,"EDITAR DATOS",Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });
       // CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
           // public void onClick(View v) {
                //Toast.makeText(central.this,"CAMBIAR CONTRASEÑA", Toast.LENGTH_SHORT).show();
            //}
        //});



        puntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(central.this,Puntajes.class);
                startActivity(intent);

            }
        });

        acercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcercaDe();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });

    }

    private void AcercaDe() {

        TextView DesarrolladoporTXT,DevTXT;
        Button Ok;

        dialog.setContentView(R.layout.acerca_de);
        DesarrolladoporTXT = dialog.findViewById(R.id.DesarrolladoporTXT);
        DevTXT = dialog.findViewById(R.id.DevTXT);
        Ok = dialog.findViewById(R.id.Ok);

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //METODO PARA EDITAR LOS DATOS
    private void EditarDatos() {
        //DEFINIENDO ARREGLO CON LAS OPCIONES QUE PODREMOS ELEGIR
        String [] Opciones = {"Foto de perfil","Cambiar nombre","Cambiar edad","Cambiar pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0){
                    ActualizarFotoPerfil();
                }
                if (i == 1){
                    ActualizarNombre("Nombre");
                }
                if (i == 2){
                    ActualizarEdad("Edad");
                }
                if (i == 3){
                    ActualizarPais("País");
                }

            }
        });
        builder.create().show();
    }

    private void ActualizarNombre(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //SI EL USUARIO HACE CLIC EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(central.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(central.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(central.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private void ActualizarEdad(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
                //SI EL USUARIO HACE CLIC EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(central.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(central.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(central.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void ActualizarPais(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //SI EL USUARIO HACE CLIC EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(central.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(central.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(central.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void ActualizarFotoPerfil() {
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
                    String edadString = ""+ds.child("Edad").getValue();
                    String paisString = ""+ds.child("País").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();


                    /*SETEO DE DATOS EN LOS TXTVIEW*/
                    virus.setText(virusString);
                    uid.setText(uidString);
                    correo.setText(emailString);
                    nombre.setText(nombreString);
                    edad.setText(edadString);
                    pais.setText(paisString);

                    try {
                        Picasso.get().load(imagen).into(imagenPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.perfil).into(imagenPerfil);
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {

            }
        });


    }


}