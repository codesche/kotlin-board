package com.dreamboard.domain.user

import com.dreamboard.common.UserRole
import com.dreamboard.config.BaseTimeEntity
import com.dreamboard.domain.board.Board
import com.dreamboard.domain.comment.Comment
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

/**
 * 사용자 엔티티
 *
 * 주요 기능:
 * - 회원가입/로그인 기본 정보 관리
 * - 게시글 및 댓글 작성자 정보
 * - JWT 인증을 위한 사용자 정보 제공
 */
@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_email", columnList = "email"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long? = null,

    // 이메일 (로그인 ID로 사용), 인덱스 적용으로 조회 성능 최적화
    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    // 비밀번호 (BCrypt 암호화 저장, 평문 저장 금지), 최소 255자 확보 (BCrypt는 60자 확보)
    var password: String,

    // 닉네임 (게시글/댓글에 표시)
    @Column(nullable = false, length = 50)
    var nickname: String,

    // 사용자 권한 (USER: 사용자, ADMIN: 관리자)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val role: UserRole = UserRole.USER,

    /**
     * 작성한 게시글 목록
     * - 양방향 연관관계 (Board와 1:N)
     * - LAZY 로딩
     * - orphanRemoval로 고아 객체 자동 삭제
     * - CascadeType.REMOVE로 사용자 삭제 시 게시글도 삭제
     */
    @OneToMany(
        mappedBy = "author",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val boards: MutableList<Board> = mutableListOf(),

    /**
     * 작성한 댓글 목록
     * - 양방향 연관관계 (Comment와 1:N)
     * - LAZY 로딩으로 N+1 문제 방지
     */
    @OneToMany(
        mappedBy = "author",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val comments: MutableList<Comment> = mutableListOf()

) : BaseTimeEntity() {

    /**
     * 닉네임 변경 메서드 (Dirty Checking 반영)
     */
    fun updateNickname(newNickname: String) {
        this.nickname = newNickname
    }

    companion object {
        /**
         * 정적 팩토리 메서드
         * - 엔티티 생성 로직을 명확하게 표현
         */
        fun create(
            email: String,
            password: String,
            nickname: String,
            role: UserRole = UserRole.USER
        ): User {
            return User(
                email = email,
                password = password,
                nickname = nickname,
                role = role
            )
        }
    }
}