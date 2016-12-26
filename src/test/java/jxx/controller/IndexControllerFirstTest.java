package jxx.controller;

import jxx.domain.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 
 * springmvc测试有3种方式
 * http://sishuok.com/forum/blogPost/list/7981.html
 * 以前的测试方式
 * @version 3.0
 * @author jxx
 * @date 2016年11月8日
 */
public class IndexControllerFirstTest {

	private IndexController indexController;

	@Before
	public void setUp() {
		indexController = new IndexController();
	}

	@Test
	public void testGetUserPage() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		String view = (String) indexController.getUserPage(req);
		Assert.assertEquals("/userPage", view);
	}

	@Test
	public void testGetUser() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		User user = new User();
		user = (User) indexController.getUser(user, req);

		Assert.assertNotNull(user);
		Assert.assertEquals("userName", user.getUserName());
		Assert.assertEquals("password", user.getPassword());
	}

}
