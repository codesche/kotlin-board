package com.dreamboard.domain.comment.repository

import com.dreamboard.domain.comment.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface CommentRepository : JpaRepository<Comment, Long> {

    /**
     * 댓글 ID로 조회 (작성자 정보 포함)
     * - 댓글 수정/삭제 시 사용
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.id = :id")
    fun findByIdWithAuthor(@Param("id") id: Long): Optional<Comment>

    /**
     * 게시글 ID로 댓글 목록 조회 (작성자 정보 포함)
     * - 게시글 상세 페이지에서 댓글 목록 표시용
     * - 최신 댓글 우선 정렬
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author " +
            "WHERE c.board.id = :boardId ORDER BY c.createdAt ASC")
    fun findByBoardIdWithAuthor(@Param("boardId") boardId: Long): List<Comment>

    /**
     * 게시글 ID로 댓글 목록 조회 (페이징)
     * - 댓글이 많은 게시글에서 페이징 처리
     */
    @Query(
        value = "SELECT c FROM Comment c JOIN FETCH c.author WHERE c.board.id = :boardId",
        countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.board.id = :boardId"
    )
    fun findByBoardIdWithAuthor(
        @Param("boardId") boardId: Long,
        pageable: Pageable
    ): Page<Comment>

    /**
     * 작성자 ID로 댓글 목록 조회 (페이징)
     * - 사용자가 작성한 댓글 목록 조회
     * - 최신 댓글 우선 정렬
     */
    @Query(
        value = "SELECT c FROM Comment c JOIN FETCH c.author JOIN FETCH c.board WHERE c.author.id = :authorId",
        countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.author.id = :authorId"
    ) fun findByAuthorId(
            @Param("authorId") authorId: Long,
            pageable: Pageable
        ): Page<Comment>

    /**
     * 게시글 ID로 댓글 개수 조회
     * - 게시글 목록에서 댓글 수 표시용
     */
    fun countByBoardId(boardId: Long): Long

    /**
     * 작성자 ID로 댓글 개수 조회
     * - 사용자 프로필에서 작성 댓글 수 표시용
     */
    fun countByAuthorId(authorId: Long): Long

    /**
     * 게시글 ID 목록으로 댓글 일괄 조회
     * - 게시글 목록에서 각 게시글의 댓글 수를 표시할 때 사용
     * - IN 절을 사용하여 한 번의 쿼리로 조회
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.board.id IN :boardIds")
    fun findByBoardIdIn(@Param("boardIds") boardIds: List<Long>): List<Comment>

    /**
     * 게시글 ID와 작성자 ID로 댓글 존재 여부 확인
     * - 특정 사용자가 특정 게시글에 댓글을 작성했는지 확인
     * - 존재하면 true, 없으면 false
     */
    fun existsByBoardIdAndAuthorId(boardId: Long, authorId: Long): Boolean

}