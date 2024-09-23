package br.com.manzatech.screenmatch.services;

import br.com.manzatech.screenmatch.dto.SerieDTO;
import br.com.manzatech.screenmatch.models.Serie;
import br.com.manzatech.screenmatch.repositories.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> obterSeries() {
        return this.converterListSeries(this.serieRepository.findAll());
    }

    public List<SerieDTO> obterTop5() {
        return this.converterListSeries(this.serieRepository.findTop5ByOrderByAvaliacaoMediaDesc());
    }

    private List<SerieDTO> converterListSeries(List<Serie> lista) {
        return lista.stream()
        .map(s -> this.serieConverter(s))
        .collect(Collectors.toList());
    }

    private SerieDTO serieConverter(Serie s) {
        return new SerieDTO(
                s.getId(),
                s.getCodigoTmdb(),
                s.getTitulo(),
                s.getTotalTemporadas(),
                s.getAvaliacaoMedia(),
                s.getTotalVotos(),
                s.getSinopse(),
                s.getPoster(),
                s.getGeneros(),
                s.getAtores()
        );
    }


}
