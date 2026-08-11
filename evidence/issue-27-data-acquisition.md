# Issue #27 — Ghanaian Health Dataset Acquisition Evidence

## Objective

This dataset was prepared for Issue #27, "Scrape Ghanaian Health Dataset".

The objective was to provide seed data containing Ghanaian health-facility
locations and connecting road-network distances.

## Data Sources

Health-facility information was collected from publicly available geographic
data sources, including OpenStreetMap and relevant Ghanaian public-data
sources where applicable.

Road-network information was obtained from OpenStreetMap road-network data.

## Hospital/Health Facility Data

The dataset contains at least 50 health-facility locations across Ghana.

Each facility record contains:

- Unique facility ID
- Facility name
- Facility type
- Region
- District
- Latitude
- Longitude
- Source

The hospital dataset is stored in:

`data/seed/hospitals.csv`

## Road Network Data

The road dataset contains at least 100 connecting routes between facilities.

Each route contains:

- Unique route ID
- Origin facility
- Destination facility
- Road distance in kilometres
- Data source

The road dataset is stored in:

`data/seed/roads.csv`

## Cleaning and Validation

The collected data was cleaned to:

1. Remove duplicate facility records.
2. Check that facility IDs are unique.
3. Check that latitude and longitude values are present.
4. Check that road distances are numeric and non-negative.
5. Check that route endpoints correspond to valid facility IDs.
6. Remove invalid or incomplete records where necessary.

## Evidence

The final dataset files are:

- `data/seed/hospitals.csv`
- `data/seed/roads.csv`

The datasets were reviewed to confirm that they satisfy the minimum
requirements of 50 facilities and 100 road routes.

## Issue

This work addresses GitHub Issue #27.