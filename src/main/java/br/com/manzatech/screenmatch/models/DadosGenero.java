package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosGenero(
    String id,
    @JsonAlias("name") String nome
) {
    
}
