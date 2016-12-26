package jxx.service.impl;

import jxx.domain.User;
import jxx.mapper.IndexMapper;
import jxx.service.IndexService;
import jxx.util.StringUtils;

import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService{

	private IndexMapper mapper;

	public User getUser() {
		User user = mapper.getUser();
		
		user.setUserName(StringUtils.out());
		
		user.setPassword(StringUtils.isNull("aa"));
		
		return user;
	}

	public User getUserMore() {
		User user = mapper.getUser();
		mapper.getUser();
		mapper.getUser();
		mapper.getUser();
		mapper.getUser();
		return user;
	}
	
	@Override
	public void writer(String str) {
		System.out.println("hello jdk writer");
	}
}
