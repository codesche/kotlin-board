package com.dreamboard.domain.board.repository

import com.dreamboard.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface BoardRepository : JpaRepository<Board, Long> {

    /**
     * 게시글 ID로 조회 (작성자 정보 포함)
     * - 게시글 상세 조회 시 사용
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.author WHERE b.id = :id")
    fun findByIdWithAuthor(@Param("id") id: Long): Optional<Board>

    /**
     * 게시글 ID로 조회 (작성자 + 댓글 포함)
     * - 게시글 상세 + 댓글 목록 조회 시 사용
     */
    @Query("SELECT DISTINCT b FROM Board b " +
                "JOIN FETCH b.author " +
                "LEFT JOIN FETCH b.comments c " +
                "LEFT JOIN FETCH c.author " +
                "WHERE b.id = :id")
    fun findByIdWithAuthorAndComments(@Param("id") id: Long): Optional<Board>

    /**
     * 모든 게시글 페이징 조회 (작성자 정보 포함)
     * - CountQuery 분리로 성능 최적화
     * - 최신 글 우선 정렬 (createdAt DESC)
     */
    @Query(
        value = "SELECT DISTINCT b FROM Board b JOIN FETCH b.author",
        countQuery = "SELECT COUNT(b) FROM Board b"
    )
    fun findAllWithAuthor(pageable: Pageable): Page<Board>

    /**
     * 작성자 ID로 게시글 목록 조회 (페이징)
     * - 특정 사용자가 작성한 게시글 목록
     * - 최신 글 우선 정렬
     */
    @Query(
        value = "SELECT b FROM Board b JOIN FETCH b.author WHERE b.author.id = :authorId",
        countQuery = "SELECT COUNT(b) FROM Board b WHERE b.author.id = :authorId"
    )
    fun findByAuthorId(@Param("authorId") authorId: Long, pageable: Pageable): Page<Board>

    /**
     * 제목으로 게시글 검색 (페이징)
     * - LIKE 검색으로 부분 일치 조회
     * - 대소문자 구분 없이 검색
     */
    @Query(
        value = "SELECT DISTINCT b FROM Board b JOIN FETCH b.author " +
                "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))",
        countQuery = "SELECT COUNT(b) FROM Board b " +
                      "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))"
    )
    fun findByTitleContainingIgnoreCase(
            @Param("title") title: String,
            pageable: Pageable
        ): Page<Board>

    /**
     * 조회수 증가
     * - Bulk 연산으로 성능 최적화
     * - 영속성 컨텍스트를 거치지 않고 직접 DB 업데이트
     * - @Modifying과 함께 사용
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    fun incrementViewCount(@Param("id") id: Long): Int

    /**
     * 조회수 TOP N 게시글 조회
     * - 인기 게시글 조회용
     * - 작성자 정보 포함
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.author ORDER BY b.viewCount DESC")
    fun findTopByViewCount(pageable: Pageable): List<Board>

    /**
     * 작성자 ID로 게시글 개수 조회
     * - 사용자 프로필에서 작성 게시글 수 표시용
     */
    fun countByAuthorId(authorId: Long): Long
}