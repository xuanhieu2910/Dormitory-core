# Dormitory Core

Scalable backend for university dormitory management, supporting student
room registration, room allocation, residency management, payments, and
high-concurrency registration workflows.

## Overview

Dormitory Core was developed to digitalize university dormitory
operations and provide a centralized backend for managing students,
buildings, rooms, registration batches, accommodation, and related
financial workflows.

A key engineering requirement is peak dormitory registration, where
approximately **3,000--4,000 students may access the system
concurrently**. The registration architecture therefore focuses on
transactional consistency, temporary resource holding, expiration
control, and scheduled capacity reconciliation.

## Key Features

-   Student dormitory registration
-   Building and room management
-   Registration batch and schedule management
-   Priority-based registration workflows
-   Temporary room holding
-   Room capacity management
-   Student residency management
-   Order and payment processing
-   Registration status tracking
-   Administrative workflows
-   Role-based access control
-   RESTful API integration

## Architecture

The backend follows a layered, domain-oriented architecture that
separates API handling, business logic, persistence, and transactional
operations.

``` text
Client
  │
  ▼
REST API / Controller
  │
  ▼
Service Layer
  │
  ├── Registration Batch
  ├── Student Registration
  ├── Room Management
  ├── Student Room
  ├── Order / Order Session
  └── Payment
  │
  ▼
Repository / Persistence
  │
  ▼
Database
```

Important service areas include:

-   `batchesRegistration`
-   `batchesRegistrationRoom`
-   `batchesRegistrationSchedule`
-   `batchesYearGroupRegistration`
-   `studentRegisterRoom`
-   `studentRoom`
-   `room`
-   `priorityGroup`
-   `orderSession`
-   `orders`
-   `orderItems`
-   `paymentService`
-   `transactionPayment`

## High-Concurrency Registration Architecture

Dormitory Core was designed for peak registration periods where
approximately **3,000--4,000 concurrent users** may access the
registration system within the same time window.

The architecture applies a similar engineering principle from
[**XSync-ticket**](https://github.com/xuanhieu2910/XSync-ticket):
high-load operations should be decomposed into controlled processing
responsibilities instead of concentrating the entire workload in a
single processing path.

However, the implementation is different.

**XSync-ticket** uses queue/thread-oriented processing for large-scale
independent jobs, while **Dormitory Core** adapts the same high-load
design philosophy to a transactional room-registration problem using:

-   Transactional registration processing
-   Temporary room holding
-   Persistent database-backed state
-   TTL-based expiration (`expiresAt`)
-   Scheduled expiration processing
-   Room-capacity reconciliation

### Transactional Room Holding

The main registration flow is handled through
`StudentRegisterRoomServiceImpl`.

When a student selects a room, the system validates the registration and
creates a temporary room registration in a **HOLD** state. The hold is
assigned an expiration timestamp so that room capacity is not
permanently occupied when a student starts but does not complete the
registration workflow.

``` text
Concurrent Registration Requests
              │
              ▼
       Validate Registration
              │
              ▼
      Transactional Processing
              │
              ▼
      Temporary Room HOLD
      ┌───────────────────┐
      │ status = HOLD     │
      │ expiresAt = TTL   │
      └───────────────────┘
              │
        ┌─────┴─────┐
        ▼           ▼
   Completed      Expired
 Registration      HOLD
                      │
                      ▼
           Scheduled Reconciliation
                      │
                      ▼
              Release Capacity
```

The registration operation is transaction-oriented, helping keep room
capacity and registration state consistent while multiple users are
interacting with limited room resources.

### Scheduled Expiration & Capacity Reconciliation

Expired room holds are processed by:

``` text
StudentRegisterScheduleTask
```

The component uses Spring scheduling:

``` java
@Scheduled(fixedDelay = 3000)
public void updateStatusHoldingRoom()
```

The scheduled task periodically processes temporary registrations by:

1.  Retrieving registrations currently holding room capacity.
2.  Recalculating the corresponding room quantities.
3.  Detecting registrations whose holding period has expired.
4.  Updating expired registration states.
5.  Releasing/reconciling room capacity for subsequent registrations.

``` text
StudentRegisterRoomServiceImpl
          │
          ├── Validate registration
          ├── Create registration
          ├── HOLD room capacity
          └── Set expiresAt
                    │
                    ▼
          Database-backed State
                    │
                    ▼
       StudentRegisterScheduleTask
              every ~3 seconds
                    │
             ┌──────┴──────┐
             ▼             ▼
       Active HOLD     Expired HOLD
                           │
                           ▼
                  Capacity Reconciliation
```

This creates a **database-backed temporary reservation model** rather
than relying on an in-memory queue. Registration state remains
persistent across processing cycles and expired reservations can be
reconciled independently from the original HTTP request.

## Relation to XSync-ticket

[**XSync-ticket**](https://github.com/xuanhieu2910/XSync-ticket) and
Dormitory Core solve different high-load problems with different
implementations.

``` text
XSync-ticket                         Dormitory Core
────────────                         ──────────────

Large-scale jobs                     Concurrent room registration
       │                                      │
       ▼                                      ▼
Concurrent Queue                     Transactional HOLD
       │                                      │
       ▼                                      ▼
Worker Threads                       Persistent HOLD State
       │                                      │
       ▼                                      ▼
Fine-grained Sync                    TTL / expiresAt
                                              │
                                              ▼
                                     Scheduled Reconciliation
```

The similarity is primarily an **engineering principle**: separate
high-load processing responsibilities, control access to critical shared
resources, and prevent expensive or state-sensitive operations from
becoming an uncontrolled synchronous workload.

For Dormitory Core, this principle is adapted to room registration
through **transactional processing, temporary resource holding,
expiration control, and scheduled capacity reconciliation**.

## Registration Domain Flow

``` text
Registration Batch
        │
        ├── Registration Room
        ├── Registration Schedule
        └── Year-group / Priority Rules
        │
        ▼
StudentRegisterRoomService
        │
        ├── Priority Group
        ├── Room
        └── Student Room
        │
        ▼
Order Session
        │
        ├── Orders
        └── Order Items
        │
        ▼
Payment / Transaction
```

This separation keeps registration configuration, room state, student
accommodation, ordering, and payment responsibilities independently
maintainable.

## Technology Stack

`Java` · `Spring Boot` · `Spring Security` · `Spring Scheduling` ·
`JPA / Hibernate` · `REST API` · `MySQL / MariaDB` · `Maven`

## Engineering Focus

-   High-Concurrency Backend Systems
-   Transactional Room Registration
-   Temporary Resource Holding
-   TTL-Based Expiration
-   Spring `@Scheduled` Processing
-   Database-Backed State Management
-   Capacity Reconciliation
-   Race-Condition Reduction
-   Persistent Registration State
-   Domain-Oriented Service Design
-   RESTful API Design
-   Authentication & Authorization
-   University Information Systems

## Scalability Note

The system architecture was designed around registration periods
involving approximately **3,000--4,000 concurrent users**.

This number represents the **design workload requirement** of the
dormitory registration system. It should not be interpreted as a formal
throughput benchmark unless accompanied by dedicated load-testing
results.

## Project History

Dormitory Core was developed as part of the digital transformation of
university dormitory operations.

The project evolved around real operational requirements such as
registration batches, limited room capacity, priority-based
registration, temporary reservations, accommodation management, and
payment workflows.

The repository was later migrated from GitLab to GitHub while preserving
its development history.
