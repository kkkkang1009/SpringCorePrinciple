## **서블릿**

### **1\. 프로젝트 생성**

준비물: Java 11, Intellij

생성:

-   스프링 부트 스타터 사이트에서 프로젝트 생성 - [https://start.spring.io](https://start.spring.io)
    -   Gradle, Java, ~2.4.x~ -> 3.1.5
    -   Group : hello
    -   Artifact: servlet
    -   Name: servlet
    -   Package name: hello.servlet
    -   Packaging: **War**
    -   Java: ~11~ -> 17
    -   Dependencies: Spring Web, Lombok
-   Postman 설치 - [https://www.postman.com/downloads](https://www.postman.com/downloads)

설정 :

-   Build,Execution,Deployment > Build Tools > Gradle - Build and Run 관련 항목 IntelliJ IDEA로 변경
-   Plugins - **Lombok 설치**
-   Build,Execution,Deployment > Compiler > Annotation Processors - **Enable annotation processing 활성화**

### **2\. Hello 서블릿**

ServletApplication.java 파일에 @ServletComponentScan 추가한다. (Servlet 자동 등록)

Servlet을 사용시 HttpServlet을 상속 받아야한다.

Servlet은 @WebServlet으로 사용한다.

-   name: Servlet 명, urlPatterns: URL 매핑
-   Ex) @WebServlet(name="helloServlet", urlPatterns ="/hello") 

해당 Servlet urlPatterns가 호출되면 HttpServlet의 service를 실행한다. -> @Override하여 내부 동작 구현

HttpServletRequest, HttpServletResponse를 통해 요청, 응답을 컨트롤할 수 있다.

```
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 	String username = request.getParameter("username");
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        // contentLength는 자동 생성해 준다.
        response.getWriter().write("hello "+ username);
    }
}
```

main > resources > application.properties에 아래와 같이 설정하면 http 로그를 디버깅할 수 있다.

```
logging.level.org.apache.coyote.http11=debug

// logging 예시
Host: localhost:8080
Connection: keep-alive
...
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7

]
HelloServlet.service
request = org.apache.catalina.connector.RequestFacade@7e689d4e
response = org.apache.catalina.connector.ResponseFacade@1bc5543a
username = kang
```

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fmb5Qw%2FbtsAszHwKK6%2FbkolbNrNUTsFTl3i6s16kK%2Fimg.png)

webapp > index.html 생성(home 설정)

### **3\. HttpServletRequest - 개요
**

HttpServletRequest의 역할 : Servlet은 HTTP 요청 메세지를 파싱하여 HttpServletRequest 객체에 담아 제공한다.

```
// HTTP 요청 메세지 예시

POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```

-   START LINE
    -   HTTP 메소드
    -   URL
    -   쿼리 스트링
    -   스키마, 프로토콜
-   헤더
    -   헤더 조회
-   바디
    -   form 파라미터 형식 조회
    -   message body 데이터 직접 조회

### **4\. HttpServletRequest - 기본 사용법**

HTTP 요청 데이터에서 HttpServletRequest를 통해 start-line, header 등 여러 정보 조회 가능

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fci5Oxa%2FbtsGdrjG1I2%2FVLHkgNbKOYaOS9HhQDeonK%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FKgb3h%2FbtsGe8JPPYq%2Fn45im9PlXgDp8eA8sluujK%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F6NOko%2FbtsGdZmRNTg%2FvZDAKugE5X1rxahIZ0tHd1%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbgy7Xs%2FbtsGff28OYG%2FEztKV0eMeOuriiFDD40pQ1%2Fimg.png)


### **5\. HTTP 요청 데이터 - 개요**

GET - Query Parameter

-   /url?name=kkkkang1009&age=92
-   URL의 Query Parameter에 데이터를 포함해서 전달
-   Ex) 검색, 페이징 등에서 사용하는 방식

POST - HTML Form

-   content-type: application/x-www-form-urlencoded
-   message Body에 Query Parameter 형식으로 전달 name=kkkkang1009&age=92
-   예) 회원 가입, 상품 주문, HTML Form 사용

HTTP message Body에 JSON, XML, TEXT 등의 데이터를 직접 담아서 요청

-   HTTP API(Rest)에서 주로 사용

### **6\. **HTTP 요청 데이터 - GET 쿼리 파라미터****

Query Parameter는 URL에 "?"를 시작으로 보내며, 추가 파라미터는 "&"로 구분

-   Ex) http://localhost:8080/request-param?name=kkkkang1009&age=92

