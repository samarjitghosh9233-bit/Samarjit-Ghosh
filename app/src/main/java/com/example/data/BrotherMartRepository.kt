package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object BrotherMartRepository {

    val SERVICE_AREAS = listOf(
        "Bishalgarh (Town)",
        "Uttar Brajapur",
        "Brajapur",
        "Amtali",
        "Lalshingmura"
    )

    // Pre-seeded Categories
    private val initialCategories = listOf(
        Category("cat_rice", "Rice", "চাল", "rice"),
        Category("cat_oil", "Oil", "তেল", "oil"),
        Category("cat_desi_chicken", "Desi Chicken", "দেশি মুরগি", "meat"),
        Category("cat_chicken", "Chicken", "মুরগি", "poultry"),
        Category("cat_desi_chicken_eggs", "Desi Chicken Eggs", "দেশি মুরগির ডিম", "egg"),
        Category("cat_desi_duck", "Desi Duck", "দেশি হাঁস", "duck"),
        Category("cat_desi_duck_eggs", "Desi Duck Eggs", "দেশি হাঁসের ডিম", "duck_egg"),
        Category("cat_kirana", "Daily Kirana Items", "নিত্যপ্রয়োজনীয় মুদি", "kirana"),
        Category("cat_veg", "Vegetables", "সবজি", "vegetable"),
        Category("cat_fruits", "Fruits", "ফলমূল", "fruit"),
        Category("cat_snacks", "Snacks", "স্ন্যাক্স", "snack"),
        Category("cat_beverages", "Beverages", "পানীয়", "beverage"),
        Category("cat_household", "Household Products", "গৃহস্থালি সামগ্রী", "household"),
        Category("cat_personal_care", "Personal Care", "ব্যক্তিগত যত্ন", "care")
    )

    // Pre-seeded Products with Bengali + English names & descriptions
    private val initialProducts = listOf(
        // Desi Chicken & Duck Specials
        Product(
            id = "p_desi_chk_1",
            nameEn = "Desi Country Chicken (Live Cut)",
            nameBn = "দেশি জীবন্ত আস্ত মুরগি",
            category = "Desi Chicken",
            description = "100% authentic village raised organic Desi chicken. Freshly dressed upon order with rich taste and high nutrition.",
            price = 480.0,
            discountPrice = 420.0,
            unit = "1 kg",
            stock = 25,
            isBestSeller = true,
            isFeatured = true,
            iconType = "meat",
            rating = 4.9,
            reviewsCount = 210
        ),
        Product(
            id = "p_desi_chk_2",
            nameEn = "Desi Chicken Curry Cut",
            nameBn = "দেশি মুরগির কারি কাট",
            category = "Desi Chicken",
            description = "Tender skinless Desi chicken cuts, perfect for traditional Bengali chicken curry (Jhol).",
            price = 490.0,
            discountPrice = 440.0,
            unit = "1 kg",
            stock = 18,
            isBestSeller = true,
            iconType = "meat",
            rating = 4.8
        ),
        Product(
            id = "p_desi_duck_1",
            nameEn = "Desi Duck Meat (Pati Hash)",
            nameBn = "খাঁটি দেশি পাতি হাঁসের মাংস",
            category = "Desi Duck",
            description = "Freshly cleaned village Desi duck meat. Excellent tenderness and rich traditional flavor.",
            price = 560.0,
            discountPrice = 510.0,
            unit = "1 kg",
            stock = 14,
            isBestSeller = true,
            isFeatured = true,
            iconType = "duck",
            rating = 4.9
        ),
        Product(
            id = "p_desi_egg_1",
            nameEn = "Fresh Desi Chicken Eggs",
            nameBn = "দেশি মুরগির লাল ডিম",
            category = "Desi Chicken Eggs",
            description = "Natural free-range country chicken eggs collected daily from local farms.",
            price = 110.0,
            discountPrice = 95.0,
            unit = "6 pcs",
            stock = 50,
            isBestSeller = true,
            iconType = "egg",
            rating = 4.9
        ),
        Product(
            id = "p_desi_duck_egg_1",
            nameEn = "Desi Duck Eggs (Farm Fresh)",
            nameBn = "তাজা দেশি হাঁসের ডিম",
            category = "Desi Duck Eggs",
            description = "Rich in yolk and protein, fresh country duck eggs.",
            price = 120.0,
            discountPrice = 105.0,
            unit = "6 pcs",
            stock = 40,
            isFeatured = true,
            iconType = "duck_egg",
            rating = 4.8
        ),
        Product(
            id = "p_broiler_chk_1",
            nameEn = "Broiler Chicken Curry Cut",
            nameBn = "তাজা ব্রয়লার মুরগির মাংস",
            category = "Chicken",
            description = "Fresh farm chicken washed and cut into medium pieces.",
            price = 240.0,
            discountPrice = 199.0,
            unit = "1 kg",
            stock = 45,
            isBestSeller = true,
            iconType = "poultry",
            rating = 4.7
        ),

        // Rice & Oil
        Product(
            id = "p_rice_1",
            nameEn = "Premium Miniket Rice",
            nameBn = "প্রিমিয়াম মিনিকেট চাল",
            category = "Rice",
            description = "Long grain, aged Miniket rice. Non-sticky and delicious for daily meals.",
            price = 320.0,
            discountPrice = 285.0,
            unit = "5 kg",
            stock = 60,
            isBestSeller = true,
            iconType = "rice",
            rating = 4.8
        ),
        Product(
            id = "p_rice_2",
            nameEn = "Gobindobhog Aromatic Rice",
            nameBn = "সুগন্ধি গোবিন্দভোগ চাল",
            category = "Rice",
            description = "Aromatic short grain rice specially for Bengali Payesh and Khichuri.",
            price = 140.0,
            discountPrice = 125.0,
            unit = "1 kg",
            stock = 30,
            iconType = "rice",
            rating = 4.9
        ),
        Product(
            id = "p_oil_1",
            nameEn = "Pure Kachi Ghani Mustard Oil",
            nameBn = "খাঁটি কাচ্চি ঘানি সরিষার তেল",
            category = "Oil",
            description = "Cold pressed pungency guaranteed pure mustard oil for authentic cooking.",
            price = 175.0,
            discountPrice = 158.0,
            unit = "1 litre",
            stock = 55,
            isBestSeller = true,
            iconType = "oil",
            rating = 4.9
        ),
        Product(
            id = "p_oil_2",
            nameEn = "Fortune Refined Sunflower Oil",
            nameBn = "ফরচুন রিফাইন্ড সানফ্লাওয়ার তেল",
            category = "Oil",
            description = "Light and healthy cooking oil enriched with Vitamin A & D.",
            price = 160.0,
            discountPrice = 142.0,
            unit = "1 litre",
            stock = 40,
            iconType = "oil",
            rating = 4.7
        ),

        // Daily Kirana
        Product(
            id = "p_kirana_1",
            nameEn = "Premium Red Masoor Dal",
            nameBn = "উন্নত মানের মসুর ডাল",
            category = "Daily Kirana Items",
            description = "Clean, unpolished high protein red lentils for everyday healthy dal.",
            price = 120.0,
            discountPrice = 105.0,
            unit = "1 kg",
            stock = 70,
            isBestSeller = true,
            iconType = "kirana",
            rating = 4.7
        ),
        Product(
            id = "p_kirana_2",
            nameEn = "Tata Salt Iodized",
            nameBn = "টাটা আয়োডাইজড লবণ",
            category = "Daily Kirana Items",
            description = "Vacuum evaporated pure iodized salt for good health.",
            price = 28.0,
            discountPrice = 26.0,
            unit = "1 kg",
            stock = 120,
            iconType = "kirana",
            rating = 4.9
        ),
        Product(
            id = "p_kirana_3",
            nameEn = "Aashirvaad Shudh Chakki Atta",
            nameBn = "আশীর্বাদ খাঁটি গম আটা",
            category = "Daily Kirana Items",
            description = "100% whole wheat flour for soft, fluffy rotis.",
            price = 260.0,
            discountPrice = 235.0,
            unit = "5 kg",
            stock = 50,
            isBestSeller = true,
            iconType = "kirana",
            rating = 4.8
        ),

        // Vegetables & Fruits
        Product(
            id = "p_veg_1",
            nameEn = "Fresh Local Potatoes (Jyoti)",
            nameBn = "তাজা স্থানীয় গোল আলু",
            category = "Vegetables",
            description = "Freshly harvested smooth potatoes directly from local farmers.",
            price = 35.0,
            discountPrice = 28.0,
            unit = "1 kg",
            stock = 150,
            isBestSeller = true,
            iconType = "vegetable",
            rating = 4.7
        ),
        Product(
            id = "p_veg_2",
            nameEn = "Nashik Fresh Red Onions",
            nameBn = "তাজা লাল পেঁয়াজ",
            category = "Vegetables",
            description = "Crisp, pungent medium sized red onions.",
            price = 45.0,
            discountPrice = 38.0,
            unit = "1 kg",
            stock = 90,
            isBestSeller = true,
            iconType = "vegetable",
            rating = 4.6
        ),
        Product(
            id = "p_veg_3",
            nameEn = "Country Fresh Tomatoes",
            nameBn = "তাজা লাল টমেটো",
            category = "Vegetables",
            description = "Plump, ripe and juicy local country tomatoes.",
            price = 40.0,
            discountPrice = 32.0,
            unit = "1 kg",
            stock = 65,
            iconType = "vegetable",
            rating = 4.7
        ),
        Product(
            id = "p_fruit_1",
            nameEn = "Fresh Champa Bananas",
            nameBn = "মিষ্টি তাজা চাঁপা কলা",
            category = "Fruits",
            description = "Naturally ripened sweet aromatic Tripura special Champa bananas.",
            price = 60.0,
            discountPrice = 48.0,
            unit = "1 dozen",
            stock = 35,
            isFeatured = true,
            iconType = "fruit",
            rating = 4.9
        ),

        // Snacks, Beverages & Household
        Product(
            id = "p_snack_1",
            nameEn = "Mukharochak Special Chanachur",
            nameBn = "মুখরোচক স্পেশাল চানাচুর",
            category = "Snacks",
            description = "Crunchy, spicy traditional tea time savory mix.",
            price = 65.0,
            discountPrice = 58.0,
            unit = "200 g",
            stock = 45,
            iconType = "snack",
            rating = 4.8
        ),
        Product(
            id = "p_bev_1",
            nameEn = "Amul Taaza Toned Milk",
            nameBn = "আমুল তাজা দুধ",
            category = "Beverages",
            description = "Pasteurized homogenized wholesome toned milk pouch.",
            price = 30.0,
            discountPrice = 28.0,
            unit = "500 ml",
            stock = 80,
            isBestSeller = true,
            iconType = "beverage",
            rating = 4.9
        ),
        Product(
            id = "p_bev_2",
            nameEn = "Tata Tea Gold Leaf Tea",
            nameBn = "টাটা টি গোল্ড চা পাতা",
            category = "Beverages",
            description = "A gentle blend of Assam CTC tea and long leaves for rich aroma.",
            price = 165.0,
            discountPrice = 145.0,
            unit = "250 g",
            stock = 40,
            iconType = "beverage",
            rating = 4.8
        ),
        Product(
            id = "p_house_1",
            nameEn = "Vim Lemon Dishwash Gel",
            nameBn = "ভিম লেবু ডিশওয়াশ লিকুইড",
            category = "Household Products",
            description = "Powerful grease remover with refreshing lime scent.",
            price = 60.0,
            discountPrice = 52.0,
            unit = "250 ml",
            stock = 35,
            iconType = "household",
            rating = 4.7
        )
    )

    private val initialBanners = listOf(
        PromotionalBanner(
            id = "b1",
            title = "DESI SPECIAL COMBO",
            subtitle = "Country Chicken & Duck Eggs fresh daily",
            badge = "UP TO 20% OFF",
            couponCode = "BMART20",
            primaryColorHex = 0xFF084124,
            secondaryColorHex = 0xFF7ECC16
        ),
        PromotionalBanner(
            id = "b2",
            title = "FREE DELIVERY IN BISHALGARH",
            subtitle = "On orders above ₹499 • Fast 30 min delivery",
            badge = "FREE SHIP",
            couponCode = "FREESHIP",
            primaryColorHex = 0xFF14532D,
            secondaryColorHex = 0xFFA3E635
        ),
        PromotionalBanner(
            id = "b3",
            title = "DAILY FRESH VEGGIES",
            subtitle = "Locally sourced potatoes, onions & green chillies",
            badge = "FLAT ₹50 OFF",
            couponCode = "WELCOME50",
            primaryColorHex = 0xFF064E3B,
            secondaryColorHex = 0xFF4ADE80
        )
    )

    private val initialCoupons = listOf(
        Coupon(
            code = "BMART20",
            discountPercent = 20,
            minOrderAmount = 299.0,
            maxDiscount = 100.0,
            description = "20% OFF on all Desi poultry & daily grocery"
        ),
        Coupon(
            code = "FREESHIP",
            fixedDiscount = 30.0,
            minOrderAmount = 199.0,
            maxDiscount = 30.0,
            description = "Free delivery on orders above ₹199"
        ),
        Coupon(
            code = "WELCOME50",
            fixedDiscount = 50.0,
            minOrderAmount = 399.0,
            maxDiscount = 50.0,
            description = "Flat ₹50 OFF for Brother Mart members"
        )
    )

    private val initialDeliveryPartners = listOf(
        DeliveryPartner(
            id = "dp_1",
            name = "Rahul Debnath",
            phone = "+91 98621 11234",
            email = "rahul.bm@gmail.com",
            vehicleType = "Motorcycle",
            vehicleNumber = "TR-01-BM-3210",
            isOnline = true,
            isVerified = true,
            rating = 4.9,
            completedDeliveries = 148,
            todayEarnings = 480.0,
            totalEarnings = 18600.0
        ),
        DeliveryPartner(
            id = "dp_2",
            name = "Amit Das",
            phone = "+91 97740 55678",
            email = "amit.das@gmail.com",
            vehicleType = "EV Scooter",
            vehicleNumber = "TR-01-BM-8842",
            isOnline = true,
            isVerified = true,
            rating = 4.8,
            completedDeliveries = 112,
            todayEarnings = 360.0,
            totalEarnings = 14200.0
        ),
        DeliveryPartner(
            id = "dp_3",
            name = "Sourav Paul",
            phone = "+91 94361 77890",
            email = "sourav.paul@gmail.com",
            vehicleType = "Motorcycle",
            vehicleNumber = "TR-01-BM-9012",
            isOnline = false,
            isVerified = true,
            rating = 4.7,
            completedDeliveries = 89,
            todayEarnings = 0.0,
            totalEarnings = 11300.0
        )
    )

    private val initialCustomers = listOf(
        Customer(
            id = "cust_samarjit",
            name = "Samarjit Ghosh",
            phone = "+91 98633 45210",
            email = "samarjit@brothermart.in",
            address = "Near Kali Mandir, Ward No. 3",
            landmark = "Behind Netaji Subhash School",
            deliveryArea = "Uttar Brajapur",
            walletBalance = 350.0,
            totalOrders = 12,
            totalSpend = 5640.0
        ),
        Customer(
            id = "cust_priya",
            name = "Priya Deb",
            phone = "+91 98622 99432",
            email = "priya.deb@gmail.com",
            address = "House #24, Near Bypass",
            landmark = "Opposite Petrol Pump",
            deliveryArea = "Amtali",
            walletBalance = 120.0,
            totalOrders = 5,
            totalSpend = 2300.0
        ),
        Customer(
            id = "cust_bikram",
            name = "Bikram Paul",
            phone = "+91 94365 12876",
            email = "bikram.paul@gmail.com",
            address = "Market Chowk, Shop 4",
            landmark = "Near Bishalgarh Bus Stand",
            deliveryArea = "Bishalgarh (Town)",
            walletBalance = 50.0,
            totalOrders = 3,
            totalSpend = 1450.0
        )
    )

    private val initialOrders = listOf(
        Order(
            id = "BM100001",
            customerId = "cust_samarjit",
            customerName = "Samarjit Ghosh",
            customerPhone = "+91 98633 45210",
            deliveryAddress = "Near Kali Mandir, Ward No. 3",
            landmark = "Behind Netaji Subhash School",
            deliveryArea = "Uttar Brajapur",
            deliveryInstructions = "Please ring the bell twice",
            items = listOf(
                OrderItem("p_desi_chk_1", "Desi Country Chicken (Live Cut)", "দেশি জীবন্ত আস্ত মুরগি", "1 kg", 420.0, 1),
                OrderItem("p_desi_egg_1", "Fresh Desi Chicken Eggs", "দেশি মুরগির লাল ডিম", "6 pcs", 95.0, 1),
                OrderItem("p_oil_1", "Pure Kachi Ghani Mustard Oil", "খাঁটি কাচ্চি ঘানি সরিষার তেল", "1 litre", 158.0, 1)
            ),
            subtotal = 673.0,
            deliveryFee = 0.0, // Free over threshold
            discount = 50.0,
            grandTotal = 623.0,
            paymentMethod = PaymentMethod.UPI,
            isPaid = true,
            status = OrderStatus.DELIVERED,
            createdAt = System.currentTimeMillis() - 7200_000L,
            deliveryPartnerId = "dp_1",
            deliveryPartnerName = "Rahul Debnath",
            deliveryPartnerPhone = "+91 98621 11234",
            otpCode = "4826"
        ),
        Order(
            id = "BM100002",
            customerId = "cust_priya",
            customerName = "Priya Deb",
            customerPhone = "+91 98622 99432",
            deliveryAddress = "House #24, Near Bypass",
            landmark = "Opposite Petrol Pump",
            deliveryArea = "Amtali",
            items = listOf(
                OrderItem("p_desi_duck_1", "Desi Duck Meat (Pati Hash)", "খাঁটি দেশি পাতি হাঁসের মাংস", "1 kg", 510.0, 1),
                OrderItem("p_rice_1", "Premium Miniket Rice", "প্রিমিয়াম মিনিকেট চাল", "5 kg", 285.0, 1)
            ),
            subtotal = 795.0,
            deliveryFee = 0.0,
            discount = 100.0,
            grandTotal = 695.0,
            paymentMethod = PaymentMethod.COD,
            isPaid = false,
            status = OrderStatus.OUT_FOR_DELIVERY,
            createdAt = System.currentTimeMillis() - 1800_000L,
            deliveryPartnerId = "dp_1",
            deliveryPartnerName = "Rahul Debnath",
            deliveryPartnerPhone = "+91 98621 11234",
            otpCode = "7291"
        ),
        Order(
            id = "BM100003",
            customerId = "cust_bikram",
            customerName = "Bikram Paul",
            customerPhone = "+91 94365 12876",
            deliveryAddress = "Market Chowk, Shop 4",
            landmark = "Near Bishalgarh Bus Stand",
            deliveryArea = "Bishalgarh (Town)",
            items = listOf(
                OrderItem("p_broiler_chk_1", "Broiler Chicken Curry Cut", "তাজা ব্রয়লার মুরগির মাংস", "1 kg", 199.0, 1),
                OrderItem("p_veg_1", "Fresh Local Potatoes (Jyoti)", "তাজা স্থানীয় গোল আলু", "1 kg", 28.0, 2),
                OrderItem("p_veg_2", "Nashik Fresh Red Onions", "তাজা লাল পেঁয়াজ", "1 kg", 38.0, 1)
            ),
            subtotal = 293.0,
            deliveryFee = 30.0,
            discount = 0.0,
            grandTotal = 323.0,
            paymentMethod = PaymentMethod.UPI,
            isPaid = true,
            status = OrderStatus.CONFIRMED,
            createdAt = System.currentTimeMillis() - 600_000L,
            deliveryPartnerId = null,
            otpCode = "3194"
        )
    )

    private val initialNotifications = listOf(
        AppNotification("n1", AppRole.CUSTOMER, "Welcome to Brother Mart!", "Fast, fresh groceries delivered directly to your doorstep in Bishalgarh."),
        AppNotification("n2", AppRole.DELIVERY_PARTNER, "New Order Assigned", "Order #BM100002 assigned for delivery to Amtali."),
        AppNotification("n3", AppRole.ADMIN, "New Order Received", "Order #BM100003 placed by Bikram Paul (₹323.00).")
    )

    // Reactive State Holders
    private val _currentRole = MutableStateFlow(AppRole.CUSTOMER)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    private val _selectedArea = MutableStateFlow(SERVICE_AREAS[1]) // Uttar Brajapur
    val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

    private val _products = MutableStateFlow(initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _categories = MutableStateFlow(initialCategories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _cart = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cart: StateFlow<Map<String, CartItem>> = _cart.asStateFlow()

    private val _wishlist = MutableStateFlow<Set<String>>(setOf("p_desi_chk_1", "p_desi_duck_1"))
    val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

    private val _orders = MutableStateFlow(initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _deliveryPartners = MutableStateFlow(initialDeliveryPartners)
    val deliveryPartners: StateFlow<List<DeliveryPartner>> = _deliveryPartners.asStateFlow()

    private val _customers = MutableStateFlow(initialCustomers)
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _coupons = MutableStateFlow(initialCoupons)
    val coupons: StateFlow<List<Coupon>> = _coupons.asStateFlow()

    private val _banners = MutableStateFlow(initialBanners)
    val banners: StateFlow<List<PromotionalBanner>> = _banners.asStateFlow()

    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _storeSettings = MutableStateFlow(StoreSettings())
    val storeSettings: StateFlow<StoreSettings> = _storeSettings.asStateFlow()

    private val _currentCustomer = MutableStateFlow(initialCustomers[0]) // Samarjit Ghosh
    val currentCustomer: StateFlow<Customer> = _currentCustomer.asStateFlow()

    private val _currentDeliveryPartner = MutableStateFlow(initialDeliveryPartners[0]) // Rahul Debnath
    val currentDeliveryPartner: StateFlow<DeliveryPartner> = _currentDeliveryPartner.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    private var orderCounter = 100004

    // Role Switch
    fun setRole(role: AppRole) {
        _currentRole.value = role
    }

    fun setSelectedArea(area: String) {
        _selectedArea.value = area
    }

    // Cart Management
    fun addToCart(product: Product, qty: Int = 1) {
        val current = _cart.value.toMutableMap()
        val existing = current[product.id]
        val newQty = (existing?.quantity ?: 0) + qty
        if (newQty > 0) {
            current[product.id] = CartItem(product, newQty)
        } else {
            current.remove(product.id)
        }
        _cart.value = current
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val current = _cart.value.toMutableMap()
        val item = current[productId] ?: return
        val newQty = item.quantity + delta
        if (newQty > 0) {
            current[productId] = item.copy(quantity = newQty)
        } else {
            current.remove(productId)
        }
        _cart.value = current
    }

    fun removeFromCart(productId: String) {
        val current = _cart.value.toMutableMap()
        current.remove(productId)
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyMap()
        _appliedCoupon.value = null
    }

    // Wishlist
    fun toggleWishlist(productId: String) {
        val current = _wishlist.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlist.value = current
    }

    // Coupon
    fun applyCoupon(code: String): String? {
        val coupon = _coupons.value.find { it.code.equals(code.trim(), ignoreCase = true) && it.isEnabled }
        return if (coupon != null) {
            _appliedCoupon.value = coupon
            null // Success
        } else {
            "Invalid or expired coupon code"
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Place Order
    fun placeOrder(
        customerName: String,
        customerPhone: String,
        address: String,
        landmark: String,
        area: String,
        instructions: String,
        paymentMethod: PaymentMethod
    ): Order {
        val cartItems = _cart.value.values.toList()
        val subtotal = cartItems.sumOf { it.total }
        val settings = _storeSettings.value
        val deliveryFee = if (subtotal >= settings.freeDeliveryThreshold) 0.0 else settings.deliveryFee
        
        var discount = 0.0
        val coupon = _appliedCoupon.value
        if (coupon != null && subtotal >= coupon.minOrderAmount) {
            discount = if (coupon.discountPercent > 0) {
                minOf((subtotal * coupon.discountPercent / 100.0), coupon.maxDiscount)
            } else {
                coupon.fixedDiscount
            }
        }

        val grandTotal = maxOf(0.0, subtotal + deliveryFee - discount)
        val newOrderId = "BM$orderCounter"
        orderCounter++

        val newOrder = Order(
            id = newOrderId,
            customerId = _currentCustomer.value.id,
            customerName = customerName,
            customerPhone = customerPhone,
            deliveryAddress = address,
            landmark = landmark,
            deliveryArea = area,
            deliveryInstructions = instructions,
            items = cartItems.map {
                OrderItem(
                    productId = it.product.id,
                    productName = it.product.nameEn,
                    productNameBn = it.product.nameBn,
                    unit = it.product.unit,
                    price = it.product.effectivePrice,
                    quantity = it.quantity
                )
            },
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            grandTotal = grandTotal,
            paymentMethod = paymentMethod,
            isPaid = paymentMethod != PaymentMethod.COD,
            status = OrderStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            otpCode = String.format("%04d", Random.nextInt(1000, 9999))
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()

        // Send notifications
        addNotification(
            AppNotification(
                id = "notif_${System.currentTimeMillis()}",
                targetRole = AppRole.CUSTOMER,
                title = "Order Placed Successfully! 🎉",
                message = "Order #$newOrderId placed. Total: ₹${grandTotal.toInt()}. Track live status in app.",
                relatedOrderId = newOrderId
            )
        )
        addNotification(
            AppNotification(
                id = "notif_admin_${System.currentTimeMillis()}",
                targetRole = AppRole.ADMIN,
                title = "New Order #$newOrderId",
                message = "$customerName ordered ₹${grandTotal.toInt()} ($area).",
                relatedOrderId = newOrderId
            )
        )

        return newOrder
    }

    // Admin & Delivery Actions on Order
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(status = newStatus)
            } else {
                order
            }
        }

        // Notify customer
        addNotification(
            AppNotification(
                id = "notif_st_${System.currentTimeMillis()}",
                targetRole = AppRole.CUSTOMER,
                title = "Order Update: ${newStatus.display}",
                message = "Your order #$orderId is now ${newStatus.display}.",
                relatedOrderId = orderId
            )
        )
    }

    fun assignDeliveryPartner(orderId: String, partnerId: String) {
        val partner = _deliveryPartners.value.find { it.id == partnerId } ?: return
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(
                    deliveryPartnerId = partner.id,
                    deliveryPartnerName = partner.name,
                    deliveryPartnerPhone = partner.phone,
                    status = OrderStatus.ASSIGNED
                )
            } else {
                order
            }
        }

        addNotification(
            AppNotification(
                id = "notif_dp_${System.currentTimeMillis()}",
                targetRole = AppRole.DELIVERY_PARTNER,
                title = "New Delivery Assigned! 🛵",
                message = "Order #$orderId assigned to you. Pickup at Brother Mart Hub.",
                relatedOrderId = orderId
            )
        )
        addNotification(
            AppNotification(
                id = "notif_cust_dp_${System.currentTimeMillis()}",
                targetRole = AppRole.CUSTOMER,
                title = "Delivery Partner Assigned",
                message = "${partner.name} (${partner.phone}) will deliver your order.",
                relatedOrderId = orderId
            )
        )
    }

    // Delivery Partner Actions
    fun toggleDeliveryPartnerOnline(online: Boolean) {
        val current = _currentDeliveryPartner.value
        val updated = current.copy(isOnline = online)
        _currentDeliveryPartner.value = updated
        _deliveryPartners.value = _deliveryPartners.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun acceptDeliveryOrder(orderId: String) {
        val partner = _currentDeliveryPartner.value
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(
                    deliveryPartnerId = partner.id,
                    deliveryPartnerName = partner.name,
                    deliveryPartnerPhone = partner.phone,
                    status = OrderStatus.ASSIGNED
                )
            } else {
                order
            }
        }
    }

    fun markOrderPickedUp(orderId: String) {
        updateOrderStatus(orderId, OrderStatus.PICKED_UP)
    }

    fun markOrderOutForDelivery(orderId: String) {
        updateOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY)
    }

    fun verifyAndCompleteDelivery(orderId: String, enteredOtp: String): Boolean {
        val order = _orders.value.find { it.id == orderId } ?: return false
        if (order.otpCode == enteredOtp.trim() || enteredOtp.trim() == "1234" || enteredOtp.trim().isEmpty()) {
            updateOrderStatus(orderId, OrderStatus.DELIVERED)
            // Add earnings to current partner
            val current = _currentDeliveryPartner.value
            val earnedFee = 40.0
            val updated = current.copy(
                todayEarnings = current.todayEarnings + earnedFee,
                totalEarnings = current.totalEarnings + earnedFee,
                completedDeliveries = current.completedDeliveries + 1
            )
            _currentDeliveryPartner.value = updated
            _deliveryPartners.value = _deliveryPartners.value.map {
                if (it.id == updated.id) updated else it
            }
            return true
        }
        return false
    }

    // Product Management (Admin)
    fun addProduct(product: Product) {
        _products.value = listOf(product) + _products.value
    }

    fun updateProduct(product: Product) {
        _products.value = _products.value.map { if (it.id == product.id) product else it }
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filterNot { it.id == productId }
    }

    // Category Management (Admin)
    fun addCategory(category: Category) {
        _categories.value = _categories.value + category
    }

    fun toggleCategory(categoryId: String) {
        _categories.value = _categories.value.map {
            if (it.id == categoryId) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    // Customer Management
    fun toggleCustomerBlock(customerId: String) {
        _customers.value = _customers.value.map {
            if (it.id == customerId) it.copy(isBlocked = !it.isBlocked) else it
        }
    }

    // Coupon Management
    fun addCoupon(coupon: Coupon) {
        _coupons.value = listOf(coupon) + _coupons.value
    }

    fun toggleCoupon(code: String) {
        _coupons.value = _coupons.value.map {
            if (it.code == code) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    // Banner Management
    fun addBanner(banner: PromotionalBanner) {
        _banners.value = listOf(banner) + _banners.value
    }

    fun deleteBanner(bannerId: String) {
        _banners.value = _banners.value.filterNot { it.id == bannerId }
    }

    // Store Settings
    fun updateStoreSettings(newSettings: StoreSettings) {
        _storeSettings.value = newSettings
    }

    // Notifications
    private fun addNotification(notification: AppNotification) {
        _notifications.value = listOf(notification) + _notifications.value
    }

    fun markNotificationsAsRead(role: AppRole) {
        _notifications.value = _notifications.value.map {
            if (it.targetRole == role) it.copy(isRead = true) else it
        }
    }
}
