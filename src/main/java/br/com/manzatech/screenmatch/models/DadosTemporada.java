package br.com.manzatech.screenmatch.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(    
    @JsonAlias("season_number") Integer numero, 
    @JsonAlias("episodes") List<DadosEpisodio> episodios,
    @JsonAlias("overview") String sinopse,
    @JsonAlias("name") String nome 
) {

}
