package ua.com.okonsergei.service;

import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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

    public void update(Post post) {
        postRepository.update(post);
    }
}

