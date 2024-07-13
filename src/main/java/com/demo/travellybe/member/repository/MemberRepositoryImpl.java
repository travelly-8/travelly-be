package com.demo.travellybe.member.repository;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.demo.travellybe.member.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .where(member.nickname.eq(nickname))
                        .fetchOne()
        );
    }

    @Override
    public void deleteByEmail(String email) {
        queryFactory
                .delete(member)
                .where(member.email.eq(email))
                .execute();
    }

    @Override
    public Optional<Role> findRoleByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(member.role)
                        .from(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<String> findNicknameByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(member.nickname)
                        .from(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        );
    }
}
