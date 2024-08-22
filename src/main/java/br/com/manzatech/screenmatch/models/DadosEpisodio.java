package br.com.manzatech.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
    @JsonAlias("name") String titulo,
    Integer numero,
    @JsonAlias("vote_average") String avaliacao,
    @JsonAlias("air_date") String dataLancamento
) {

}
