# :pushpin: Story
>NIO non-blocking 통신 기반 채팅 프로그램

</br>

## 1. 제작 기간 & 참여 인원 (개발 1개월차)
- 2021.12.1 ~ 2021.12.9 (9일)
- 개인 프로젝트 (1명)

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 8
  - Oracle Database Express Edition (XE) 18c
  - SQL Developer
#### `Front-end`
  - Java FX 
  - SceneBuilder 
  - CSS

</br>

## 3. 핵심 ERD

<img src="https://tjdtls690.github.io/assets/img/github_img/important_erd_mini01.PNG"  width="800"/>

## 4. 핵심 기능
이 서비스의 핵심 기능은 **회원들간의 자유로운 방 만들기와 의사소통, 그리고 관리자의 로그 기록 시스템**입니다. 
<br/>

**핵심 기능 설명**을 펼쳐서 기능의 흐름을 보면, 서비스가 어떻게 동작하는지 알 수 있습니다.

<br/>

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">

### 4.1. 전체 흐름
<img src="https://tjdtls690.github.io/assets/img/github_img/important_mini_flow01.PNG"  width="800"/>

### 4.2. 사용자 요청 (Client < - > Server Controller)
  
- #### 클라이언트에서 서버에 접속 요청 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/ClientController/ClientController.java#L58)
  - ##### (1) 소켓채널 open 후, 5001 번 포트 접속 요청합니다.
  <br/>
- #### 서버에서 사용자의 요청 받기 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ClientSocket.java#L42)
  - ##### (1) 서버소켓채널과 셀렉터 객체 생성합니다. (60행)
  - ##### (2) non-blocking 설정 후, 클라 접속 대기상태로 전환합니다. (70 ~ 73행)
  - ##### (3) 멀티스레드가 기다리고 있다가 셀렉트의 관심 키셋에서 작업처리 준비된 키를 가지고 와서 요청을 처리합니다. (81 ~ 142행)
  <br/>
- #### ClientSocket 클래스 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ClientSocket.java#L14)
  - ##### 서버에서 각각의 클라이언트의 요청 받기와 응답, 소켓채널을 셀렉터에 등록하는 역할을 담당합니다.
  <br/>
- #### 프로토콜을 통해 처리할 작업(메서드)을 설정
  - ##### (1) 서버 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L104)
    - switch문을 통해 프로토콜을 분류하고 실행할 메서드를 지정합니다.
  - ##### (2) 클라이언트 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/ClientController/ClientController.java#L91)
    - 서버와 동일
  <br/>
- #### 데이터 전송 메서드
  - ##### (1) 서버 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ClientSocket.java#L71)
    - Controller의 모든 메서드에서 클라에게 응답할 시, SelectionKey 에서 클라의 요청을 받아온 해당 ClientSocket 클래스를 꺼냅니다.
    - 해당 ClientSocket 의 보낼 메시지를 담을 변수에 프로토콜을 포함한 메시지를 담은 후 전송합니다.
  - ##### (2) 클라이언트 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/ClientController/ClientController.java#L156)
    - send 메서드를 통해 바로 서버에 메시지를 전송합니다.
  <br/>
- #### 1:1 채팅방 생성 (상대가 대화신청 수락 시) :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L433)
  - ##### (1) 룸 객체를 생성합니다
  - ##### (2) 접속한 모든 유저 리스트(allUserList)에서 해당 두 유저의 응답을 맡고있는 ClientSocket 두개를 꺼냅니다.
  - ##### (3) ClientSocket 객체를 해당 룸 객체의 유저리스트에 집어넣은 후, 두 유저의 아이디와 룸 생성 성공 프로토콜을 두 유저에게 모두 응답해줍니다.
  <br/>
- #### 그룹 채팅방 생성 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L381)
  - ##### (1) 초대받은 유저들 중, 한 명이라도 접속 안한 유저가 있다면 그룹 방 생성 실패 (Protocol.GROUP_NOTHING)
  - ##### (2) 전부 접속 중이라면 그룹방 생성 성공 (Protocol.GROUPROOM_SUCCESS)
    - (1) 룸 객체 생성
    - (2) 접속한 모든 유저 리스트(allUserList)에서 초대받은 유저들의 ClientSocket 클래스를 꺼내서 생성한 룸 객체의 유저리스트에 넣습니다.
    - (3) 초대받은 유저들의 아이디 목록과 룸 생성 성공 프로토콜을 메시지로 뿌립니다.
  <br/>
