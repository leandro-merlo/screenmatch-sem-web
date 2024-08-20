package br.com.manzatech.screenmatch;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.manzatech.screenmatch.models.DadosSerie;
import br.com.manzatech.screenmatch.services.ConsumoAPI;
import br.com.manzatech.screenmatch.services.ConversorDados;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGQ3ZjhmY2FhOTBjYmE3N2ZkYmZhZTQ4NGQ2YTIzNCIsIm5iZiI6MTcyMzY2MjEyNy45OTIwOSwic3ViIjoiNjZiY2ZkN2NmYjA4YjhhYjYyODI1ZWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.9hF5_X8dVqoKUquEGxNjurae2uUFtbmLISWj0R9QwyM");
		var consumoAPI = new ConsumoAPI("https://api.themoviedb.org/3/", headers);
		var result = consumoAPI.obterDados(
				"search/tv?query=%s&language=pt-BR".formatted(URLEncoder.encode("sobrenatural", "UTF-8")));
		// System.out.println(result);
		
		
		ConversorDados conversorDados = new ConversorDados();
		var results = conversorDados.obterJsonFromPath(result, "results");
		var lista = conversorDados.obterListaDados(results.toString(), DadosSerie.class);

		System.out.println(lista);
		// results.forEach(s -> System.out.println(s));

		// var cafeAPI = new ConsumoAPI("https://coffee.alexflipnote.dev/");
		// System.out.println(cafeAPI.obterDados("random.json"));
	}

}
