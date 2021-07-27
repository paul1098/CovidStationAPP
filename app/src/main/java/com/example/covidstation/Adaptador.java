package com.example.covidstation;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {

    private Context context;
    private List<Usuario> usuarioList;

    //CONSTRUCTOR
    public Adaptador(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull

    @Override
    //INFLAMOS EL DISEÑO
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jugadores,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {


        //OBTENEMOS LOS DATOS DEL MODELO
        String Nombres = usuarioList.get(i).getNombre();
        String Correo = usuarioList.get(i).getCorreo();
        int Kills = usuarioList.get(i).getKills();
        //CONVERSION A STRING
        String K = String.valueOf(Kills);

        //DATOS DEL JUGADOR
        holder.NombreJugador.setText(Nombres);
        holder.CorreoJugador.setText(Correo);
        holder.PuntajeJugador.setText(K);
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{


        TextView NombreJugador,CorreoJugador,PuntajeJugador;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //INICIALIZAR

            NombreJugador = itemView.findViewById(R.id.NombreJugador);
            CorreoJugador = itemView.findViewById(R.id.CorreoJugador);
            PuntajeJugador = itemView.findViewById(R.id.PuntajeJugador);
        }
    }
}
