package academy.campus.nantes.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import academy.campus.nantes.rest.entity.Author;

/**
 * Extension du Repository CRUD pour y ajouter des méthodes métier.
 * 
 * @author Matthieu BACHELIER
 * @since 2021
 * @version 1.0
 */
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    /**
     * Recherche un auteur par son nom ou son prénom.
     * 
     * @param name paramètre de recherche
     * @return une liste d'auteurs
     */
    @Query("SELECT a FROM Author a WHERE a.lastname LIKE %:name% or a.firstname LIKE %:name%")
    public List<Author> findByName(@Param("name") String name);

}