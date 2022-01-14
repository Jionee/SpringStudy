package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
    //서블릿과 비슷한 모양
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age = {}",username,age);
        response.getWriter().write("ok");
    }

    //RequestParam 사용하기
    @ResponseBody //return값이 string일 때 view 조회를 하지 않고 바로 응답에 string이 박혀서 나간다. (RestController와 같은 역할)
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ){
        log.info("username={}, age={}",memberName,memberAge);
        return "ok";
    }

    @ResponseBody //return값이 string일 때 view 조회를 하지 않고 바로 응답에 string이 박혀서 나간다. (RestController와 같은 역할)
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username, //매핑값과 이름이 같으면 이름 생략 가능
            @RequestParam int age
    ){
        log.info("username={}, age={}",username,age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4( String username, int age){ //다 없앨 수 있음
        log.info("username={}, age={}",username,age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username, //꼭 들어와야 함
            @RequestParam(required = false) Integer age //안들어와도 됨
    ){
        log.info("username={}, age={}",username,age);
        return "ok";
    }

    //defaultValue는 "" 빈 문자의 경우도 빈 것으로 간주한다.
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username, //꼭 들어와야 함
            @RequestParam(required = false, defaultValue = "-1") int age //안들어와도 됨
    ){
        log.info("username={}, age={}",username,age);
        return "ok";
    }

    //파라미터를 한꺼번에 Map으로 받을 수 있다.
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String,Object> paramMap){
        log.info("username={}, age={}",paramMap.get("username"),paramMap.get("age"));
        return "ok";
    }
}
