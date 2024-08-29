package principal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.manzatech.screenmatch.models.DadosEpisodio;
import br.com.manzatech.screenmatch.models.DadosSerie;
import br.com.manzatech.screenmatch.models.DadosTemporada;
import br.com.manzatech.screenmatch.models.Episodio;
import br.com.manzatech.screenmatch.services.ConsumoAPI;
import br.com.manzatech.screenmatch.services.ConversorDados;
import br.com.manzatech.screenmatch.services.RecordDeserializer;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private	ConversorDados conversorDados = new ConversorDados();
    private ConsumoAPI consumoAPI;
    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGQ3ZjhmY2FhOTBjYmE3N2ZkYmZhZTQ4NGQ2YTIzNCIsIm5iZiI6MTcyMzY2MjEyNy45OTIwOSwic3ViIjoiNjZiY2ZkN2NmYjA4YjhhYjYyODI1ZWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.9hF5_X8dVqoKUquEGxNjurae2uUFtbmLISWj0R9QwyM";
    private final String API_BASE_URL = "https://api.themoviedb.org/3/";

    public Principal() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer %s".formatted(BEARER_TOKEN));
        this.consumoAPI = new ConsumoAPI(API_BASE_URL, headers);
		initConversorDados();
    }

    private void initConversorDados() {        
		var des = new RecordDeserializer(DadosEpisodio.class);
		this.conversorDados.addDeserializerForClass(DadosEpisodio.class, des);
		this.conversorDados.register();
    }

    public void exibeMenu() {
        System.out.println("Digite o nome da série para buscar: ");
        var nome = scanner.nextLine();
        var nomeSerie = formatarTextoParaURL(nome);
        var data = consumoAPI.obterDados("search/tv?query=%s&language=pt-BR".formatted(nomeSerie));
        var results = conversorDados.obterJsonFromPath(data, "results");
        var lista = conversorDados.obterListaDados(results.toString(), DadosSerie.class);
        if (!lista.isEmpty()) {
            var dadosSerie = consumoAPI.obterDados("tv/%s?language=pt-BR".formatted(lista.get(0).id()));
            var serie = conversorDados.obterDados(dadosSerie, DadosSerie.class);
            System.out.println(serie);
            ArrayList<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serie.totalTemporadas(); i++) {
                var season_data = consumoAPI.obterDados("tv/%s/season/%d?language=pt-BR".formatted(serie.id(), i));
                temporadas.add(conversorDados.obterDados(season_data, DadosTemporada.class));
            }    
            temporadas.forEach((temporada) -> {
                System.out.println();
                System.out.println(temporada.nome());
                temporada.episodios().forEach((episodio) -> {
                    System.out.println("\t%s\t- %s.".formatted(episodio.numero(), episodio.titulo()));
                });                
            });
            List<DadosEpisodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

            System.out.println();
            System.out.println("Top 5:");
            episodios.stream()
                .sorted(getComparator().reversed())
                .limit(5)
                .map(e -> "%s - %s".formatted(e.titulo(), e.avaliacao()))
                .forEach(System.out::println);;

            List<Episodio> listaEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                    .map(e -> new Episodio(t.numero(), e))
                ).collect(Collectors.toList());

            listaEpisodios.forEach(System.out::println);
        } else {
            System.out.println("Nenhum resultado encontrado para %s".formatted(nome));
        }
    }

    private Comparator<DadosEpisodio> getComparator() {
        return new Comparator<DadosEpisodio>() {
            @Override
            public int compare(DadosEpisodio ep1, DadosEpisodio ep2) {
                return Double.compare(
                    Double.parseDouble(ep1.avaliacao()), 
                    Double.parseDouble(ep2.avaliacao())
                );
            }
        };
    }

    private String formatarTextoParaURL(String texto) {
        try {
            return URLEncoder.encode(texto, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Houve um problema ao tentar converter o texto %s".formatted(texto));
            e.printStackTrace();
            return null;
        }
    }
}
