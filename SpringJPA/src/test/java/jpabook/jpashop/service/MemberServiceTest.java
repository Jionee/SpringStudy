package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class) //JPA까지 다 연결되는 것을 볼 것이므로 아래 세 개 어노테이션 모두 필요
@SpringBootTest //AutoWired하기 위해 필요
@Transactional //테스트가 끝나면 롤백함, 같은 transaction 안에서 pk값이 같으면 같은 것으로 증명 가능
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired //2. 눈으로 보고싶어
    EntityManager em;

    @Test
    //@Rollback(false) //1. @Transactional은 기본적으로 롤백을 하는데, 롤백안하고 눈으로 다 보고싶어 /But 테스트는 반복해서 해야하기 때문에 롤백되어야 함 -> 2
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("kim");

        //when
        em.flush(); //2. 눈으로 보고싶어
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUsername("kim");
        Member member2 = new Member();
        member2.setUsername("kim");

        //when
        memberService.join(member1);

        //then
        //fail("예외가 발생해야 한다."); //위에서 예외가 터져서 여기 도달하지 말아야 함
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());

    }
}