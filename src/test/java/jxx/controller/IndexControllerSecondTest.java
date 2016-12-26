package jxx.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 独立测试方式
 * 1、首先自己创建相应的控制器，注入相应的依赖
   2、通过MockMvcBuilders.standaloneSetup模拟一个Mvc测试环境，通过build得到一个MockMvc
   3、MockMvc：是我们以后测试时经常使用的API
	   1、mockMvc.perform执行一个请求；
	   2、MockMvcRequestBuilders.get("/user/1")构造一个请求
	   3、ResultActions.andExpect添加执行完成后的断言
	   4、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
	   5、ResultActions.andReturn表示执行完成后返回相应的结果。
		   	1、准备测试环境
			2、通过MockMvc执行请求
			3.1、添加验证断言
			3.2、添加结果处理器
			3.3、得到MvcResult进行自定义断言/进行下一步的异步请求
			4、卸载测试环境
 * @version 3.0
 * @author jxx
 * @date 2016年11月8日
 */
public class IndexControllerSecondTest {
	private IndexController indexController;
    private MockMvc mockMvc;
	
	@Before
    public void setUp() {
		indexController = new IndexController();
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

	@Test
	public void testGetUserPage() throws Exception{
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getUserPage"))
	                .andExpect(MockMvcResultMatchers.view().name("/userPage"))
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();

		Assert.assertEquals("/userPage", result.getModelAndView().getViewName());
	}
	
	@Test
	public void testGetUser() throws Exception{
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getUser"))
				.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

		JSONObject jsonObject = JSONObject.parseObject(result.getResponse().getContentAsString());

        Assert.assertEquals(jsonObject.get("userName"),"userName");
	}
	
}
