package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor //알아서 autowired되는것
//스프링 컨테이너가 생성될 때 자동 주입을 통해 MyLogger를 넣게 되면 request가 오지도 않았는데 달라고 해서 오류를 낸다.
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    //private final ObjectProvider<MyLogger> myLoggerObjectProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        //MyLogger myLogger = myLoggerObjectProvider.getObject();
        System.out.println("myLogger = "+myLogger.getClass()); //-> 내가 만든 myLogger가 아니라 스프링이 만들어 둔 애이다. proxy, CGLIB

        myLogger.setRequestURL(requestURL); //호출하는 시점에 진짜가 동작한다.

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
