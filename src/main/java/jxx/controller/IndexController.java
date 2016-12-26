package jxx.controller;

import javax.servlet.http.HttpServletRequest;

import jxx.domain.User;
import jxx.service.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@Autowired
	private IndexService indexService;
	
	@RequestMapping(value="/loginPage1")
	public String loginPage(Model model){
		return  "/loginPage";
	}
	
	@RequestMapping(value="/index1")
	public String index(Model model){
		return  "/index";
	}
	
	@RequestMapping(value="/getUser")
	@ResponseBody
	public Object getUser(User user,HttpServletRequest request){
//		user = indexService.getUser();
		user.setUserName("userName");
		user.setPassword("password");
		user.setMobile("mobile");
		return  user;
	}
	
	
	@RequestMapping(value="/getUserPage")
	public Object getUserPage(HttpServletRequest request){
		return "/userPage";
	}
}
