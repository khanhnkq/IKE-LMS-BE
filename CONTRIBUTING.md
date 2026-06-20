# Contributing

## Setup
```bash
# 1. Khởi động hạ tầng
docker compose up -d

# 2. Build toàn bộ
./gradlew build

# 3. Chạy dev
./gradlew :app:bootRun --args='--spring.profiles.active=dev'
```

## Trước khi commit
- `./gradlew detekt` pass (static analysis)
- `./gradlew test` pass (all tests)
- Mọi **entity mới** kế thừa `BaseEntity` (3 metadata fields + soft-delete)
- Mọi **bảng mới** có `tenant_id` + RLS policy
- Tiền tệ dùng `BigDecimal`, không `double`/`float`
- Lock thứ tự: ClassStudent → StudentWallet (tránh deadlock)

## Cấu trúc module
```
platform/   → shared (BaseEntity, TenantContext, AuditDeltaBuilder)
finance/    → Finance BC (Phase 2)
academic/   → Academic BC (Phase 2)
notification/ → Notification BC (Phase 5)
audit/      → Audit BC
app/        → entry point @SpringBootApplication
```

## Code style
- Kotlin, Spring Boot 3.3
- Gradle Kotlin DSL
- JUnit 5 + Testcontainers
- ktlint (detekt) enforcement
