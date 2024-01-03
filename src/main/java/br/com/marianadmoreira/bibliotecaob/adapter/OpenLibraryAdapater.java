package br.com.marianadmoreira.bibliotecaob.adapter;

import java.util.ArrayList;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.Category;

public class OpenLibraryAdapater {
    
    public static Book adaptOpenLibraryDataToBookModel(JSONObject bookObject) throws JSONException{
        Book book = new Book();

        book.setTitle(bookObject.getString("title"));
        book.setCategory(Category.DESCONHECIDA);
        book.setPublisher(bookObject.getString("publishers"));
        if(bookObject.getString("publish_date").length() > 4){
            System.out.println(bookObject.getString("publish_date").length());
            book.setYear((Integer.parseInt(bookObject.getString("publish_date").substring(8, 12))));
        }
        else{
           book.setYear(Integer.parseInt(bookObject.getString("publish_date"))); 
        }
        
        book.setLoans(new ArrayList<>());

        return book;

    }
}
