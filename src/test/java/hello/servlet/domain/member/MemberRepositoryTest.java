package hello.servlet.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest {
    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void getInstance() {
    }

    @Test
    void save() {
        //given
        Member member = new Member("hello",20);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
        //given
        Member member = new Member("member1",20);
        Member member2 = new Member("member2",30);
        memberRepository.save(member);
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member,member2);
    }

    @Test
    void clearStore() {
    }
}