- #### 방 나가기 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L565)
  - ##### 방 인원이 두명일 때 (if문)
    - (1) 방에 남은 마지막 한 명 입장에서, 해당 룸 객체의 인덱스를 구하고 삭제 프로토콜과 인덱스를 메시지로 응답해줍니다.
    - (2) 유저를 담당하는 ClientSocket 의 룸 리스트에서 해당 룸 객체를 삭제합니다.
    - (3) 해당 룸 객체를 모든 룸 리스트에서 삭제하고 룸 안의 유저리스트도 전부 삭제합니다.
  - ##### 방 인원이 세명 이상일 때 (else문)
    - (1) 해당 룸에 있는 모든 유저에게 각각의 입장에서의 해당 룸 객체의 인덱스를 구합니다.
    - (2) 나간 유저 아이디, 각 유저 입장에서의 룸 객체 인덱스, 유저 탈주 프로토콜을 룸 안의 유저들에게 데이터로 응답해줍니다.
  <br/>
- #### 사용자가 채팅방에서 메시지 전송 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L459)
  - ##### 1:1 방, 그룹 방 메시지 전송 전부 커버
  - ##### (1) 클라에서 현재 메시지를 전송한 유저가, 자신이 접속해있는 룸 리스트 중 현재 채팅중인 룸 객체의 인덱스를 데이터에 포함시켜 전송합니다.
  - ##### (2) 서버에서 그 인덱스를 통해 해당 룸 객체를 꺼냅니다.
  - ##### (3) 그 룸 객체 안의 접속 유저 리스트에 들어있는 유저들의 입장에서, 해당 룸 객체가, 현재 접속한 룸 객체들이 들어있는 각각의 리스트에서의 인덱스를 구합니다.
  - ##### (4) 각 유저들에게 맞는 해당 룸 객체의 인덱스를 프로토콜, 채팅 내용과 함께 클라이언트에 응답합니다.
  <br/>
- #### 로그인 유효 검사 :pushpin: [코드 확인](https://github.com/tjdtls690/Story/blob/main/src/server/controller/ServerController.java#L207)
  - ##### (1) DB 데이터와 검사 후 아이디 또는 비번이 다르면 로그인 실패 (Protocol.LOGIN_DIF)
  - ##### (2) 이미 접속한 아이디일때도 로그인 실패 (Protocol.LOGIN_FAIL)
  - ##### (3) 위 두 조건을 전부 피했다면 로그인 성공 (Protocol.LOGIN_SUCCESS)
  <br/>

</div>
</details>

<br/>
  
## 5. 핵심 트러블 슈팅
  
### 5.1. 각 채팅방들이 서로 영향을 주지 않게끔 구조를 짜는 문제

- ### (1) 문제

  - ##### 여러 유저들이 각자 다른 방에서 채팅을 할 때, 다른방에 영향을 주지 않도록 구조를 짜는 것부터 쉽지가 않았다.

- ### (2) 해결

  - ##### (1) 룸 객체와 유저가 서로 상호 참조를 하도록 구현했다.
  - ##### (2) 해당 유저가 메시지를 보낼 때, 룸 객체의 인덱스를 같이 보내서 해당 룸 객체를 꺼내고
  - ##### (3) 그 룸 객체를 이용해서, 같은 방에 있는 유저들 각자의 입장에서, 각자의 룸 리스트 안에서 룸 객체를 꺼낼 수 있도록 한다.
  - ##### 개선된 코드 참조

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

  ```java
  // 1번
  // 룸 객체가 방에 들어온 유저들(userList)을 참조한다.
  public class Room {

    private List<ClientSocket> userList;	// 룸 객체 안에 참여한 유저 리스트
    private StringBuffer communication;		// 대화내용 저장

      // getter, setter 코드 생략
  }



  // 2번
  // 각 유저의 소통을 담당하는 ClientSocket에서 현재 들어가있는 방(roomList)을 참조합니다.
  public class ClientSocket {
    private Selector selector;
    private SocketChannel socketChannel;
    private String sendData;
    private List<ClientSocket> allUserList;
    private String[] str;
  
    private List<Room> roomList; // 방 목록 참조

    public ClientSocket(SocketChannel socketChannel, Selector selector, List<ClientSocket> allUserList) throws IOException{
      this.allUserList = allUserList;
      this.selector = selector;
      this.socketChannel = socketChannel; 
      socketChannel.configureBlocking(false);
      SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
      selectionKey.attach(this);
      roomList = new Vector<Room>();
    }

      // 이 외의 코드 생략
  }
  ```

