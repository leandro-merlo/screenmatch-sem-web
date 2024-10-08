package br.com.manzatech.screenmatch.controllers;

import br.com.manzatech.screenmatch.dto.SerieDTO;
import br.com.manzatech.screenmatch.services.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping()
    public List<SerieDTO> obterSeries() {
        return serieService.obterSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> top5() {
        return serieService.obterTop5();
    }
}
