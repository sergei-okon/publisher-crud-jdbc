package ua.com.okonsergei.repository.json;

import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.repository.WriterRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonWriterRepositoryImpl implements WriterRepository {

    private final JsonDataSource<Writer> jsonDataSource;

    public JsonWriterRepositoryImpl() {
        jsonDataSource = new JsonDataSource<>(new File("src/main/resources/writers.json"));
    }

    @Override
    public List<Writer> findAll() {
        List<Writer> writers = jsonDataSource.getJsonFromFile(Writer.class);

        if (writers == null) {
            System.out.println("DB is empty");
            return new ArrayList<>();
        }
        return writers;
    }

    @Override
    public Writer findById(Long id) {
        List<Writer> writers = findAll();
        return writers.stream()
                .filter(writerTemp -> Objects.equals(writerTemp.getId(), id))
                .findAny().orElse(null);
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> writers = findAll();

        Long writerId = jsonDataSource.incrementId("writerId");
        writer.setId(writerId);
        writers.add(writer);

        jsonDataSource.putJsonToFile(writers);
        System.out.println("Added writer with id " + writer.getId());

        return writer;
    }

    @Override
    public void deleteById(Long id) {
        List<Writer> writers = findAll();

        if (findById(id) != null) {
            writers.removeIf(writer -> Objects.equals(writer.getId(), id));
            jsonDataSource.putJsonToFile(writers);
            System.out.println("Deleted writer by id " + id);

        } else {
            System.out.println("Unable to delete writer from database. " +
                    "Writer with id " + id + " not found");
        }
    }

    @Override
    public void update(Long id, Writer writer) {
        if (id == null) {
            System.out.println("Id is null. Unable to update writer");
        }

        List<Writer> writers = findAll();

        if (findById(id) != null) {
            for (Writer writerTemp : writers) {
                if (Objects.equals(writerTemp.getId(), id)) {
                    writerTemp.setFirstName(writer.getFirstName());
                    writerTemp.setLastName(writer.getLastName());
                    writerTemp.setPosts(writer.getPosts());
                }
                jsonDataSource.putJsonToFile(writers);
            }
        } else {
            System.out.println("Writer with id " + id + " not found");
        }
    }
}
