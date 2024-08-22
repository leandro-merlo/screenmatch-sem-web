package br.com.manzatech.screenmatch.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import br.com.manzatech.screenmatch.exceptions.ModuleNotRegisteredException;


public class ConversorDados implements IConversorDados {
    
    private ObjectMapper mapper = new ObjectMapper();
    private SimpleModule module = new SimpleModule();
    private boolean wasRegistered = false;
    public ConversorDados() {
    }

    @Override
    public <T> T obterDados(String json, Class<T> clazz) {
        this.checkRegister(); 
        try {
                        
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkRegister() {
        if (!wasRegistered) {
            throw new ModuleNotRegisteredException("Você deve usar o metodo register antes de usar esta chamada");
        }
    }

    public <T> List<T> obterListaDados(String jsonArray, Class<T> clazz) {
        this.checkRegister();
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
        this.checkRegister();
        try {
            JsonNode root = mapper.readTree(fullJson);
            return root.get(path);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String obterJsonFromPathAsString(String fullJson, String path) {
        this.checkRegister();
        JsonNode node = this.obterJsonFromPath(fullJson, path);
        return null != node ? node.toString() : "";
    }

    public void addDeserializerForClass(Class clazz, JsonDeserializer deserializer) {
        this.module.addDeserializer(clazz, deserializer);
    }

    public void register() {
        this.mapper.registerModule(module);
        this.wasRegistered = true;
    }
}
