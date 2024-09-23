package br.com.manzatech.screenmatch.dto;

import br.com.manzatech.screenmatch.models.Ator;
import br.com.manzatech.screenmatch.models.Episodio;
import br.com.manzatech.screenmatch.models.Genero;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public record SerieDTO(
    Long id,
    String codigoTmdb,
    String titulo,
    Integer totalTemporadas,
    Double avaliacaoMedia,
    Integer totalVotos,
    String sinopse,
    String poster,
    List<Genero> generos,
    List<Ator> atores
) {
}
