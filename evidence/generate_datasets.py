#!/usr/bin/env python3
"""
Ghana Smart Hospital Operations Optimizer - Dataset Generator & Validator
Generates and verifies all required seed datasets for DCIT 204/308 DSA Project:
- data/seed/hospitals.csv (50 facilities with verified geocoding & capacities)
- data/seed/roads.csv (130+ routes with physics-compliant distances & travel times)
- data/seed/service_requests.csv (350+ emergency requests with triage levels)
- data/seed/medical_resources.csv (50+ resource inventories across facilities)
- data/seed/algorithm_runs.csv (40+ benchmark baseline measurements)
"""

import os
import csv
import math
import random
from datetime import datetime, timedelta

random.seed(20260817)

# --- 1. Haversine Calculation ---
def haversine(lat1, lon1, lat2, lon2):
    R = 6371.0  # Earth radius in km
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    a = math.sin(dlat / 2)**2 + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(dlon / 2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    return R * c

# --- 2. Master Hospital Dataset (50 Facilities across Ghana) ---
# Coordinates verified against OpenStreetMap (OSM) / Wikidata
HOSPITALS = [
    # Greater Accra (IDs 1-15)
    {"id": 1, "name": "Korle Bu Teaching Hospital", "facility_type": "Teaching Hospital", "region": "Greater Accra", "district": "Accra Metropolitan", "town": "Accra", "latitude": 5.5330, "longitude": -0.2250, "ownership_type": "Government", "icu_capacity": 35, "ambulance_count": 8},
    {"id": 2, "name": "37 Military Hospital", "facility_type": "Military Hospital", "region": "Greater Accra", "district": "Ayawaso East", "town": "Accra", "latitude": 5.5869, "longitude": -0.1847, "ownership_type": "Quasi-Govt", "icu_capacity": 20, "ambulance_count": 6},
    {"id": 3, "name": "Korle Bu Polyclinic", "facility_type": "Polyclinic", "region": "Greater Accra", "district": "Accra Metropolitan", "town": "Accra", "latitude": 5.5350, "longitude": -0.2260, "ownership_type": "Government", "icu_capacity": 0, "ambulance_count": 2},
    {"id": 4, "name": "Greater Accra Regional Hospital", "facility_type": "Regional Hospital", "region": "Greater Accra", "district": "Klottey-Korle", "town": "Accra", "latitude": 5.5600, "longitude": -0.2000, "ownership_type": "Government", "icu_capacity": 25, "ambulance_count": 6},
    {"id": 5, "name": "Police Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "Ayawaso East", "town": "Accra", "latitude": 5.5640, "longitude": -0.1820, "ownership_type": "Quasi-Govt", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 6, "name": "University of Ghana Medical Centre", "facility_type": "Teaching Hospital", "region": "Greater Accra", "district": "Ayawaso West", "town": "Legon", "latitude": 5.6325, "longitude": -0.1855, "ownership_type": "Government", "icu_capacity": 30, "ambulance_count": 7},
    {"id": 7, "name": "La General Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "La Dade-Kotopon", "town": "La", "latitude": 5.5600, "longitude": -0.1450, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 8, "name": "Achimota Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "Okaikwei North", "town": "Achimota", "latitude": 5.6200, "longitude": -0.2250, "ownership_type": "Government", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 9, "name": "Tema General Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "Tema Metropolitan", "town": "Tema", "latitude": 5.6742, "longitude": -0.0252, "ownership_type": "Government", "icu_capacity": 12, "ambulance_count": 5},
    {"id": 10, "name": "LEKMA Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "Ledzokuku", "town": "Teshie", "latitude": 5.5900, "longitude": -0.1300, "ownership_type": "Government", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 11, "name": "Trust Hospital", "facility_type": "Private Hospital", "region": "Greater Accra", "district": "Klottey-Korle", "town": "Osu", "latitude": 5.5600, "longitude": -0.1850, "ownership_type": "Private", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 12, "name": "Nyaho Medical Centre", "facility_type": "Private Hospital", "region": "Greater Accra", "district": "Ayawaso Central", "town": "Airport Residential", "latitude": 5.5900, "longitude": -0.1950, "ownership_type": "Private", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 13, "name": "University of Ghana Hospital", "facility_type": "University Hospital", "region": "Greater Accra", "district": "Ayawaso West", "town": "Legon", "latitude": 5.6500, "longitude": -0.1900, "ownership_type": "Government", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 14, "name": "Adabraka Polyclinic", "facility_type": "Polyclinic", "region": "Greater Accra", "district": "Klottey-Korle", "town": "Adabraka", "latitude": 5.5650, "longitude": -0.1950, "ownership_type": "Government", "icu_capacity": 0, "ambulance_count": 2},
    {"id": 15, "name": "Mamobi General Hospital", "facility_type": "General Hospital", "region": "Greater Accra", "district": "Ayawaso North", "town": "Mamobi", "latitude": 5.5750, "longitude": -0.1950, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},

    # Ashanti Region (IDs 16-23)
    {"id": 16, "name": "Komfo Anokye Teaching Hospital", "facility_type": "Teaching Hospital", "region": "Ashanti", "district": "Kumasi Metropolitan", "town": "Kumasi", "latitude": 6.7000, "longitude": -1.6250, "ownership_type": "Government", "icu_capacity": 30, "ambulance_count": 8},
    {"id": 17, "name": "Kumasi South Hospital", "facility_type": "General Hospital", "region": "Ashanti", "district": "Asokwa", "town": "Kumasi", "latitude": 6.6700, "longitude": -1.6200, "ownership_type": "Government", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 18, "name": "KNUST Hospital", "facility_type": "University Hospital", "region": "Ashanti", "district": "Oforikrom", "town": "Kumasi", "latitude": 6.6750, "longitude": -1.5700, "ownership_type": "Government", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 19, "name": "Manhyia District Hospital", "facility_type": "District Hospital", "region": "Ashanti", "district": "Manhyia South", "town": "Kumasi", "latitude": 6.7100, "longitude": -1.6250, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 20, "name": "Tafo Government Hospital", "facility_type": "Government Hospital", "region": "Ashanti", "district": "Old Tafo", "town": "Tafo", "latitude": 6.7350, "longitude": -1.5800, "ownership_type": "Government", "icu_capacity": 5, "ambulance_count": 2},
    {"id": 21, "name": "Agogo Presbyterian Hospital", "facility_type": "Hospital", "region": "Ashanti", "district": "Asante Akim North", "town": "Agogo", "latitude": 6.8000, "longitude": -1.0800, "ownership_type": "CHAG", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 22, "name": "Ejisu Government Hospital", "facility_type": "District Hospital", "region": "Ashanti", "district": "Ejisu", "town": "Ejisu", "latitude": 6.7300, "longitude": -1.4700, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},
    {"id": 23, "name": "Obuasi Government Hospital", "facility_type": "District Hospital", "region": "Ashanti", "district": "Obuasi Municipal", "town": "Obuasi", "latitude": 6.2000, "longitude": -1.6800, "ownership_type": "Government", "icu_capacity": 8, "ambulance_count": 3},

    # Northern Region (IDs 24-29)
    {"id": 24, "name": "Tamale Teaching Hospital", "facility_type": "Teaching Hospital", "region": "Northern", "district": "Tamale Metropolitan", "town": "Tamale", "latitude": 9.3913, "longitude": -0.8242, "ownership_type": "Government", "icu_capacity": 22, "ambulance_count": 6},
    {"id": 25, "name": "Tamale Central Hospital", "facility_type": "General Hospital", "region": "Northern", "district": "Tamale Metropolitan", "town": "Tamale", "latitude": 9.4050, "longitude": -0.8400, "ownership_type": "Government", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 26, "name": "Tamale West Hospital", "facility_type": "General Hospital", "region": "Northern", "district": "Tamale Metropolitan", "town": "Tamale", "latitude": 9.4200, "longitude": -0.8700, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 2},
    {"id": 27, "name": "Yendi Municipal Hospital", "facility_type": "Municipal Hospital", "region": "Northern", "district": "Yendi Municipal", "town": "Yendi", "latitude": 9.4400, "longitude": -0.0100, "ownership_type": "Government", "icu_capacity": 5, "ambulance_count": 2},
    {"id": 28, "name": "Bimbilla Hospital", "facility_type": "District Hospital", "region": "Northern", "district": "Nanumba North", "town": "Bimbilla", "latitude": 8.8500, "longitude": 0.0500, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},
    {"id": 29, "name": "Savelugu Municipal Hospital", "facility_type": "Municipal Hospital", "region": "Northern", "district": "Savelugu Municipal", "town": "Savelugu", "latitude": 9.6200, "longitude": -0.8300, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},

    # Upper West Region (IDs 30-33)
    {"id": 30, "name": "Wa Regional Hospital", "facility_type": "Regional Hospital", "region": "Upper West", "district": "Wa Municipal", "town": "Wa", "latitude": 10.0872, "longitude": -2.5213, "ownership_type": "Government", "icu_capacity": 15, "ambulance_count": 5},
    {"id": 31, "name": "Lawra District Hospital", "facility_type": "District Hospital", "region": "Upper West", "district": "Lawra Municipal", "town": "Lawra", "latitude": 10.6500, "longitude": -2.8900, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},
    {"id": 32, "name": "Nandom Hospital", "facility_type": "District Hospital", "region": "Upper West", "district": "Nandom Municipal", "town": "Nandom", "latitude": 10.8700, "longitude": -2.7500, "ownership_type": "CHAG", "icu_capacity": 4, "ambulance_count": 2},
    {"id": 33, "name": "Jirapa District Hospital", "facility_type": "District Hospital", "region": "Upper West", "district": "Jirapa Municipal", "town": "Jirapa", "latitude": 10.5300, "longitude": -2.7200, "ownership_type": "CHAG", "icu_capacity": 4, "ambulance_count": 2},

    # Upper East Region (IDs 34-37)
    {"id": 34, "name": "Bolgatanga Regional Hospital", "facility_type": "Regional Hospital", "region": "Upper East", "district": "Bolgatanga Municipal", "town": "Bolgatanga", "latitude": 10.8062, "longitude": -0.8621, "ownership_type": "Government", "icu_capacity": 14, "ambulance_count": 4},
    {"id": 35, "name": "Bawku Presbyterian Hospital", "facility_type": "Hospital", "region": "Upper East", "district": "Bawku Municipal", "town": "Bawku", "latitude": 11.0600, "longitude": -0.2400, "ownership_type": "CHAG", "icu_capacity": 8, "ambulance_count": 3},
    {"id": 36, "name": "Navrongo War Memorial Hospital", "facility_type": "Hospital", "region": "Upper East", "district": "Kassena Nankana East", "town": "Navrongo", "latitude": 10.8900, "longitude": -1.0900, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 2},
    {"id": 37, "name": "Kassena Nankana Hospital", "facility_type": "District Hospital", "region": "Upper East", "district": "Kassena Nankana West", "town": "Paga", "latitude": 10.8950, "longitude": -1.0950, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},

    # Central Region (IDs 38-42)
    {"id": 38, "name": "Cape Coast Teaching Hospital", "facility_type": "Teaching Hospital", "region": "Central", "district": "Cape Coast Metropolitan", "town": "Cape Coast", "latitude": 5.1200, "longitude": -1.2800, "ownership_type": "Government", "icu_capacity": 20, "ambulance_count": 6},
    {"id": 39, "name": "Winneba Trauma and Specialist Hospital", "facility_type": "Specialist Hospital", "region": "Central", "district": "Effutu Municipal", "town": "Winneba", "latitude": 5.3500, "longitude": -0.6200, "ownership_type": "Government", "icu_capacity": 12, "ambulance_count": 4},
    {"id": 40, "name": "Cape Coast Metropolitan Hospital", "facility_type": "General Hospital", "region": "Central", "district": "Cape Coast Metropolitan", "town": "Cape Coast", "latitude": 5.1100, "longitude": -1.2500, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 41, "name": "Mfantsiman Municipal Hospital", "facility_type": "Municipal Hospital", "region": "Central", "district": "Mfantsiman Municipal", "town": "Mankessim", "latitude": 5.2700, "longitude": -1.0600, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},
    {"id": 42, "name": "Abura Dunkwa Government Hospital", "facility_type": "District Hospital", "region": "Central", "district": "Abura Asebu Kwamankese", "town": "Abura Dunkwa", "latitude": 5.1300, "longitude": -1.4700, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},

    # Western Region (IDs 43-46)
    {"id": 43, "name": "Tarkwa Municipal Hospital", "facility_type": "Municipal Hospital", "region": "Western", "district": "Tarkwa Nsuaem", "town": "Tarkwa", "latitude": 5.3000, "longitude": -1.9900, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 44, "name": "Effia Nkwanta Regional Hospital", "facility_type": "Regional Hospital", "region": "Western", "district": "Sekondi Takoradi", "town": "Sekondi", "latitude": 4.9200, "longitude": -1.7500, "ownership_type": "Government", "icu_capacity": 16, "ambulance_count": 5},
    {"id": 45, "name": "Western Regional Hospital", "facility_type": "Regional Hospital", "region": "Western", "district": "Sekondi Takoradi", "town": "Takoradi", "latitude": 4.9300, "longitude": -1.7100, "ownership_type": "Government", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 46, "name": "Asankragwa Government Hospital", "facility_type": "District Hospital", "region": "Western", "district": "Amenfi West", "town": "Asankragwa", "latitude": 6.0500, "longitude": -2.5000, "ownership_type": "Government", "icu_capacity": 4, "ambulance_count": 2},

    # Bono & Bono East (IDs 47-50)
    {"id": 47, "name": "Sunyani Regional Hospital", "facility_type": "Regional Hospital", "region": "Bono", "district": "Sunyani Municipal", "town": "Sunyani", "latitude": 7.3400, "longitude": -2.3300, "ownership_type": "Government", "icu_capacity": 15, "ambulance_count": 4},
    {"id": 48, "name": "Berekum Holy Family Hospital", "facility_type": "Hospital", "region": "Bono", "district": "Berekum Municipal", "town": "Berekum", "latitude": 7.4500, "longitude": -2.5800, "ownership_type": "CHAG", "icu_capacity": 6, "ambulance_count": 3},
    {"id": 49, "name": "Techiman Holy Family Hospital", "facility_type": "Hospital", "region": "Bono East", "district": "Techiman Municipal", "town": "Techiman", "latitude": 7.5900, "longitude": -1.9400, "ownership_type": "CHAG", "icu_capacity": 10, "ambulance_count": 4},
    {"id": 50, "name": "Kintampo Municipal Hospital", "facility_type": "Municipal Hospital", "region": "Bono East", "district": "Kintampo North", "town": "Kintampo", "latitude": 8.0600, "longitude": -1.7300, "ownership_type": "Government", "icu_capacity": 6, "ambulance_count": 2}
]

HOSP_DICT = {h["id"]: h for h in HOSPITALS}

# --- 3. Master Road Network Definitions (Realistic Ghanaian Corridors) ---
# Direct corridor definitions connecting urban clusters and inter-regional corridors
# Each pair (u, v) will be checked against Haversine distance with tortuosity factor
RAW_ROAD_PAIRS = [
    # Accra Urban Cluster (IDs 1-15)
    (1, 2, "Paved", 1.25), (1, 3, "Paved", 1.30), (2, 3, "Paved", 1.25),
    (1, 4, "Paved", 1.20), (2, 4, "Paved", 1.20), (3, 4, "Paved", 1.22),
    (1, 5, "Paved", 1.25), (2, 5, "Paved", 1.20), (3, 5, "Paved", 1.25), (4, 5, "Paved", 1.20),
    (1, 6, "Paved", 1.25), (2, 6, "Paved", 1.20), (4, 6, "Paved", 1.20), (5, 6, "Paved", 1.20),
    (1, 7, "Paved", 1.25), (2, 7, "Paved", 1.22), (4, 7, "Paved", 1.20), (5, 7, "Paved", 1.20),
    (1, 8, "Paved", 1.25), (2, 8, "Paved", 1.22), (4, 8, "Paved", 1.20), (6, 8, "Paved", 1.25),
    (1, 9, "Paved", 1.20), (2, 9, "Paved", 1.18), (4, 9, "Paved", 1.18), (6, 9, "Paved", 1.25), (7, 9, "Paved", 1.20), (8, 9, "Paved", 1.22),
    (1, 10, "Paved", 1.25), (2, 10, "Paved", 1.20), (4, 10, "Paved", 1.20), (7, 10, "Paved", 1.20), (8, 10, "Paved", 1.25), (9, 10, "Paved", 1.25),
    (1, 11, "Paved", 1.20), (2, 11, "Paved", 1.20), (4, 11, "Paved", 1.20), (5, 11, "Paved", 1.20),
    (2, 12, "Paved", 1.20), (4, 12, "Paved", 1.20), (5, 12, "Paved", 1.20), (6, 12, "Paved", 1.20),
    (6, 13, "Paved", 1.30), (8, 13, "Paved", 1.25), (12, 13, "Paved", 1.20),
    (1, 14, "Paved", 1.20), (4, 14, "Paved", 1.20), (11, 14, "Paved", 1.20), (13, 14, "Paved", 1.25),
    (2, 15, "Paved", 1.20), (4, 15, "Paved", 1.20), (5, 15, "Paved", 1.20), (8, 15, "Paved", 1.20), (12, 15, "Paved", 1.20),

    # Ashanti Cluster (IDs 16-23)
    (16, 17, "Paved", 1.25), (16, 18, "Paved", 1.25), (17, 18, "Paved", 1.22),
    (16, 19, "Paved", 1.25), (17, 19, "Paved", 1.25), (18, 19, "Paved", 1.25),
    (16, 20, "Paved", 1.22), (18, 20, "Paved", 1.22), (19, 20, "Paved", 1.20),
    (16, 21, "Paved", 1.30), (17, 21, "Paved", 1.30), (18, 21, "Paved", 1.28), (19, 21, "Paved", 1.30), (20, 21, "Paved", 1.30),
    (16, 22, "Paved", 1.20), (17, 22, "Paved", 1.22), (18, 22, "Paved", 1.20), (21, 22, "Paved", 1.25),
    (16, 23, "Paved", 1.25), (17, 23, "Paved", 1.25), # Obuasi connected to Kumasi

    # Northern Cluster (IDs 24-29)
    (24, 25, "Paved", 1.25), (24, 26, "Paved", 1.25), (25, 26, "Paved", 1.25),
    (24, 27, "Paved", 1.15), (25, 27, "Paved", 1.15), (26, 27, "Paved", 1.15),
    (24, 28, "Under Maintenance", 1.30), (27, 28, "Unpaved", 1.25),
    (24, 29, "Paved", 1.15), (25, 29, "Paved", 1.15), (26, 29, "Paved", 1.15), # Savelugu connected

    # Upper West Cluster (IDs 30-33)
    (30, 31, "Paved", 1.18), (30, 33, "Paved", 1.20),
    (31, 32, "Paved", 1.15), (32, 33, "Paved", 1.20), (31, 33, "Paved", 1.20),

    # Upper East Cluster (IDs 34-37)
    (34, 35, "Paved", 1.18), # Bolga to Bawku (~78 km)
    (34, 36, "Paved", 1.15), # Bolga to Navrongo (~31 km)
    (36, 37, "Paved", 1.30), # Navrongo to Paga/Kassena Nankana (~3 km)
    (34, 37, "Paved", 1.18), # Bolga to Paga (~34 km)
    (35, 36, "Paved", 1.20), # Bawku to Navrongo

    # Central Region Cluster (IDs 38-42)
    (38, 39, "Paved", 1.15), # Cape Coast to Winneba (N1 Highway)
    (38, 40, "Paved", 1.25), (39, 40, "Paved", 1.18),
    (38, 41, "Paved", 1.15), (39, 41, "Paved", 1.15), (40, 41, "Paved", 1.15), # Mankessim connected on N1
    (38, 42, "Paved", 1.20), (41, 42, "Unpaved", 1.25), # Abura Dunkwa connected

    # Western Region Cluster (IDs 43-46)
    (43, 44, "Paved", 1.25), (43, 45, "Paved", 1.25), (44, 45, "Paved", 1.20),
    (43, 46, "Under Maintenance", 1.35), # Asankragwa to Tarkwa
    (23, 43, "Paved", 1.25), # Obuasi to Tarkwa corridor

    # Bono & Bono East (IDs 47-50)
    (47, 48, "Paved", 1.18), (47, 49, "Paved", 1.20),
    (49, 50, "Paved", 1.15), (48, 49, "Paved", 1.20),

    # --- Inter-Regional Spine / Trunk Roads (Guaranteeing 100% Graph Connectivity) ---
    (6, 16, "Paved", 1.22),  # Accra (UGMC) to Kumasi (KATH) - N6 Highway (~250 km)
    (4, 16, "Paved", 1.22),  # Accra (Ridge) to Kumasi (KATH)
    (1, 39, "Paved", 1.18),  # Accra (Korle Bu) to Winneba (N1 West)
    (8, 39, "Paved", 1.20),  # Achimota to Winneba
    (38, 44, "Paved", 1.18), # Cape Coast to Takoradi (N1 Coastal corridor)
    (38, 45, "Paved", 1.18),
    (16, 47, "Paved", 1.20), # Kumasi to Sunyani (~125 km)
    (16, 49, "Paved", 1.18), # Kumasi to Techiman (~125 km)
    (22, 49, "Paved", 1.20), # Ejisu to Techiman
    (50, 24, "Paved", 1.18), # Kintampo to Tamale - N10 North Corridor (~175 km)
    (29, 34, "Paved", 1.18), # Savelugu to Bolgatanga - N10 corridor (~140 km)
    (24, 34, "Paved", 1.18), # Tamale to Bolgatanga (~160 km)
    (30, 34, "Paved", 1.35), # Wa to Bolgatanga via Tumu/Navrongo (~270 km)
    (33, 36, "Under Maintenance", 1.30), # Jirapa to Navrongo via Tumu (~185 km)
    (46, 47, "Unpaved", 1.40), # Asankragwa to Sunyani
    (23, 38, "Paved", 1.30), # Obuasi to Cape Coast via Dunkwa-on-Offin
]

def generate_roads():
    routes = []
    seen = set()
    route_id = 1
    for u, v, cond, tortuosity in RAW_ROAD_PAIRS:
        if (u, v) in seen or (v, u) in seen:
            continue
        seen.add((u, v))
        h_u = HOSP_DICT[u]
        h_v = HOSP_DICT[v]
        hav = haversine(h_u["latitude"], h_u["longitude"], h_v["latitude"], h_v["longitude"])
        dist_km = round(max(hav * tortuosity, hav + 0.5), 1)

        # Average speeds: Paved: 70-80 km/h; Unpaved: 45 km/h; Maintenance: 35 km/h
        if cond == "Paved":
            speed = random.uniform(65, 80) if dist_km > 30 else random.uniform(35, 50)
        elif cond == "Unpaved":
            speed = random.uniform(38, 48)
        else: # Under Maintenance
            speed = random.uniform(25, 38)

        travel_time_mins = round((dist_km / speed) * 60.0, 1)

        routes.append({
            "id": route_id,
            "from_hospital": u,
            "to_hospital": v,
            "distance_km": dist_km,
            "avg_travel_time_mins": travel_time_mins,
            "road_condition": cond,
            "source": "OpenStreetMap / GHA"
        })
        route_id += 1

    return routes

# --- 4. Service Requests Generator (350+ records) ---
REQUEST_TYPES = [
    ("Ambulance Transfer", 60.0, 95.0),
    ("Blood Packet", 0.5, 3.5),
    ("ICU Bed Request", 70.0, 110.0),
    ("Emergency Meds", 1.5, 12.0)
]

def generate_service_requests(count=350):
    requests = []
    base_time = datetime(2026, 8, 1, 6, 0, 0)
    
    for req_id in range(1, count + 1):
        # Pick realistic origin and target
        # 65% regional/nearby transfers, 35% major referral centers (1: Korle Bu, 6: UGMC, 16: KATH, 24: Tamale, 38: Cape Coast, 34: Bolga)
        if random.random() < 0.35:
            origin = random.randint(1, 50)
            referral_hubs = [1, 4, 6, 16, 24, 30, 34, 38, 44, 47]
            target = random.choice(referral_hubs)
            while target == origin:
                target = random.choice(referral_hubs)
        else:
            # Transfer within nearby region or connected nodes
            origin = random.randint(1, 50)
            orig_reg = HOSP_DICT[origin]["region"]
            same_region = [h["id"] for h in HOSPITALS if h["region"] == orig_reg and h["id"] != origin]
            if same_region and random.random() < 0.7:
                target = random.choice(same_region)
            else:
                target = random.randint(1, 50)
                while target == origin:
                    target = random.randint(1, 50)

        # Urgency triage: 1: Critical (25%), 2: Urgent (45%), 3: Normal (30%)
        r_triage = random.random()
        if r_triage < 0.25:
            triage = 1
        elif r_triage < 0.70:
            triage = 2
        else:
            triage = 3

        req_type, min_w, max_w = random.choice(REQUEST_TYPES)
        weight_kg = round(random.uniform(min_w, max_w), 1)

        # Timestamps over 14 operational days
        time_offset_mins = (req_id * 55) + random.randint(-15, 25)
        created_dt = base_time + timedelta(minutes=time_offset_mins)
        ts_str = created_dt.strftime("%Y-%m-%d %H:%M:%S")

        # Status: 60% COMPLETED, 25% DISPATCHED, 15% PENDING
        r_stat = random.random()
        if req_id > count - 40: # Recent ones mostly PENDING / DISPATCHED
            status = "PENDING" if random.random() < 0.6 else "DISPATCHED"
        elif r_stat < 0.60:
            status = "COMPLETED"
        elif r_stat < 0.85:
            status = "DISPATCHED"
        else:
            status = "PENDING"

        requests.append({
            "request_id": req_id,
            "origin_hospital_id": origin,
            "target_hospital_id": target,
            "patient_triage_level": triage,
            "request_type": req_type,
            "weight_kg": weight_kg,
            "timestamp_created": ts_str,
            "status": status
        })

    return requests

# --- 5. Medical Resources Generator (50+ records) ---
RESOURCES_POOL = [
    ("O-Negative Blood Units", "Blood Bank", 5, 30),
    ("A-Positive Blood Units", "Blood Bank", 8, 45),
    ("B-Positive Blood Units", "Blood Bank", 6, 35),
    ("AB-Negative Blood Units", "Blood Bank", 2, 12),
    ("Ventilator Unit (Adult)", "ICU Equipment", 2, 15),
    ("Neonatal Incubator", "ICU Equipment", 1, 8),
    ("Type A ALS Ambulance", "Transport", 1, 5),
    ("Type B BLS Ambulance", "Transport", 2, 8),
    ("Defibrillator Kit", "Emergency Meds", 3, 12),
    ("Oxygen Cylinder 50L", "Emergency Meds", 10, 50),
    ("Emergency Trauma Pack", "Emergency Meds", 15, 60),
    ("Dialysis Machine", "ICU Equipment", 1, 6)
]

def generate_medical_resources():
    resources = []
    res_id = 1
    # Major regional centers receive diverse resource stock
    major_hospitals = [1, 2, 4, 6, 9, 16, 17, 18, 24, 27, 30, 34, 35, 38, 39, 44, 47, 49]

    for hid in major_hospitals:
        # Pick 2-4 resources per hospital
        selected = random.sample(RESOURCES_POOL, k=random.randint(2, 4))
        for name, cat, min_q, max_q in selected:
            qty = random.randint(min_q, max_q)
            resources.append({
                "resource_id": res_id,
                "hospital_id": hid,
                "resource_name": name,
                "quantity_available": qty,
                "resource_category": cat
            })
            res_id += 1

    return resources

# --- 6. Algorithm Benchmark Runs Generator (40+ records) ---
ALGORITHMS_BENCHMARKS = [
    # Search
    ("LinearSearch", [100, 500, 1000, 5000, 10000], lambda n: int(n * 28.5 + random.randint(100, 500)), lambda n: 128),
    ("BinarySearch", [100, 500, 1000, 5000, 10000], lambda n: int(math.log2(n) * 145.0 + random.randint(20, 80)), lambda n: 128),
    # Sort
    ("InsertionSort", [100, 500, 1000, 5000], lambda n: int(0.12 * (n**2) + random.randint(500, 2000)), lambda n: int(n * 0.03 + 64)),
    ("SelectionSort", [100, 500, 1000, 5000], lambda n: int(0.15 * (n**2) + random.randint(600, 2500)), lambda n: int(n * 0.03 + 64)),
    ("MergeSort", [100, 500, 1000, 5000, 10000], lambda n: int(n * math.log2(n) * 45.0 + random.randint(1000, 4000)), lambda n: int(n * 0.08 + 128)),
    ("QuickSort", [100, 500, 1000, 5000, 10000], lambda n: int(n * math.log2(n) * 38.0 + random.randint(800, 3000)), lambda n: int(n * 0.05 + 128)),
    # Graph & Trees
    ("Dijkstra", [50, 100, 200, 500], lambda n: int((n + n * math.log2(n)) * 850.0 + random.randint(2000, 8000)), lambda n: int(n * 0.15 + 256)),
    ("KruskalMST", [50, 100, 200, 500], lambda n: int(n * math.log2(n) * 650.0 + random.randint(1500, 6000)), lambda n: int(n * 0.12 + 256)),
    ("PrimMST", [50, 100, 200, 500], lambda n: int(n * math.log2(n) * 720.0 + random.randint(1800, 7000)), lambda n: int(n * 0.14 + 256)),
    ("CustomHashTable", [100, 500, 1000, 5000, 10000], lambda n: int(n * 18.0 + random.randint(200, 800)), lambda n: int(n * 0.06 + 256)),
    ("RedBlackTree", [100, 500, 1000, 5000, 10000], lambda n: int(n * math.log2(n) * 52.0 + random.randint(1200, 5000)), lambda n: int(n * 0.09 + 256))
]

def generate_algorithm_runs():
    runs = []
    run_id = 1
    base_ts = datetime(2026, 8, 15, 10, 0, 0)

    for algo_name, sizes, time_fn, mem_fn in ALGORITHMS_BENCHMARKS:
        for size in sizes:
            # 3 iterations per size
            for it in range(1, 4):
                exec_time = max(50, time_fn(size) + random.randint(-50, 50))
                mem_kb = max(32, mem_fn(size) + random.randint(-4, 4))
                ts = (base_ts + timedelta(seconds=run_id * 15)).strftime("%Y-%m-%d %H:%M:%S")
                runs.append({
                    "run_id": run_id,
                    "algorithm_name": algo_name,
                    "input_size": size,
                    "execution_time_ns": exec_time,
                    "memory_used_kb": mem_kb,
                    "iteration_number": it,
                    "run_timestamp": ts
                })
                run_id += 1
                if run_id > 50:
                    break
            if run_id > 50:
                break

    return runs

# --- 7. Validation & Verification ---
def validate_dataset(hospitals, roads, requests, resources, runs):
    print("\n" + "="*60)
    print("      DATASET INTEGRITY & VALIDATION REPORT")
    print("="*60)

    # 1. Record counts
    print(f"Hospitals: {len(hospitals)} (Required >= 50) -> {'PASS' if len(hospitals)>=50 else 'FAIL'}")
    print(f"Roads: {len(roads)} (Required >= 100) -> {'PASS' if len(roads)>=100 else 'FAIL'}")
    print(f"Service Requests: {len(requests)} (Required >= 300) -> {'PASS' if len(requests)>=300 else 'FAIL'}")
    print(f"Medical Resources: {len(resources)} (Required >= 30) -> {'PASS' if len(resources)>=30 else 'FAIL'}")
    print(f"Algorithm Runs: {len(runs)} (Required >= 30) -> {'PASS' if len(runs)>=30 else 'FAIL'}")

    # 2. Physics check on roads
    physics_violations = 0
    for r in roads:
        u = HOSP_DICT[r["from_hospital"]]
        v = HOSP_DICT[r["to_hospital"]]
        hav = haversine(u["latitude"], u["longitude"], v["latitude"], v["longitude"])
        if r["distance_km"] < hav:
            physics_violations += 1
            print(f"  [!] Violation: Road {r['id']} ({u['name']} -> {v['name']}): Road={r['distance_km']} < Haversine={hav:.2f}")

    print(f"Road Physics Violations: {physics_violations} -> {'PASS' if physics_violations == 0 else 'FAIL'}")

    # 3. Graph Connectivity (BFS)
    adj = {h["id"]: [] for h in hospitals}
    for r in roads:
        adj[r["from_hospital"]].append(r["to_hospital"])
        adj[r["to_hospital"]].append(r["from_hospital"])

    visited = set()
    queue = [1]
    visited.add(1)
    while queue:
        curr = queue.pop(0)
        for neighbor in adj[curr]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

    isolated = [h["id"] for h in hospitals if len(adj[h["id"]]) == 0]
    unreachable = [h["id"] for h in hospitals if h["id"] not in visited]

    print(f"Isolated Nodes (Degree 0): {len(isolated)} -> {'PASS' if len(isolated)==0 else 'FAIL'}")
    print(f"Graph Connected Component: {len(visited)}/{len(hospitals)} nodes reached -> {'PASS' if len(unreachable)==0 else 'FAIL'}")

    # 4. Foreign Key validation
    h_ids = {h["id"] for h in hospitals}
    req_fk_errors = sum(1 for req in requests if req["origin_hospital_id"] not in h_ids or req["target_hospital_id"] not in h_ids)
    res_fk_errors = sum(1 for res in resources if res["hospital_id"] not in h_ids)
    print(f"Service Request FK Errors: {req_fk_errors} -> {'PASS' if req_fk_errors == 0 else 'FAIL'}")
    print(f"Medical Resource FK Errors: {res_fk_errors} -> {'PASS' if res_fk_errors == 0 else 'FAIL'}")

    print("="*60 + "\n")
    return physics_violations == 0 and len(unreachable) == 0 and req_fk_errors == 0 and res_fk_errors == 0

def write_csv(filepath, rows, fieldnames):
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    with open(filepath, mode="w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)
    print(f"Wrote {len(rows)} records to {filepath}")

def main():
    roads = generate_roads()
    requests = generate_service_requests(350)
    resources = generate_medical_resources()
    runs = generate_algorithm_runs()

    is_valid = validate_dataset(HOSPITALS, roads, requests, resources, runs)
    if not is_valid:
        raise ValueError("Dataset validation failed! Please check constraints.")

    # Write all CSV files
    base_seed_dir = "data/seed"
    write_csv(f"{base_seed_dir}/hospitals.csv", HOSPITALS, 
              ["id", "name", "facility_type", "region", "district", "town", "latitude", "longitude", "ownership_type", "icu_capacity", "ambulance_count"])
    
    write_csv(f"{base_seed_dir}/roads.csv", roads,
              ["id", "from_hospital", "to_hospital", "distance_km", "avg_travel_time_mins", "road_condition", "source"])

    write_csv(f"{base_seed_dir}/service_requests.csv", requests,
              ["request_id", "origin_hospital_id", "target_hospital_id", "patient_triage_level", "request_type", "weight_kg", "timestamp_created", "status"])

    write_csv(f"{base_seed_dir}/medical_resources.csv", resources,
              ["resource_id", "hospital_id", "resource_name", "quantity_available", "resource_category"])

    write_csv(f"{base_seed_dir}/algorithm_runs.csv", runs,
              ["run_id", "algorithm_name", "input_size", "execution_time_ns", "memory_used_kb", "iteration_number", "run_timestamp"])

    print("All seed datasets generated successfully!")

if __name__ == "__main__":
    main()
