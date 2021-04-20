package academy.campus.nantes.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import academy.campus.nantes.rest.entity.Author;
import academy.campus.nantes.rest.entity.Book;
import academy.campus.nantes.rest.repository.AuthorRepository;
import academy.campus.nantes.rest.repository.BookRepository;

/**
 * Point d'entrée du projet.
 * 
 * @author Matthieu BACHELIER
 * @since 2021
 * @version 1.0
 */
@SpringBootApplication
public class RestWsTP1Exo5Application {

    static Logger LOGGER = LoggerFactory.getLogger(RestWsTP1Exo5Application.class);

    public static void main(String[] args) {
        SpringApplication.run(RestWsTP1Exo5Application.class, args);
    }

    @Value(value = "${populatedb}")
    private boolean populatedb;

    @Value(value = "${authorsToGenerate}")
    private int authorsToGenerate;

    @Value(value = "${booksToGenerate}")
    private int booksToGenerate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Pour ce TP on autorise les requêtes cross domain car il est fort probable que le client REST tourne sur un autre
     * port de la machine.
     * 
     * @return un filtrage CORS
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @PostConstruct
    private void init() throws IOException {
        if (populatedb) {
            LOGGER.info("About to populate database from generated data");
        } else {
            LOGGER.info("Populate database at startup is disabled");
            return;
        }

        Faker faker = new Faker(new Locale("fr"));

        LOGGER.info("Generating " + authorsToGenerate + " authors");
        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < authorsToGenerate; i++) {
            Name name = faker.name();
            Author a = new Author();
            a.setBirth(faker.number().numberBetween(1800, 1830));
            a.setFirstname(name.firstName());
            a.setLastname(name.lastName());
            a.setAlias(a.getFirstname().toLowerCase() + "." + a.getLastname().toLowerCase());
            authors.add(a);
        }
        authorRepository.saveAll(authors);

        LOGGER.info("Generating " + booksToGenerate + " books");
        long totalAuthor = authorRepository.count();
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < booksToGenerate; i++) {
            Book book = new Book();
            book.setTitle(faker.book().title());
            book.setIsbn(faker.number().digits(13));
            Author author1 = authors.get((int) (Math.random() * totalAuthor));
            Author author2 = authors.get((int) (Math.random() * totalAuthor));
            Author author3 = authors.get((int) (Math.random() * totalAuthor));

            List<Author> as = new ArrayList<>();
            as.add(author1);
            if (i % 5 == 0 && !author1.getId().equals(author2.getId())) {
                as.add(author2);
            }
            if (i % 5 == 0 && !author2.getId().equals(author3.getId())) {
                as.add(author3);
            }
            book.setAuthors(as);
            int birth = author1.getBirth();
            for (Author a : authors) {
                if (a.getBirth() > birth) {
                    birth = a.getBirth();
                }
            }
            int year = (int) (birth + 18 + Math.random() * 40);
            book.setYear(year);
            books.add(book);
        }
        bookRepository.saveAll(books);
    }
}
