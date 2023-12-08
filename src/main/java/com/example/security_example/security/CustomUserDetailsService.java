package com.example.security_example.security;

import com.example.security_example.entity.Member;
import com.example.security_example.repository.AuthorityRepository;
import com.example.security_example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository, AuthorityRepository authorityRepository) {
        this.memberRepository = memberRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

        // 사용자 정보를 UserDetails로 변환하여 반환
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword()) // 비밀번호 해싱 등의 처리 필요
                .roles("USER") // 사용자의 롤(Role) 정보 설정
                .build();
    }

}
