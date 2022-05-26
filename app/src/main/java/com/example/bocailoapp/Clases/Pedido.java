package com.example.bocailoapp.Clases;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido implements Serializable
{
    int numPedido;
    LocalDate fecha;
    String usuario;
    String nombre;
    String apellidos;
    String email;
    String calle;
    String poblacion;
    String cp;
    String telefono;
    String estado;
    String observaciones;

    boolean enviado;
    ArrayList<Plato> pedido = new ArrayList<Plato>();

    public Pedido()
    {
    }

    public Pedido(int numPedido, LocalDate fecha, String usuario, String nombre, String apellidos, String email, String calle, String poblacion, String cp, String telefono, String estado, boolean enviado, ArrayList<Plato> pedido)
    {
        this.numPedido = numPedido;
        this.fecha = fecha;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.calle = calle;
        this.poblacion = poblacion;
        this.cp = cp;
        this.telefono = telefono;
        this.estado = estado;
        this.enviado = enviado;
        this.pedido = pedido;
    }

    public Pedido(int numPedido, LocalDate fecha, String usuario, String nombre, String apellidos, String email, String calle, String poblacion, String cp, String telefono, String estado, String observaciones, boolean enviado, ArrayList<Plato> pedido)
    {
        this.numPedido = numPedido;
        this.fecha = fecha;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.calle = calle;
        this.poblacion = poblacion;
        this.cp = cp;
        this.telefono = telefono;
        this.estado = estado;
        this.observaciones = observaciones;
        this.enviado = enviado;
        this.pedido = pedido;
    }

    public int getNumPedido()
    {
        return numPedido;
    }

    public void setNumPedido(int numPedido)
    {
        this.numPedido = numPedido;
    }

    public LocalDate getFecha()
    {
        return fecha;
    }

    public void setFecha(LocalDate fecha)
    {
        this.fecha = fecha;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getEstado()
    {
        return estado;
    }

    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }

    public boolean isEnviado()
    {
        return enviado;
    }

    public void setEnviado(boolean enviado)
    {
        this.enviado = enviado;
    }

    public ArrayList<Plato> getPedido()
    {
        return pedido;
    }

    public void setPedido(ArrayList<Plato> pedido)
    {
        this.pedido = pedido;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCalle()
    {
        return calle;
    }

    public void setCalle(String calle)
    {
        this.calle = calle;
    }

    public String getPoblacion()
    {
        return poblacion;
    }

    public void setPoblacion(String poblacion)
    {
        this.poblacion = poblacion;
    }

    public String getCp()
    {
        return cp;
    }

    public void setCp(String cp)
    {
        this.cp = cp;
    }
}
