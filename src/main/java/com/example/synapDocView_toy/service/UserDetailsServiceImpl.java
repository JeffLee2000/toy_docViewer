package com.example.synapDocView_toy.service;

import com.example.synapDocView_toy.entity.Member;
import com.example.synapDocView_toy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String m_id) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(m_id);
        if(member == null){
            throw new UsernameNotFoundException("해당하는 계정이 없습니다.");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(member.getMemberId())
                .password(member.getMemberPw())
                .roles("USER")
                .build();
    }
}
