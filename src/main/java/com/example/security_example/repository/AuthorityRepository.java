package com.example.security_example.repository;

import com.example.security_example.entity.Authority;
import com.example.security_example.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query("select a.name from Authority a where a.member = :member")
    String findAllByMember(@Param("member") Member member);
}