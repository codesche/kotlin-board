package com.dreamboard.domain.user.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * - 닉네임과 비밀번호 수정 가능
 * - 모든 필드는 선택적 (null 가능)
 */
data class UserUpdateRequest(

    /**
     * 수정할 닉네임
     * - 선택적 항목 (null 가능)
     * - 입력 시 2~20자 제한
     * - 한글, 영문, 숫자만 허용
     */
    @field:Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @field:Pattern(
        regexp = "^[가-힣a-zA-Z0-9]+\\$",
        message = "닉네임은 한글, 영문, 숫자만 사용 가능합니다."
    )
    val nickname: String? = null,

    /**
     * 수정할 비밀번호
     * - 선택적 항목 (null 가능)
     * - 입력 시 8~20자 제한
     * - 영문 대소문자, 숫자, 특수문자 조합 필수
     */
    @field:Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@\\\$!%*?&])[A-Za-z\\\\d@\\\$!%*?&]+\\\$",
        message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    val password: String? = null,

)