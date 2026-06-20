-- ===== IKE v4.0.0 Base Schema V1 =====
-- Tạo bảng gốc cho tenant, center, user, role, student.
-- Mọi bảng có 3 metadata fields (created_at, updated_at, deleted_at) theo đặc tả Chương 3.
-- Row-Level Security (RLS) cho tenant isolation.

-- ===== TENANTS & CENTERS =====
CREATE TABLE tenants (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL
);

CREATE TABLE centers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL,
    UNIQUE (tenant_id, code)
);

-- ===== USERS (business profile, mirrors Keycloak subject) =====
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    keycloak_subject VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL
);

-- ===== ROLES (business role enum mirror) =====
CREATE TABLE roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL
);

INSERT INTO roles (code, description) VALUES
    ('IKE_ADMIN', 'Quan tri he thong'),
    ('CENTER_MANAGER', 'Quan ly trung tam'),
    ('TEACHER', 'Giao vien'),
    ('FINANCE_OFFICER', 'Can bo tai chinh'),
    ('STUDENT', 'Hoc vien');

-- ===== STUDENTS (FK root cho Finance context Phase 2) =====
CREATE TABLE students (
    user_id BIGINT NOT NULL PRIMARY KEY REFERENCES users(id),
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    center_id BIGINT NOT NULL REFERENCES centers(id),
    student_code VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL,
    UNIQUE (tenant_id, student_code)
);

-- ===== INDEXES =====
CREATE INDEX idx_centers_tenant ON centers(tenant_id);
CREATE INDEX idx_users_tenant ON users(tenant_id);
CREATE INDEX idx_students_tenant ON students(tenant_id);

-- ===== ROW-LEVEL SECURITY: tenant isolation =====
-- Policy: app role chỉ thấy row khớp session var app.tenant_id
ALTER TABLE centers ENABLE ROW LEVEL SECURITY;
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE students ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_centers ON centers
    USING (tenant_id = current_setting('app.tenant_id', true)::bigint);
CREATE POLICY tenant_isolation_users ON users
    USING (tenant_id = current_setting('app.tenant_id', true)::bigint);
CREATE POLICY tenant_isolation_students ON students
    USING (tenant_id = current_setting('app.tenant_id', true)::bigint);
