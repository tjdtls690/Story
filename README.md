# :pushpin: Saladit!
>건강식 품목별 판매 서비스 <br/>
>[demo site link](http://ec2-13-125-186-247.ap-northeast-2.compute.amazonaws.com/lastSalad/main.do)

</br>

## 1. 제작 기간 & 참여 인원 (개발 4개월차)
- 2022.2.18 ~ 2022.3.30
- 팀 프로젝트 (6명)

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 8
  - Spring Framework
  - Spring MVC
  - MySQL 5.7
  - Mybatis 
  - SQL Developer
  - mobaxterm
#### `Front-end`
  - HTML 
  - CSS 
  - JS
  - JQuery
  - JSON
  - BootStrap

</br>

## 3. 핵심 ERD

### 사진을 클릭하여 크게 보실 수 있습니다.

- **첫번째 사진의 payment_item_mapping 테이블의 payment_item_mapping_item_code(결제 상품 고유 번호) 컬럼**과 
- **두번째 사진의 item, subscribe 테이블의 각각의 Primary key인 고유번호 컬럼들**이 외래키로 묶여있지만 한 이미지에 전부 넣을 수 없어 불가피하게 두 개의 이미지로 나눠놓았습니다.

![](https://tjdtls690.github.io/assets/img/github_img/main_erd.PNG)

![](https://tjdtls690.github.io/assets/img/github_img/sub_erd.PNG)


## 4. 핵심 기능
이 서비스의 핵심 기능은 **'고객의 상품 주문 / 결제'** 와 **'관리자의 통계 시스템'** 입니다.  

- (1) **고객**은 **상품의 주문 및 결제**를 하고, 결제한 상품에 한해서 **이미지 첨부가 가능한 리뷰를 댓글 형식으로 작성**할 수 있습니다. 
- (2) **관리자**는 **매출 통계 표시 및 파일로 다운로드(PDF, Excel)** 가 가능하고,  **회원 관리와 쿠폰 관리 등등**이 있습니다.

핵심 기능 설명을 펼쳐서 기능의 흐름을 보면, 서비스가 어떻게 동작하는지 알 수 있습니다.  

<br/>

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">

### 4.1. 전체 흐름
![](https://tjdtls690.github.io/assets/img/github_img/important_flow01.PNG)

### 4.2. 사용자 요청 (JSP < - > Controller)

- #### 회원가입

  - ##### 이메일 정규식, 이미 존재하는지 여부 체크 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/signup/SignUpController.java#L48)

    - 이미 존재한다면 카카오, 구글, 샐러딧 중 어떤 타입으로 가입되어있는지 모달창을 띄워 알려줍니다.

  - **휴대폰 번호 인증** 

    - ##### 휴대폰 번호 정규식 체크 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/signup/SignUpController.java#L72)

    - ##### 문자 인증 과정

      - ##### (1) 인증번호 문자를 보내는 과정 (네이버 SMS API) (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/naver_sms_api/NaverSensSMS.java#L68)

         - 랜덤 숫자 6자리를 뽑고 세션에 들어가있는 MemberVO에 저장해줍니다.
         - 네이버 SMS API 를 이용해 랜덤 숫자 6자리를 포함한 문자를 보냅니다.

      - ##### (2) 세션에 저장된 6자리 숫자와 사용자가 적은 숫자를 비교 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/signup/SignUpController.java#L123)

        <br/>

- #### 로그인
  
  - ##### 카카오 로그인 API (화면단 : JavaScript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/kakaoLogin.jsp#L18)

    - 카카오 로그인 팝업창을 띄워서 로그인 정보를 **로그인 타입 체크 메서드**에 전달
  
  - ##### 로그인 타입 체크 (Controller) - 카카오로 로그인 시 :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/login/LoginController.java#L65)
  
    - 이메일이 구글이거나 샐러딧 전용 이메일이라면 다시 로그인 화면으로 돌려보내며 경고창을 띄웁니다.
  
    - 카카오 이메일이 맞다면, 세션에 회원 정보를 DB에서 가져와서 넣고 메인페이지로 이동합니다.
  
      
  <br/>
	
- #### 주문 페이지로 이동 (3가지 루트를 전부 감안하여 만든 메서드) (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/order/OrderController.java#L32)

  - ###### (1) 상품 상세페이지에서 구매 버튼 클릭

  - ###### (2) 장바구니에서 구매 버튼 클릭

  - ###### (3) 바로주문하기 버튼 클릭

     - ##### 3가지 루트를 커버하기 위해, 필요한 데이터들은 전부 배열 형식으로 받아서 처리.

       <br/>

- #### 주문 페이지 (주문하기 버튼 클릭 후) -> 결제 페이지로 이동

  - ###### 여러개가 나오는 데이터 종류들은 List VO에, 하나씩 나오는 데이터는 단일 데이터 VO에 넣어서 컨트롤러에 전달 (화면단 : Javascript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/order.jsp#L2593)

  - ###### 리스트 VO(PaymentSingleListVO), 단일 데이터 VO(PaymentSingleSideInfoVO) 를 통해 결제 페이지에 데이터 전달 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/paymentSingle/PaymentSingleController.java#L33)

    - 리스트 형식으로 받는 클래스 코드 (PaymentSingleListVO) **(Java) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/paymentSingle/PaymentSingleListVO.java#L5)**

      <br/>

- #### 결제 페이지 -> 결제 완료 페이지

  - ##### (1) 결제하기 버튼 클릭 시

     - ###### 가격이 0원 이상일 때 (iamport API 를 이용해서 결제 진행 후 데이터 넘기기) (화면단 : Javascript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/paymentSingle.jsp#L493)

     - ###### 포인트, 쿠폰으로 인해 가격이 0원일 때 (API 는 진행하지 않고 바로 데이터 넘기기) (화면단 : Javascript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/paymentSingle.jsp#L218)

  - ##### (2) 결제 데이터 전달, 장바구니 목록 갱신, 쿠폰 갱신, 포인트 갱신의 역할을 담당하는 메서드 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/paymentComplete/PaymentCompleteController.java#L37)

    <br/>

- #### 구매한 상품 리뷰 작성 (댓글) - AWS S3에 이미지 파일 업로드 포함

  - ##### 리뷰 정보 (별점, 내용, 이미지 파일 업로드, 해당 상품 정보 등등) 를 DB에 저장 후, 리뷰 작성 혜택인 포인트 적립하는 메서드 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/my_review_write/MyReviewWriteController.java#L126)

  - ##### 이미지 파일을 AWS S3 에 업로드할 때 사용되는 AwsS3 클래스 코드

    - createS3Client() 메서드로 AmazonS3 객체 생성 후 **(Service) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/s3/AwsS3.java#L70)**

    - upload(InputStream is, String key, String contentType, long contentLength) 메서드로 S3 에 이미지 파일 업로드 실행 **(Service) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/s3/AwsS3.java#L94)**

      <br/>

- #### 카카오 우편번호 (주소) 서비스 API

  - ##### 먼저 카카오 주소 서비스 API 의 iframe 을 담기 위한 모달창을 띄웁니다. (화면단 : JavaScript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/order.jsp#L2262)
  - ##### 띄워진 모달창 안에 API iframe 을 껴맞춰 넣어서 띄웁니다. (화면단 : JavaScript) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/views/order.jsp#L87)
  
  <br/>
  

### 4.3. Controller < - > Service

- ##### 상품(데이터) 목록 요청 (대부분의 Controller < - > Service 계층은 이러한 구조를 가지고 있습니다.)

  - ##### 요청 확인 (Controller) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/order/OrderController.java#L123)

    - 화면단에서 태그 정보(DetailVO.item_tag_main)와 모달창 상품 목록 요청을 함께 받고, 그 정보들을 Service 로 넘깁니다. 

  - ##### 결과 응답 (Service) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/order/impl/OrderServiceImpl.java#L18)

    - Service 계층에서 Controller에서 넘어온 데이터를 통해 단품, 구독 상품간의 분류를 거치고 Repository 로 넘겨서 데이터를 받아옵니다.

      
<br/>

### 4.4. Repository (DAO) - 4.3 과 이어진 구조로 설명

- **Service 의 요청을 처리  (Repository) :pushpin: [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/java/project/spring/web/order/impl/OrderDAO.java#L17)**

  - Service로부터 넘어온 데이터를 SqlSessionTemplate 을 통해 Mybatis로 넘깁니다.

  - Mybatis에서 반환 된 상품 리스트(데이터) 를 받아서 자신을 호출한 Service로 응답합니다.

    <br/>

### 4.5. MyBatis (MySQL)

- ##### Repository -> MyBatis - 4.4 와 이어진 구조로 설명

  - (1) applicationContext.xml 에서 sqlSessionTemplate 의 bean 객체 생성 **(xml 설정파일) :pushpin:  [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/webapp/WEB-INF/spring/applicationContext.xml#L42)**

  - (2) mybatis 환경설정 파일(mybatis-config.xml) 에 SQL 매퍼파일 등록 **(xml 설정 파일) :pushpin:  [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/resources/mybatis-config.xml#L44)**

  - (3) 매퍼파일에서 SQL 문을 통해 상품 데이터를 반환 **(xml 매퍼 파일) :pushpin:  [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/resources/mappings/order-mapping.xml#L38)**

    <br/>

- ##### 상품 검색 요청에 대한 응답 SQL문 :pushpin:  [코드 확인](https://github.com/tjdtls690/mainProject/blob/dev/src/main/resources/mappings/search-mapping.xml#L39)

  - ##### 두 테이블을 left join으로 합쳐서 데이터를 가공 및 리스트로 반환
  
    - (상품 이미지, 이름, 가격) + (상품의 리뷰 평균 별점, 리뷰 총 개수)

</div>
</details>

<br/>

## 5. 핵심 트러블 슈팅

### 5.1. 통계 데이터 PDF 파일 다운로드 처리 문제

- 저는 이 프로젝트가 상품 판매 사이트이기에 여러 종류의 매출 통계를 내는 것도 핵심 중 하나라 생각합니다.

- WAS(tomcat 서버)를 통해 war 파일로 배포 후, 관리자 통계 페이지에서 통계를 PDF 파일로 다운 실패

- ### (1) 문제 

  - ##### 경로가 "C:\\금주 매출.pdf" 로 잡혀있는데, war파일로 배포 후 war파일 안의 폴더로 경로가 잡히는 문제가 발생 (클라이언트로 다운이 안받아짐)
  - ##### 기존 코드 참조


<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">
	
~~~java
@RequestMapping("/pdfDown.mdo")
public void pdfDown(HttpServletRequest request)throws Exception{

    try {
        Document document = new Document(); // pdf문서를 처리하는 객체

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\금주 매출.pdf"));
        // pdf파일의 저장경로를 C드라이브에 '금주 매출.pdf'로 저장한다는 뜻

        document.open(); // 웹페이지에 접근하는 객체를 연다
        String path =request.getSession().getServletContext().getRealPath("/");

        /* 중간 코드 생략 */

        document.add(table); // 웹접근 객체에 table를 저장한다.
        document.close(); // 저장이 끝났으면 document객체를 닫는다.
        System.out.println("성공");

    }catch (Exception e) {
        System.out.println("실패");
        e.printStackTrace();
    }
}
~~~

</div>
</details>
	
<br/>

- ### (2) 해결 

  - ##### 기존 코드에서 pdf 파일에 내용을 집어넣고 아예 새창으로 띄워서, 클라이언트에서 인쇄와 미리보기가 전부 가능하도록 구현하는 것으로 변경

  - ##### 총 4가지 절차를 통해 해결
  - ##### 개선된 코드 참조


<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
// 1번
// pom.xml 에 의존성 추가
// PDF 출력을 위한 의존성 설정
<dependency>
	<groupId>com.lowagie</groupId>
	<artifactId>itext</artifactId>
	<version>2.1.7</version>
</dependency>
    
    

// 2번
// application.xml 부모 설정파일에 pdf 출력할 클래스를 빈객체로 생성
// 빈객체 생성
<context:component-scan base-package="com.pdf.*" />
    
// Controller에서 'pdf' 로 뷰 이름 전송 시 밑의 com.pdf.web.PdfDownView 클래스가 처리 후 화면출력
<bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
    <property name="order" value="0" />
</bean>
    
// pdf라는 뷰 이름이 왔을 때 출력할 뷰를 설정
<bean id="pdf" class="com.pdf.web.PdfDownView"/>
    

    
// 3번
// 컨트롤러에서 리스트로 pdf에 찍을 값을 넘긴 후 pdf 란 이름으로 뷰 이름 보내기
@RequestMapping("/pdfDown.mdo")
public String pdfDownload(Model model){
    //날짜용 메서드
    LocalDate now = LocalDate.now();
    DecimalFormat df = new DecimalFormat("00");
    Calendar currentCalendar = Calendar.getInstance();
    // 이번 년도	--> 2022
    int year = now.getYear();
    //이번달		--> 03
    String month  = df.format(currentCalendar.get(Calendar.MONTH) + 1);
    // 이번달 시작일
    String startDay = year+"-"+month+"-"+"01";
    // 이번달 마지막일
    int str = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    String endDay = year+"-"+month+"-"+str;

    AdminReportDayVO vo = new AdminReportDayVO();
    vo.setDate(startDay);
    vo.setImpl(endDay);
    List<AdminReportDayVO> list2 = adminReportDayService.reportMonth(vo);

    //출력할 뷰 이름 리턴
    model.addAttribute("list", list2);
    return "pdf";
}



// 4번
// com.pdf.web.PdfDownView 클래스의 pdf를 새창으로 띄워서 출력하는 메서드
	//첫번째 매개변수가 Controller가 넘겨준 데이터 
    //두번째 매개변수는 출력할 문서
@Override
protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer, HttpServletRequest request,HttpServletResponse response) throws Exception {

    List<String> list = (List<String>)model.get("list");
    XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
    //테이블을 생성
    //1열 list.size()+1 행으로 생성
    Table table = new Table(1,list.size()+1);

    
    /* 중간 코드 생략 */

    
    String path =request.getSession().getServletContext().getRealPath("/");
    BaseFont baseFont= BaseFont.createFont(path +"/resources/pdfFresh/fresh.ttf".replace('/', File.separatorChar), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font font = new Font(baseFont);

    Cell cell = new Cell(new Paragraph("결제 영수증",font));
    cell.setHeader(true);
    table.addCell(cell);
    table.endHeaders();

    //데이터를 테이블의 셀에 출력
    for(String language : list){
        Cell imsi = new Cell(new Paragraph(language,font));
        table.addCell(imsi);
    }
    //문서에 테이블 추가
    doc.add(table);

}
~~~

</div>
</details>
	
- ### (3) 시연 
	
<details>
<summary><b>PDF 파일 다운 시연 영상 (10초)</b></summary>
<div markdown="1">
	
https://user-images.githubusercontent.com/85877080/166142993-e414ae1b-5f20-4118-aeed-555b77dad81b.mp4
	
</div>
</details>

<br/>

### 5.2. DB에서 한 컬럼에 여러개의 이미지 URL 을 한번에 처리해야 하는 문제.

- ### (1) 문제 : 기존 코드의 상황

  - ##### 한 상품 당 이미지 파일 개수만큼 컬럼을 만들어서, 컬럼의 개수가 비정상적으로 늘어난 상황


- ### (2) 해결 : 개선된 코드의 상황

  - ##### 한 컬럼 안에 URL 간에 구분자(:;:)를 줘서 Controller에서 split 메서드로 나눈다음 List에 담아서 처리
  - ##### 개선된 코드 참조

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

  ```java
  // 현재 DB에서 이미지 URL을 담은 컬럼의 상황
  // URL:;:URL:;:URL:;:URL:;:URL ...
  
  
  // Controller에서 해당 컬럼의 데이터를 다루는 코드
  // replace 메서드로 :;: -> \\ 로 변경 후 StringTokenizer 로 \\ 를 구분자 삼아서 나눈다.
  StringTokenizer st = new StringTokenizer(itemNut.getItem_nut().replace(":;:", "\\"), "\\");
  List<String> nut = new ArrayList<String>();
  
  // 리스트에 나눠진 토큰을 하나씩 담는다.
  while(st.hasMoreTokens()) {
      String str1 = st.nextToken();
      nut.add(str1);
  }
  mav.addObject("itemNut", nut);
  
  // 이후 화면단에서 c:foreach 문을 통해 전체 이미지 파일 적용
  ```

</div>
</details>

</br>

### 5.3. 달력 처리 문제 (달력 알고리즘 직접 구현)

- ### (1) 문제 : 기존 코드의 상황

  - ##### 상품 선택 후, 배송 예정일을 선택할 때 달력을 띄우기 위해 DatePicker API 를 이용해 가져왔지만 해당 페이지와의 CSS 가 너무 엇나가는 느낌이 드는 문제가 발생 (CSS 의 조절이 불가능)


- ### (2) 해결 : 개선된 코드의 상황

  - ##### 현재 달력 모달창의 CSS를 페이지에 맞게 수정 후 달력의 날짜가 매일 갱신되도록 달력 알고리즘을 직접 구현
  - ##### 개선된 코드 참조


<details>
<summary><b>개선된 코드 1번</b></summary>
<div markdown="1">

~~~java
// 1번
// 화면단에서 연, 월, 일을 받아서 윤년, 월을 분류해서 그에 맞는 배열 길이를 넣습니다.
// 화면단에서 받은 getDay (현재 날짜가 일주일 중 몇번째 요일인지) 정보를 달력 모달창에 넘겨서 
	// 월마다 어떤 요일부터 시작하는지 구현하도록 합니다.

@RequestMapping("/orderDateCalendar.do")
public ModelAndView orderDateCalendarDo(ModelAndView mav, String year, String month, String day, String getDay) {
    System.out.println(Integer.parseInt(month));
    String[] dateNum;
    if(Integer.parseInt(month) == 1 || Integer.parseInt(month) == 3 || Integer.parseInt(month) == 5 || Integer.parseInt(month) == 7
       || Integer.parseInt(month) == 8 || Integer.parseInt(month) == 10 || Integer.parseInt(month) == 12) {
        dateNum = new String[38];
    }else if(Integer.parseInt(month) == 2) {
        if(Integer.parseInt(year) % 4 == 0){
            if(Integer.parseInt(year) % 400 != 0 && Integer.parseInt(year) % 100 == 0) {
                dateNum = new String[35];
            }else {
                dateNum = new String[36];
            }
        }else {
            dateNum = new String[35];
        }
    }else {
        dateNum = new String[37];
    }
    int getDayNum = Integer.parseInt(getDay);
    String[] getD = new String[getDayNum];
    int mon = Integer.parseInt(month);

    mav.addObject("remainSu", getDayNum);
    mav.addObject("remainCheck", getD);
    mav.addObject("year", year);
    mav.addObject("check", day);
    mav.addObject("month", mon);
    mav.addObject("list", dateNum);
    mav.setViewName("orderDateCalendar");
    return mav;
}
~~~

</div>
</details>

<details>
<summary><b>개선된 코드 2번</b></summary>
<div markdown="1">
	
~~~jsp
<!-- 2번 -->

<!-- 조건 4가지를 if문으로 나눠서, 위치를 지정해주는 style의 grid-area 속성을 통해 알고리즘 구현 -->
<!-- 1. 저번 달에 속한 날짜 (빈칸 비워두는 용도)-->
<!-- 2. 이번 달에 속했지만 오늘 날짜보다 전인 날(오늘날짜 포함) -->
<!-- 3. 이번 달에 속하면서 오늘 날짜보다 후의 날 -->
<!-- 4. 달력을 5줄로 맞춰야 하기에 4줄로 끝나는 달은 공백 날짜 일주일을 더 추가 -->

<!-- span 태그 id속성, text에 날짜 입력 -->
<!-- 보기 쉽게 하기 위해 class 속성은 전부 지웠습니다. -->



<!-- 1. 저번 달에 속한 날짜 (빈칸 비워두는 용도)-->
<c:forEach var="remain" items="${remainCheck }" varStatus="i">
	
    <!-- grid-area 속성에 알고리즘 적용 -->
    <div data-v-3ca35a05="" style="grid-area: 2/${i.count}/auto/auto;">
        <div data-v-2aac4f42="" data-v-4a5f2beb="" attributes="[object Object]" data-v-3ca35a05="">
            <div data-v-2aac4f42="">
                <span data-v-2aac4f42=""></span>
            </div>
        </div>
    </div>
</c:forEach>



<c:forEach var="list1" items="${list }" varStatus="i">
    <c:if test="${fn:length(list) == 38}">
        <c:if test="${i.count <= 31}">
		
	    <!-- grid-area 속성에 알고리즘 적용 -->
            <div data-v-3ca35a05="" style="grid-area: ${(i.count + remainSu - 1) / 7 + 2}/${(i.count + remainSu - 1) % 7 + 1}/auto/auto;">
                <div data-v-2aac4f42="" data-v-4a5f2beb="" data-v-3ca35a05="">
                    
                    
                    <!-- 2. 이번 달에 속했지만 오늘 날짜보다 전인 날(오늘날짜 포함) -->
                    <c:if test="${i.count <= check }">
                        <div data-v-2aac4f42="" class="vc-h-full">
                            <span data-v-2aac4f42="" tabindex="-1" id="${year}/${month}/${i.count}">${i.count }</span>
                        </div>
                    </c:if>
                    
                    
                    <!-- 3. 이번 달에 속하면서 오늘 날짜보다 후의 날 -->
                    <c:if test="${i.count > check }">
                        <div data-v-2aac4f42="" class="vc-h-full">
                            <span data-v-2aac4f42="" tabindex="-1" id="${year}/${month}/${i.count}">${i.count }</span>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>
        
        
        <!-- 4. 달력을 5줄로 맞춰야 하기에 4줄로 끝나는 달은 공백 날짜 일주일을 더 추가 -->
        <c:if test="${i.count > 31 && remainSu < 5}">
		
	    <!-- grid-area 속성에 알고리즘 적용 -->
            <div data-v-3ca35a05="" style="grid-area: ${(i.count + remainSu - 1) / 7 + 2}/${(i.count + remainSu - 1) % 7 + 1}/auto/auto;">
                <div data-v-2aac4f42="" data-v-4a5f2beb="" data-v-3ca35a05="">
                    <div data-v-2aac4f42="" >
                        <span data-v-2aac4f42="" id="${year}/${month}/${i.count}">27</span>
                    </div>
                </div>
            </div>
        </c:if>
    </c:if>
    
    
    <!-- 이 이후론 넘어온 배열의 길이마다 조건만 다르게 주면서 위의 형식과 동일하게 3번 더 반복하기에 생략 -->
    
    
    <c:if test="${fn:length(list) == 37}">
        <c:if test="${i.count <= 30}">
            <div data-v-3ca35a05="" style="grid-area: ${(i.count + remainSu - 1) / 7 + 2}/${(i.count + remainSu - 1) % 7 + 1}/auto/auto;">
                
                			<!-- 생략 -->
    </c:if>
</c:forEach>
~~~

</div>
</details>
	
- ### (3) 시연 
	
<details>
<summary><b>달력 모달창 시연 영상 (10초)</b></summary>
<div markdown="1">
	
https://user-images.githubusercontent.com/85877080/166143499-eda5c261-bb66-4ca5-bb93-2969040bf612.mp4
	
</div>
</details>

<br/>

## 6. 그 외 트러블 슈팅
<details>
<summary><b>Mybatis를 통해 두 테이블에 차례대로 insert 할 때, 해당 두 데이터를 Auto increment 값(primary key)으로 연결시키는 문제.</b></summary>
<div markdown="1">


- ##### 해결 : 1번째 테이블에 insert 함과 동시에 Auto increment 값(primary key)을 반환 받는 방법으로 해결


  - (1) SQL 매퍼파일 중 insert문에 useGeneratedKeys="true" keyProperty="payment_code" 두 속성 추가
  - (2) 이 insert 문을 실행 시, **자동으로 1씩 증가하며 insert 되는 payment_code 컬럼(Auto increment)의 값**을 VO객체가 반환받고 그대로 다시 나온다.

- #### SQL 매퍼파일

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```xml
<!-- insert 태그의 속성 중 useGeneratedKeys, keyProperty 속성 -->

<insert id="paymentInfoSave" parameterType="paymentDetailSide" useGeneratedKeys="true" keyProperty="payment_code">
    
		insert into payment_info(payment_member_code, payment_date, payment_recipient, payment_recipient_phone, payment_delivery_type,
		payment_zipcode, payment_address, payment_memo, payment_price, payment_delivery_price, payment_sum_price,
		payment_coupon_price, payment_point_price, payment_sail_price, payment_final_price) 
		values(#{payment_member_code}, #{payment_date}, #{payment_recipient}, #{payment_recipient_phone}, #{payment_delivery_type}, #{payment_zipcode}, #{payment_address}, 
		#{payment_memo}, #{payment_price}, #{payment_delivery_price}, #{payment_sum_price}, #{payment_coupon_price}, #{payment_point_price}, #{payment_sail_price}, #{payment_final_price});
    
</insert>
```

</div>
</details>

- ### Controller

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```java
// 결제 테이블
vo.setPayment_member_code(vo1.getMemberCode());
vo.setPayment_date(date);
paymentCompleteService.paymentInfoSave(vo); // mybatis 해당 insert태그의 keyProperty 속성의 컬럼 값이 vo에 자동 저장

// 결제 한 건당 매핑 된 아이템들 저장하는 테이블 (테이블 생성은 완료)
// 위에 vo에서 payment_code 꺼내서 리스트에 있는 vo에 전부 넣어주면서 저장하면 끝
for(int i = 0; i < list.getPaymentMyDetailInfo().size(); i++) {
    list.getPaymentMyDetailInfo().get(i).setPayment_code(vo.getPayment_code());
	list.getPaymentMyDetailInfo().get(i).setPayment_member_code(vo1.getMemberCode());
   	paymentCompleteService.paymentMappingItemInfoSave(list.getPaymentMyDetailInfo().get(i));
}
```

</div>
</details>
	
<br/>

</div>
</details>
	
<br/>

<details>
<summary><b>aws s3 엑세스 키, 시크릿 키 처리 방법</b></summary>
<div markdown="1">


  - #### 실수로 깃허브에 AWS S3의 엑세스, 시크릿 키를 올리게 돼서 AWS 계정 폐쇄 위기 직면

  - ##### 해결 


    - (1) AWS 루트 계정 암호와 IAM(Identity and Access Management) 사용자의 암호 변경

    - (2) AWS Management 콘솔을 확인하여 모든 사용이 승인되었는지 확인

    - ##### (3) 자바 파일이 아닌 DB에 엑세스 키, 시크릿 키를 저장하고, 필요 시 꺼내서 사용하도록 변경
	
<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```java
// 관리자의 상품 등록 코드 중...
    
// 메인이미지 업로드
try {
        AwsS3 awsS3 = new AwsS3();
        
        // awsS3Service 서비스 클래스를 통해 DB에서 키 꺼내오기
        AwsS3VO avo = awsS3Service.getKey();
        awsS3.setAccessKey(avo.getA_key());
        awsS3.setSecretKey(avo.getS_key());
        
        // 받아온 키를 통해 AmazonS3 객체 생성
        awsS3.createS3Client();
        String key = "menu/" + uploadFile.getOriginalFilename();
        InputStream is = uploadFile.getInputStream();
        String contentType = uploadFile.getContentType();
        long contentLength = uploadFile.getSize();
        
        // 이미지 파일 S3에 업로드
        awsS3.upload(is, key, contentType, contentLength);
    
        // DB에 S3 이미지 파일 URL 저장
        vo.setItem_image(url + key);
	adminItemService.itemInsert(vo);
} catch (IOException e) {
	System.out.println("메인이미지 업로드 실패");
}
```

</div>
</details>

<br/>

</div>
</details> 
	
<br/>

<details>
<summary><b>WAS(tomcat 서버)를 통해 war 파일로 배포 후 구글 로그인 API가 "popup_closed_by_user" 경고창이 뜨면서 로그인이 안되는 문제</b></summary>
<div markdown="1">


  - ##### 해결 : 크롬 우측 상단 더보기 -> 도구 더보기 -> 인터넷 사용 기록 삭제 -> 캐시된 이미지 및 파일만 체크 -> 인터넷 사용 기록 삭제 버튼 클릭

</div>
</details>
	
<br/>

<details>
<summary><b>카카오 로그인 API가 실행될 때 크롬 콘솔창에서 samesite 에러가 지속적으로 뜨면서 카카오 로그인 팝업창이 안뜨는 문제</b></summary>
<div markdown="1">


  - #####  해결 : 개발자 모드(f12)를 끄고 실행 


    - 해결책을 찾아보는 중에 <code><strong>쿠키 설정을 SameSite=None 로 변경</strong></code>해줘야한다고 했지만 **쿠키설정 변경 없이 개발자 모드를 꺼주고 실행해주는 것만으로 해결**이 됐다.


</div>
</details>
	
<br/>

<details>
<summary><b>카카오 로그인 API 테스트 실행 시 두번째 로그인부터 정보 제공 동의 화면이 띄워지지 않는 문제.</b></summary>
<div markdown="1">


  -  해결 : https://accounts.kakao.com/weblogin/account/info 링크(계정관리 사이트)에 들어가서 **상단에 계정 이용 탭 -> 외부 서비스 전체보기 -> 해당 서비스(앱) 연결 끊기**

</div>
</details>
	
<br/>
    
<details>
<summary><b>카카오 로그인 API 테스트 실행 시 두번째 로그인부터 자동로그인만 돼서 새로운 계정으로 로그인이 안되는 문제</b></summary>
<div markdown="1">

  - #####  해결 : https://developers.kakao.com/logout 링크를 들어가는 것만으로 로그인 되어있는 계정 로그아웃.

</div>
</details>  
	
<br/>

<details>
<summary><b>이미지 미리보기 처리 문제</b></summary>
<div markdown="1">

- 이미지 추가 버튼 클릭 -> 이미지 파일을 선택 후, 선택한 이미지 파일을 버튼 옆에 미리보기 할 수 있도록 개선

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```javascript
function readURL1(input) {
    
    // 인풋 태그에 파일이 있는 경우
    if (input.files && input.files[0]) {
        
        // FileReader 인스턴스 생성
        var reader = new FileReader();
        
        // 이미지가 로드 된 경우
        reader.onload = function (e) {
            // 이미지 미리보기 띄우기
            $(".preview-images1").css({"background":"url(" + e.target.result + ")"});
        }
        
        // reader 가 이미지 읽도록 하기
        reader.readAsDataURL(input.files[0]);
    }

    $('#image-file-button1').parent().css('display', 'none');
    $('#image-file-button2').parent().css('display', '');
}
```

</div>
</details> 
	
<br/>

</div>
</details> 

</br>
