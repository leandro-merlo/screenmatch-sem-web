package br.com.manzatech.screenmatch.principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${console.encoding:UTF-8}") 
    public String consoleEncoding;

    
}