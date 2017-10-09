package modules.post.controller;

import java.sql.Timestamp;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.controller.UserApiController;
import modules.user.model.User;
import modules.user.service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class PostApiController {
	private PostService postService;
	private UserService userService;

	public PostApiController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	public Object getPosts(Request req, Response res) {
		return postService.getPublicWallPosts();
	}

	public Object getUserPosts(Request req, Response res) {
		String username = req.params("username");
		User profileUser = userService.getUserbyUsername(username);
		return postService.getUserWallPosts(profileUser);
	}

	public Object createPost(Request req, Response res) {
		UserApiController contrl = new UserApiController(userService);
		User authenticatedUser = contrl.login(req, res);
		
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
			} else {
				post.setWall(authenticatedUser);
			}
		} catch (Exception e) {
			Spark.halt(501);
			return null;
		}

		postService.addPost(post);
		return post;
	}
}
