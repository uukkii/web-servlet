package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
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
        long checkId = savePost.getId();
        if (checkId == 0) {
            long id = counter.incrementAndGet();
            savePost.setId(id);
            allPosts.put(id, savePost);
        } else if (!allPosts.containsKey(checkId)) {
            allPosts.put(checkId, savePost);
        } else {
            allPosts.replace(checkId, savePost);
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
