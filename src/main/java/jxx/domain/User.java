package jxx.domain;

public class User {

	private String uuid;
	
	private String userName;
	
	private String password;
	
	private String mobile;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [uuid=" + uuid + ", userName=" + userName + ", password=" + password + ", mobile=" + mobile + "]";
	}

	
}
