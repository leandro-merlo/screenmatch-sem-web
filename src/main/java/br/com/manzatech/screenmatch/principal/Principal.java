package br.com.manzatech.screenmatch.principal;

import static java.util.function.Function.identity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import br.com.manzatech.screenmatch.models.DadosAtor;
import br.com.manzatech.screenmatch.models.DadosEpisodio;
import br.com.manzatech.screenmatch.models.DadosSerie;
import br.com.manzatech.screenmatch.models.DadosTemporada;
import br.com.manzatech.screenmatch.models.Episodio;
import br.com.manzatech.screenmatch.models.Genero;
import br.com.manzatech.screenmatch.models.Serie;
import br.com.manzatech.screenmatch.repositories.SerieRepository;
import br.com.manzatech.screenmatch.services.ConsumoAPI;
import br.com.manzatech.screenmatch.services.ConversorDados;
import br.com.manzatech.screenmatch.services.RecordDeserializer;

@Component
public class Principal {

    private Scanner scanner;
    private ConversorDados conversorDados = new ConversorDados();
    private ConsumoAPI consumoAPI;
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGQ3ZjhmY2FhOTBjYmE3N2ZkYmZhZTQ4NGQ2YTIzNCIsIm5iZiI6MTcyMzY2MjEyNy45OTIwOSwic3ViIjoiNjZiY2ZkN2NmYjA4YjhhYjYyODI1ZWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.9hF5_X8dVqoKUquEGxNjurae2uUFtbmLISWj0R9QwyM";
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String POSTER_PATH = "https://image.tmdb.org/t/p/original/";

    @SuppressWarnings("rawtypes")
    private Map<Class, RecordDeserializer> deserializers = new HashMap<>();

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private Config config;

    public Principal() {
    }

    private void init() {
        // Executa somente se não tiver sido inicializado 
        if (null == scanner) {
            scanner = new Scanner(System.in, config.consoleEncoding);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer %s".formatted(BEARER_TOKEN));
            this.consumoAPI = new ConsumoAPI(API_BASE_URL, headers);
            initConversorDados();
        }
    }

    @SuppressWarnings("unchecked")
    private void initConversorDados() {
        this.deserializers.put(DadosEpisodio.class, new RecordDeserializer<>(DadosEpisodio.class));
        this.deserializers.put(DadosSerie.class, new RecordDeserializer<>(DadosSerie.class));
        this.deserializers
                .forEach((clazz, serializer) -> this.conversorDados.addDeserializerForClass(clazz, serializer));
        this.conversorDados.register();
    }

