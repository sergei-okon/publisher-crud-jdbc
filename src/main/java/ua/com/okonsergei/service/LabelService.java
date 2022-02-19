package ua.com.okonsergei.service;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.repository.LabelRepository;
import ua.com.okonsergei.repository.json.JsonLabelRepositoryImpl;

import java.util.List;

public class LabelService {

    private final LabelRepository labelRepository = new JsonLabelRepositoryImpl();

    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    public Label findById(Long id) {
        return labelRepository.findById(id);
    }

    public Label save(Label label) {
        return labelRepository.save(label);
    }

    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }

    public void update(Long id, Label label) {
        labelRepository.update(id, label);
    }
}