```
//단일 파라미터 조회 
String name = request.getParameter("name"); 

//복수 파라미터 조회
String[] names = request.getParameterValues("name"); 

//파라미터 이름들 모두 조회
Enumeration<String> parameterNames = request.getParameterNames(); 

//파라미터를 Map 으로 조회
Map<String, String[]> parameterMap = request.getParameterMap();
```

호출 URL : http://localhost:8080/request-param?name=kkkkang1009&age=92&name=kkkang1009

출력 결과 : 

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FA1PoJ%2FbtsGhpE9e5M%2FVHr3Rsm0kWbUnfex5D1ij0%2Fimg.png)

### **7\. **HTTP 요청 데이터 - POST HTML Form****

POST의 HTML Form을 통해 전송한다.

Body를 통해 보내기 떄문에 content-type 지정이 필수

```
context-type:application/x-www-form-urlencoded
```

GET방식과 형식이 동일하여 Query Parameter 조회 메소드를 그대로 사용하면 된다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbz993d%2FbtsGieROs6d%2F7kkcuFQrdIKVi2qRCK9PO1%2Fimg.png)

### **8. **HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트****

POST 방식에서 content-type을 text/plain으로 지정 후 요청시 단순 텍스트 전달 가능

```
// body의 text/plain 데이터를 byte 코드로 변환
ServletInputStream inputStream = request.getInputStream();

//해당 byte 코드를 UTF-8 Charset을 통해 문자열로 변환
String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
```

### **9. **HTTP 요청 데이터 - API 메시지 바디 - JSON****

POST 방식에서 content-type을 application/json 으로 지정 후 전달

Json 결과를 Jackson, GSON 등의 라이브러리를 통해 변환하여 사용한다.

```
private ObjectMapper objectMapper = new ObjectMapper();

ServletInputStream inputStream = request.getInputStream();
// HTML Form 데이터도 message body를 통해 전송되므로 직접 읽을 수 있다.
String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
```

### **10. **HttpServletResponse - 기본 사용법****

HttpServletResponse

-   HTTP 응답 메시지 생성(HTTP 응답코드, 헤더, 바디 생성)
-   Content-type, Cookie, Redirect 등 편의 기능 제공

```
HttpServletResponse response

// Content 설정
// response.setHeader("Content-Type", "text/plain;charset=utf-8");
response.setContentType("text/plain");
response.setCharacterEncoding("utf-8");
// response.setContentLength(2); //(생략시 자동 생성)

// Cookie 설정
// response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
Cookie cookie = new Cookie("myCookie", "good");
cookie.setMaxAge(600); //600초
response.addCookie(cookie);

// Redirect 설정
// response.setStatus(HttpServletResponse.SC_FOUND); //302
// response.setHeader("Location", "/basic/hello-form.html");
response.sendRedirect("/basic/hello-form.html");
```

### **11. **HTTP 응답 데이터 - 단순 텍스트, HTML****

HTTP 응답 메시지는 단순 텍스트, HTML 응답 존재

HTML 응답시 content-type 'text/html'로 지정 필요

```
response.setContentType("text/html");
response.setCharacterEncoding("utf-8");
PrintWriter writer = response.getWriter();
writer.println("<html>");
writer.println("<body>");
writer.println("<div>안녕?</div>");
writer.println("</body>");
writer.println("</html>");
```

### **12. **HTTP 응답 데이터 - API JSON****

HTTP 응답으로 JSON을 반환할 때는 content-type을 application/json 로 지정 필요

Jackson 라이브러리가 제공하는 objectMapper.writeValueAsString() 를 사용하면 객체를 JSON 문자로 변경 가능

```
response.setHeader("content-type", "application/json");
// application/json은 스펙상 utf-8이 정의로 charset=utf-8은 무의미 지정
response.setCharacterEncoding("utf-8");
HelloData data = new HelloData();
data.setName("kang");
data.setAge(92);
//{"name":"kang","age":92}
String result = objectMapper.writeValueAsString(data);
response.getWriter().write(result);
```

### **13\. 정리**

HTTP 요청 데이터

-   GET - Query Parameter
-   POST - HTML Form, Body(JSON, XML, TEXT)

HttpServletRequest

-   HTTP 요청 메세지
-   start-line, header 등 여러 정보 조회 가능

HttpServletResponse

-   HTTP 응답 메시지 생성 (HTTP 응답코드, 헤더, 바디 생성)
-   Content-type, Cookie, Redirect 등 편의 기능 제공