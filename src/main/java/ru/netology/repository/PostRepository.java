package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final ConcurrentMap<Long, Post> allPosts;
    private final AtomicLong counter = new AtomicLong();

    public PostRepository() {
        this.allPosts = new ConcurrentHashMap<>() {
        };
    }

    public Collection<Post> all() {
        return allPosts.values();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(allPosts.get(id));
    }

    public Post save(Post savePost) {
        if (savePost.getId() == 0 || !allPosts.containsKey(savePost.getId())) {
            long id = counter.incrementAndGet();
            savePost.setId(id);
            allPosts.put(id, savePost);
        } else if (savePost.getId() != 0) {
            allPosts.replace(savePost.getId(), savePost);
        }
        return savePost;
    }

    public void removeById(long id) {
        allPosts.remove(id);
    }

    public ConcurrentMap<Long, Post> getAllPosts() {
        return allPosts;
    }
}
