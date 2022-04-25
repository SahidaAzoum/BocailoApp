package com.example.bocailoapp.Clases;

import java.util.ArrayList;

public class Plato
{

    int id;
    String nombre;
    String descripcion;
    double precio;
    String imagen;
    ArrayList<String> ingredientes;
    ArrayList<String> alergenos;

    public Plato()
    {
    }

    public Plato(int id, String nombre, String descripcion, double precio, String imagen, ArrayList<String> ingredientes, ArrayList<String> alergenos)
    {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public double getPrecio()
    {
        return precio;
    }

    public void setPrecio(double precio)
    {
        this.precio = precio;
    }

    public String getImagen()
    {
        return imagen;
    }

    public void setImagen(String imagen)
    {
        this.imagen = imagen;
    }

    public ArrayList<String> getIngredientes()
    {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes)
    {
        this.ingredientes = ingredientes;
    }

    public ArrayList<String> getAlergenos()
    {
        return alergenos;
    }

    public void setAlergenos(ArrayList<String> alergenos)
    {
        this.alergenos = alergenos;
    }

    @Override
    public String toString()
    {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                ", ingredientes=" + ingredientes +
                ", alergenos=" + alergenos +
                '}';
    }
}
