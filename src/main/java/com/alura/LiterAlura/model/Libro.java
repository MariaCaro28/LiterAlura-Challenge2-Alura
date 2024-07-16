package com.alura.LiterAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "idioma")
    private String idioma;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autorId")
    private Autor autor;

    @Column(name = "numeroDedescargas")
    private Double numeroDeDescargas;

    public Libro() {
    }

    public Libro(DatosLibros datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().isEmpty() ? "Desconocido" : (String) datosLibro.idiomas().get(0);
        this.numeroDeDescargas = Double.valueOf(datosLibro.numeroDeDescargas());
        this.autor = autor;
    }

    public Libro(String titulo, String idioma, Autor autor, Double aDouble) {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*********** Libro ***********\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Autor: ").append(autor != null ? autor.getNombre() : "Desconocido").append("\n");
        sb.append("Idioma: ").append(idioma).append("\n");
        sb.append("Número de descargas: ").append(numeroDeDescargas).append("\n");
        sb.append("****************************\n");
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = this.numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Double numeroDeDescargas() {
        return numeroDeDescargas;
    }
}

