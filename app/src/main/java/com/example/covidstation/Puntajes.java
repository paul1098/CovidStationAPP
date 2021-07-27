package com.example.covidstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Puntajes extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerviewUsuarios;
    Adaptador adaptador;
    List<Usuario> usuarioList;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);




        mLayoutManager = new LinearLayoutManager(this);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerviewUsuarios = findViewById(R.id.recyclerviewUsuarios);

        mLayoutManager.setReverseLayout(true); //ORDENA Z-A//
        mLayoutManager.setStackFromEnd(true);  /*EMPIEZA DE ARRIBA SIN TENER DESLIZ*/
        recyclerviewUsuarios.setHasFixedSize(true);
        recyclerviewUsuarios.setLayoutManager(mLayoutManager);

        usuarioList = new ArrayList<>();
        ObtenerTodosLosUsuarios();
    }

    private void ObtenerTodosLosUsuarios(){
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser(); /*OBTIENE TODOS LOS USUARIOS*/
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Jugadores");
            ref.orderByChild("Kills").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                    usuarioList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        //DEL MODELO USUARIOS
                        Usuario usuario = ds.getValue(Usuario.class);

                        usuarioList.add(usuario);
                        adaptador = new Adaptador(Puntajes.this, usuarioList);
                        recyclerviewUsuarios.setAdapter(adaptador);



                    }

                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });
        }



}
