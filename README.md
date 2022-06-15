# Домашнее задание к занятию «2.1. Servlet Containers»

## CRUD

**v. 2.2 Решение исправлено.**

Изменен метод save() класса PostRepository:

```
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
```

**v. 2.1 Решение исправлено.**

Изменен метод save() класса PostRepository:

```
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
```

- Добавлена проверка на наличие id сохраняемого post в репозитории allPost;
- Исправлен код для обновления post (в случае, если id сохраняемого post != 0):
вместо сохранения id в переменную и изменения методом put, теперь применяется метод replace, принимающий id сохраняемого поста и сам пост.

**v. 2.0 Задание полность переделано.**

Описание решения:

### 1. ru.netology.controller.PostController
- В классе **PostController** в конструктор добавлен объект класса Gson.
- Из методов **_all()_** и **_save()_** удалены строки:
```
final var gson = new Gson();
```
- Реализован метод **_getById(long id, HttpServletResponse response)_**:
```
  public void getById(long id, HttpServletResponse response) throws IOException, NotFoundException {
    response.setContentType(APPLICATION_JSON);
    final var post = service.getById(id);
    response.getWriter().print(gson.toJson(post));
  }
```
- Реализован метод **_removeById(long id, HttpServletResponse response)_**:
```
  public void removeById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.getById(id);
    service.removeById(id);
    response.getWriter().print(gson.toJson(data));
  }
```

### 2. ru.netology.repository.PostRepository
- Добавлен конструктор класса:
```
  public PostRepository(ConcurrentMap<Long, Post> allPosts) {
    this.allPosts = new ConcurrentHashMap<>() {
    };
  }
```
Для конструктора добавлен геттер: 
```
  public ConcurrentMap<Long, Post> getAllPosts() {
    return allPosts;
  }
```
Также добавлен счетчик:
```private final AtomicLong counter = new AtomicLong();```

- Изменен метод **_all()_**:
```
    public Collection<Post> all() {
      return allPosts.values();
    }
```

- Изменен метод **_getById()_**:
```
    public Optional<Post> getById(long id) {
      return Optional.ofNullable(allPosts.get(id));
    }
```

- Изменен метод **_save()_**:
```
public Post save(Post savePost) {
    if (savePost.getId() == 0) {
      long id = idCounter.incrementAndGet();
      savePost.setId(id);
      allPosts.put(id,savePost);
    } else if (savePost.getId() != 0) {
      Long currentId = savePost.getId();
      allPosts.put(currentId, savePost);
    }
    return savePost;
  }
```

- Изменен метод **_removeById()_**:
```
    public void removeById(long id) {
        allPosts.remove(id);
    }
```

### 3. ru.netology.service.PostService
- Изменен метод **_all()_**:
```
 public Collection<Post> all() {
    return repository.all();
  }
```

### 4. ru.netology.service.MainServlet
- Добавлены константы: ```public static final String API_POSTS = "/api/posts"```,
  ```public static final String API_POSTS_D_PLUS = "/api/posts/\\d+"```,
  ```public static final String SLASH = "/"```,
  ```public static final String GET_METHOD = "GET"```,
  ```public static final String POST_METHOD = "POST"```,
  ```public static final String DELETE_METHOD = "DELETE"```.

- Исправлен метод **_init()_**.

- Убраны магические стрки и магический метод.

- Добавлен новый блок **_catch_** на **_NotFoundException_**. 