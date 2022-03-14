package ua.com.okonsergei.service;

import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.repository.WriterRepository;
import ua.com.okonsergei.repository.db.WriterRepositoryImpl;

import java.util.List;

public class WriterService {

    private final WriterRepository writerRepository = new WriterRepositoryImpl();

    public Writer findById(Long id) {
        return writerRepository.findById(id);
    }

    public List<Writer> findAll() {
        return writerRepository.findAll();
    }

    public Writer save(Writer writer) {
        return writerRepository.save(writer);
    }

    public void deleteById(Long id) {
        writerRepository.deleteById(id);
    }

    public void update(Long id, Writer writer ) {
        writerRepository.update(id, writer);
    }
}
