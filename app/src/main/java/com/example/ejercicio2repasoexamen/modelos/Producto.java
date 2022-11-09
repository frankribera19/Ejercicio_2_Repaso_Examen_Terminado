package com.example.ejercicio2repasoexamen.modelos;

import java.io.Serializable;

public class Producto implements Serializable{

    //VARIABLES
    private String nombre;
    private float precio;
    private int cantidad;
    private float total;

    //CONSTRUCTORES, UNO VACIO Y EL OTRO CON TODAS LAS VARIABLES
    public Producto() {
    }

    public Producto(String nombre, float precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.total = this.cantidad * this.precio;
    }

    //GETTER & SETTERS DE LAS VARIABLES
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    //GENÉRICOS
    public void calcularTotal(){
        this.total = this.precio * this.cantidad;
    }
}
