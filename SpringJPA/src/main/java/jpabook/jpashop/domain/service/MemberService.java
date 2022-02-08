package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //public 메소드들은 기본적으로 트랜잭셔널해진다. (spring이 제공하는 transactional 어노테이션 사용하기)
@RequiredArgsConstructor //생성자가 하나만 있는 경우는 스프링이 알아서 injection해줌 (final이 있는 필드만 인젝션)
public class MemberService {
    //1. 필드주입방식 -> 테스트 할 때 바꿔야할 때가 있는데 엑세스할 수 없음
//    @Autowired
    private final MemberRepository memberRepository;

    //2. setter주입방식 -> 테스트할 때 mock을 직접 주입 가능 / But 런타임에 누군가가 이거를 바꿀 수 있음(원래는 부팅시에 조립이 끝나고 그 후에는 바꿀 일이 없음)
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //3. 생성자주입방식 -> 직접 주입하기 때문에 놓치지 않고 주입 가능 //RequiredArgsConstructor -> 이걸 안써도 됨
//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    //회원 가입
    @Transactional //쓰기에서는 readOnly면 안됨
    public Long join(Member member){
        //중복회원 검증 로직
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    //@Transactional(readOnly = true) //읽기전용에서는 더 최적화됨
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 조회
    //@Transactional(readOnly = true) //읽기전용에서는 더 최적화됨
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
