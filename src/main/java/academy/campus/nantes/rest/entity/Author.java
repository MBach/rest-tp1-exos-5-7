package academy.campus.nantes.rest.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entité Auteur persistente en base de données.
 * 
 * @author Matthieu BACHELIER
 * @since 2021
 * @version 1.0
 */
@Entity
@Table(name = "author")
public class Author implements Comparable<Author> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String alias;

    private Integer birth;

    private String firstname;

    private String lastname;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "authors")
    @JsonIgnore
    private List<Book> books = new ArrayList<>();

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return the birth
     */
    public Integer getBirth() {
        return birth;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @param birth the birth to set
     */
    public void setBirth(Integer birth) {
        this.birth = birth;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int compareTo(Author o) {
        if (id < o.id) {
            return -1;
        } else if (id > o.id) {
            return 1;
        } else {
            return 0;
        }
    }

}