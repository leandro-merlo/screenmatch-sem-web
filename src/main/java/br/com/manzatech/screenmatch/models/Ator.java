package br.com.manzatech.screenmatch.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "atores")
public class Ator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String personagem;
    private Double popularidade;

    public Ator(DadosAtor dadosAtor) {
        this.nome = dadosAtor.nome();
        this.personagem = dadosAtor.personagem();
        try {
            this.popularidade = Double.parseDouble(dadosAtor.popularidade());
        } catch (Exception e) {
            this.popularidade = 0d;
        }
    }

    public Ator(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPersonagem() {
        return personagem;
    }

    public void setPersonagem(String personagem) {
        this.personagem = personagem;
    }

    public Double getPopularidade() {
        return popularidade;
    }

    public void setPopularidade(Double popularidade) {
        this.popularidade = popularidade;
    }

    @Override
    public String toString() {
        return "Ator [nome=" + nome + ", personagem=" + personagem + ", popularidade=" + popularidade + "]";
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
        Ator other = (Ator) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
