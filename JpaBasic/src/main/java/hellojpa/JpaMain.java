package hellojpa;

import hellojpa.relation.Member;
import hellojpa.relation.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code
        try{
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            //member.setTeamId(team.getId());
            member.setTeam(team);
            em.persist(member);

            //조회
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            //양방향
            List<Member> members = findTeam.getMembers();

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {

            em.close(); //WAS가 내려갈 때 닫아줘야 한
        }


        emf.close();
    }
}
