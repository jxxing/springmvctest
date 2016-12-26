package jxx.service;

import jxx.domain.User;

public interface IndexService {

	public User getUser();
	
	public User getUserMore();

	public void writer(String str);
}
