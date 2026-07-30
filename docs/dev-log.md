# Team Weekly Development Log

Per Brief Section 15.iv, each team must maintain a development log showing weekly progress, challenges, and decisions.

---

## Week 0: Project Setup & Architecture Initialisation
- **Date**: 29 July 2026
- **Context Selected**: Ghana Hospital & Clinic Operations
- **Key Milestones Accomplished**:
  - Gradle Multi-Module repository scaffolded with Java 21 LTS toolchain.
  - SQLite JDBC database schema drafted (`data/schema/schema.sql`).
  - GitHub Actions CI pipeline configured (`.github/workflows/ci.yml`).
  - Pod responsibility matrix established (7 pods across 12-16 team members).
- **Challenges & Decisions**:
  - Selected Hospital domain due to rich availability of geocoded facility lists from `data.gov.gh` and HOTOSM Ghana exports.
  - Enforced strict custom data structure rules across core modules.
