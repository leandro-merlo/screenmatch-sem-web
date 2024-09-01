package br.com.manzatech.screenmatch.models;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import br.com.manzatech.screenmatch.services.ConsultaMyMemory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacaoMedia;
    private Integer totalVotos;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String sinopse;
    private String poster;
    @ElementCollection(targetClass = Genero.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "genero_serie")
    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private List<Genero> generos = new ArrayList<>();
    @OneToMany(targetEntity = Ator.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "serie_id")
    private List<Ator> atores = new ArrayList<>();
    @OneToMany(targetEntity = Episodio.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "serie_id")
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacaoMedia = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacaoMedia())).orElse(0);
        this.totalVotos = dadosSerie.totalVotos();
        int strLen = dadosSerie.sinopse().length() > 500 ? 500 : dadosSerie.sinopse().length();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse().substring(0, strLen));
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

    @Transient
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
        return "Serie [generos=" + generos + ", titulo=" + titulo + ", totalTemporadas=" + totalTemporadas
                + ", avaliacaoMedia=" + avaliacaoMedia
                + ", totalVotos=" + totalVotos + ", sinopse=" + sinopse + ", poster=" + poster + ", atores=" + atores
                + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Serie other = (Serie) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    
    
}
