package com.example.synapDocView_toy.repository;


import com.example.synapDocView_toy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByMemberId(String m_id);
}
