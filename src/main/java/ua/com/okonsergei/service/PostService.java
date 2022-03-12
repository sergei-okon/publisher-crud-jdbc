package ua.com.okonsergei.service;

import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.repository.PostRepository;
import ua.com.okonsergei.repository.db.PostRepositoryImpl;

import java.util.List;

public class PostService {

    private final PostRepository postRepository = new PostRepositoryImpl();

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public void update(Long id, Post post) {
        postRepository.update(id, post);
    }
}

