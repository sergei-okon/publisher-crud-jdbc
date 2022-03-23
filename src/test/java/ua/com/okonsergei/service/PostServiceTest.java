package ua.com.okonsergei.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.repository.PostRepository;
import ua.com.okonsergei.repository.jdbc.JdbcPostRepositoryImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostServiceTest {

    PostRepository postRepositoryMock;
    PostService postService;

    @BeforeEach
    void setUp() {
        postRepositoryMock = mock(JdbcPostRepositoryImpl.class);
        postService = new PostService(postRepositoryMock);
    }

    @Test
    void findAll_Success() {
        postService.findAll();
        verify(postRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 5L;
        postService.findById(id);
        verify(postRepositoryMock).findById(id);
    }

    @Test
    void save_Success() {
        Post post = new Post();

        postService.save(post);
        verify(postRepositoryMock).save(post);
    }

    @Test
    void deleteById_Success() {
        Long id = 5L;

        postService.deleteById(id);
        verify(postRepositoryMock).deleteById(id);
    }

    @Test
    void update_Success() {
        Long id = 5L;
        Post post = new Post();

        postService.update(post);
        verify(postRepositoryMock).update(post);
    }
}
