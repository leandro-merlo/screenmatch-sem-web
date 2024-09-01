package br.com.manzatech.screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.manzatech.screenmatch.repositories.SerieRepository;
import principal.Principal;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	@Autowired
    private SerieRepository serieRepository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal p = new Principal(serieRepository);
		p.exibeMenu();
		// Map<String, String> headers = new HashMap<>();
		// headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGQ3ZjhmY2FhOTBjYmE3N2ZkYmZhZTQ4NGQ2YTIzNCIsIm5iZiI6MTcyMzY2MjEyNy45OTIwOSwic3ViIjoiNjZiY2ZkN2NmYjA4YjhhYjYyODI1ZWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.9hF5_X8dVqoKUquEGxNjurae2uUFtbmLISWj0R9QwyM");
		// var consumoAPI = new ConsumoAPI("https://api.themoviedb.org/3/", headers);
		// var result = consumoAPI.obterDados(
		// 		"search/tv?query=%s&language=pt-BR".formatted(URLEncoder.encode("sobrenatural", "UTF-8")));
		// System.out.println(result);
		

		// var results = conversorDados.obterJsonFromPath(result, "results");
		// var lista = conversorDados.obterListaDados(results.toString(), DadosSerie.class);

		// des.addDynamicValue("numero", 1);
		// var serie = lista.get(0);
		// var serie_data = consumoAPI.obterDados("tv/%s?language=pt-BR".formatted(serie.id()));
		// serie = conversorDados.obterDados(serie_data, DadosSerie.class);
		// System.out.println(serie);
		// System.out.println();

		// var episode_data = consumoAPI.obterDados("tv/%s/season/%d/episode/%d?language=pt-BR".formatted(serie.id(), 1, 1));
		// var episode = conversorDados.obterDados(episode_data, DadosEpisodio.class);
		// System.out.println(episode);
		// System.out.println();

		// des.addDynamicValue("numero", 2);
		// episode_data = consumoAPI.obterDados("tv/%s/season/%d/episode/%d?language=pt-BR".formatted(serie.id(), 1, 2));
		// episode = conversorDados.obterDados(episode_data, DadosEpisodio.class);
		// System.out.println(episode);

		// ArrayList<DadosTemporada> temporadas = new ArrayList<>();
		// System.out.println();
		// for (int i = 1; i <= serie.totalTemporadas(); i++) {
		// 	var season_data = consumoAPI.obterDados("tv/%s/season/%d?language=pt-BR".formatted(serie.id(), i));
		// 	temporadas.add(conversorDados.obterDados(season_data, DadosTemporada.class));
		// }

		// temporadas.forEach(System.out::println);

		// var cafeAPI = new ConsumoAPI("https://coffee.alexflipnote.dev/");
		// System.out.println(cafeAPI.obterDados("random.json"));
	}

}
