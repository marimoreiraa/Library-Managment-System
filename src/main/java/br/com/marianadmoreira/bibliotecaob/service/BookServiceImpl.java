package br.com.marianadmoreira.bibliotecaob.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marianadmoreira.bibliotecaob.adapter.OpenLibraryAdapater;
import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.BookStatus;
import br.com.marianadmoreira.bibliotecaob.repository.IBookRepository;

@Service
public class BookServiceImpl implements BookService{
    
    @Autowired
    private IBookRepository bookRepository;

    private final OpenLibraryService openLibraryService;

    @Autowired
    public BookServiceImpl(@Autowired(required = false) OpenLibraryService openLibraryService) {
        this.openLibraryService = openLibraryService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> listBooks() {
        return this.bookRepository.findAll();

    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        book.setStatus(BookStatus.DISPONIVEL);
        return this.bookRepository.save(book);

    }

    @Override
    public Book importBook(String isbn) throws JSONException {

        String jsonResponse = openLibraryService.getBookByIsbn(isbn);
        System.out.println(jsonResponse);
        JSONObject bookObject = new JSONObject(jsonResponse);
        // JSONObject bookObject = jsonObject.getJSONObject("isbn_13");

        Book book = OpenLibraryAdapater.adaptOpenLibraryDataToBookModel(bookObject);
        

        if(book != null){
            book.setIsbn(isbn);
            return this.addBook(book);
        }
        else{
            return null;
        }

    }

    @Override
    @Transactional
    public void deleteBook(Book book) {
        this.bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        Book bookUpdating = bookRepository.findById(book.getIdBook()).get();
        bookUpdating.setIdBook(book.getIdBook());
        bookUpdating.setAuthor(book.getAuthor());
        bookUpdating.setTitle(book.getTitle());
        bookUpdating.setCategory(book.getCategory());
        bookUpdating.setDescription(book.getDescription());
        bookUpdating.setYear(book.getYear());
        bookUpdating.setPublisher(book.getPublisher());
        bookUpdating.setLoans(book.getLoans());
        bookUpdating.setStatus(book.getStatus());

        this.bookRepository.save(bookUpdating);

    }

    @Override
    @Transactional(readOnly = true)
    public Book searchBook(Long idBook) {
        return this.bookRepository.findById(idBook).orElse(null);
    }


    @Transactional(readOnly = true)
    @Override
    public List<Book> searchBookByTitle(String title) {
        var allBooks = this.listBooks();
        List<Book> books = new ArrayList<>();

        if(title.isBlank()){
            return allBooks;
        }
        
        for(Book book: allBooks){
            if(book.toString().toUpperCase().contains(title.toUpperCase())){
                books.add(book);
            }
        }

        return books;
    }

    @Override
    public boolean checkIfBookExists(String isbn) {
        Book bookExists = bookRepository.findByIsbn(isbn);
        if(bookExists != null){
            return true ;     
        }
        else{
            return false;
        }
    }
    
}
