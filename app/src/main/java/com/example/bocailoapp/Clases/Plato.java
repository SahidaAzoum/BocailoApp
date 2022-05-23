package com.example.bocailoapp.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Plato implements Serializable
{

    int id;
    String nombre;
    String descripcion;
    String tipo;
    double precio;
    String imagen;
    String observaciones;
    ArrayList<String> ingredientes;
    ArrayList<String> alergenos;

    public Plato()
    {
    }

    public Plato(int id, String nombre, String descripcion, String tipo, double precio, String imagen, String observaciones)
    {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.imagen = imagen;
        this.observaciones = observaciones;
    }

    public Plato(int id, String nombre, String descripcion, String tipo, double precio, String imagen, ArrayList<String> ingredientes, ArrayList<String> alergenos)
    {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo= tipo;
        this.precio = precio;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
    }

    public Plato(int id, String nombre, String descripcion, String tipo, double precio, String imagen, String observaciones, ArrayList<String> ingredientes, ArrayList<String> alergenos)
    {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.imagen = imagen;
        this.observaciones = observaciones;
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

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
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

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    @Override
    public String toString()
    {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                ", ingredientes=" + ingredientes +
                ", alergenos=" + alergenos +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plato plato = (Plato) o;
        return id == plato.id && Double.compare(plato.precio, precio) == 0 && nombre.equals(plato.nombre) && descripcion.equals(plato.descripcion) && tipo.equals(plato.tipo) && imagen.equals(plato.imagen) && Objects.equals(observaciones, plato.observaciones) && Objects.equals(ingredientes, plato.ingredientes) && Objects.equals(alergenos, plato.alergenos);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, nombre, descripcion, tipo, precio, imagen, observaciones, ingredientes, alergenos);
    }
}
