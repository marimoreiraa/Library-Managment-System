package br.com.marianadmoreira.bibliotecaob.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "emprestimos")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBorrow;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate returnLimitDate;

    @Column(nullable = false)
    private LocalDate returnDate;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private  BorrowStatus status;

    @Column(nullable = false)
    private Double fine;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    
}
