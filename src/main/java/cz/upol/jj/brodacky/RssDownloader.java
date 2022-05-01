package main.java.cz.upol.jj.brodacky;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class RssDownloader {
    public String download(String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(url))
              .build();
              
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            throw new Exception("Error while downloading feed!", e);
        }
    }
}
