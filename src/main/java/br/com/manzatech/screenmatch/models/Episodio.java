package br.com.manzatech.screenmatch.models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import br.com.manzatech.screenmatch.services.ConsultaMyMemory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;
    private Integer numero;
    private String titulo;
    private Double avaliacao;

    @JsonBackReference()
    @ManyToOne(targetEntity = Serie.class)
    private Serie serie;
    
    @Temporal(TemporalType.DATE)
    private LocalDate dataLancamento;

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {
        this.setDadosEpisodio(temporada, dadosEpisodio);
    }

    public Episodio(){}

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Episodio [temporada=" + temporada + ", numero=" + numero + ", titulo=" + titulo + ", avaliacao="
                + avaliacao + ", dataLancamento=" + dataLancamento + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDadosEpisodio(Integer temporada, DadosEpisodio dadosEpisodio) {
        this.temporada = temporada;
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());            
        } catch (NumberFormatException e) {
            this.avaliacao = 0d;
        }
        this.numero = dadosEpisodio.numero();
        this.titulo =  ConsultaMyMemory.obterTraducao(dadosEpisodio.titulo());
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());            
        } catch (DateTimeParseException e) {
            this.dataLancamento = null;
        }
    }

    public Serie getSerie() {
        return serie;
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
        Episodio other = (Episodio) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

    
}
