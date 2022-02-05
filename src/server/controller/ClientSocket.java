package server.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;

import server.Room.Room;

public class ClientSocket {
	private Selector selector;
	private SocketChannel socketChannel;
	private String sendData;
	private List<ClientSocket> allUserList;
	private String[] str;
	private List<Room> roomList;

	private String name;
	private long jumin;
	private String id;
	private String password;
	private int age;
	private Boolean gender ;
	private String tel;

	public ClientSocket(SocketChannel socketChannel, Selector selector, List<ClientSocket> allUserList) throws IOException{
		this.allUserList = allUserList;
		this.selector = selector;
		this.socketChannel = socketChannel; // 클라와 연결된 소켓 채널 받아오기
		socketChannel.configureBlocking(false); // 소켓 채널 넌블록킹
		SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ); // 소켓채널을 셀렉터에 등록
		selectionKey.attach(this);
		// 작업 스레드가 읽기 작업을 할 때 클라 객체가 필요하기에
		// 셀렉션 키에 첨부 객체로 클라 객체 저장.
		roomList = new Vector<Room>();
	}
	
	public void receive(SelectionKey selectionKey) { // 클라이언트로부터 메세지 받기
		
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
			int byteCount = socketChannel.read(byteBuffer);
			if(byteCount == -1) { // 클라가 정상적으로 소켓을 종료하면 -1 리턴
				throw new IOException();
			}
			
			String message = "요청처리 : " + socketChannel.getRemoteAddress() + ":" + Thread.currentThread().getName();
			System.out.println(message);
			
			byteBuffer.flip(); // 읽기 위해 포지션 맨 앞으로 땡기기
			Charset charset = Charset.forName("UTF-8");
			String data = charset.decode(byteBuffer).toString();
			str = data.split(":;:");
			
			System.out.println("받기 완료 : " + data); // 클라가 전송한 메세지 서버에서 출력
			
		}catch(Exception e) {
			try {
				allUserList.remove(this);
				String message = "클라 통신 안됨 : " + socketChannel.getRemoteAddress() + ":" + Thread.currentThread().getName();
				System.out.println(message);
				socketChannel.close();
			}catch(Exception e1) { }
		}
	}
	
	public void send(SelectionKey selectionKey) { // 담당 클라에게 메세지 전송
		try {
			Charset charset = Charset.forName("UTF-8");
			ByteBuffer byteBuffer = charset.encode(sendData);
			socketChannel.write(byteBuffer);
			selectionKey.interestOps(SelectionKey.OP_READ);
			System.out.println("보내기 완료 : \n" + sendData);
		}catch(Exception e) {
			try {
				
				String message = "클라 통신 안됨 : " + socketChannel.getRemoteAddress() + ":" + Thread.currentThread().getName();
				System.out.println(message);
				allUserList.remove(this);
				socketChannel.close();
			}catch(Exception e1) { }
		}
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public String getSendData() {
		return sendData;
	}

	public void setSendData(String sendData) {
		this.sendData = sendData;
	}

	public List<ClientSocket> getAllUserList() {
		return allUserList;
	}

	public void setAllUserList(List<ClientSocket> allUserList) {
		this.allUserList = allUserList;
	}
	
	public String[] getStr() {
		return str;
	}

	public void setStr(String[] str) {
		this.str = str;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getJumin() {
		return jumin;
	}

	public void setJumin(long jumin) {
		this.jumin = jumin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setAge(int age) {
		this.age = age;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}
}
