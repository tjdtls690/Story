package server.Room;

import java.util.List;
import java.util.Vector;

import server.controller.ClientSocket;

public class Room {
	
	private List<ClientSocket> userList;	// 룸 객체 안에 참여한 유저 리스트
	private StringBuffer communication;		// 대화내용 저장
	
	public Room() {
		userList = new Vector<ClientSocket>();
		communication = new StringBuffer();
	}
	
	public List<ClientSocket> getUserList() {
		return userList;
	}
	public void setUserList(List<ClientSocket> userList) {
		this.userList = userList;
	}
	public StringBuffer getCommunication() {
		return communication;
	}
	public void setCommunication(StringBuffer communication) {
		this.communication = communication;
	}
	
	public void setRoomMassage(String msg) {
		communication.append(msg + "\n");
	}

	
}
