package application.objectVO;

import java.io.Serializable;

public class MemberVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private long jumin;
	private String id;
	private String password;
	private int age;
	private Boolean gender ;
	private String tel;
	private String message;
	
	public MemberVO() { }
	
	public MemberVO(String name, long jumin, String id, String password, int age, Boolean gender, String tel) {
		this.name = name;
		this.jumin = jumin;
		this.id = id;
		this.password = password;
		this.age = age;
		this.gender = gender;
		this.tel = tel;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}
	
	public long getJumin() {
		return jumin;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getAge() {
		return age;
	}
	
	public Boolean isGender() {
		return gender;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}

}
