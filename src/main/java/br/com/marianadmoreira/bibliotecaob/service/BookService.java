package br.com.marianadmoreira.bibliotecaob.service;

import java.util.List;

import org.json.JSONException;

import br.com.marianadmoreira.bibliotecaob.model.Book;

public interface BookService {
    List<Book> listBooks();

    Book addBook(Book book);

    Book importBook(String isbn) throws JSONException;

    void deleteBook(Book book);

    void updateBook(Book book);

    Book searchBook(Long idBook);

    List<Book> searchBookByTitle(String title);

    boolean checkIfBookExists(String isbn);
}
