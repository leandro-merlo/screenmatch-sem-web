package principal;

import static java.util.function.Function.identity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.manzatech.screenmatch.models.DadosAtor;
import br.com.manzatech.screenmatch.models.DadosEpisodio;
import br.com.manzatech.screenmatch.models.DadosSerie;
import br.com.manzatech.screenmatch.models.Serie;
import br.com.manzatech.screenmatch.services.ConsumoAPI;
import br.com.manzatech.screenmatch.services.ConversorDados;
import br.com.manzatech.screenmatch.services.RecordDeserializer;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private	ConversorDados conversorDados = new ConversorDados();   
    private ConsumoAPI consumoAPI;
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGQ3ZjhmY2FhOTBjYmE3N2ZkYmZhZTQ4NGQ2YTIzNCIsIm5iZiI6MTcyMzY2MjEyNy45OTIwOSwic3ViIjoiNjZiY2ZkN2NmYjA4YjhhYjYyODI1ZWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.9hF5_X8dVqoKUquEGxNjurae2uUFtbmLISWj0R9QwyM";
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String POSTER_PATH = "https://image.tmdb.org/t/p/original/";
    
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private Map<Class, RecordDeserializer> deserializers = new HashMap<>();

    public Principal() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer %s".formatted(BEARER_TOKEN));
        this.consumoAPI = new ConsumoAPI(API_BASE_URL, headers);
		initConversorDados();
    }

    @SuppressWarnings("unchecked")
    private void initConversorDados() {
        this.deserializers.put(DadosEpisodio.class, new RecordDeserializer<>(DadosEpisodio.class));
        this.deserializers.put(DadosSerie.class, new RecordDeserializer<>(DadosSerie.class));
        this.deserializers.forEach((clazz, serializer) ->  this.conversorDados.addDeserializerForClass(clazz, serializer));
		this.conversorDados.register();
    }

    public void exibeMenu() {
        String option = "";
        var menu = """
                1 - Buscar séries;
                2 - Buscar episódios;
                3 - Listar séries buscadas;
                0 - Sair;
                """;
        while (!option.equals("0")) {
            System.out.println(menu);
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    buscarSeriesWeb();
                    break;
                case "2":
                    System.out.println("Opção 2");
                    break;
                case "3":
                    listarSeriesBuscadas();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
        System.out.println("Tchau! Obrigado por utilizar o ScreenMatch");
        // System.out.println("Digite o nome da série para buscar: ");
        // var nome = scanner.nextLine();
        // var nomeSerie = formatarTextoParaURL(nome);
        // var data = consumoAPI.obterDados("search/tv?query=%s&language=pt-BR".formatted(nomeSerie));
        // var results = conversorDados.obterJsonFromPath(data, "results");
        // var lista = conversorDados.obterListaDados(results.toString(), DadosSerie.class);
        // if (!lista.isEmpty()) {
        //     var dadosSerie = consumoAPI.obterDados("tv/%s?language=pt-BR".formatted(lista.get(0).id()));
        //     var serie = conversorDados.obterDados(dadosSerie, DadosSerie.class);
        //     System.out.println(serie);
        //     ArrayList<DadosTemporada> temporadas = new ArrayList<>();
        //     for (int i = 1; i <= serie.totalTemporadas(); i++) {
        //         final var season_data = consumoAPI.obterDados("tv/%s/season/%d?language=pt-BR".formatted(serie.id(), i));
        //         temporadas.add(conversorDados.obterDados(season_data, DadosTemporada.class));
        //     }    
        //     temporadas.forEach(temporada -> {
        //         System.out.println();
        //         System.out.println(temporada.nome());
        //         temporada.episodios().forEach(episodio -> 
        //             System.out.println("\t%s\t- %s.".formatted(episodio.numero(), episodio.titulo()))
        //         );                
        //     });
        //     List<DadosEpisodio> episodios = temporadas.stream()
        //         .flatMap(t -> t.episodios().stream())
        //         .collect(Collectors.toList());

        //     System.out.println();
        //     System.out.println("Top 5:");
        //     episodios.stream()
        //         .sorted(comparator.reversed())
        //         .limit(5)
        //         .map(e -> "%s - %s".formatted(e.titulo(), e.avaliacao()))
        //         .forEach(System.out::println);

        //     List<Episodio> listaEpisodios = temporadas.stream()
        //         .flatMap(t -> t.episodios().stream()
        //             .map(e -> new Episodio(t.numero(), e))
        //         ).collect(Collectors.toList());

        //     listaEpisodios.forEach(System.out::println);
        // } else {
        //     System.out.println("Nenhum resultado encontrado para %s".formatted(nome));
        // }
    }
    
    private void listarSeriesBuscadas() {
        dadosSeries.stream()
            .map(ds -> new Serie(ds))
            .collect(Collectors.toList())
            .stream()
            .forEach(System.out::println);
    }

    private void buscarSeriesWeb() {
        DadosSerie d = getDadosSerie();
        if (null != d) {            
            dadosSeries.add(d);
        }
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série desejada para busca: ");
        var serie = formatarTextoParaURL(scanner.nextLine());
        var json = consumoAPI.obterDados("search/tv?query=%s".formatted(serie));
        json = conversorDados.obterJsonFromPathAsString(json, "results");
        var tempDadosSeries = conversorDados.obterListaDados(json, DadosSerie.class);
        if (tempDadosSeries.size() > 1) {
            var series = tempDadosSeries.stream().collect(Collectors.toMap(DadosSerie::id, identity()));
            System.out.println("Foram encontradas mais de uma série para o termo procurado:");

            series.forEach((k, v) -> 
                System.out.println("id - %s -> %s".formatted(k, v.titulo()))
            );
            System.out.println("Selecione o id da série desejada: ");
            var id = scanner.nextLine();
            while (!series.keySet().contains(id)) {
                System.out.println("Id não encontrado na lista. Tente outro");
                id = scanner.nextLine();
            }
            var dadosSerie = consumoAPI.obterDados("tv/%s".formatted(id));
            var dadosAtores = consumoAPI.obterDados("tv/%s/credits".formatted(id));
            var atoresJson = conversorDados.obterJsonFromPathAsString(dadosAtores, "cast");
            var atores = conversorDados.obterListaDados(atoresJson, DadosAtor.class);
            this.deserializers.get(DadosSerie.class).addDynamicValue("cast", atores);
            var x = conversorDados.obterDados(dadosSerie, DadosSerie.class);            
            this.deserializers.get(DadosSerie.class).addDynamicValue("cast", null);
            return x;
        } else if (tempDadosSeries.isEmpty()) {
            System.out.println("Não foram encontradas séries para o termo digitado %s".formatted(serie));
            return null;
        } else {
            var dadosSerie = consumoAPI.obterDados("tv/%s".formatted(tempDadosSeries.get(0).id()));
            return conversorDados.obterDados(dadosSerie, DadosSerie.class);
        }
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

    class DadosEpisodioAvaliacaoComparator implements Comparator<DadosEpisodio> {
        @Override
        public int compare(DadosEpisodio ep1, DadosEpisodio ep2) {
            return Double.compare(
                    Double.parseDouble(ep1.avaliacao()),
                    Double.parseDouble(ep2.avaliacao()));
        }
    }

}

