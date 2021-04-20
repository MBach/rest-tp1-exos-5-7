package academy.campus.nantes.rest.controller;

import java.util.List;
import java.util.Optional;

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

import academy.campus.nantes.rest.entity.Book;
import academy.campus.nantes.rest.repository.BookRepository;

@RestController
@RequestMapping("book")
public class BookController {

    static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;

    @ResponseBody
    @GetMapping
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @ResponseBody
    @GetMapping("{id}")
    public Book getBookById(final @PathVariable("id") String bookId) {
        try {
            Optional<Book> book = bookRepository.findById(Integer.valueOf(bookId));
            return book.get();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        Book saved = bookRepository.save(book);
        return saved;
    }

    @ResponseBody
    @PutMapping("{id}")
    public Book editBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("{id}")
    public boolean deleteAuthor(final @PathVariable("id") Integer authorId) {
        long c = bookRepository.count();
        bookRepository.deleteById(authorId);
        return c > bookRepository.count();
    }

    ///

    @ResponseBody
    @GetMapping(path = "/search", params = "title")
    public List<Book> getBooksByTitle(@RequestParam(value = "title", defaultValue = "") String title) {
        return bookRepository.findByTitle(title);
    }

}
