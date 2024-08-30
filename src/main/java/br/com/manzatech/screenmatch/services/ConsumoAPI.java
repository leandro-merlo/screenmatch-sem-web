package br.com.manzatech.screenmatch.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ConsumoAPI {


    private String baseUrl = "";
    private Map<String, String> headers = new HashMap<>();

    public ConsumoAPI() {
    }
    
    public ConsumoAPI(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ConsumoAPI(Map<String, String> headers) {
        this.headers = headers;
    }

    public ConsumoAPI(String baseUrl, Map<String, String> headers) {
        this.baseUrl = baseUrl;
        this.headers = headers;
    }

    public String obterDados(String endereco) {
        var http = HttpClient.newHttpClient();
        var builder = HttpRequest.newBuilder()
                .uri(URI.create("%s%s".formatted(this.baseUrl, endereco)));
        if (null != this.headers) {
            for (var header : this.headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }
        var request = builder.build();
        HttpResponse<String> response = null;

        try {
            response = http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return response.body();
    }

}
