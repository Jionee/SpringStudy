package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        //param 받아오기
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));
        //저장 비즈니스 로직
        Member member = new Member(username,age);
        memberRepository.save(member);

        model.put("member",member); //모델은 put으로만, 따로 전달X
        return "save-result";
    }
}
