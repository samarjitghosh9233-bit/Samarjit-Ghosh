# BROTHER MART — BACKEND, DATABASE & API SPECIFICATION

This document outlines the complete production backend architecture, database schemas, REST API endpoints, authentication flows, and deployment guidelines for **BROTHER MART**.

---

## 1. System Architecture

```
                  ┌────────────────────────────────────────┐
                  │          BROTHER MART CLIENT           │
                  │   (Android App: Customer / Delivery /  │
                  │              Admin Panels)             │
                  └───────────────────┬────────────────────┘
                                      │  HTTPS / REST / WebSocket
                                      ▼
                  ┌────────────────────────────────────────┐
                  │       NODE.JS + EXPRESS BACKEND        │
                  │  - JWT + OTP Authentication            │
                  │  - Order Dispatch Engine               │
                  │  - Payment & UPI Gateway Integration   │
                  │  - Push Notification Service           │
                  └───────────────────┬────────────────────┘
                                      │
                   ┌──────────────────┴──────────────────┐
                   ▼                                     ▼
        ┌─────────────────────┐               ┌─────────────────────┐
        │   MONGODB ATLAS     │               │   REDIS CACHE       │
        │   (Primary Store)   │               │   (Live GPS/Status) │
        └─────────────────────┘               └─────────────────────┘
```

---

## 2. Database Schema (MongoDB / Room Persistence)

### 2.1 `users` & `customers` Collection
```json
{
  "_id": "ObjectId",
  "userId": "cust_samarjit",
  "name": "Samarjit Ghosh",
  "phone": "+919863345210",
  "email": "samarjit@brothermart.in",
  "role": "CUSTOMER",
  "passwordHash": "$2b$10$abcdef123456...",
  "addresses": [
    {
      "addressLine": "Near Kali Mandir, Ward No. 3",
      "landmark": "Behind Netaji Subhash School",
      "area": "Uttar Brajapur",
      "isDefault": true
    }
  ],
  "walletBalance": 350.0,
  "totalOrders": 12,
  "totalSpend": 5640.0,
  "isBlocked": false,
  "createdAt": "2026-09-01T10:00:00Z"
}
```

### 2.2 `delivery_partners` Collection
```json
{
  "_id": "ObjectId",
  "partnerId": "dp_1",
  "name": "Rahul Debnath",
  "phone": "+919862111234",
  "email": "rahul.bm@gmail.com",
  "vehicleType": "Motorcycle",
  "vehicleNumber": "TR-01-BM-3210",
  "isOnline": true,
  "isVerified": true,
  "isSuspended": false,
  "rating": 4.9,
  "completedDeliveries": 148,
  "todayEarnings": 480.0,
  "totalEarnings": 18600.0,
  "pendingPayout": 1240.0,
  "upiId": "partner@okaxis",
  "currentLocation": {
    "lat": 23.6821,
    "lng": 91.2745,
    "updatedAt": "2026-09-28T11:45:00Z"
  }
}
```

### 2.3 `products` Collection
```json
{
  "_id": "ObjectId",
  "productId": "p_desi_chk_1",
  "nameEn": "Desi Country Chicken (Live Cut)",
  "nameBn": "দেশি জীবন্ত আস্ত মুরগি",
  "category": "Desi Chicken",
  "description": "100% authentic village raised organic Desi chicken...",
  "price": 480.0,
  "discountPrice": 420.0,
  "unit": "1 kg",
  "stock": 25,
  "isAvailable": true,
  "isBestSeller": true,
  "isFeatured": true,
  "rating": 4.9,
  "reviewsCount": 210,
  "iconType": "meat"
}
```

### 2.4 `orders` & `order_items` Collection
```json
{
  "_id": "ObjectId",
  "orderId": "BM100001",
  "customerId": "cust_samarjit",
  "customerName": "Samarjit Ghosh",
  "customerPhone": "+919863345210",
  "deliveryAddress": "Near Kali Mandir, Ward No. 3",
  "landmark": "Behind Netaji Subhash School",
  "deliveryArea": "Uttar Brajapur",
  "deliveryInstructions": "Please ring bell twice",
  "items": [
    {
      "productId": "p_desi_chk_1",
      "productName": "Desi Country Chicken (Live Cut)",
      "productNameBn": "দেশি জীবন্ত আস্ত মুরগি",
      "unit": "1 kg",
      "price": 420.0,
      "quantity": 1,
      "total": 420.0
    }
  ],
  "subtotal": 673.0,
  "deliveryFee": 0.0,
  "discount": 50.0,
  "grandTotal": 623.0,
  "paymentMethod": "UPI",
  "isPaid": true,
  "status": "DELIVERED",
  "deliveryPartnerId": "dp_1",
  "deliveryPartnerName": "Rahul Debnath",
  "deliveryPartnerPhone": "+919862111234",
  "otpCode": "4826",
  "createdAt": "2026-09-28T09:30:00Z"
}
```

