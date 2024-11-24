package com.shareportfolio.antplanet.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    private String userid; // 사용자 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String email; // 이메일 주소

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios; // 회원의 포트폴리오 목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards; // 회원이 작성한 게시글 목록
}
