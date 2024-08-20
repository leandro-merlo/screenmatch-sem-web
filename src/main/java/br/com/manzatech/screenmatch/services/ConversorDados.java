package br.com.manzatech.screenmatch.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConversorDados implements IConversorDados {
    
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> obterListaDados(String jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            var node = mapper.readTree(jsonArray);
            if (!node.isArray()) {
                list.add(this.obterDados(node.toString(), clazz));
            } else {
                node.forEach(el -> {
                    list.add(this.obterDados(el.toString(), clazz));
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }

    public JsonNode obterJsonFromPath(String fullJson, String path) {
        try {
            JsonNode root = mapper.readTree(fullJson);
            return root.get(path);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String obterJsonFromPathAsString(String fullJson, String path) {
        JsonNode node = this.obterJsonFromPath(fullJson, path);
        return null != node ? node.toString() : "";
    }
}