### 2.5 `coupons`, `banners` & `settings` Collections
- **Coupons**: `code`, `discountPercent`, `fixedDiscount`, `minOrderAmount`, `maxDiscount`, `isEnabled`
- **Banners**: `title`, `subtitle`, `badge`, `couponCode`, `primaryColorHex`
- **Settings**: `storeName`, `tagline`, `phone`, `whatsapp`, `address`, `openingHours`, `deliveryFee`, `freeDeliveryThreshold`, `upiId`

---

## 3. REST API Endpoints Specification

### 3.1 Authentication & Profile
- `POST /api/v1/auth/customer/send-otp`  
  Request: `{ "phone": "+919863345210" }`  
  Response: `{ "success": true, "message": "OTP sent successfully" }`

- `POST /api/v1/auth/customer/verify-otp`  
  Request: `{ "phone": "+919863345210", "otp": "4826" }`  
  Response: `{ "token": "jwt_token_here", "customer": { ... } }`

- `POST /api/v1/auth/delivery/login`  
  Request: `{ "phone": "+919862111234", "password": "secure_password" }`  
  Response: `{ "token": "jwt_token_here", "partner": { ... } }`

- `POST /api/v1/auth/admin/login`  
  Request: `{ "email": "admin@brothermart.in", "password": "super_admin_pass" }`  
  Response: `{ "token": "jwt_token_here", "role": "ADMIN" }`

### 3.2 Catalog & Categories
- `GET /api/v1/categories` — List all categories (Rice, Oil, Desi Chicken, Vegetables, etc.)
- `GET /api/v1/products?category=Desi Chicken&search=curry` — Filter products
- `POST /api/v1/admin/products` — Add new product (Admin only)
- `PUT /api/v1/admin/products/:id` — Update product details, stock, or prices
- `DELETE /api/v1/admin/products/:id` — Remove item from catalog

### 3.3 Orders & Tracking
- `POST /api/v1/orders/create`  
  Request:  
  ```json
  {
    "customerName": "Samarjit Ghosh",
    "customerPhone": "+919863345210",
    "deliveryAddress": "Near Kali Mandir",
    "landmark": "Netaji School",
    "deliveryArea": "Uttar Brajapur",
    "paymentMethod": "UPI",
    "items": [{ "productId": "p_desi_chk_1", "quantity": 1 }],
    "couponCode": "BMART20"
  }
  ```
  Response: `{ "success": true, "orderId": "BM100004", "otpCode": "7193", "grandTotal": 420.0 }`

- `GET /api/v1/orders/:id/track` — Real-time progression state
- `PUT /api/v1/orders/:id/status` — Update order status (Admin/Partner)
- `POST /api/v1/orders/:id/assign-rider` — Dispatch to Delivery Partner
- `POST /api/v1/delivery/verify-otp`  
  Request: `{ "orderId": "BM100004", "otp": "7193" }`  
  Response: `{ "success": true, "status": "DELIVERED", "feeCredited": 40.0 }`

---

## 4. Admin Credentials Setup
Default Administrator Account:
- **Email**: `admin@brothermart.in`
- **Default Password**: `BrotherMart@2026`
- **Role**: `SUPER_ADMIN`
- **Two-Factor Auth**: Enabled via SMS / Authenticator app in production.

---

## 5. Google Play Store Deployment Instructions
1. **Application ID**: `com.aistudio.brothermart.rxkpqw`
2. **App Title**: Brother Mart (30 characters or fewer, compliant with Play Store metadata policies).
3. **Adaptive Icon**: Verified custom vector foreground (`ic_launcher_foreground.xml`) and dark green background (`ic_launcher_background.xml`).
4. **Permissions Compliance**:
   - Zero-permission photo handling via standard Activity Result contracts.
   - Declarations restricted to network access (`INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`).
5. **Generate Signed Release Bundle**:
   ```bash
   gradle bundleRelease
   ```
6. **Upload to Google Play Console**:
   - Navigate to **Production > Releases > Create new release**.
   - Upload the generated `.aab` file from `app/build/outputs/bundle/release/app-release.aab`.
   - Submit for Google Play Store review.
