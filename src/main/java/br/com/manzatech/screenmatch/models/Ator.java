package br.com.manzatech.screenmatch.models;

public class Ator {

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

    
}
