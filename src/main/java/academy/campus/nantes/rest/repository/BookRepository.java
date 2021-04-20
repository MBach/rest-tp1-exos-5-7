package academy.campus.nantes.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import academy.campus.nantes.rest.entity.Book;

/**
 * Extension du Repository CRUD pour y ajouter des méthodes métier.
 * 
 * @author Matthieu BACHELIER
 * @since 2021
 * @version 1.0
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * Recherche un livre selon son titre.
     * 
     * @param title paramètre de recherche
     * @return une liste de livres
     */
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    public List<Book> findByTitle(@Param("title") String title);
    
    
    @Query("SELECT b.title FROM Book b GROUP BY b.title HAVING COUNT(*) > 1 ORDER BY COUNT(*) ASC")
    public List<String> findDuplicates();
}