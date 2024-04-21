<%--<%@ page import="hello.servlet.domain.member.Member" %>--%>
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