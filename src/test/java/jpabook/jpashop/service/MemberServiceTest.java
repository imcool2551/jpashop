package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);

        assertThat(memberRepository.findOne(savedId)).isEqualTo(member);
    }

    @Test
    void 중복_회원_예외() {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        assertThatThrownBy(() -> {
            memberService.join(member1);
            memberService.join(member2);
        }).isInstanceOf(IllegalStateException.class);
    }

}