package ua.com.okonsergei.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.repository.WriterRepository;
import ua.com.okonsergei.repository.jdbc.JdbcWriterRepositoryImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WriterServiceTest {

    WriterRepository writerRepositoryMock;
    WriterService writerService;

    @BeforeEach
    void setUp() {
        writerRepositoryMock = mock(JdbcWriterRepositoryImpl.class);
        writerService = new WriterService(writerRepositoryMock);
    }

    @Test
    void findAll_Success() {
        writerService.findAll();
        verify(writerRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 5L;
        writerService.findById(id);
        verify(writerRepositoryMock).findById(id);
    }

    @Test
    void save_Success() {
        Writer writer = new Writer();

        writerService.save(writer);
        verify(writerRepositoryMock).save(writer);
    }

    @Test
    void deleteById_Success() {
        Long id = 5L;

        writerService.deleteById(id);
        verify(writerRepositoryMock).deleteById(id);
    }

    @Test
    void update_Success() {
        Writer writer = new Writer();

        writerService.update(writer);
        verify(writerRepositoryMock).update(writer);
    }
}
