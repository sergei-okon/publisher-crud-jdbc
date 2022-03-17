package ua.com.okonsergei.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.repository.LabelRepository;
import ua.com.okonsergei.repository.db.LabelRepositoryImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class LabelServiceTest {

    LabelRepository labelRepositoryMock;
    LabelService labelService;

    @BeforeEach
    void setUp() {
        labelRepositoryMock = mock(LabelRepositoryImpl.class);
        labelService = new LabelService(labelRepositoryMock);
    }

    @Test
    void findAll_Success() {
        labelService.findAll();
        verify(labelRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 5L;
        labelService.findById(id);
        verify(labelRepositoryMock).findById(id);
    }

    @Test
    void save_Success() {
        Label label = new Label();

        labelService.save(label);
        verify(labelRepositoryMock).save(label);
    }

    @Test
    void deleteById_Success() {
        Long id = 5L;

        labelService.deleteById(id);
        verify(labelRepositoryMock).deleteById(id);
    }

    @Test
    void update_Success() {
        Long id = 5L;
        Label label = new Label();

        labelService.update(id, label);
        verify(labelRepositoryMock).update(id, label);
    }
}