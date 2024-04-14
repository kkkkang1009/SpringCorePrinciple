package hello.servlet.basic.request;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?name=hello&age=20
 * <p>
 * 2. 동일한 파라미터 전송 가능
 * http://localhost:8080/request-param?name=hello&name=kim&age=20
 * */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");
        /*
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName + "=" +
            request.getParameter(paramName));
        }
        */
        request.getParameterNames().asIterator().forEachRemaining(
                paramName -> System.out.println(paramName + "=" + request.getParameter(paramName))
        );
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();
        System.out.println("[단일 파라미터 조회]");

        String name = request.getParameter("name");
        System.out.println("request.getParameter(name) = " + name);

        String age = request.getParameter("age");
        System.out.println("request.getParameter(age) = " + age);
        System.out.println();
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        System.out.println("request.getParameterValues(name)");

        String[] names = request.getParameterValues("name");
        for (String nm : names) {
            System.out.println("names=" + nm);
        }
        resp.getWriter().write("ok");
    }
}