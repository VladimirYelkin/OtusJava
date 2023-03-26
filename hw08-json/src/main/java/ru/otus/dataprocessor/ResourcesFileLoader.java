package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.otus.model.Measurement;

import java.io.*;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var jsonReader = new JsonReader(new BufferedReader(new FileReader(fileName)))) {
            Gson gson = new Gson();
            return gson.fromJson(jsonReader,new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e ) {
            throw new FileProcessException(e);
        }

    }

}
