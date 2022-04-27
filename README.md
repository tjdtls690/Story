# miniProject (개발 입문 1개월차 1인 프로젝트) 
# &nbsp;&nbsp;&nbsp; - 1인 개발, 기간 10일 (밑에 영상 2개 참고)
## - nio 넌블록킹 통신 기반 채팅 프로그램<br/><br/>
## - 기술 스택 : <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">&nbsp;&nbsp;<img src="https://img.shields.io/badge/javafx-00599C?style=for-the-badge&logo=java&logoColor=white">&nbsp;&nbsp;<img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
### 1. Mini Project 영상 자료
#### &nbsp;&nbsp;&nbsp;- 영상을 접고싶으면 영상 상단에 있는 파일 명 탭을 눌러주시기 바랍니다.
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(1) 서버 로그인 -> 유저 로그인 -> 서로 친구추가 -> 1:1 채팅 신청 -> 1:1 대화
https://user-images.githubusercontent.com/85877080/161698170-67902bcd-b63d-4be0-a196-a4d0c480a8b3.mp4

[코드 확인](https://github.com/tjdtls690/miniProject/blob/main/src/application/objectVO/MemberVO.java#L67)
<br/>

#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(2) -> 그룹 채팅 -> 그룹채팅 및 1:1 채팅 동시 진행 -> 방 나가기 -> 서버 로그 확인 -> 서버 셧다운.
https://user-images.githubusercontent.com/85877080/161700823-3050b15d-754d-4370-9df7-a1ed6f043e43.mp4

<br/>

### == 어플리케이션 실행 방법 ==<br/><br/>
### 1. 관리자 서버 프로그램 시작 클래스 : src/server/controller/ServerIndex.java 실행
#### &nbsp;&nbsp;&nbsp;- 서버쪽 주요 컨트롤러 클래스 : src/server/controller/ServerController.java
#### &nbsp;&nbsp;&nbsp;- 오라클 디비에 들어있는 관리자 아이디와 비번 일치하면 로그인 성공 및 서버채널소켓 실행
#### &nbsp;&nbsp;&nbsp;- 클라이언트를 서버소켓채널로 받아들일 준비 완료<br/><br/>
### 2. 클라이언트 프로그램 시작 클래스 : src/Index.java 실행
#### &nbsp;&nbsp;&nbsp;- 클라이언트쪽 주요 컨트롤러 클래스 : src/ClientController/ClientController.java
#### &nbsp;&nbsp;&nbsp;- 이것도 마찬가지로 오라클 디비에 들어있는 아이디, 비번 일치할 시 로그인 성공
#### &nbsp;&nbsp;&nbsp;- 테스트는 여러 클라이언트를 실행 후 여러 아이디로 접속하여 서로 친구추가를 하고 대화방에 초대해서 채팅
