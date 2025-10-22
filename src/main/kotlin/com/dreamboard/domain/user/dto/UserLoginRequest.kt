package com.dreamboard.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * 로그인 요청 DTO
 * - 이메일과 비밀번호로 사용자 인증 수행
 */
data class UserLoginRequest(

    /**
     * 사용자 이메일 주소
     * - 필수 입력 항목
     * - 이메일 형식 검증
     */
    @field:NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String,

    /**
     * 사용자 비밀번호
     * - 필수 입력 항목
     */
    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    val password: String
)