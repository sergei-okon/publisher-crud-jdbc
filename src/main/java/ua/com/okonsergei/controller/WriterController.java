package ua.com.okonsergei.controller;

import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.service.WriterService;

import java.util.List;

public class WriterController {

    WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    public List<Writer> findAll() {
        return writerService.findAll();
    }

    public Writer findById(Long id) {
        return writerService.findById(id);
    }

    public Writer save(Writer writer) {
        return writerService.save(writer);
    }

    public void deleteById(Long id) {
        writerService.deleteById(id);
    }

    public void update(Long id, Writer writer) {
        writerService.update(id, writer);
    }
}

