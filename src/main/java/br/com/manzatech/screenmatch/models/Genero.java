package br.com.manzatech.screenmatch.models;

import java.util.Arrays;
import java.util.Optional;

public enum Genero {

    ACTION_AND_ADVENTURE("Ação e Aventura", 10759),
    ANIMATION("Animação", 16),
    COMEDY("Comédia", 35),
    CRIME("Crime", 80),
    DOCUMENTARY("Documentário", 99),
    DRAMA("Drama", 18),
    FAMILY("Família", 10751),
    KIDS("Kids", 10762),
    MISTERY("Mistério", 9648),
    NEWS("Jornalismo", 10763),
    REALITY("Realidade", 10764),
    SCIFI_AND_FANTASY("Sci-Fi e Fantasia", 10765),
    SOAP("Telenovela", 10766),
    TALK("Entrevista", 10767),
    WAR_AND_POLITICS("Guerra e Política", 10768),
    WESTERN("Velho Oeste", 37),
    UNCLASSIFIED("Não classificadada", -1);
    ;

    private String nome;
    private Integer id;

    private Genero(String nome, Integer id) {
        this.nome = nome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getId() {
        return id;
    }

    public static Optional<Genero> getById(Integer id) {
        return Arrays.stream(values()).filter(v -> v.id.equals(id)).findFirst();
    }

    public static Optional<Genero> getByName(String nome) {
        return Arrays.stream(values()).filter(v -> v.nome.equals(nome)).findFirst();
    }    

    @Override
    public String toString() {
        return this.getNome();
    }
}
