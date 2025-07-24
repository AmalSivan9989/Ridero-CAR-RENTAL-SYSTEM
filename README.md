# 🚀 Faster Bike Taxi Application System

A full-stack bike taxi and car rental platform designed for efficient bookings, secure payments, and admin/agent management. Built using **Angular** (frontend) and **Spring Boot** (backend) with **JWT Authentication** and **role-based access control**.

---

## 📦 Tech Stack

| Layer         | Technology              |
|---------------|--------------------------|
| Frontend      | Angular 16, TypeScript, Angular Forms |
| Backend       | Spring Boot 3, Spring Security, Spring Data JPA |
| Database      | H2 (dev), MySQL/(prod-ready) |
| Authentication| JWT (JSON Web Token) |
| Testing       | JUnit |
| UI Styling    | Custom CSS with Lavender-Purple Theme |

---

---

## ✨ Key Features

### 🚖 Customer Functionality
- 🔍 **Search cars by location**
- 📅 **Book a ride**
- 💳 **Make payment** (via wallet)
- 🕓 **Auto check-in after payment**
- 🕗 **Pending payment handling**
- 🧾 **View payment history**
- 👛 **Wallet: View, Add funds, Deduct funds**
- 📝 **Submit feedback after checkout**

### 🧑‍🔧 Agent Functionality
- 🛠️ **Report maintenance issues**
- 📋 **View assigned maintenance requests**

### 🛡️ Admin Functionality
- 👥 **Manage users (add, update, delete)**
- 🚘 **Update vehicle availability**
- 🔧 **View maintenance issues per agent**
- 📊 **Monitor bookings**

---

## 🔐 Authentication & Authorization

- JWT-based login system
- Roles: `ROLE_ADMIN`, `ROLE_AGENT`, `ROLE_CUSTOMER`
- Angular Route Guards prevent unauthorized access
- Token-based API access with `Authorization: Bearer <token>` headers

---

## 📡 REST API Endpoints (Spring Boot)

| Endpoint                              | Method | Description                            |
|---------------------------------------|--------|----------------------------------------|
| `/addBiketaxi`                        | POST   | Register a bike                        |
| `/getBiketaxi`                        | GET    | Fetch all registered bikes             |
| `/createBooking`                      | POST   | Create a new booking                   |
| `/makePayment`                        | POST   | Process booking payment                |
| `/createWallet/{userId}`             | POST   | Create a wallet                        |
| `/addFunds/{userId}/{amount}`        | PUT    | Add money to wallet                    |
| `/deductFunds/{userId}/{amount}`     | PUT    | Deduct money from wallet               |
| `/getWalletBalance/{userId}`         | GET    | Get wallet balance                     |
| `/submitMaintenanceRequest`          | POST   | Agent reports an issue                 |
| `/getRequestsByAgent/{agentId}`      | GET    | Admin views issues for an agent        |
| `/getAllAgents`                      | GET    | List of agent users                    |
| `/submitFeedback`                    | POST   | Customer feedback after check-out      |

---

Author
Amal Sivan Nair
