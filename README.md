# miniProject (개발 입문 1개월 프로젝트)
## - nio 넌블록킹 통신 기반 채팅 프로그램<br/><br/>  
### == 어플리케이션 실행 방법 ==<br/><br/>
### 1. 관리자 서버 프로그램 시작 클래스 : src/server/controller/ServerController.java 실행
#### &nbsp;&nbsp;&nbsp;- 오라클 디비에 들어있는 관리자 아이디와 비번 일치하면 로그인 성공 및 서버채널소켓 실행
#### &nbsp;&nbsp;&nbsp;- 클라이언트를 서버소켓채널로 받아들일 준비 완료<br/><br/>
### 2. 클라이언트 프로그램 시작 클래스 : src/Index.java 실행
#### &nbsp;&nbsp;&nbsp;- 이것도 마찬가지로 오라클 디비에 들어있는 아이디, 비번 일치할 시 로그인 성공
#### &nbsp;&nbsp;&nbsp;- 테스트는 여러 클라이언트를 실행 후 여러 아이디로 접속하여 서로 친구추가를 하고 대화방에 초대해서 채팅
<br/>

https://user-images.githubusercontent.com/85877080/161683761-584e5950-2977-4b80-9f69-95fad786844d.mp4
