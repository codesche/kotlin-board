# 🚀 Kotlin Spring Boot 테스트 코드 훈련 프로젝트

## 📝 프로젝트 개요
Kotlin과 Spring Boot를 활용한 **게시판(Board) 시스템**으로, 테스트 코드 작성 역량 강화를 목표로 하는 실전 훈련 프로젝트입니다.

## 🎯 프로젝트 목표
- ✅ Kotlin 문법 및 개발 흐름 완벽 숙지
- ✅ JUnit 5 + MockK를 활용한 단위/통합 테스트 마스터
- ✅ Testcontainers를 통한 실제 환경과 유사한 테스트
- ✅ 실무 수준의 코드 품질 및 아키텍처 구현

## 🛠 기술 스택

### Backend
- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.4.0
- **Build Tool**: Gradle (Kotlin DSL)
- **Java Version**: 17

### Database
- **Development**: H2 (In-Memory)
- **Production**: MySQL 8.0
- **Cache**: Redis

### Security & Auth
- **Authentication**: JWT (JSON Web Token)
- **Security**: Spring Security
- **Token Storage**: Redis (Refresh Token)

### Testing
- **Unit Test**: JUnit 5, MockK
- **Integration Test**: Spring Boot Test
- **Container Test**: Testcontainers

### Documentation
- **API Docs**: Swagger (SpringDoc OpenAPI 3.0)

---

## 📂 프로젝트 구조
```
src/main/kotlin/com/example/board/
├── BoardApplication.kt
├── global/                         # 전역 설정 및 공통 컴포넌트
│   ├── config/                     # 설정 클래스
│   │   ├── RedisConfig.kt
│   │   ├── SecurityConfig.kt
│   │   ├── SwaggerConfig.kt
│   │   └── CorsConfig.kt
│   ├── common/                     # 공통 응답 및 유틸
│   │   ├── response/
│   │   │   ├── ApiResponse.kt
│   │   │   └── ErrorCode.kt
│   ├── exception/                  # 전역 예외 처리
│   │   ├── GlobalExceptionHandler.kt
│   │   └── BusinessException.kt
│   └── security/                   # 보안 관련
│       ├── jwt/
│       │   ├── JwtTokenProvider.kt
│       │   └── JwtAuthenticationFilter.kt
│       └── CustomUserDetails.kt
├── domain/                          # 도메인별 패키지
│   ├── user/                       # 사용자 도메인
│   │   ├── entity/User.kt
│   │   ├── repository/UserRepository.kt
│   │   ├── service/UserService.kt
│   │   ├── controller/UserController.kt
│   │   └── dto/
│   ├── board/                      # 게시판 도메인
│   │   ├── entity/Board.kt
│   │   ├── repository/BoardRepository.kt
│   │   ├── service/BoardService.kt
│   │   ├── controller/BoardController.kt
│   │   └── dto/
│   └── comment/                    # 댓글 도메인
│       ├── entity/Comment.kt
│       ├── repository/CommentRepository.kt
│       ├── service/CommentService.kt
│       ├── controller/CommentController.kt
│       └── dto/
└── infrastructure/                  # 인프라 계층
    └── BaseTimeEntity.kt
```

## ✅ 개발 가이드라인 (체크리스트)

### Architecture & Design
- [x] 전역 예외 처리 (@ControllerAdvice) + 도메인 예외
- [x] 공통 API 응답 객체
- [x] 비즈니스 로직은 서비스 계층에만
- [x] DTO 활용 (Entity는 매개변수로 사용 금지)
- [x] 효율적인 디자인 패턴 적용

### Security
- [ ] JWT 토큰 (Access + Refresh)
- [ ] Refresh Token은 Redis 저장
- [ ] @AuthenticationPrincipal 적용
- [ ] CustomUserDetails 구현
- [ ] 시큐어 코딩 수준 고려

