
CREATE DATABASE IF NOT EXISTS campusfix_db;
USE campusfix_db;

-- 1. Departments table
CREATE TABLE IF NOT EXISTS departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- 2. Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'STAFF', 'ADMIN') NOT NULL,
    department_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_department
        FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
);

-- 3. Complaints table
CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_code VARCHAR(20) UNIQUE,
    student_id INT NOT NULL,

    category ENUM(
        'IT', 'ELECTRICAL', 'PLUMBING',
        'FURNITURE', 'CLEANING',
        'INFRASTRUCTURE', 'OTHER'
    ) NOT NULL,

    location VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,

    priority ENUM(
        'LOW', 'MEDIUM', 'HIGH', 'CRITICAL'
    ) NOT NULL DEFAULT 'MEDIUM',

    status ENUM(
        'PENDING', 'ASSIGNED', 'IN_PROGRESS',
        'RESOLVED', 'CLOSED'
    ) NOT NULL DEFAULT 'PENDING',

    assigned_staff_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL DEFAULT NULL,

    CONSTRAINT fk_complaints_student
        FOREIGN KEY (student_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_complaints_staff
        FOREIGN KEY (assigned_staff_id)
        REFERENCES users(user_id)
);

-- 4. Feedback table
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL UNIQUE,
    rating INT NOT NULL,
    comments TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_feedback_rating
        CHECK (rating BETWEEN 1 AND 5),

    CONSTRAINT fk_feedback_complaint
        FOREIGN KEY (complaint_id)
        REFERENCES complaints(complaint_id)
);

-- 5. Complaint history table
CREATE TABLE IF NOT EXISTS complaint_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,

    old_status ENUM(
        'PENDING', 'ASSIGNED', 'IN_PROGRESS',
        'RESOLVED', 'CLOSED'
    ) NULL,

    new_status ENUM(
        'PENDING', 'ASSIGNED', 'IN_PROGRESS',
        'RESOLVED', 'CLOSED'
    ) NOT NULL,

    changed_by INT NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,

    CONSTRAINT fk_history_complaint
        FOREIGN KEY (complaint_id)
        REFERENCES complaints(complaint_id),

    CONSTRAINT fk_history_user
        FOREIGN KEY (changed_by)
        REFERENCES users(user_id)
);

INSERT IGNORE INTO departments (department_name, description)
VALUES
('IT Support', 'Computer and network maintenance'),
('Electrical', 'Electrical maintenance'),
('Civil Maintenance', 'Building and infrastructure'),
('Housekeeping', 'Cleaning and housekeeping');
('Mechanical', 'Mechanical department');
('ICT', 'Institute of Computer Science & Technology'),
('MBA', 'Master of Business Administration'),
('Marine', 'Marine Engineering'),
('UVPCE', 'UV Patel College of Engineering');

-- Verify database and tables
SHOW TABLES;
USE campusfix_db;

SELECT department_id, department_name, description
FROM departments
ORDER BY department_id;

SELECT user_id, name, username, role, department_id
FROM users
ORDER BY user_id;

SELECT * FROM feedback ORDER BY submitted_at DESC;


SELECT complaint_code, status, assigned_staff_id
FROM complaints
ORDER BY complaint_id DESC;

SELECT *
FROM complaint_history
ORDER BY changed_at DESC;

SELECT *
FROM feedback
ORDER BY submitted_at DESC;
