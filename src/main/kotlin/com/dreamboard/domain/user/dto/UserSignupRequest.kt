package com.dreamboard.domain.user.dto

import com.dreamboard.common.UserRole
import com.dreamboard.domain.user.entity.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * 회원가입 요청 DTO
 *
 * 사용자 계정 생성 시 필요한 정보를 담고 있는 요청 객체
 * - 이메일, 비밀번호, 닉네임, 역할 정보 포함
 * - 각 필드에 대한 유효성 검증 어노테이션 적용
 */
data class UserSignupRequest(

    /**
     * 사용자 이메일 주소
     * - 필수 입력 항목
     * - 이메일 형식 검증
     * - 최대 100자
     */
    @field:NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @field:Size(max = 100, message = "이메일은 100자 이하로 입력해주세요.")
    val email: String,

    /**
     * 사용자 비밀번호
     * - 필수 입력 항목
     * - 8~20자 제한
     * - 영문 대소문자, 숫자, 특수문자 조합 필수
     */
    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @field:Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@\\\$!%*?&])[A-Za-z\\\\d@\\\$!%*?&]+\\\$",
        message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    val password: String,

    /**
     * 사용자 닉네임
     * - 필수 입력 항목
     * - 2~20자 제목
     * - 한글, 영문, 숫자만 허용
     */
    @field:NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @field:Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @field:Pattern(
        regexp = "^[가-힣a-zA-Z0-9]+\\\$",
        message = "닉네임은 한글, 영문, 숫자만 사용 가능합니다."
    )
    val nickname: String,

    /**
     * 사용자 역할
     * - 기본값: USER
     * - ADMIN, USER 중 선택
     */
    val role: UserRole = UserRole.USER
) {

    /**
     * Request DTO를 User Entity로 변환
     * Kotlin은 생성자에 named arguments를 사용하는 것이 가장 관례적
     */
    fun toEntity(encodedPassword: String): User {
        return User(
            email = email,
            password = encodedPassword,
            nickname = nickname,
            role = role
        )
    }
}