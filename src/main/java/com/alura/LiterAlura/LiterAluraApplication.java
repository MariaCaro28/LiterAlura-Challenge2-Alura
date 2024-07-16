package com.alura.LiterAlura;

import com.alura.LiterAlura.principal.Principal;
import com.alura.LiterAlura.repository.IAutorRepository;
import com.alura.LiterAlura.repository.ILibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private ILibroRepository libroRepository;

	@Autowired
	private IAutorRepository AutorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Autowired
	private  Principal principal;

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, AutorRepository);
		principal.muestraMenu();

	}

}
