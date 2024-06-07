package com.demo.travellybe.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m.role FROM Member m WHERE m.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);

    void deleteByEmail(String email);

    @Query("SELECT m.nickname FROM Member m WHERE m.email = :email")
    Optional<String> findNicknameByEmail(String email);
}
