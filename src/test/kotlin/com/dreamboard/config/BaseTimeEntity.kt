package com.dreamboard.config

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

/**
 * 생성일시, 수정일시를 자동으로 관리하는 BaseEntity
 * 모든 엔티티는 이 클래스를 상속받아 시간 정보를 관리합니다.
 *
 * Instant를 사용하는 이유:
 * - LocalDateTime보다 확장성이 좋음 (타임존 독립적)
 * - UTC 기준으로 저장되어 글로벌 서비스에 유리
 * - 나노초 단위 정밀도 제공
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    /**
     * 생성 일시
     * - 엔티티가 처음 저장될 때 자동으로 설정됨
     * - 이후 변경 불가 (updatable = false)
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()

    /**
     * 수정 일시
     * - 엔티티가 수정될 때마다 자동으로 갱신됨
     */
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

}