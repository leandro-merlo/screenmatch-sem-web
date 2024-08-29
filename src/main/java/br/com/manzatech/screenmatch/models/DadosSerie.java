package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        String id,
        @JsonAlias("name") String titulo,
        @JsonAlias("vote_average") String avaliacaoMedia,
        @JsonAlias("vote_count") Integer totalVotos,
        @JsonAlias("number_of_seasons") Integer totalTemporadas,
        @JsonAlias("overview") String descricao) {

}
