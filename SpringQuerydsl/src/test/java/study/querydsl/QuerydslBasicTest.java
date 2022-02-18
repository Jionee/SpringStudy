package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory; //필드에 작성해도 됨. 동시성 문제가 발생하지 않음 (em자체가 멀티스레드에 대응되도록 설계해놓음)

    @BeforeEach
    public void before(){ //각 테스트 실행 전 데이터 생성해주기
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);
        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        //member1을 찾아라
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQueryDsl(){
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search(){
        Member findMember = queryFactory
                .selectFrom(member) //하나로 합칠 수 있음
                .where(member.username.eq("member1")
                        .and(member.age.eq(10))) //and걸 수 있음(chain)
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member) //하나로 합칠 수 있음
                .where(member.username.eq("member1") ,
                        (member.age.eq(10))) //and걸 수 있음 (쉼표로 and 걸 수 있음) -> 중간에 null이 들어가면 무시하기 때문에 동적쿼리 생성 시 좋음
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();
//        Member fetchOne = queryFactory
//                .selectFrom(QMember.member)
//                .fetchOne();
//        Member fetchFirst = queryFactory
//                .selectFrom(QMember.member)
//                .fetchFirst();
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults(); //데이터가 복잡해지게 되면 성능때문에 count쿼리를 따로 만드는 경우가 있다. 이런 경우는 COunt쿼리를 따로 날려야 한다

        results.getTotal();
        List<Member> content = results.getResults();
        results.getLimit(); // limit 몇 번째까지 가져와 -> 페이징에서 사용
        results.getOffset(); //offset 몇 번째까지 가져와

        long total = queryFactory
                .selectFrom(member)
                .fetchCount(); //count쿼리만 나간다.
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     * */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1(){
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc()) //페이징에는 정렬 필수
                .offset(1) //몇번째부터 끊어서 앞에 몇 개를 스킵할거야, 0부터 시작 , 1이라고 하면 하나를 스킵한다는 뜻임
                .limit(2) //갯수
                .fetchResults(); //실무에서 카운트 쿼리를 따로 분리해서 작성해야할 경우에는 fetchResults로 사용하지 않고 카운트 쿼리를 따로 작성해야 한다.
                                //where제약조건 등이 복잡하게 붙어 있는데 카운트에서는 제약 조건을 줄일 수 있는 경우가 있기 때문

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);

       // assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
                .select(
                        member.count(), //이렇게 하나씩 찍게 되면 querydsl 튜플로 리턴된다. -> 데이터 타입이 여러개 나올 경우 -> 실무에서는 dto 로 뽑아오는 방식을 사용한다.
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();
        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     * */
    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name) //group-by
                //.having()도 사용 가능함
                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    public void join(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) //뒤 별칭은 Qteam을 말하는 것임 , innerJoin, leftJoin 등 사용 가능
                .where(team.name.eq("teamA"))
                .fetch();
        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");
    }

    /**
     * 세타조인 - 연관관계가 없는 것들 조인하기
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team) //여러 엔티티를 ,로 가져와서 세타조인
                .where(member.username.eq(team.name))
                .fetch(); //모든 멤버를 가져오고 모든 팀을 가져와서 조인함

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 TeamA인 팀만 조인, 회원은 모두 조회
     * JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering(){
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA")) //id 외에 이름으로도 조건이 걸림
                //.join(member.team,team)
                //.where(team.name.eq("teamA")) //inner 조인일 때는 on절과 where는 결과가 같음 (leftjoin일 때 말고)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 것들 외부 조인하기
     * 회원의 이름이 팀 이름과 같은 회원을 외부 조인
     */
    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
                .select(member,team)
                .from(member, team) //여러 엔티티를 ,로 가져와서 세타조인
                .leftJoin(team).on(member.username.eq(team.name)) //막조인할 때는 leftJoin에 조인 대상 테이블명만 넣으면 됨
                //.leftJoin(member.team,team)으로 하면 연관있는 것들 id를 비교하는 것임
                .fetch(); //모든 멤버를 가져오고 모든 팀을 가져와서 조인함

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }
    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin() //fetchJoin -> 연관된 엔티티를 한번에 조회하는 기능
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery(){
        QMember memberSub = new QMember("memberSub"); //alis가 겹치는 경우는 새로 생성해줘야 함
        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max()) //-> 40이라는 값이 나옴
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result).extracting("age").containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원 조회
     */
    @Test
    public void subQueryGoe(){
        QMember memberSub = new QMember("memberSub"); //alis가 겹치는 경우는 새로 생성해줘야 함
        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result).extracting("age").containsExactly(30,40);
    }

    /**
     * in
     */
    @Test
    public void subQueryIn(){
        QMember memberSub = new QMember("memberSub"); //alis가 겹치는 경우는 새로 생성해줘야 함
        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                        .where(memberSub.age.gt(10))
                ))
                .fetch();
        assertThat(result).extracting("age").containsExactly(20,30,40);
    }

    @Test
    public void selectSubQuery(){
        QMember memberSub = new QMember("memberSub"); //alis가 겹치는 경우는 새로 생성해줘야 함
        List<Tuple> result = queryFactory
                .select(member.username,
                        select(memberSub.age.avg()).from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void basicCase(){
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase(){
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant(){
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A")) //결과에서 상수 나옴
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void concat(){
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue())) //타입이 다르면 string으로 캐스팅 해줘야 함
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

    }
}