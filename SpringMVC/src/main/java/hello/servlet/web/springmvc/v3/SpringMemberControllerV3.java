package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    //@RequestMapping(value = "/new-form", method = RequestMethod.GET) // GET일때만 호출됨
    @GetMapping("/new-form")
    public String newForm(){
        //그냥 문자를 반환해도 view이름으로 알고 알아서 진행해줌
        return "new-form"; //자동으로 viewResolver가 view 반환해줌
    }

    //@RequestMapping(value = "/save", method = RequestMethod.POST)
    @PostMapping("/save")
    public String save(@RequestParam("username") String username, //httprequest이런거 안받아도 됨(애노테이션 강점)
                             @RequestParam("age") int age,
                             Model model){
        //비즈니스 로직
        Member member = new Member(username,age);
        memberRepository.save(member);

        //모델에 담기
        model.addAttribute("member",member);
        return "save-result";
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    protected String members(Model model){
        //비즈니스 로직
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members",members);
        return "members";
    }
}
//test