package com.alura.LiterAlura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fechaNacimiento")
    private Integer fechaNacimiento;

    @Column( name = "fechaFallecimiento")
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*********** Autor ***********\n");
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("Fecha de nacimiento: ").append(fechaNacimiento).append("\n");
        sb.append("Fecha de fallecimiento: ").append(fechaFallecimiento).append("\n");
        sb.append("****************************\n");
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }


}

