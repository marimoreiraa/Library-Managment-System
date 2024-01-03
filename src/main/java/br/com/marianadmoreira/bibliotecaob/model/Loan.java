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
import lombok.ToString;

@Data
@Entity
@Table(name = "emprestimos")
public class Loan {

    public static final int LIMIT_BORROWS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoan;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate returnLimitDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private  LoanStatus status;

    @Column(nullable = false)
    private Double fine;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    private Book book;
    
    @Override
    public String toString() {
        return "Loan{" +
                "idLoan=" + idLoan +
                ", loanDate=" + loanDate +
                ", returnLimitDate=" + returnLimitDate +
                ", status=" + status +
                ", fine=" + fine +
                // Evitar recursão infinita
                ", user=" + (user != null ? user.getIdUser() : null) +
                ", book=" + (book != null ? book.getIdBook() : null) +
                '}';
    }
    
}
