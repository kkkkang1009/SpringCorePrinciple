### **1\. 회원 관리 웹 애플리케이션 요구 사항**

Member, MemberRespository를 생성한다.

Repository의 경우 싱글톤 패턴을 위해 생성자를 Private 로 막아둔다.

```
package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String name;
    private int age;

    public Member() {
    }

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

```
package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;
    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    private MemberRepository() {
    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
```

### **2\. 서블릿으로 회원 관리 웹 애플리케이션 만들기**

Servlet, Java를 이용하여 회원 정보, 회원 목록 등 동적인 웹 페이지를 만들 수 있다.

```
@WebServlet(name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Member> members = memberRepository.findAll();
        PrintWriter w = response.getWriter();
        w.write("<html>");
        w.write("<head>");
        w.write("    <meta charset=\"UTF-8\">");
        w.write("    <title>Title</title>");
        w.write("</head>");
        w.write("<body>");
        w.write("<table>");
        w.write("    <thead>");
        w.write("    <th>id</th>");
        w.write("    <th>name</th>");
        w.write("    <th>age</th>");
        w.write("    </thead>");
        w.write("    <tbody>");
        /* 정적인 리스트
        w.write("    <tr>");
        w.write("       <td>1</td>");
        w.write("       <td>userA</td>");
        w.write("       <td>10</td>");
        w.write("    </tr>");
         */
        for (Member member: members) { // 동적인 리스트
            w.write("    <tr>");
            w.write("        <td>" + member.getId() + "</td>");
            w.write("        <td>" + member.getName() + "</td>");
            w.write("        <td>" + member.getAge() + "</td>");
            w.write("    </tr>");
        }
        w.write("    </tbody>");
        w.write("</table>");
        w.write("</body>");
        w.write("</html>");
    }
}
```

Java를 이용하여 HTML을 만들어 내는 것은 매우 비효율적이다.

템플릿 엔진을 통해 HTML에서 필요한 곳만 코드를 적용하여 동적으로 변경할 수 있다.

-   JSP, Thymeleaf, Freemarker, Velocity 등
-   JSP는 점점 사장되어 가는 추세, Thymeleaf가 Spring과 잘 통합됨

### **3\. JSP로 회원 관리 웹 애플리케이션 만들기**

build.gradle 파일에 JSP관련 패키지를 추가한다.

```
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'javax.servlet:jstl'
    
// Spring Boot 3.0 이상인 경우는 아래와 같다.    
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'jakarta.servlet:jakarta.servlet-api'
implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api'
implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl'
```

JSP 사용법

```
// JSP 문서는 아래 처럼 시작하며 JSP파일 이라는 뜻
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

// 자바 import 부분
<%@ page import="hello.servlet.domain.member.MemberRepository" %>

// 자바 코드 입력 부분
<% ~~ %>

// 자바 코드 출력 부분
<%= ~~ %>
```

회원 목록 JSP 파일

```
<%@ page import="java.util.List" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();
    List<Member> members = memberRepository.findAll();
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<a href="/index.html">메인</a>
<table>
    <thead>
    <th>id</th>
    <th>name</th>
    <th>age</th>
    </thead>
    <tbody>
    <%
        for (Member member : members) {
            out.write(" <tr>");
            out.write(" <td>" + member.getId() + "</td>");
            out.write(" <td>" + member.getName() + "</td>");
            out.write(" <td>" + member.getAge() + "</td>");
            out.write(" </tr>");
        }
    %>
    </tbody>
</table>
</body>
</html>
```

서블릿과 JSP의 한계

-   Servlet으로 개발시 HTML 작업이 자바 코드에 섞여 지저분하고 복잡
-   JSP를 통해 HTML 작업이 깔끔해졌으며, 동적 부분에 자바 코드 적용 가능
-   기존 비즈니스 로직이 JSP에 노출됨 -> JSP가 너무 많은 역할 가짐 -> 유지보수 힘듬

### **4\. MVC 패턴 - 개요**

하나의 Servlet, JSP로 비즈니스 로직, 뷰 렌더링 까지 처리하게되면 역할이 많아지고, 유지보수가 어려워진다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F6oOlf%2FbtsGNbUkWTD%2F2CT7pa2VF1YWZYiwhoVqe1%2Fimg.png)

변경의 라이프 사이클 - UI변경과 비즈니스 로직 수정하는 일은 라이프 사이클이 다르고, 따로 관리하는 것이 좋다.

Model View Controller - 비즈니스 로직(Controller), 화면(View) 역할 분리

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdshMoC%2FbtsGNZZT45c%2F0ORpAL23BMBLrvBy8UKc71%2Fimg.png)

-   Controller : HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행하여 결과 데이터를 조회해서 모델에 담는다.
-   Model : View에 출력할 데이터
-   View : Model의 데이터를 사용해서 HTML을 생성

### **5\. MVC 패턴 - 적용**

Servlet을 Controller로, JSP를 View로, HttpServletRequest를 Model로 사용하여 MVC 패턴을 적용할 수 있다.

```
request.setAttribute()	// 데이터 저장
request.getAttribute()	// 데이터 조회
```

Servlet은 @WebServlet으로 사용한다.

-   name: Servlet 명, urlPatterns: URL 매핑



Servlet을 이용하여 Controller 구현

```
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

```
@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String name = request.getParameter("name");
         int age = Integer.parseInt(request.getParameter("age"));
         Member member = new Member(name, age);
         System.out.println("member = " + member);
         memberRepository.save(member);
         //Model에 데이터를 보관한다.
         request.setAttribute("member", member);
         String viewPath = "/WEB-INF/views/save-result.jsp";
         RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
         dispatcher.forward(request, response);
     }
 }
```

```
@WebServlet(name = "mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MvcMemberListServlet.service");
        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

**Redirect -** 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청, URL 경로도 실제로 변경된다.

**Forward -** 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

JSP를 이용하여 View 구현 - /WEB-INF 경로 내부에 있으면 외부에서 직접 접근 불가

```
// new-form.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!-- 상대경로 사용, [현재 URL이 속한 계층 경로 + /save] -->
<form action="save" method="post">
    name: <input type="text" name="name" />
    age: <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
```

```
// save-result.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
성공
<ul>
<%--    <li>id=<%=((Member)request.getAttribute("member")).getId()%>></li>--%>
<%--    <li>name=<%=((Member)request.getAttribute("member")).getName()%></li>--%>
<%--    <li>age=<%=((Member)request.getAttribute("member")).getAge()%></li>--%>
    <li>id=${member.id}</li>
    <li>name=${member.name}</li>
    <li>age=${member.age}</li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
```

<%= request.getAttribute("member")%> 로 모델에 저장한 member 객체 접근 가능

JSP는 ${} 문법을 통해 request의 attribute에 담긴 데이터를 편리하게 조회 가능

```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<a href="/index.html">메인</a>
<table>
  <thead>
  <th>id</th>
  <th>username</th>
  <th>age</th>
  </thead>
  <tbody>
  <c:forEach var="item" items="${members}">
    <tr>
      <td>${item.id}</td>
      <td>${item.username}</td>
      <td>${item.age}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</body>
</html>
```

<%@ taglib prefix="c" uri="[http://java.sun.com/jsp/jstl/core](http://java.sun.com/jsp/jstl/core)"%>를 통해 <c:forEach> 기능을 사용가능

### **6\. MVC 패턴 - 한계**

1.  Forward를 위해 View로 이동하는 코드가 항상 중복 호출
2.  ViewPath 중복코드가 반복된다. 
3.  Response 객체 미사용한다.
4.  공통 처리가 어렵다.
5.  정리하면 공통 처리가 어렵다