</div>
</details>

</br> 
  
### 5.2. 프로그램을 실행만 하고 로그인은 안한 유저들이 있을 때, NullPointerException 에러 뜨는 현상

- ### (1) 문제
  - ##### 어떤 기능이 실행될 때, 유저들(ClientSocket)에 저장되어있는 id 들을 뒤져보는 경우가 많다.
  - ##### 그 때, 프로그램은 켜놓고 로그인창에서 로그인은 안한 상태의 클라이언트가 존재하면 ClientSocket는 생성되지만 그 안의 id값은 null값이다.
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">
  
  ```java
  public static void chatRefuse(String[] str) {  // 대화 신청 거절
  	  for(int i = 0; i < allUserList.size(); i++) {
					      
		  // if문에서 getId() 값이 null 값인 클라이언트 객체가 하나라도 존재한다면, NullPointerException 에러가 뜸
		  if(allUserList.get(i).getId().equals(str[1])) {
			  allUserList.get(i).setSendData(Protocol.CHAT_REFUSE + ":;:" + str[1] + ":;:" + str[2]);
			  SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
			  key.interestOps(SelectionKey.OP_WRITE);
			  break;
		  }
	  }
  }
  ```
  
</div>
</details>

</br> 

- ### (2) 해결
  - ##### 유저의 정보를 뒤져보는 기능들이 실행될 때마다 밑의 코드가 실행되도록 해서 null 값인 객체는 건너뛰도록 구현했다.
  - ##### 개선된 코드 참조
<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">
  
  ```java
  public static void chatRefuse(String[] str) {  // 대화 신청 거절
  	  for(int i = 0; i < allUserList.size(); i++) {
					      
		  // id가 null 값인 클라이언트 객체는 건너뛰기(continue)
		  if(allUserList.get(i).getId() == null) continue;
					      
		  if(allUserList.get(i).getId().equals(str[1])) {
			  allUserList.get(i).setSendData(Protocol.CHAT_REFUSE + ":;:" + str[1] + ":;:" + str[2]);
			  SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
			  key.interestOps(SelectionKey.OP_WRITE);
			  break;
		  }
	  }
  }
  ```
  
</div>
</details>

</br> 
  
### 5.3. 프로토콜과 메시지를 구분하는 구분자에 의한 에러

- ### (1) 문제
  - ##### 구분자가 ':' 여서 유저가 채팅을 칠 때 ':' 가 포함되는 순간 에러가 뜨는 현상이 일어났다.
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">
	
  ```java
  // 1. 데이터 전송 시 형식
  ClientController.send(Protocol.CHAT_MESSAGE + ":" + roomListIndex + ":" + ClientController.mv.getId() + ":" + msgText.getText()); 
	
  // 2. 데이터를 받고 분류하는 형식
  String data = charset.decode(byteBuffer).toString();
  str = data.split(":");
  ```

</div>
</details>	
	
<br/>
  
- ### (2) 해결
  - ##### 구분자를 ':' 에서 ':;:' 로 바꿨다.
  - ##### split으로 ':;:' 를 기준으로 구분하도록 구현했다.
<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">
	
  ```java
  // 1. 데이터 전송 시 형식
  ClientController.send(Protocol.CHAT_MESSAGE + ":;:" + roomListIndex + ":;:" + ClientController.mv.getId() + ":;:" + msgText.getText()); 
	
  // 2. 데이터를 받고 분류하는 형식
  String data = charset.decode(byteBuffer).toString();
  str = data.split(":;:");
  ```

</div>
</details>	
	
<br/>

## 6. 채팅 프로그램 시연 영상

- ### (1) 서버 로그인 -> 유저 로그인 -> 서로 친구추가 -> 1:1 채팅 신청 -> 1:1 대화
<details>
<summary><b>첫번째 영상 (54초)</b></summary>
<div markdown="1">

https://user-images.githubusercontent.com/85877080/161698170-67902bcd-b63d-4be0-a196-a4d0c480a8b3.mp4

</div>
</details>

<br/>
	
- ### (2) -> 그룹 채팅 -> 그룹채팅 및 1:1 채팅 동시 진행 -> 방 나가기 -> 서버 로그 확인 -> 서버 셧다운.
<details>
<summary><b>두번째 영상 (77초)</b></summary>
<div markdown="1">

https://user-images.githubusercontent.com/85877080/161700823-3050b15d-754d-4370-9df7-a1ed6f043e43.mp4

</div>
</details>
	
	
