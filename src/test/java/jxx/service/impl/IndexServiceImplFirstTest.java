package jxx.service.impl;

import jxx.domain.User;
import jxx.mapper.IndexMapper;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * easyMock使用
 * @version 3.0
 * @author jxx
 * @date 2016年11月8日
 */
public class IndexServiceImplFirstTest {

	/**
	 * 
	 * andReturn
	 * @author jxx
	 * @date 2016年11月8日
	 */
	@Test
	public void testGetUser(){
		IndexServiceImpl indexServiceImpl = new IndexServiceImpl();
		User userRecord = new User();
		userRecord.setMobile("mobile");
		User userReturn = new User();
		
		IndexMapper mapper = EasyMock.createMock(IndexMapper.class);
		EasyMock.expect(mapper.getUser()).andReturn(userRecord);
		EasyMock.replay(mapper);
		
		ReflectionTestUtils.setField(indexServiceImpl, "mapper", mapper);//反射
		
		userReturn = indexServiceImpl.getUser();

		Assert.assertEquals("aa", userReturn.getPassword());
		Assert.assertEquals("hello", userReturn.getUserName());
		Assert.assertEquals("mobile", userReturn.getMobile());
		EasyMock.verify(mapper);
	}
	
	/**
	 * 
	 * andStubReturn后，可以使用多次
	 * @author jxx
	 * @date 2016年11月8日
	 */
	@Test
	public void testGetUserMore(){
		IndexServiceImpl indexServiceImpl = new IndexServiceImpl();
		User userRecord = new User();
		userRecord.setMobile("mobile");
		User userReturn = new User();
		
		IndexMapper mapper = EasyMock.createMock(IndexMapper.class);
		EasyMock.expect(mapper.getUser()).andStubReturn(userRecord);
		EasyMock.replay(mapper);
		
		ReflectionTestUtils.setField(indexServiceImpl, "mapper", mapper);
		
		userReturn = indexServiceImpl.getUserMore();
		
		Assert.assertEquals("mobile", userReturn.getMobile());
		EasyMock.verify(mapper);
	}
}
