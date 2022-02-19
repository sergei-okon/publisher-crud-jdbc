package ua.com.okonsergei.repository.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDataSource<T> {

    private final static Gson gson = new Gson();
    private final File file;

    public JsonDataSource(File file) {
        this.file = file;
    }

    public void putJsonToFile(List<T> t) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(gson.toJson(t));

        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public List<T> getJsonFromFile(Class<T> clazz) {
        List<T> list = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file)) {
            Type founderListType = TypeToken.getParameterized(ArrayList.class, clazz).getType();

            list = gson.fromJson(fileReader, founderListType);

        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        return list;
    }

    public Long incrementId(String tableName) {
        Map<String, Long> idMap = new HashMap<>();

        long incrementedId = 0L;

        try (FileReader fileReader = new FileReader("src/main/resources/id.json")) {

            Type founderListType = new TypeToken<HashMap<String, Long>>() {
            }.getType();
            idMap = gson.fromJson(fileReader, founderListType);

        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter("src/main/resources/id.json")) {
            incrementedId = idMap.get(tableName) + 1;

            idMap.put(tableName, incrementedId);
            System.out.println(idMap);

            fileWriter.write(gson.toJson(idMap));

        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        return incrementedId;
    }
}