### Database & JPA
- [x] BaseTimeEntity (Instant 사용)
- [ ] JPA N+1 문제 방지 (LAZY + fetch join)
- [ ] @Transactional (readOnly=true for 조회)
- [ ] Dirty Checking 활용
- [ ] Cascade 설정으로 연관 객체 삭제 처리

### Code Quality
- [x] @Getter, @Builder 활용 (@Setter 지양)
- [x] Record 미사용, DTO 클래스 작성
- [x] 일관된 코딩 스타일 (from/toEntity)
- [x] 가독성 좋은 코드 + 상세 주석
- [x] REST API 규칙 준수

### Performance
- [ ] 캐싱 도입 (Redis)
- [ ] 페이징 처리
- [ ] 인덱스 최적화
- [ ] 성능 프로파일링

### Testing
- [ ] JUnit 5 + MockK 단위 테스트
- [ ] @WebMvcTest, @DataJpaTest 슬라이스 테스트
- [ ] Testcontainers 통합 테스트
- [ ] 커버리지 80% 이상

### Monitoring & Operations
- [ ] 로깅 전략 수립
- [ ] 부하 테스트 환경 구성
- [ ] 메트릭 수집

## 🔧 설정 및 실행 방법

### 1. 사전 요구사항
```bash
- JDK 17 이상
- Gradle 8.x
- Docker (Redis, MySQL, Testcontainers)
```

### 2. 프로젝트 클론
```bash
git clone [repository-url]
cd board-service
```

### 3. Redis 실행 (Docker)
```bash
docker run -d -p 6379:6379 --name redis redis:latest
```

### 4. MySQL 실행 및 초기화 (Docker)
```bash
docker run -d \
  -p 3306:3306 \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=board_db \
  mysql:8.0

# schema.sql 실행
docker exec -i mysql mysql -uroot -ppassword board_db < schema.sql
```

### 5. 애플리케이션 실행

**Local 환경 (H2)**
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

**Production 환경 (MySQL)**
```bash
export JWT_SECRET=your-production-secret-key
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### 6. H2 Console 접속
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (공백)
```

### 7. Swagger UI 접속
```
http://localhost:8080/swagger-ui.html
```

## 테스트 실행

### 전체 테스트
```bash
./gradlew test
```

### 특정 테스트 클래스
```bash
./gradlew test --tests UserServiceTest
```

### 커버리지 리포트
```bash
./gradlew test jacocoTestReport
```

## 📊 진행 상황

### Phase 1: 프로젝트 초기 설정
- [x] build.gradle.kts 작성
- [x] application.yml 작성
- [x] MySQL schema.sql 작성
- [x] README.md 작성

### Phase 2: 공통 컴포넌트
- [ ] BaseTimeEntity
- [ ] 공통 응답 객체 (ApiResponse)
- [ ] 전역 예외 처리 (GlobalExceptionHandler)
- [ ] 커스텀 예외 클래스

### Phase 3: 보안 & 인증
- [ ] JWT 토큰 Provider
- [ ] JWT 인증 Filter
- [ ] CustomUserDetails
- [ ] SecurityConfig

### Phase 4: 도메인 구현
- [ ] User 도메인 (회원가입/로그인)
- [ ] Board 도메인 (게시글 CRUD)
- [ ] Comment 도메인 (댓글 CRUD)

### Phase 5: 테스트 코드
- [ ] Unit Test (Service Layer)
- [ ] Slice Test (Repository, Controller)
- [ ] Integration Test
- [ ] Testcontainers Test

### Phase 6: 성능 최적화
- [ ] Redis 캐싱
- [ ] 페이징 처리
- [ ] N+1 쿼리 최적화


## 트러블슈팅

### Redis 연결 실패
```bash
# Redis가 실행 중인지 확인
docker ps | grep redis

# Redis 재시작
docker restart redis
```

### MySQL 연결 실패
```bash
# MySQL 상태 확인
docker exec -it mysql mysql -uroot -ppassword -e "SELECT 1"
```

## 📚 참고 자료
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [MockK Documentation](https://mockk.io/)
- [Testcontainers Guide](https://www.testcontainers.org/)


