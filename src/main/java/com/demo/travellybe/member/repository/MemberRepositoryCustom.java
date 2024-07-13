package com.demo.travellybe.member.repository;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.Role;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Role> findRoleByEmail(String email);

    void deleteByEmail(String email);

    Optional<String> findNicknameByEmail(String email);
}
