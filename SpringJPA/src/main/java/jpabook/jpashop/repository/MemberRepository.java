package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //component스캔에 의해 자동으로 spring bean으로 관리
@RequiredArgsConstructor
public class MemberRepository {
    //@PersistenceContext //JPA를 사용하므로 JPA표준 어노테이션 사용
    private final EntityManager em;//spring이 entityMAnager 만들어서 자동으로 주입해줌

    public Long save(Member member){
        em.persist(member); //db에 insert쿼리
        return member.getId();
    }

    public Member findOne(Long id){
        return em.find(Member.class, id); //단건 조회 (타입,pk)
    }

    //SQL은 테이블을 대상으로 쿼리를 하지만, JPQL은 엔티티(객체)를 대상으로 쿼리를 한다.
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class) //createQuery(쿼리,반환타입)
            .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.username = :name", Member.class)
                .setParameter("name",name) //파라미터바인딩
                .getResultList();
    }


}