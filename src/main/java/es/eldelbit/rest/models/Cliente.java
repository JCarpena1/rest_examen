/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.eldelbit.rest.models;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import java.sql.Timestamp;

/**
 *
 * @author virtualbox
 */
@JsonbPropertyOrder(PropertyOrderStrategy.ANY)
public class Cliente {

    private Integer id;
    
    private String nombre;
        
    // @JsonbProperty(nillable = true)
    private Integer edad;
    
    private String direccion;
    
    @JsonbProperty("fecha_nacimiento_1")
    private Timestamp fechaNacimiento;
    
    private Timestamp createdAt;
    
    private Timestamp updatedAt;

    public Cliente() {
    }

    public Cliente(Integer id, String nombre, Integer edad, String direccion, Timestamp fechaNacimiento, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.nombre = nombre;       
        this.edad = edad;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp FechaNacimiento) {
        this.fechaNacimiento = FechaNacimiento;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}