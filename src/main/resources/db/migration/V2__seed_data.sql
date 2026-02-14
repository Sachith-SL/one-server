-- =====================================================
-- V2: Seed basic data
-- =====================================================

-- ── Departments ─────────────────────────────────────
INSERT INTO t_department (name) VALUES ('Engineering');
INSERT INTO t_department (name) VALUES ('Human Resources');
INSERT INTO t_department (name) VALUES ('Marketing');
INSERT INTO t_department (name) VALUES ('Finance');

-- ── Employees ───────────────────────────────────────
--   department_id:  1=Engineering, 2=HR, 3=Marketing, 4=Finance
INSERT INTO t_employee (name, salary, department_id) VALUES ('Sachith Perera',  85000.00, 1);
INSERT INTO t_employee (name, salary, department_id) VALUES ('Kamal Silva',     72000.00, 1);
INSERT INTO t_employee (name, salary, department_id) VALUES ('Nimal Fernando',  65000.00, 2);
INSERT INTO t_employee (name, salary, department_id) VALUES ('Amara Jayasinghe',78000.00, 3);
INSERT INTO t_employee (name, salary, department_id) VALUES ('Sunil Rathnayake',90000.00, 4);
