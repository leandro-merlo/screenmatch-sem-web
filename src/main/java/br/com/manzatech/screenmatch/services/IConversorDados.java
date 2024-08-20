package br.com.manzatech.screenmatch.services;

public interface IConversorDados {

    <T> T obterDados(String json, Class<T> clazz);
}
