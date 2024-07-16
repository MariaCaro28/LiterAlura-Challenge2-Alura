package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.Autor;
import com.alura.LiterAlura.model.Datos;
import com.alura.LiterAlura.model.DatosLibros;
import com.alura.LiterAlura.model.Libro;
import com.alura.LiterAlura.repository.IAutorRepository;
import com.alura.LiterAlura.repository.ILibroRepository;
import com.alura.LiterAlura.service.ConsumoAPI;
import com.alura.LiterAlura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final IAutorRepository autorRepository;
    private final ILibroRepository libroRepository;

    @Autowired
    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    ***** ELIJA UNA OPCION *****
                    1- Buscar libro por título
                    2- Buscar libro por autor
                    3- Lista de libros registrados
                    4- Lista de autores registrados
                    5- Lista de autores vivos en un año determinado
                    6- Lista de libros por idioma
                    7- Top 10 libros más descargados
                    8- Estadísticas
                    0- Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> buscarLibroPorAutor();
                case 3 -> listarLibrosRegistrados();
                case 4 -> listarAutoresRegistrados();
                case 5 -> listarAutoresVivos();
                case 6 -> listarLibrosPorIdioma();
                case 7 -> top10();
                case 8 -> estadisticas();
                case 0 -> System.out.println("Cerrando la aplicación. Gracias por sus consultas.");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibros getDatosLibroPorTitulo() {
        System.out.println("Escribe el título del libro: ");
        String nombreLibro = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        if (datos.resultados() != null && !datos.resultados().isEmpty()) {
            return datos.resultados().get(0); // Tomar el primer resultado
        }
        return null;
    }

    private DatosLibros getDatosLibroPorAutor() {
        System.out.println("Escribe el nombre del autor: ");
        String nombreAutor = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreAutor.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        if (datos.resultados() != null && !datos.resultados().isEmpty()) {
            return datos.resultados().get(0); // Tomar el primer resultado
        }
        return null;
    }

    private Libro almacenarLibro(DatosLibros datosLibro, Autor autor) {
        List<Libro> existingLibros = libroRepository.findByTitulo(datosLibro.titulo());
        if (!existingLibros.isEmpty()) {
            return existingLibros.get(0); // Devuelve el primer libro encontrado
        }
        Libro libro = new Libro(datosLibro, autor);
        return libroRepository.save(libro);
    }

    //Método 1----------------------------------------------------------------------------
    private void buscarLibroPorTitulo() {
        DatosLibros datosLibros = getDatosLibroPorTitulo();
        StringBuilder sb = new StringBuilder();
        if (datosLibros != null) {
            Autor autor = datosLibros.autor().stream()
                    .map(da -> {
                        Autor existingAutor = autorRepository.findByNombre(da.nombre());
                        if (existingAutor != null) {
                            return existingAutor;
                        } else {
                            Autor nuevoAutor = new Autor(da);
                            return autorRepository.save(nuevoAutor);
                        }
                    })
                    .findFirst()
                    .orElse(null);
            if (autor != null) {
                Libro libro = almacenarLibro(datosLibros, autor);
                sb.append(libro).append("\n");
            }
        } else {
            sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
                    .append("° No se encontraron datos para el título proporcionado. °\n")
                    .append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        }
        System.out.println(sb.toString());
    }

    //Método 2 -----------------------------------------------------------------------------
    private void buscarLibroPorAutor() {
        DatosLibros datosLibros = getDatosLibroPorAutor();
        if (datosLibros != null) {
            Autor autor = datosLibros.autor().stream()
                    .map(da -> {
                        Autor existingAutor = autorRepository.findByNombre(da.nombre());
                        if (existingAutor != null) {
                            return existingAutor;
                        } else {
                            Autor nuevoAutor = new Autor(da);
                            return autorRepository.save(nuevoAutor);
                        }
                    })
                    .findFirst()
                    .orElse(null);
            if (autor != null) {
                Libro libro = almacenarLibro(datosLibros, autor);
                System.out.println(libro);
            }
        } else {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("° No se encontraron datos para el autor proporcionado.  °");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
    }

    //Método 3------------------------------------------------------------------------------
    private void listarLibrosRegistrados() {
        StringBuilder sb = new StringBuilder("Los libros registrados son: \n");
        List<Libro> libros = libroRepository.findAll();
        if (!libros.isEmpty()) {
            libros.forEach(libro -> sb.append(libro).append("\n"));
        } else {
            sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
                    .append("° No hay libros registrados. °\n")
                    .append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        }
        System.out.println(sb.toString());
    }

    //Método 4 -----------------------------------------------------------------------------
    private void listarAutoresRegistrados() {
        StringBuilder sb = new StringBuilder("Los autores registrados son: \n");
        List<Autor> autores = autorRepository.findAll();
        if (!autores.isEmpty()) {
            autores.forEach(autor -> sb.append(autor).append("\n"));
        } else {
            sb.append("No hay autores registrados.\n");
        }
        System.out.println(sb.toString());
    }

    //Método 5 -----------------------------------------------------------------------------
    private void listarAutoresVivos() {
        System.out.println("Escribe el año que quieras saber qué autor estaba vivo: ");
        int fecha = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autores = autorRepository.findByFechaNacimientoLessThanEqualAndFechaFallecimientoGreaterThanEqual(fecha, fecha);
        StringBuilder sb = new StringBuilder();
        if (!autores.isEmpty()) {
            autores.forEach(autor -> sb.append(autor).append("\n"));
        } else {
            sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
                    .append("° No se encontraron autores vivos para el año proporcionado.°\n")
                    .append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        }
        System.out.println(sb.toString());
    }

    //Método 6-------------------------------------------------------------------------------
    private void listarLibrosPorIdioma() {
        var menu = """
            Idiomas para elegir libros a buscar:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """;
        System.out.println(menu);
        System.out.print("Ingrese las dos letras para el idioma elegido: ");
        var idioma = teclado.nextLine();
        List<Libro> listaDeLibrosPorIdioma = libroRepository.buscarLibrosPorIdioma(idioma);
        StringBuilder sb = new StringBuilder();
        if(listaDeLibrosPorIdioma.isEmpty()){
            sb.append("No hay libros registrados en ese idioma\n");
        } else {
            sb.append("Los libros registrados en ese idioma son:\n");
            listaDeLibrosPorIdioma.forEach(libro -> sb.append(libro).append("\n"));
        }
        System.out.println(sb.toString());
    }

    //Método 7 -----------------------------------------------------------------------------
    public void top10() {
        // Top 10 libros más descargados
        StringBuilder sb = new StringBuilder("Top 10 libros más descargados:\n");
        libroRepository.findAll().stream()
                .sorted(Comparator.comparing(Libro::getNumeroDeDescargas).reversed())
                .limit(10)
                .forEach(libro -> sb.append(libro.getTitulo().toUpperCase()).append("\n"));
        System.out.println(sb.toString());
    }

    //Método 8 -----------------------------------------------------------------------------
    public void estadisticas() {
        // Trabajando con estadísticas
        DoubleSummaryStatistics est = libroRepository.findAll().stream()
                .filter(d -> d.getNumeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));
        StringBuilder sb = new StringBuilder();
        sb.append("Cantidad media de descargas: ").append(Math.round(est.getAverage())).append("\n")
                .append("Cantidad máxima de descargas: ").append(Math.round(est.getMax())).append("\n")
                .append("Cantidad mínima de descargas: ").append(Math.round(est.getMin())).append("\n")
                .append("Cantidad de registros evaluados para calcular las estadísticas: ").append(est.getCount()).append("\n");
        System.out.println(sb.toString());
    }
}
