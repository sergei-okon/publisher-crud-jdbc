package ua.com.okonsergei.controller;

import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.service.PostService;

import java.util.List;

public class PostController {

    PostService postService = new PostService();

    public List<Post> findAll() {
        return postService.findAll();
    }

    public Post findById(Long id) {
        return postService.findById(id);
    }

    public Post save(Post post) {
        return postService.save(post);
    }

    public void deleteById(Long id) {
        postService.deleteById(id);
    }

    public void update(Long id, Post post) {
        postService.update(id, post);
    }
}


