package br.com.marianadmoreira.bibliotecaob.api;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OpenLibraryApiResponse {
    
    @JsonProperty("ISBN")
    private Map<String, OpenLibraryData> isbnData;

    public Map<String, OpenLibraryData> getIsbnData() {
        return isbnData;
    }

    public void setIsbnData(Map<String, OpenLibraryData> isbnData) {
        this.isbnData = isbnData;
    }

    public OpenLibraryData getOpenLibraryDataByIsbn(String isbn) {
        if (isbnData != null) {
            return isbnData.get(isbn);
        }
        return null;
    }
}
