# IKE v4.0.0 Backend Architecture

## Tổng quan
Modular monolith Spring Boot 3 + Kotlin với Spring Modulith.
4 bounded context modules + 1 platform shared.

## Modules
- `platform` — shared trung tâm: BaseEntity, TenantContext, AuditDeltaBuilder, JPA Config
- `finance` — StudentWallet Aggregate Root (Phase 2+)
- `academic` — Class Aggregate Root (Phase 2+)
- `notification` — NotificationQueue Aggregate Root (Phase 5+)
- `audit` — AuditLog Aggregate Root (Phase 2+)
- `app` — entry point Application (boot assembly)

## Bounded Context map (đặc tả Chương 2)
```
Finance BC      Academic BC       Notification BC    Audit BC
(StudentWallet) (Class)           (NotificationQueue) (AuditLog)
  └──WalletTxn     └──Session          └──Channel        └──JSON Delta
  └──TuitionTxn    └──Grade            └──Retry/Backoff   └──Partition
                   └──Attendance
                   └──Homework
```

## Quy ước bắt buộc
1. **Mọi entity kế thừa `BaseEntity`** — đảm bảo `created_at`, `updated_at`, `deleted_at` (soft delete).
2. **Mọi bảng nghiệp vụ có `tenant_id`** + RLS policy Postgres (task P0.9).
3. **Tiền tệ = `BigDecimal`** — tuyệt đối không dùng `double`/`float`.
4. **Pessimistic locking** (`PESSIMISTIC_WRITE`) khi chạm số dư ví / `amount_paid`.
5. **Outbox trước broker** — domain event chèn vào `outbox_messages` trong cùng DB transaction.

## Hạ tầng dev
```bash
docker compose up -d
```
| Service   | Port  | Credentials                  |
|-----------|-------|------------------------------|
| PostgreSQL| 5432  | ike / ike_dev_pass           |
| Redis     | 6379  | —                            |
| RabbitMQ  | 5672/15672 | ike / ike_dev_pass       |
| Keycloak  | 8180  | admin / admin, realm: `ike`  |

## Build
```bash
./gradlew build    # compile + test + detekt
./gradlew :app:bootRun --args='--spring.profiles.active=dev'
```

## Tài liệu liên quan
- [Roadmap tổng thể](../docs/roadmap/2026-06-20-IKE-v4-deployment-plan.md)
- [ADR quyết định kiến trúc](docs/adr/)

## IKE v4.0.0 Spec alignment
- ✅ Chương 1: JSON Delta audit, Wallet tập trung, Notification queue → Phase 0 foundation ready
- ✅ Chương 3: BaseEntity 3 metadata fields + soft-delete → BaseEntity.kt
- ✅ Chương 4.1: CQRS → Quy ước read-path (Phase 3)
- ✅ Multi-tenant: TenantContext + RLS
- 🔜 Chương 2 Bounded Contexts: Phase 1/2
- 🔜 Chương 4.2 Transactional Outbox: Phase 4
- 🔜 Chương 5 Finance service: Phase 2
