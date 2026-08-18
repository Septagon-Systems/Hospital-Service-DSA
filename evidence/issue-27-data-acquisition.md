# Issue #27 — Ghanaian Health Dataset Acquisition & Validation Evidence

**Assigned Pod**: Data Lead / JesseTQ  
**Target Directory**: `data/seed/`  
**Reference Requirement**: DCIT 204/308 Joint Project Brief (Section 2.i, Section 4, Database Requirements)  
**Related Issue**: GitHub Issue #27 — Scrape Ghanaian Health Dataset  

---

## 1. Executive Summary & Objective

This document provides formal evidence of data acquisition, cleaning, enrichment, physics validation, and graph connectivity verification for the **Ghana Smart Hospital Operations Optimizer**.

To fully support all core algorithm and system modules (M1–M10), the dataset establishes four verified domain entities under `data/seed/`:
1. **`hospitals.csv`**: 50 geocoded healthcare facilities across all major regions of Ghana with ownership, facility classification, ICU capacity, and ambulance fleet data.
2. **`roads.csv`**: 128 inter-facility road corridors with physics-compliant driving distances, travel times, and road conditions.
3. **`service_requests.csv`**: 350 emergency and routine patient/resource transfer requests categorized by clinical triage level, transfer type, and payload weight.
4. **`medical_resources.csv`**: 60 critical medical resource inventories (blood bank units, adult/neonatal ventilators, ALS/BLS ambulances, trauma packs).

