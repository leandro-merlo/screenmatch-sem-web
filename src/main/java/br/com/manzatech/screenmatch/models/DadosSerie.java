package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        String id,
        @JsonAlias("id") String codigoTmdb,
        @JsonAlias("name") String titulo,
        @JsonAlias("vote_average") String avaliacaoMedia,
        @JsonAlias("vote_count") Integer totalVotos,
        @JsonAlias("number_of_seasons") Integer totalTemporadas,
        @JsonAlias("overview") String sinopse,
        @JsonAlias("poster_path") String poster,
        @JsonAlias("genres") List<DadosGenero> generos,   
        @JsonAlias("cast") List<DadosAtor> atores) {

        public DadosSerie {
           if (null == generos) generos = new ArrayList<>();
           if (null == atores) atores = new ArrayList<>();
        }
}
