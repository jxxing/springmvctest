package jxx.service.impl;

import jxx.domain.User;
import jxx.mapper.IndexMapper;
import jxx.util.StringUtils;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * PowerMock使用
 * @version 3.0
 * @author jxx
 * @date 2016年11月8日
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class})
public class IndexServiceImplSecondTest {

	@Test
	public void testGetUser(){
		IndexServiceImpl indexServiceImpl = new IndexServiceImpl();
		User userRecord = new User();
		User userReturn = new User();
		
		IndexMapper mapper = EasyMock.createMock(IndexMapper.class);
		EasyMock.expect(mapper.getUser()).andReturn(userRecord);
		EasyMock.replay(mapper);
		
		
		PowerMock.mockStatic(StringUtils.class);//Mock静态方法         
		EasyMock.expect(StringUtils.out()).andReturn("bbbb");
		EasyMock.expect(StringUtils.isNull(EasyMock.anyString())).andReturn("aaaa");
		PowerMock.replay(StringUtils.class);       
		
		ReflectionTestUtils.setField(indexServiceImpl, "mapper", mapper);
		
		userReturn = indexServiceImpl.getUser();
		
		Assert.assertEquals("aaaa", userReturn.getPassword());
		Assert.assertEquals("bbbb", userReturn.getUserName());
		EasyMock.verify(mapper);
	}
}
