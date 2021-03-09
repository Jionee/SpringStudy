package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id); //findById 같은 동작을 했을 때 null 예외처리해줌
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
