package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI(); // /front-controller/V3/members/new-form 이런 부분 얻어옴
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //파라미터 뽑기
        Map<String, String> paramMap = createParamMap(request);

        //ModelView에 뷰의 논리이름과 + jsp에서 필요로하는 데이터 넣어옴
        ModelView mv = controller.process(paramMap);//controller 호출

        //viewResolver호출해서 논리 이름 -> 실제 주소로 변경하기
        String viewName = mv.getViewName();
        MyView myView = viewResolver(viewName);

        //렌더링
        myView.render(mv.getModel(),request,response); //model 정보도 함께 전달
    }

    private MyView viewResolver(String viewName) {
        MyView myView = new MyView("/WEB-INF/views/" + viewName + ".jsp"); //중복되는 부분 해결
        return myView;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        //paramMap
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator() //request에 있는 모든 파라미터 정보들 가져오기
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}