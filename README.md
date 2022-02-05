# miniProject (개발 입문 2개월차 프로젝트)
## - nio 넌블록킹 통신 기반 채팅 프로그램


## - 어플리케이션 실행 방법
### 1. 관리자 서버 프로그램 시작 클래스 : src/server/controller/ServerController.java 실행
#### ---- 오라클 디비에 들어있는 관리자 아이디와 비번 일치하면 로그인 성공 및 서버채널소켓 실행
#### ---- 클라이언트를 서버소켓채널로 받아들일 준비 완료

### 2. 클라이언트 프로그램 시작 클래스 : src/index.java 실행
#### ---- 이것도 마찬가지로 오라클 디비에 들어있는 아이디, 비번 일치할 시 로그인 성공
#### ---- 테스트는 여러 클라이언트를 실행 후 여러 아이디로 접속하여 서로 친구추가를 하고 대화방에 초대해서 채팅