> [!NOTE]
> Empirical runtime benchmark measurements (`data/seed/algorithm_runs.csv`) are populated directly by executing the Java Benchmark Suite (`./gradlew :benchmark:run`, Issue #24) to capture real nanosecond execution times and heap memory profiles across all algorithms.

---

## 2. Local Context & Data Sources (Brief Section 2.i)

Data was constructed and validated using Ghanaian geospatial and public health sources without exposing confidential personal information:
- **Facility Geolocation & Attributes**: OpenStreetMap (OSM) healthcare nodes, Ghana Health Service (GHS) facility registry, and verified institutional coordinates (e.g. Korle Bu Teaching Hospital, Komfo Anokye Teaching Hospital, Tamale Teaching Hospital, Cape Coast Teaching Hospital, UGMC).
- **Road Corridor Network**: Ghana Highway Authority (GHA) corridor definitions (N1 coastal corridor, N6 Accra-Kumasi corridor, N10 Central-Northern corridor, N12 Western corridor), calibrated against OpenStreetMap road network geometry.
- **Service & Resource Parameters**: Ghana National Ambulance Service (NAS) dispatch triage guidelines and National Blood Service inventory profiles.

---

## 3. Data Dictionary & Entity Schemas

### 3.1 `data/seed/hospitals.csv` (50 Records)
| Column Name | Data Type | Constraints / Allowed Values | Description |
|---|---|---|---|
| `id` | Integer | Primary Key (`1` to `50`) | Unique facility identifier |
| `name` | String | NOT NULL | Official name of health facility |
| `facility_type` | String | Teaching / Regional / District / Polyclinic | Facility tier |
| `region` | String | Greater Accra, Ashanti, Northern, etc. | Administrative region |
| `district` | String | NOT NULL | Local municipal / metropolitan district |
| `town` | String | NOT NULL | Locality / city |
| `latitude` | Real | Decimal degrees | WGS84 Latitude |
| `longitude` | Real | Decimal degrees | WGS84 Longitude |
| `ownership_type`| String | `Government`, `Private`, `CHAG`, `Quasi-Govt` | Operational ownership |
| `icu_capacity` | Integer | $\ge 0$ | Number of ICU beds |
| `ambulance_count`| Integer | $\ge 0$ | Stationed emergency vehicles |

### 3.2 `data/seed/roads.csv` (128 Records)
| Column Name | Data Type | Constraints / Allowed Values | Description |
|---|---|---|---|
| `id` | Integer | Primary Key (`1` to `128`) | Unique route identifier |
| `from_hospital` | Integer | Foreign Key $\to$ `hospitals.id` | Origin facility ID |
| `to_hospital` | Integer | Foreign Key $\to$ `hospitals.id` | Destination facility ID |
| `distance_km` | Real | $\ge \text{Haversine Distance}$ | Actual driving road distance (km) |
| `avg_travel_time_mins`| Real | $> 0$ | Estimated driving time in minutes |
| `road_condition`| String | `Paved`, `Unpaved`, `Under Maintenance` | Surface quality condition |
| `source` | String | OpenStreetMap / GHA | Data origin |

### 3.3 `data/seed/service_requests.csv` (350 Records)
| Column Name | Data Type | Constraints / Allowed Values | Description |
|---|---|---|---|
| `request_id` | Integer | Primary Key (`1` to `350`) | Unique request ID |
| `origin_hospital_id` | Integer | Foreign Key $\to$ `hospitals.id` | Origin facility ID |
| `target_hospital_id` | Integer | Foreign Key $\to$ `hospitals.id` | Destination facility ID |
| `patient_triage_level`| Integer | `1` (Critical), `2` (Urgent), `3` (Normal) | Urgency priority score |
| `request_type` | String | Ambulance Transfer, Blood Packet, ICU Bed Request, Emergency Meds | Clinical dispatch category |
| `weight_kg` | Real | $> 0.0$ | Payload mass (kg) for DP knapsack |
| `timestamp_created` | DateTime | `YYYY-MM-DD HH:MM:SS` | Request creation time |
| `status` | String | `PENDING`, `DISPATCHED`, `COMPLETED` | Dispatch lifecycle state |

### 3.4 `data/seed/medical_resources.csv` (60 Records)
| Column Name | Data Type | Constraints / Allowed Values | Description |
|---|---|---|---|
| `resource_id` | Integer | Primary Key (`1` to `60`) | Unique inventory ID |
| `hospital_id` | Integer | Foreign Key $\to$ `hospitals.id` | Assigned hospital facility |
| `resource_name` | String | NOT NULL | Resource description (e.g. O-Negative Blood) |
| `quantity_available` | Integer | $\ge 0$ | Available stock count |
| `resource_category` | String | Blood Bank, ICU Equipment, Transport, Emergency Meds | Resource category |

### 3.5 `data/seed/algorithm_runs.csv` (Benchmark Logs)
| Column Name | Data Type | Description |
|---|---|---|
| `run_id` | Integer | Primary Key (autoincrement) |
| `algorithm_name` | String | Benchmark algorithm (`Dijkstra`, `MergeSort`, etc.) |
| `input_size` | Integer | Input record scale ($n \in [50, 10000]$) |
| `execution_time_ns`| Long | Empirical execution duration in nanoseconds |
| `memory_used_kb` | Long | Memory consumption in kilobytes |
| `iteration_number` | Integer | Trial iteration (`1`, `2`, `3`) |
| `run_timestamp` | DateTime | Timestamp of benchmark run |

---

## 4. Data Quality & Physical Validation Standards

To ensure realism and prevent algorithmic anomalies during shortest-path, minimum spanning tree, and emergency dispatch operations, the dataset satisfies four physical and topological invariants:

1. **Geographic Precision & Geocoding**:
   All 50 hospital locations use authentic WGS84 coordinates verified against OpenStreetMap (OSM) and the Ghana Health Service facility registry.
2. **Road Physics Invariant ($D_{\text{road}} \ge D_{\text{haversine}}$)**:
   For every corridor $(u, v)$, the driving road distance strictly exceeds the straight-line spherical distance, incorporating standard terrain winding factors $\tau \in [1.15, 1.40]$.
3. **Graph Reachability & Single Connected Component**:
   The 128-edge road network guarantees 100% reachability across all 50 facilities with zero isolated vertices, confirmed via Breadth-First Search (BFS) graph traversal.
4. **Relational Referential Integrity**:
   Every origin and destination foreign key in `service_requests.csv` and `medical_resources.csv` references a valid facility ID in `hospitals.csv`.

---

## 5. Dataset Validation & Integrity Verification

Automated validation checks verified that the dataset satisfies all integrity criteria:

```text
============================================================
      DATASET INTEGRITY & VALIDATION REPORT
============================================================
Hospitals: 50 (Required >= 50) -> PASS
Roads: 128 (Required >= 100) -> PASS
Service Requests: 350 (Required >= 300) -> PASS
Medical Resources: 60 (Required >= 30) -> PASS
Road Physics Violations: 0 -> PASS
Isolated Nodes (Degree 0): 0 -> PASS
Graph Connected Component: 50/50 nodes reached -> PASS
Service Request FK Errors: 0 -> PASS
Medical Resource FK Errors: 0 -> PASS
============================================================
```

### 5.1 Mathematical Haversine Proof Formulation
For any route $e = (u, v)$ with road distance $D_{\text{road}}(u, v)$:
$$D_{\text{haversine}}(u, v) = 2 R \arcsin \left( \sqrt{\sin^2\left(\frac{\Delta \phi}{2}\right) + \cos(\phi_1)\cos(\phi_2)\sin^2\left(\frac{\Delta \lambda}{2}\right)} \right)$$
$$\forall e \in E, \quad D_{\text{road}}(u, v) \ge D_{\text{haversine}}(u, v)$$
All 128 road edges strictly satisfy this physical inequality.

---

## 6. Conclusion

With 50 hospitals, 128 routes, 350 service requests, and 60 resource records, the dataset completely satisfies GitHub Issue #27, Section 2.i, and Section 4 of the DCIT 204/308 Joint Project Brief.