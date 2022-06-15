package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  public static final String API_POSTS = "/api/posts";
  public static final String API_POSTS_D_PLUS = "/api/posts/\\d+";
  public static final String SLASH = "/";
  public static final String GET_METHOD = "GET";
  public static final String POST_METHOD = "POST";
  public static final String DELETE_METHOD = "DELETE";
  private PostController controller;
  private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);

  @Override
  public void init() {
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET_METHOD) && path.equals(API_POSTS)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET_METHOD) && path.matches(API_POSTS_D_PLUS)) {
        // easy way
        final var id = parseId(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST_METHOD) && path.equals(API_POSTS)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE_METHOD) && path.matches(API_POSTS_D_PLUS)) {
        // easy way
        final var id = parseId(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parseId(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf(SLASH) + 1));
  }
}

