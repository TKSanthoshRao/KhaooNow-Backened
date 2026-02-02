# KhaooNow-Backened
🍽️ KhaaoNow — Online Food Ordering Backend (Spring Boot)

KhaaoNow is a scalable, enterprise-grade backend system for an online food ordering platform.
It focuses on clean architecture, security-first authentication, and real-world backend design patterns.

This project is built to demonstrate production-ready backend engineering, not just CRUD APIs.

🚀 Features
🔐 Authentication & Verification

Email-based OTP verification

Secure OTP lifecycle (CREATED → SENT → VERIFIED / EXPIRED / FAILED)

Attempt limiting to prevent brute force

JWT-based authentication

Role-based authorization (USER, ADMIN, RESTAURANT_OWNER)

👤 User Management

User registration only after email verification

Password hashing using BCrypt

Email uniqueness enforcement

User lifecycle states (ACTIVE, BLOCKED, DELETED)


**🍴 Restaurant Management**

Restaurant onboarding

One-to-one restaurant–address mapping

Owner association (future-ready)

Open/Closed status handling

📍 Location-Based Restaurant Search

Nearby restaurant discovery using latitude & longitude

Distance calculation using Haversine formula

Radius-based filtering

Sorted results by nearest distance


🏠 **Address Architecture**

Reusable Address entity

Separate mapping for:

User addresses

Restaurant addresses

Indexed latitude & longitude for faster geo queries


📦 **Order Foundations (Extensible)**

Cart structure

Order & order items

Snapshot delivery address (order_addresses)

Status-driven order flow


🧠 **Architecture Overview**

Controller
↓

Service (Business Logic)
↓

Repository (JPA / DB)


Separation of concerns is strictly followed:

AuthService → authentication & login

VerificationService → OTP lifecycle

EmailService → email delivery only

RestaurantService → restaurant logic

UserService → user persistence & checks


🛠️ **Tech Stack**

Layer	Technology

Language	Java 21

Framework	Spring Boot

Security	Spring Security + JWT

ORM	Hibernate / JPA

Database	MySQL

Validation	Jakarta Validation

Build Tool	Maven

Email	JavaMailSender

API Style	REST


🔄 **Email Verification Flow**

User requests email verification

OTP generated and stored in DB

Previous tokens expired

OTP emailed to user

User submits OTP

OTP validated (attempts + expiry)

Email marked as VERIFIED

User allowed to register

✔ No user record exists before verification
✔ One verified email → one registration

📍 Nearby Restaurant API
GET /api/v1/restaurants?lat=12.9716&lng=77.5946&radius=5


Returns:

Restaurant name

Distance (km)

Open/Closed status

Sorted by nearest first



🧪 **Testing Strategy (Recommended)**

Unit tests: JUnit + Mockito

API tests: Postman / Newman

Edge cases:

OTP expiry

OTP resend

Attempt limit

Duplicate email

Token reuse prevention



🔐 **Security Highlights**

BCrypt password hashing

JWT stateless authentication

OTP attempt limiting

Token invalidation strategy

Email uniqueness enforcement

Clear separation between auth & verification logic


📈 Future Enhancements

Phone number OTP verification

Rate limiting (Redis / Bucket4j)

Restaurant owner onboarding

Order payment integration

Delivery partner module

Admin dashboard APIs

Elasticsearch for geo search


🤝 Contributing

Contributions are welcome!

Fork the repository

Create a feature branch

Follow existing architecture & naming conventions

Submit a pull request


## Author

T K Santhosh Rao  
Backend Engineer | Java | Spring Boot | System Design

📫 Email: santhoshraotk14@gmail.com

🔗 LinkedIn: https://www.linkedin.com/in/tksanthoshrao/

🐙 GitHub: https://github.com/TKSanthoshRao

## Ownership
This project was originally created and is maintained by T K Santhosh Rao.

## License
This project is licensed under the MIT License.  
See the LICENSE file for details.


