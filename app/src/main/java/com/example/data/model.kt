package com.example.data

enum class AppRole {
    CUSTOMER,
    DELIVERY_PARTNER,
    ADMIN
}

enum class OrderStatus(val display: String, val stepIndex: Int) {
    PENDING("Order Placed", 0),
    CONFIRMED("Order Confirmed", 1),
    PREPARING("Preparing", 2),
    READY_FOR_PICKUP("Ready for Pickup", 3),
    ASSIGNED("Delivery Partner Assigned", 4),
    PICKED_UP("Picked Up", 5),
    OUT_FOR_DELIVERY("Out for Delivery", 6),
    DELIVERED("Delivered", 7),
    CANCELLED("Cancelled", -1)
}

enum class PaymentMethod(val label: String) {
    COD("Cash on Delivery"),
    ONLINE("Online Payment (Card/NetBanking)"),
    UPI("UPI / QR Code")
}

data class Category(
    val id: String,
    val name: String,
    val nameBn: String,
    val iconKey: String,
    val isEnabled: Boolean = true
)

data class Product(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: String,
    val description: String,
    val price: Double,
    val discountPrice: Double,
    val unit: String, // kg, litre, pc, packet, dozen
    val stock: Int,
    val isAvailable: Boolean = true,
    val isBestSeller: Boolean = false,
    val isFeatured: Boolean = false,
    val iconType: String = "grocery",
    val rating: Double = 4.8,
    val reviewsCount: Int = 120
) {
    val effectivePrice: Double get() = if (discountPrice > 0 && discountPrice < price) discountPrice else price
    val discountPercent: Int get() = if (price > 0 && discountPrice > 0 && discountPrice < price) (((price - discountPrice) / price) * 100).toInt() else 0
}

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val total: Double get() = product.effectivePrice * quantity
}

data class OrderItem(
    val productId: String,
    val productName: String,
    val productNameBn: String,
    val unit: String,
    val price: Double,
    val quantity: Int
) {
    val total: Double get() = price * quantity
}

data class Order(
    val id: String, // e.g. BM100001
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val landmark: String,
    val deliveryArea: String, // Brajapur, Amtali, Uttar Brajapur, Bishalgarh, Lalshingmura
    val deliveryInstructions: String = "",
    val items: List<OrderItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val grandTotal: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean = false,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveryPartnerId: String? = null,
    val deliveryPartnerName: String? = null,
    val deliveryPartnerPhone: String? = null,
    val otpCode: String = "4826", // For delivery verification
    val rejectionReason: String? = null
)

data class DeliveryPartner(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val vehicleType: String, // Motorcycle, EV Scooter, Bicycle
    val vehicleNumber: String,
    val isOnline: Boolean = true,
    val isVerified: Boolean = true,
    val isSuspended: Boolean = false,
    val rating: Double = 4.9,
    val completedDeliveries: Int = 142,
    val todayEarnings: Double = 520.0,
    val totalEarnings: Double = 18450.0,
    val pendingPayout: Double = 1240.0,
    val upiId: String = "partner@okaxis",
    val activeOrderId: String? = null
)

data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
    val landmark: String,
    val deliveryArea: String,
    val walletBalance: Double = 250.0,
    val totalOrders: Int = 8,
    val totalSpend: Double = 4320.0,
    val isBlocked: Boolean = false
)

data class Coupon(
    val code: String,
    val discountPercent: Int = 0,
    val fixedDiscount: Double = 0.0,
    val minOrderAmount: Double = 299.0,
    val maxDiscount: Double = 100.0,
    val description: String,
    val isEnabled: Boolean = true
)

data class PromotionalBanner(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val couponCode: String? = null,
    val primaryColorHex: Long = 0xFF0C5A34,
    val secondaryColorHex: Long = 0xFF7ECC16
)

data class AppNotification(
    val id: String,
    val targetRole: AppRole,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val relatedOrderId: String? = null
)

data class StoreSettings(
    val storeName: String = "BROTHER MART",
    val tagline: String = "FAST • FRESH • TO YOUR DOOR",
    val phone: String = "+91 98765 43210",
    val whatsapp: String = "+91 98765 43210",
    val address: String = "Brother Mart Hub, Main Road, Bishalgarh, Tripura",
    val openingHours: String = "06:30 AM - 10:00 PM",
    val deliveryFee: Double = 30.0,
    val freeDeliveryThreshold: Double = 499.0,
    val upiId: String = "brothermart@okaxis",
    val taxRatePercent: Double = 0.0
)
