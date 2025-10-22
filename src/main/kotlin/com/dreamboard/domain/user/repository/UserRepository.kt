package com.dreamboard.domain.user.repository

import com.dreamboard.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 조회
     * - 로그인 시 사용
     * - 인덱스 적용되어 빠른 조회 가능
     */
    fun findByEmail(email: String): Optional<User>

    /**
     * 이메일 존재 여부 확인
     * - 회원가입 시 중복 검사에 사용
     * - exists 쿼리는 count 쿼리보다 성능이 좋음
     */
    fun existsByEmail(email: String): Boolean

    /**
     * 닉네임으로 사용자 목록 조회 (검색 기능)
     * - LIKE 검색으로 부분 일치 조회
     * - 대소문자 구분 없이 검색
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))")
    fun findByNicknameContainingIgnoreCase(@Param("nickname") nickname: String): List<User>

    /**
     * 이메일로 사용자 조회 (작성한 게시글 포함)
     * - fetch join을 사용하여 N+1 문제 해결
     * - 사용자와 게시글을 한 번의 쿼리로 조회
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.boards WHERE u.email = :email")
    fun findByEmailWithBoards(@Param("email") email: String): Optional<User>

    /**
     * 이메일로 사용자 조회 (작성한 댓글 포함)
     * - 사용자와 댓글을 한 번의 쿼리로 조회
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.comments WHERE u.email = :email")
    fun findByEmailWithComments(@Param("email") email: String): Optional<User>

}