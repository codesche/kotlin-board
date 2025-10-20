package com.dreamboard.common

import com.fasterxml.jackson.annotation.JsonValue

enum class UserRole(@JsonValue val rolName: String) {

    USER("user"),
    ADMIN("admin");

}