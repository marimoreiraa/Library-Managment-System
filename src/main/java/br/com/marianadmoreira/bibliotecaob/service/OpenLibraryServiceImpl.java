package br.com.marianadmoreira.bibliotecaob.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class OpenLibraryServiceImpl implements OpenLibraryService{
    

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getBookByIsbn(String isbn){
        String url = "https://openlibrary.org/isbn/" + isbn + ".json";

        return restTemplate.getForObject(url, String.class);
    }
}
