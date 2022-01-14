package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogTestController {
    //private final Logger log = LoggerFactory.getLogger(getClass());
    //@Slf4j를 넣으면 자동으로 이게 넣어진다.

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";
        System.out.println("name = " + name);
        //log.info("name = "+ name); //사용할 수 있지만 사용하지 말자! (+연산이 일어나서 쓸모없는 리소스가 사용되는 것임)

        //log의 레벨을 정할 수 있다.
        log.trace("trace log = {}", name);
        log.debug("debug log = {}", name);
        log.info("info log = {}",name);
        log.warn("warn log = {}",name);
        log.error("error log = {}",name);
        return "ok";
    }
}