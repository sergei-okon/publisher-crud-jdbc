package ua.com.okonsergei.controller;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.service.LabelService;

import java.util.List;

public class LabelController {

    LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    public List<Label> findAll() {
        return labelService.findAll();
    }

    public Label findById(Long id) {
        return labelService.findById(id);
    }

    public Label save(Label label) {
        return labelService.save(label);
    }

    public void deleteById(Long id) {
        labelService.deleteById(id);
    }

    public void update(Long id, Label label) {
        labelService.update(label);
    }
}
