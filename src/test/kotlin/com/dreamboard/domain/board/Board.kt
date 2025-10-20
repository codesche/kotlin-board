package com.dreamboard.domain.board

import com.dreamboard.config.BaseTimeEntity
import com.dreamboard.domain.comment.Comment
import com.dreamboard.domain.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

/**
 * 게시글 엔티티
 *
 * 주요 기능:
 * - 게시글 CRUD
 * - 조회수 관리
 * - 댓글과의 연관관계 관리
 */
@Entity
@Table(
    name = "boards",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at"),
        Index(name = "idx_title", columnList = "title")
    ]
)
class Board(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val id: Long? = null,

    // 게시글 제목 - 검색을 위한 인덱스 적용
    @Column(nullable = false, length = 200)
    var title: String,

    // 게시글 내용 - TEXT 타입으로 긴 내용 저장 가능
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    // 조회수 - 기본값 0, 게시글 조회 시마다 증가
    @Column(nullable = false)
    var viewCount: Long = 0L,

    /**
     * 작성자 (User와 N:1 관계)
     * - LAZY 로딩으로 성능 최적화
     * - ForeignKey user_id 컬럼 생성
     * - 작성자 삭제 시 게시글도 함께 삭제 (ON DELETE CASCADE)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.CONSTRAINT))
    val author: User,

    /**
     * 댓글 목록
     * - 양방향 연관관계 (Comment 1:N)
     * - LAZY 로딩으로 N+1 문제 방지
     * - 게시글 삭제 시 댓글도 함께 삭제
     */
    @OneToMany(
        mappedBy = "board",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val comments: MutableList<Comment> = mutableListOf()

) : BaseTimeEntity() {

    /**
     * 게시글 수정 메서드 (Dirty Checking 반영)
     */
    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }

    /**
     * 조회수 증가 메서드
     * - 게시글 조회 시 호출
     * - 동시성 문제는 별도 처리 필요 (낙관적 락 등)
     */
    fun increaseViewCount() {
        this.viewCount++;
    }

    companion object {
        /**
         * 정적 팩토리 메서드
         * - 엔터리 생성 로직 명확히 표현
         */
        fun create(
            title: String,
            content: String,
            author: User
        ): Board {
            return Board(
                title = title,
                content = content,
                author = author
            )
        }
    }

}