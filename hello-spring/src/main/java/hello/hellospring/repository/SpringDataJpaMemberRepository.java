package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    //JPQL select m from Member m where m.name = ?
    @Override
    Optional<Member> findByName(String name);

    //다른 이름으로 찾고 싶으면 method 이름을 findByEmai(String email) 이런식으로 작성하기만 하면 된다.
}
