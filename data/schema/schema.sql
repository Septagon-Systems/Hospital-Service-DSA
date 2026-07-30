-- Ghana Smart Hospital Operations Optimizer Database Schema
-- This is subject to change once we get osm data


CREATE TABLE IF NOT EXISTS hospitals (
    hospital_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    region TEXT NOT NULL,
    district TEXT NOT NULL,
    town TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    ownership_type TEXT CHECK(ownership_type IN ('Government', 'Private', 'CHAG', 'Quasi-Govt')),
    facility_type TEXT NOT NULL,
    icu_capacity INTEGER DEFAULT 0,
    ambulance_count INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS routes (
    route_id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_hospital_id INTEGER NOT NULL,
    destination_hospital_id INTEGER NOT NULL,
    distance_km REAL NOT NULL,
    avg_travel_time_mins REAL NOT NULL,
    road_condition TEXT CHECK(road_condition IN ('Paved', 'Unpaved', 'Under Maintenance')),
    FOREIGN KEY(source_hospital_id) REFERENCES hospitals(hospital_id),
    FOREIGN KEY(destination_hospital_id) REFERENCES hospitals(hospital_id)
);

CREATE TABLE IF NOT EXISTS service_requests (
    request_id INTEGER PRIMARY KEY AUTOINCREMENT,
    origin_hospital_id INTEGER NOT NULL,
    target_hospital_id INTEGER NOT NULL,
    patient_triage_level INTEGER CHECK(patient_triage_level IN (1, 2, 3)), -- 1: Critical, 2: Urgent, 3: Normal
    request_type TEXT CHECK(request_type IN ('Ambulance Transfer', 'Blood Packet', 'ICU Bed Request', 'Emergency Meds')),
    weight_kg REAL DEFAULT 1.0,
    timestamp_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'PENDING',
    FOREIGN KEY(origin_hospital_id) REFERENCES hospitals(hospital_id),
    FOREIGN KEY(target_hospital_id) REFERENCES hospitals(hospital_id)
);

CREATE TABLE IF NOT EXISTS medical_resources (
    resource_id INTEGER PRIMARY KEY AUTOINCREMENT,
    hospital_id INTEGER NOT NULL,
    resource_name TEXT NOT NULL,
    quantity_available INTEGER NOT NULL,
    resource_category TEXT NOT NULL,
    FOREIGN KEY(hospital_id) REFERENCES hospitals(hospital_id)
);

-- Performance Benchmark Experiment Results (Brief 8.v Requirement)
CREATE TABLE IF NOT EXISTS algorithm_runs (
    run_id INTEGER PRIMARY KEY AUTOINCREMENT,
    algorithm_name TEXT NOT NULL,
    input_size INTEGER NOT NULL,
    execution_time_ns LONG NOT NULL,
    memory_used_kb LONG NOT NULL,
    iteration_number INTEGER NOT NULL,
    run_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);
