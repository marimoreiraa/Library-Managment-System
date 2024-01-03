package br.com.marianadmoreira.bibliotecaob.api;

import lombok.Data;

@Data
public class OpenLibraryData {
    private String isbn;
    private String title;
    private String author_name;
    private int publish_date;
    private String publisher;
}
