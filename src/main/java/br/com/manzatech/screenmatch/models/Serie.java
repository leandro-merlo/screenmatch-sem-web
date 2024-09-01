package br.com.manzatech.screenmatch.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import br.com.manzatech.screenmatch.services.ConsultaMyMemory;

public class Serie {

    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacaoMedia;
    private Integer totalVotos;
    private String sinopse;
    private String poster;
    private List<Genero> generos = new ArrayList<>();
    private List<Ator> atores = new ArrayList<>();

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacaoMedia = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacaoMedia())).orElse(0);
        this.totalVotos = dadosSerie.totalVotos();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse().substring(0, 500));
        this.poster = dadosSerie.poster();
        dadosSerie.generos().forEach(g -> {
            Optional<Genero> optional = Genero.getById(Integer.parseInt(g.id()));
            if (optional.isPresent()) {
                this.generos.add(optional.get());
            }
        });
        dadosSerie.atores().forEach(a -> {
            this.atores.add(new Ator(a));            
        });
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Integer getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(Integer totalVotos) {
        this.totalVotos = totalVotos;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Genero getGeneroPrincipal() {
        return this.generos.stream().findFirst().orElse(Genero.UNCLASSIFIED);
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }

    @Override
    public String toString() {
        return "Serie [generos=" + generos + ", titulo=" + titulo + ", totalTemporadas=" + totalTemporadas + ", avaliacaoMedia=" + avaliacaoMedia
                + ", totalVotos=" + totalVotos + ", sinopse=" + sinopse + ", poster=" + poster +  ", atores=" + atores + "]";
    }

}
