---
name: Feature / Algorithm / Data Structure
about: Create a task for implementing a custom data structure, algorithm, graph search, or dispatch engine.
title: '[FEATURE] <Pod Name>: <Feature Name>'
labels: feature, enhancement
assignees: ''
---

### Feature Summary
A clear description of the data structure, algorithm, or module feature to be implemented.

### Module & Package
- **Target Module**: `datastructures` | `algorithms` | `graph` | `scheduling` | `db` | `benchmark` | `app`
- **Package Path**: e.g., `edu.ug.dcit308.hospital.ds.tree`

### Algorithmic Contract & Constraints
- **Time Complexity Requirement**: e.g. $O(1)$ amortized insert, $O(\log n)$ search
- **Space Complexity Requirement**: e.g. $O(n)$
- **Core Rule Check**: Strictly NO `java.util` collections allowed! Custom implementation required.

### Implementation Checklist
- [ ] Interface & class definition created
- [ ] Core methods implemented from scratch
- [ ] Unit tests written for Normal Case
- [ ] Unit tests written for Boundary / Edge Case
- [ ] Unit tests written for Invalid Input Case
- [ ] Integrated into CLI Console Menu / Service Layer
