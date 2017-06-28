package modules.post.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.Renderer;
import spark.Request;
import spark.Response;
import spark.Spark;

public class PostController {

	private PostService postService;
	private UserService userService;

	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	public String getPosts(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		User user = userService.getAuthenticatedUser(req);
		if (user != null) {
			model.put("authenticatedUser", user);
		}
		List<Post> posts = postService.getPublicWallPosts();
		model.put("posts", posts);

		return Renderer.render(model, "posts/wall.ftl");
	}

	public String getUserPosts(Request req, Response res) {
		String username = req.params("username");
		User profileUser = userService.getUserbyUsername(username);
		User authenticatedUser = userService.getAuthenticatedUser(req);
		Map<String, Object> model = new HashMap<>();
		if (authenticatedUser != null) {
			model.put("authenticatedUser", authenticatedUser);
		}
		;
		model.put("user", profileUser);
		List<Post> posts = postService.getUserWallPosts(profileUser);
		model.put("posts", posts);

		return Renderer.render(model, "posts/wall.ftl");
	}

	public String createPost(Request req, Response res) {
		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser == null) {
			Spark.halt(401, "Du bist nicht angemeldet!");
			return null;
		}

		Post post = new Post();
		post.setUser(authenticatedUser);
		post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
		try { // populate post attributes by params
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			BeanUtils.populate(post, params);
			String username = req.params("username");
			if (username != null) {
				post.setWall(userService.getUserbyUsername(username));
				res.redirect("/pinnwand/" + username);
			} else {
				post.setWall(authenticatedUser);
				res.redirect("/pinnwand/" + authenticatedUser.getUsername());
			}
		} catch (Exception e) {
			Spark.halt(501);
			return null;
		}

		postService.addPost(post);
		return null;
	}
}
