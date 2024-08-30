package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAtor(
    String id,
    @JsonAlias("name") String nome,
    @JsonAlias("character") String personagem,
    @JsonAlias("popularity") String popularidade
) {
    
}
