# miniProject (개발 입문 1개월차 프로젝트) 
# &nbsp;&nbsp;&nbsp; - 1인 개발, 기간 1주일 (밑에 영상 2개 참고)
## - nio 넌블록킹 통신 기반 채팅 프로그램<br/><br/>
## - 기술 스택 : <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">&nbsp;&nbsp;<img src="https://img.shields.io/badge/javafx-00599C?style=for-the-badge&logo=java&logoColor=white">&nbsp;&nbsp;<img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
### 1. Mini Project 영상 자료

https://user-images.githubusercontent.com/85877080/161683761-584e5950-2977-4b80-9f69-95fad786844d.mp4

<br/>

### == 어플리케이션 실행 방법 ==<br/><br/>
### 1. 관리자 서버 프로그램 시작 클래스 : src/server/controller/ServerController.java 실행
#### &nbsp;&nbsp;&nbsp;- 서버쪽 주요 컨트롤러 클래스 : src/server/controller/ServerController.java
#### &nbsp;&nbsp;&nbsp;- 오라클 디비에 들어있는 관리자 아이디와 비번 일치하면 로그인 성공 및 서버채널소켓 실행
#### &nbsp;&nbsp;&nbsp;- 클라이언트를 서버소켓채널로 받아들일 준비 완료<br/><br/>
### 2. 클라이언트 프로그램 시작 클래스 : src/Index.java 실행
#### &nbsp;&nbsp;&nbsp;- 클라이언트쪽 주요 컨트롤러 클래스 : src/ClientController/ClientController.java
#### &nbsp;&nbsp;&nbsp;- 이것도 마찬가지로 오라클 디비에 들어있는 아이디, 비번 일치할 시 로그인 성공
#### &nbsp;&nbsp;&nbsp;- 테스트는 여러 클라이언트를 실행 후 여러 아이디로 접속하여 서로 친구추가를 하고 대화방에 초대해서 채팅
