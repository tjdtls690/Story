package server.serverService;

import protocol.Protocol;
import server.serverDAO.ServerDAO;

public class MemberServiceCon implements MemberService{
	
	@Override
	public String login(String id, String pw) {	// 로그인 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		if(sd.login(id, pw)==1) return sd.getData(id);
		else return Protocol.LOGIN_FAIL;
	}
	
	@Override
	public Boolean isId(String id) {			// 아이디 중복 확인
		ServerDAO sd = new ServerDAO();
		if(sd.getid(id) == 0) return true;
		else if(sd.getid(id) > 0) return false;
		return false;
	}

	@Override
	public String signUpCheckMember(String[] str) {	// 회원가입 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		if(sd.insert(str) == true) return Protocol.SIGNUP_SUCCESS;
		else return Protocol.SIGNUP_FAIL;
	}
	
	@Override
	public String searchId(String id) {		// 아이디 중복 확인 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		return sd.searchId(id);
	}
	
	@Override
	public void addFriend(String[] str) {	// 친구 추가 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		sd.insertFriend(str);
	}
	
	@Override
	public String setFriend(String id) {	// 친구 목록 불러오기
		ServerDAO sd = new ServerDAO();
		return sd.getFriend(id);
	}
	
	@Override
	public String delFriend(String[] str) {	// 친구 삭제
		ServerDAO sd = new ServerDAO();
		sd.delFriend(str);
		return Protocol.DEL_FRIENDLIST_SUCCESS;
	}
	
	@Override
	public String serverLogin(String id, String pw) {	// 서버 로그인 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		if(sd.serverLogin(id, pw)==1) return Protocol.SERVER_LOGIN_SUCCESS;
		else return Protocol.SERVER_LOGIN_FAIL;
	}
	
	@Override
	public String serverAllUserList() {	// 서버 모든 유저 리스트 불러오기
		ServerDAO sd = new ServerDAO();
		return sd.getAll();
	}
	
	@Override
	public String updateTel(String tel, String id) {	// 전화번호 수정 성공, 실패 여부
		ServerDAO sd = new ServerDAO();
		if(sd.updateTel(tel, id)) return Protocol.UPDATE_TEL_SUCCESS;
		else return Protocol.UPDATE_TEL_FAIL;
	}

}
