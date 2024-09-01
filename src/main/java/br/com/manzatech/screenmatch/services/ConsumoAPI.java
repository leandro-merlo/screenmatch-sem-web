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
        return this.obterDados(endereco, "GET", null);
    }

    public String obterDados(String endereco, String method, String body) {
        var http = HttpClient.newHttpClient();
        var builder = HttpRequest.newBuilder()
                .uri(URI.create("%s%s".formatted(this.baseUrl, endereco)));
        if (null != this.headers) {
            for (var header : this.headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }

        HttpResponse<String> response = null;
        HttpRequest request = null;

        if (null != method) {
            switch (method) {
                case "POST":
                    builder.POST(HttpRequest.BodyPublishers.ofString(body));
                    break;
                default:
            }
        }
        
        try {
            request = builder.build();
            response = http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response.body();
    }

}
