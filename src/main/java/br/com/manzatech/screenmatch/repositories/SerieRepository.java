package br.com.manzatech.screenmatch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import br.com.manzatech.screenmatch.models.Episodio;
import br.com.manzatech.screenmatch.models.Genero;
import br.com.manzatech.screenmatch.models.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long>{  

    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtores_NomeContainingIgnoreCase(String nomeAtor);

    List<Serie> findTop5ByOrderByAvaliacaoMediaDesc();

    List<Serie> findByGeneros(Optional<Genero> g);

    @Query("select s from Serie s where s.totalTemporadas <= :temporadas and s.avaliacaoMedia >= :avaliacao")
    List<Serie> seriesPorTemporadaAvalicao(int temporadas, double avaliacao);

    @Query("select e from Serie s join s.episodios e where e.titulo ilike %:trecho%")
    List<Episodio> episodiosPorTrecho(String trecho);

    @Query("select e from Serie s join s.episodios e where s = :serie order by e.avaliacao desc limit 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);
}
