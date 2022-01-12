package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public ModelView process(Map<String, String> paramMap) {
        //param 받아오기
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));
        //저장 비즈니스 로직
        Member member = new Member(username,age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result"); //뷰의 논리적 이름 리턴
        mv.getModel().put("member",member);
        return mv;
    }
}
