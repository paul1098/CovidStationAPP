package com.example.covidstation;

public class Usuario {
String Uid, Nombre, Edad, País, Correo, Contraseña;
int Kills;

    public Usuario(){

    }

    public Usuario(String uid, String nombre, String edad, String país, String correo, String contraseña, int kills) {
        Uid = uid;
        Nombre = nombre;
        Edad = edad;
        País = país;
        Correo = correo;
        Contraseña = contraseña;
        Kills = kills;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getPaís() {
        return País;
    }

    public void setPaís(String país) {
        País = país;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public int getKills() {
        return Kills;
    }

    public void setKills(int kills) {
        Kills = kills;
    }
}
