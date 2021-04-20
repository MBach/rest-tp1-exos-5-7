package academy.campus.nantes.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import academy.campus.nantes.rest.entity.Author;
import academy.campus.nantes.rest.entity.Book;
import academy.campus.nantes.rest.repository.AuthorRepository;
import academy.campus.nantes.rest.repository.BookRepository;

@RestController
@RequestMapping("author")
public class AuthorController {

    static Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Page<Author> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @ResponseBody
    @GetMapping("{id}")
    public Author getAuthorById(final @PathVariable("id") String authorId) {
        try {
            Optional<Author> author = authorRepository.findById(Integer.valueOf(authorId));
            return author.get();
        } catch (Exception e) {
            return null;
        }
    }
    
    @ResponseBody
    @GetMapping("{id}/book")
    public List<Book> getBooksByAuthor(final @PathVariable("id") String authorId) {
        try {
            Optional<Author> author = authorRepository.findById(Integer.valueOf(authorId));
            return author.get().getBooks();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    public Author addAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @ResponseBody
    @PutMapping
    public Author editAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @DeleteMapping("{id}")
    public boolean deleteAuthor(final @PathVariable("id") Integer authorId) {
        long c = authorRepository.count();
        authorRepository.deleteById(authorId);
        return c > authorRepository.count();
    }

    @ResponseBody
    @GetMapping(path = "/search", params = "name")
    public List<Author> getAuthorsByName(@RequestParam(value = "name", defaultValue = "") String name) {
        return authorRepository.findByName(name);
    }

    @ResponseBody
    @GetMapping("/duplicates")
    public Set<Author> findIdenticalBookNames() {
        List<String> duplicates = bookRepository.findDuplicates();
        Set<Author> authors = new TreeSet<>();
        for (int i = 0; i < duplicates.size(); i++) {
            LOGGER.info(duplicates.get(i));
            List<Book> b = bookRepository.findByTitle(duplicates.get(i));
            for (int j = 0; j < b.size(); j++) {
                authors.addAll(b.get(j).getAuthors());
            }
        }
        return authors;
    }

}
