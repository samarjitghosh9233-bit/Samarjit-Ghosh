# BROTHER MART — FAST • FRESH • TO YOUR DOOR

**Brother Mart** is a modern, responsive, 3-in-1 grocery and daily essentials delivery platform built with **Kotlin** and **Jetpack Compose** (Material Design 3). It features a unified state and backend model serving three distinct user panels:
1. **CUSTOMER APP** — Intuitive grocery shopping, live order status tracking, coupons, and wallet.
2. **DELIVERY PARTNER APP** — Order acceptance, live GPS route navigation, OTP delivery verification, and earnings analytics.
3. **ADMIN PANEL** — Operations dashboard, catalog management, partner dispatch, coupon creation, and sales reports.

---

## 🎨 Brand Identity & Theme
- **Brand Name**: BROTHER MART
- **Tagline**: FAST • FRESH • TO YOUR DOOR
- **Header**: Dark Green container (`#084124`), White **BROTHER** text, Vibrant Lime Green **MART** text (`#7ECC16`)
- **Theme**: Premium Dark Green (`#0C5A34`), Lime Green Accent (`#7ECC16`), Soft Green Background (`#F6F9F6`)
- **Service Zones (Bishalgarh Hub)**:
  - Bishalgarh (Town)
  - Uttar Brajapur
  - Brajapur
  - Amtali
  - Lalshingmura

---

## 📱 Three-in-One Integrated Panels

### 1. Customer App
- **Header**: Brand logo, location chip with quick switch between Bishalgarh service zones, real-time unread notification bell, and cart item badge.
- **Home**: Real-time product search, promotional banners with quick-apply coupon tags, category pills, "Recommended for You" flash deals, and "Best Sellers" grid.
- **Catalog Categories**:
  - **Rice**: Miniket, Gobindobhog, Basmati
  - **Oil**: Pure Kachi Ghani Mustard Oil, Sunflower Oil
  - **Desi Chicken**: Country Chicken (Live Cut), Chicken Curry Cut
  - **Chicken**: Broiler Chicken Curry Cut
  - **Desi Chicken Eggs**: Free-range Country Chicken Eggs (6 pcs)
  - **Desi Duck**: Village Duck Meat (Pati Hash)
  - **Desi Duck Eggs**: Farm fresh Duck Eggs (6 pcs)
  - **Daily Kirana**: Masoor Dal, Tata Salt, Chakki Atta
  - **Vegetables**: Potatoes, Red Onions, Tomatoes, Green Chillies
  - **Fruits, Snacks, Beverages, Household**: Bananas, Chanachur, Amul Taaza Milk, Vim Gel
- **Product Detail**: Bengali + English names, descriptions, unit selection, stock indicator, quantity stepper (+/-), Wishlist toggle, and Buy Now.
- **Cart & Coupons**: Cart drawer with item quantities, subtotal, delivery fee calculation (free above ₹499), discount coupon application (`BMART20`, `FREESHIP`, `WELCOME50`), and grand total.
- **Checkout & Payment**: Name, phone, address, landmark, delivery area, delivery instructions, and payment method selection (Cash on Delivery, Online, UPI with QR / UPI ID).
- **Live 7-Stage Order Tracking**:
  `Order Placed` ➔ `Order Confirmed` ➔ `Preparing` ➔ `Ready for Pickup` ➔ `Delivery Partner Assigned` ➔ `Out for Delivery` ➔ `Delivered`
  Displays the unique **4-Digit Delivery OTP** to ensure verified handoff.
- **Customer Profile**: User details, wallet balance (with top-up capability), saved addresses, wishlist, order history, and store support info.

### 2. Delivery Partner App
- **Online / Offline Toggle**: Instant toggle with visual state indicator.
- **Dashboard Metrics**: Today's earnings (₹), completed deliveries count, and active job counters.
- **Incoming Delivery Requests**: Order ID, customer name, pickup store address (Brother Mart Bishalgarh Hub), drop location, distance (~2.6 km), estimated delivery time (~18 mins), delivery fee earned (₹40.00), with **ACCEPT** and **DECLINE** buttons.
- **Active Delivery Workflow**:
  - Pickup order from Brother Mart Hub ➔ Mark **"Picked Up"**
  - Navigate to customer with simulated turn-by-turn interactive map
  - One-tap Customer Phone Call & WhatsApp chat integration
  - Mark **"Out for Delivery"**
  - Final handoff: **OTP Security Verification** (validates against the 4-digit security code generated on customer order placement) ➔ Marks **"Delivered"** and credits ₹40 fee.
- **Earnings & Payouts**: Lifetime earnings, pending bank transfers, weekly totals, and trip transaction history.
- **Partner Profile**: KYC verified badge, vehicle specifications (Motorcycle / EV Scooter / Bicycle), vehicle number, and UPI settlement details.

### 3. Admin Panel
- **Operations Dashboard**: Metric summary cards (Total Revenue, Today's Sales, Pending Orders, Out for Delivery, Completed Orders, Fleet Count, Catalog Count).
- **Weekly Trend Chart**: Custom visual bar chart tracking sales volume across weekdays.
- **Order Management**: Search by ID/customer/area, filter by status, change status (Confirm, Preparing, Ready), assign/reassign online delivery partners, and view/print professional Tax Invoices.
- **Product Management**: Search and filter catalog, Add Product dialog (English & Bengali names, price, discount, unit, stock, best seller flag), Edit Product dialog, and Delete Product.
- **Category Management**: Enable/disable categories, add custom categories.
- **Customer Management**: View order count, total spending, and Block/Unblock customers.
- **Delivery Partner Fleet**: View online/offline status, vehicle info, total trips, and partner ratings.
- **Coupon Management**: Create promo coupons with percentage/fixed discounts, minimum order threshold, and max discount cap.
- **Banner Management**: Upload and schedule promotional carousel cards.
- **Store Settings**: Configure store name, helpline, address, opening/closing hours, delivery charges, free delivery threshold, and UPI ID.
- **Reports**: Operations summary and category sales breakdown.

---

## 🔄 Instant Multi-Role Switcher
In the top bar header, tap the role badge (`Customer` / `Delivery` / `Admin`) to toggle between any of the 3 panels at any moment. Changes made in one panel (e.g. placing an order in Customer App) immediately appear in the Admin and Delivery Partner views!

---

## 🗄️ Database & Backend Architecture
For the complete Node.js + Express backend specifications, MongoDB schemas, REST endpoints, and authentication workflows, refer to `BACKEND_AND_DATABASE_SPEC.md`.

---

## 🚀 Building & Running the Android App

### Requirements
- Android SDK 34+
- Java 11 or 17
- Gradle 8.0+

### Build Debug APK
```bash
gradle assembleDebug
```
The output APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Run Unit & Robolectric Tests
```bash
gradle :app:testDebugUnitTest
```

### Build Production Release APK / AAB
```bash
gradle assembleRelease
# Or for Google Play App Bundle:
gradle bundleRelease
```
