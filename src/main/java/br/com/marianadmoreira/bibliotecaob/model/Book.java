package br.com.marianadmoreira.bibliotecaob.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "livros")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    @Column(nullable = false,unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String publisher;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    @Override
    public String toString() {
        return "Book{idBook=" + idBook + ", title=" + title + ", author=" + author + ", category=" + category +", year="+ year + ", publisher=" + publisher +
                ", description=" + description + ", status=" + status + ", loans=" + loans +"}";
    }
}
