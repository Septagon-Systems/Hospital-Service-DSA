# Trace Table: Circular Queue Operations & Wrap-Around

## Problem Context
Hospital service request buffer operating with a fixed capacity of $N = 3$.

## Initial State
* `capacity` = 3
* `front` = 0
* `rear` = -1
* `size` = 0
* `elements` = `[null, null, null]`

## Operation Trace Table

| Step | Operation | Element | `front` | `rear` | `size` | `elements` Array State | Decision / Action |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **0** | `setUp()` | — | 0 | -1 | 0 | `[null, null, null]` | Queue initialized. |
| **1** | `enqueue("REQ-101")` | `"REQ-101"` | 0 | 0 | 1 | `["REQ-101", null, null]` | `rear = ( -1 + 1 ) % 3 = 0`. Inserted at index 0. |
| **2** | `enqueue("REQ-102")` | `"REQ-102"` | 0 | 1 | 2 | `["REQ-101", "REQ-102", null]` | `rear = ( 0 + 1 ) % 3 = 1`. Inserted at index 1. |
| **3** | `enqueue("REQ-103")` | `"REQ-103"` | 0 | 2 | 3 | `["REQ-101", "REQ-102", "REQ-103"]` | `rear = ( 1 + 1 ) % 3 = 2`. Queue is now full (`size == capacity`). |
| **4** | `dequeue()` | — | 1 | 2 | 2 | `[null, "REQ-102", "REQ-103"]` | Returned `"REQ-101"`. `front = ( 0 + 1 ) % 3 = 1`. Index 0 set to `null`. |
| **5** | `enqueue("REQ-104")` | `"REQ-104"` | 1 | 0 | 3 | `["REQ-104", "REQ-102", "REQ-103"]` | **Wrap-Around**: `rear = ( 2 + 1 ) % 3 = 0`. Inserted at index 0. |
| **6** | `peek()` | — | 1 | 0 | 3 | `["REQ-104", "REQ-102", "REQ-103"]` | Returned `"REQ-102"`. `size` remains unchanged at 3. |

## Final Output
* `peek()` item: `"REQ-102"`
* Final `size`: 3
* Front pointer wrapped successfully without memory re-allocation.