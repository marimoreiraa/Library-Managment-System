package br.com.marianadmoreira.bibliotecaob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.service.BookService;
import br.com.marianadmoreira.bibliotecaob.service.LoanService;
import br.com.marianadmoreira.bibliotecaob.service.OpenLibraryService;
import jakarta.validation.Valid;

@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    LoanService loanService;

    @Autowired
    OpenLibraryService openLibraryService;

    @GetMapping("/books")
    public String books(Model model){
        var books = bookService.listBooks();
        var totalBooks = books.size();
        var loanedBooks = this.loanService.activeLoans().size();

        model.addAttribute("books", books);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("loanedBooks", loanedBooks);

        return "books/books";
    }

    
    @GetMapping("/books/search")
    public String searchByTitle(Model model,@RequestParam String title) {
        var books = bookService.searchBookByTitle(title);
        var totalBooks = books.size();

        model.addAttribute("books", books);
        model.addAttribute("totalBooks", totalBooks);

        return "books/search";
    }
    
    @GetMapping("/books/add")
    public String addNewBook(Book book) {
        return "books/add";
    }

    @PostMapping("/books/add")
    public String saveNewBook(@Valid Book book, Errors errors) {
        if (errors.hasErrors()) {
            return "add";
        }
        if(bookService.checkIfBookExists(book.getIsbn())){
            return "error/bookExists";
        }
        else{
            this.bookService.addBook(book);
            return "redirect:/books";
            
        }
        

    }

    @GetMapping("/books/edit/{idBook}")
    public String editBook(Book book, Model model){
        book = this.bookService.searchBook(book.getIdBook());
        model.addAttribute("book", book);
        return "books/edit";
    }

    @PostMapping("/books/edit")
    public String afterEdit(@Valid Book book, Errors errors){
        if (errors.hasErrors()) {
            return "edit";
        }

        this.bookService.updateBook(book);
        return "redirect:/books";

    }
    @GetMapping("/books/delete/{idBook}")
    public String removeBook(Book book){
        this.bookService.deleteBook(book);

        return "redirect:/books";
    }

    @GetMapping("/books/import")
    public String showImportPage() {
        return "books/import";
    }

    @PostMapping("/books/import")
    public String importBookByIsbn(@RequestParam String isbn) throws JSONException {
         if(bookService.checkIfBookExists(isbn)){
            return "error/bookExists";
        }
        else{
            Book book = bookService.importBook(isbn);
            bookService.addBook(book);

            return "redirect:/books";
        }
    }

}
