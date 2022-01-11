package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberRespositoryTest {
    MemberRespository memberRespository = MemberRespository.getInstance();

    @AfterEach
    void afterEach(){
        memberRespository.clearStore();
    }

    @Test
    void getInstance() {
    }

    @Test
    void save() {
        //given
        Member member = new Member("hello",20);

        //when
        Member savedMember = memberRespository.save(member);

        //then
        Member findMember = memberRespository.findById(savedMember.getId());
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
        memberRespository.save(member);
        memberRespository.save(member2);

        //when
        List<Member> result = memberRespository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member,member2);
    }

    @Test
    void clearStore() {
    }
}