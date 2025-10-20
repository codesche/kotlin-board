package com.dreamboard.domain.comment

import com.dreamboard.config.BaseTimeEntity
import com.dreamboard.domain.board.Board
import com.dreamboard.domain.user.User
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
import jakarta.persistence.Table
import kotlin.math.PI

/**
 * 댓글 엔티티
 *
 * 주요 기능:
 * - 게시글에 대한 댓글 작성
 * - 작성자 정보 관리
 * - 게시글과의 연관관계 관리
 */
@Entity
@Table(
    name = "comments",
    indexes = [
        Index(name = "idx_board_id", columnList = "board_id"),
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
class Comment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long? = null,

    /**
     * 댓글 내용
     * - 최대 500자 제한
     */
    @Column(nullable = false, length = 500)
    var content: String,

    /**
     * 게시글 (Board와 N:1 관계)
     * - LAZY 로딩으로 성능 최적화
     * - 게시글 삭제 시 댓글도 함께 삭제 (ON DELETE CASCADE)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.CONSTRAINT))
    val board: Board,

    /**
     * 작성자 (User와 N:1 관계)
     * - LAZY 로딩으로 성능 최적화
     * - 작성자 삭제 시 댓글도 함께 삭제 (ON DELETE CASCADE)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.CONSTRAINT))
    val author: User

) : BaseTimeEntity() {

    /**
     * 댓글 수정 메서드 (Dirty Checking 반영)
     */
    fun update(content: String) {
        this.content = content;
    }

    companion object {
        /**
         * 정적 팩토리 메서드 적용
         */
        fun create(
            content: String,
            board: Board,
            author: User
        ): Comment {
            return Comment(
                content = content,
                board = board,
                author = author
            )
        }
    }

}