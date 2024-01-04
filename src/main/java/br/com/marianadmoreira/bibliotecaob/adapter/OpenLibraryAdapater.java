package br.com.marianadmoreira.bibliotecaob.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.Category;

public class OpenLibraryAdapater {
    
    public static Book adaptOpenLibraryDataToBookModel(JSONObject bookObject) throws JSONException{
        Book book = new Book();

        book.setTitle(bookObject.getString("title"));
        book.setCategory(Category.DESCONHECIDA);

        JSONArray publishersArray = bookObject.getJSONArray("publishers");

        if(publishersArray.length() > 0){
            String firstPublisher = publishersArray.getString(0);
            book.setPublisher(firstPublisher);
        }
        else{
            book.setPublisher("Desconhecido");
        }
        
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
