package jxx.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 集成Web环境方式
   1、@WebAppConfiguration：测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的；value指定web应用的根,默认src/main/webapp；
   2、@ContextHierarchy：指定容器层次，即spring-context.xml是父容器，而spring-mvc.xml是子容器
   3、通过@Autowired WebApplicationContext wac：注入web环境的ApplicationContext容器；
   4、然后通过MockMvcBuilders.webAppContextSetup(wac).build()创建一个MockMvc进行测试
 * @version 3.0
 * @author jxx
 * @date 2016年11月8日
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = "classpath:spring-context.xml"),
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})
public class IndexControllerThirdTest { @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
	
	@Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
