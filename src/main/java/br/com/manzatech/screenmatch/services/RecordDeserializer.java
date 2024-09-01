package br.com.manzatech.screenmatch.services;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.manzatech.screenmatch.models.DadosGenero;

public class RecordDeserializer<T> extends JsonDeserializer<T> {

    private final Map<String, Object> dynamicValues = new HashMap<>();
    private final Class<T> clazz;
    private Constructor<?> constructor;
    private ArrayList<Field> fields = new ArrayList<>();
    private ConversorDados conversorDados = new ConversorDados();

    public RecordDeserializer(Class<T> clazz) {
        this.clazz = clazz;
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException("A Classe de argumento deve ser do tipo Record");
        }
        this.fields.addAll(Arrays.asList(this.clazz.getFields()));
        this.fields.addAll(Arrays.asList(this.clazz.getDeclaredFields()));
        this.constructor = this.clazz.getConstructors()[0];
        this.conversorDados.register();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectNode node = jp.getCodec().readTree(jp);

        Object[] args = new Object[this.constructor.getParameterCount()];
        AtomicInteger count = new AtomicInteger(-1);
        Arrays.stream(this.constructor.getParameters())
                .forEach(parameter -> getConstructorArguments(node, count, parameter, args));

        try {
            return (T) this.constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void addDynamicValue(String key, Object value) {
        this.dynamicValues.put(key, value);
    }

    private void getConstructorArguments(ObjectNode node, AtomicInteger count, Parameter parameter, Object[] args) {
        int idx = count.incrementAndGet();
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add(parameter.getName());
        this.fields.forEach(f -> getAliases(parameter, aliases, f));
        String toSearch = parameter.getName();
        for (String alias : aliases) {
            if (node.has(alias)) {
                toSearch = alias;
                break;
            }
        }
        Object val = node.has(toSearch) ? getNodeAs(node, toSearch, parameter) : null;
        if (null == val) {
            Object temp = null;
            for (String alias : aliases) {
                temp = dynamicValues.get(alias);
                if (null != temp) {
                    val = temp;
                    break;
                }
            }
        }
        args[idx] = val != null ? val : dynamicValues.get(parameter.getName());
    }

    private void getAliases(Parameter parameter, ArrayList<String> aliases, Field f) {
        if (f.getName().equals(parameter.getName()) && f.isAnnotationPresent(JsonAlias.class)) {
            JsonAlias ja = f.getAnnotation(JsonAlias.class);
            aliases.addAll(Arrays.asList(ja.value()));
        }
    }

    private Object getNodeAs(ObjectNode node, String searchString, Parameter parameter) {
        switch (parameter.getType().getSimpleName()) {
            case "String":
                return node.get(searchString).asText();
            case "Integer":
                return node.get(searchString).asInt();
            case "Double":
                return node.get(searchString).asDouble();
            case "List":
                Field f = this.fields.stream().filter(field -> 
                    field.getName().equalsIgnoreCase(parameter.getName())
                ).findFirst().orElseThrow(); 
                ParameterizedType pt = (ParameterizedType) f.getGenericType();
                return this.conversorDados.obterListaDados(node.get(searchString).toString()
                    , (Class<?>)pt.getActualTypeArguments()[0]);
            default:
                return null;
        }
    }

    @SuppressWarnings("hiding")
    private <T> TypeReference<List<T>> getTypeReference(Class<T> clazz) {
        return new TypeReference<List<T>>() {};
    }

}
