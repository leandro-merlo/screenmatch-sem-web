package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        String id,
        @JsonAlias("name") String titulo,
        @JsonAlias("vote_average") String avaliacaoMedia,
        @JsonAlias("vote_count") Integer totalVotos,
        @JsonAlias("number_of_seasons") Integer totalTemporadas,
        @JsonAlias("overview") String sinopse,
        @JsonAlias("poster_path") String poster,
        @JsonAlias("genres") List<DadosGenero> generos,   
        @JsonAlias("cast") List<DadosAtor> atores) {

}
