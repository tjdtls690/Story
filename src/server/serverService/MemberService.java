package server.serverService;

public interface MemberService {
	public abstract String signUpCheckMember(String[] str);
	public abstract String login(String id, String pw);
	public abstract Boolean isId(String id);
	public abstract String searchId(String id);
	public abstract void addFriend(String[] str);
	public abstract String setFriend(String id);
	public abstract String delFriend(String[] str);
	public abstract String serverLogin(String id, String pw);
	public abstract String serverAllUserList();
	public abstract String updateTel(String tel, String id);
}