    public void exibeMenu() {
        init();
        String option = "";
        var menu = """
                1 - Buscar séries;
                2 - Buscar episódios;
                3 - Listar séries buscadas;
                4 - Buscar séries por título;
                5 - Buscar séries por ator;
                6 - Top 5 séries;
                7 - Buscar séries por categoria;
                8 - Filtrar séries;
                9 - Buscar Episódio por trecho;
                10 - Top 5 episódios por série;
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
                    buscarEpisodiosPorSerie();
                    break;
                case "3":
                    listarSeriesBuscadas();
                    break;
                case "4":
                    buscarSeriePorTitulo();
                    break;
                case "5":
                    buscarSeriesPorAtor();
                    break;
                case "6":
                    top5Series();
                    break;
                case "7":
                    buscarSeriesPorCategoria();
                    break;
                case "8":
                    filtrarSeriesPorTemporadasEAvalicacao();
                    break;
                case "9":
                    buscarEpisodioPorTrecho();
                    break;
                case "10":
                    topEpisodiosPorSerie();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
        System.out.println("Tchau! Obrigado por utilizar o ScreenMatch");
    }

    private void topEpisodiosPorSerie() {
        Serie s = buscarSeriePorTitulo(false);
        if (null == s) {
            System.out.println("Série não encontrada!");
        } else {
            List<Episodio> top = this.serieRepository.topEpisodiosPorSerie(s);
            top.forEach(e -> {
                System.out.println("Série: %s - Temporada: %d - Episódio: %d - %s - Avaliação - %.2f".formatted(
                    e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getTitulo(), e.getAvaliacao()
                ));
            });
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio: ");
        var nomeEpisodio = scanner.nextLine();
        List<Episodio> episodios = this.serieRepository.episodiosPorTrecho(nomeEpisodio);
        episodios.forEach(e -> {
            System.out.println("Série: %s - Temporada: %d - Episódio: %d - %s".formatted(
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getTitulo()
            ));
        });
    }

    private void filtrarSeriesPorTemporadasEAvalicacao() {
        System.out.println("Filtra séries até quantas temporadas:");
        var temporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Com avaliação a partir de qual valor:");
        var avaliacao = scanner.nextDouble();
        scanner.nextLine();
        List<Serie> series = this.serieRepository.seriesPorTemporadaAvalicao(temporadas, avaliacao);
        System.out.println(" *** Séries Filtradas *** ");
        series.forEach(s -> {
            System.out.println("%s - avaliação: %.2f - temporadas - %d".formatted(
                s.getTitulo(), s.getAvaliacaoMedia(), s.getTotalTemporadas()));
        });
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Lista de Gêneros");
        Arrays.stream(Genero.values()).forEach(g -> System.out.println(g.getNome()));
        System.out.println("Digite o nome do categoria: ");
        String nomeCat = scanner.nextLine();
        Optional<Genero> g = Genero.getByName(nomeCat);
        if (g.isPresent()) {
            List<Serie> list = serieRepository.findByGeneros(g);
            if (!list.isEmpty()) {
                System.out.println("Séries encontradas!");            
                list.forEach(System.out::println);
            } else {
                System.out.println("Nenhuma Série encontrada!");            
            }
        } else {
            System.out.println("Gênero inválido!");
        }
    }

    private void top5Series() {
        List<Serie> list = serieRepository.findTop5ByOrderByAvaliacaoMediaDesc();
        System.out.println("Lista Top 5 Séries");            
        list.forEach(s -> {
            System.out.println("%s - Avaliação: %.2f".formatted(s.getTitulo(), s.getAvaliacaoMedia()));            
        });
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        String nomeAtor = scanner.nextLine();
        List<Serie> list = serieRepository.findByAtores_NomeContainingIgnoreCase(nomeAtor);

        if (!list.isEmpty()) {
            System.out.println("Séries encontradas!");            
            list.forEach(System.out::println);
        } else {
            System.out.println("Nenhuma Série encontrada!");            
        }
    }

    private Serie buscarSeriePorTitulo() {
        return this.buscarSeriePorTitulo(false);
    }

    private Serie buscarSeriePorTitulo(boolean print) {
        System.out.println("Escolha uma série pelo nome: ");
        String nomeSerie = scanner.nextLine();
        Optional<Serie> opt = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);        
        if (print) {
            if (opt.isPresent()) {
                System.out.println("Dados da série: " + opt.get());
            } else {
                System.out.println("Série não encontrada!");            
            }    
        }        
        return opt.orElse(null);
    }

    private void buscarEpisodiosPorSerie() {
        List<Serie> all = findAllSeries();
        System.out.println("Lista de Títulos");
        all.stream().map(s -> s.getTitulo()).forEach(System.out::println);
        System.out.println("Selecione sua série pelo título:");
        String nomeSerie = scanner.nextLine();
        Optional<Serie> opt = all.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).findFirst();
        if (opt.isPresent()) {
            var serie = opt.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
                var json = consumoAPI.obterDados("tv/%s/season/%d".formatted(serie.getCodigoTmdb(), i));
                var dadosTemporada = conversorDados.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> {
                                var epOptional = serie.getEpisodios().stream().filter(ep -> 
                                    e.temporada().equals(ep.getTemporada()) &&
                                    e.numero().equals(ep.getNumero())
                                ).findFirst();
                                if (epOptional.isPresent()) {
                                    var get = epOptional.get();
                                    get.setDadosEpisodio(t.numero(), e);
                                    return get;
                                }
                                return new Episodio(t.numero(), e);
                            }))
                    .collect(Collectors.toList());
            serie.setEpisodios(episodios);
            serieRepository.saveAndFlush(serie);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        findAllSeries()
                .stream()
                .forEach(System.out::println);
    }

    private List<Serie> findAllSeries() {
        return this.serieRepository.findAll();
    }

    private void buscarSeriesWeb() {
        DadosSerie d = getDadosSerie();
        if (null != d) {
            Serie s = new Serie(d);
            Serie ex = new Serie();
            ex.setTitulo(s.getTitulo());
            Optional<Serie> opt = serieRepository.findOne(Example.of(ex));
            if (opt.isPresent()) {
                Serie optionalGet = opt.get();
                optionalGet.update(s);
                serieRepository.save(optionalGet);
            } else {
                serieRepository.save(s);
            }
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

            series.forEach((k, v) -> System.out.println("id - %s -> %s".formatted(k, v.titulo())));
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
