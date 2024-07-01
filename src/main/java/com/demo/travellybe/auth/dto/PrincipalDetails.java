package com.demo.travellybe.auth.dto;

import com.demo.travellybe.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class PrincipalDetails implements UserDetails {
    private Member member;

    // Form 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    /**
     * UserDetails 인터페이스 메서드
     */
    // 해당 User 의 권한을 리턴(role)
    // SecurityFilterChain 에서 권한 체크할때 사용
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
