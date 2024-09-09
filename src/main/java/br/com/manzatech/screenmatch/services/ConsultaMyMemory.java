package br.com.manzatech.screenmatch.services;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import br.com.manzatech.screenmatch.models.DadosTraducao;

public class ConsultaMyMemory {
    
    private static final String API_URL = "https://api.mymemory.translated.net/";
    private static ConsumoAPI api = new ConsumoAPI(API_URL);
    private static ConversorDados conversorDados = new ConversorDados();

    static {
        if (!conversorDados.wasRegistered()) {
            conversorDados.register();
        }
    }

    public static String obterTraducao(String texto) {

        String langPair = URLEncoder.encode("en|pt-br", Charset.forName("utf-8"));
        String toTranslate = URLEncoder.encode(texto, Charset.forName("utf-8"));
        String json = api.obterDados("get?q=%s&langpair=%s".formatted(toTranslate, langPair));
        DadosTraducao dadosTraducao = conversorDados.obterDados(json, DadosTraducao.class);
        if (dadosTraducao.dadosResposta().textoTraduzido().startsWith("MYMEMORY WARNING")) {
            return "%s - Texto Não traduzido".formatted(texto);            
        }
        return dadosTraducao.dadosResposta().textoTraduzido();
    }
}
