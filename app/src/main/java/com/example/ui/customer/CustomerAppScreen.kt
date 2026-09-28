package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRole
import com.example.data.BrotherMartRepository
import com.example.data.CartItem
import com.example.data.Category
import com.example.data.Order
import com.example.data.OrderStatus
import com.example.data.PaymentMethod
import com.example.data.Product
import com.example.data.PromotionalBanner
import com.example.ui.common.BrotherMartHeader
import com.example.ui.common.ProductVisual
import com.example.ui.theme.DarkGreenHeader
import com.example.ui.theme.DarkGreenPrimary
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LimeGreenAccent
import com.example.ui.theme.LimeGreenLight
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusOutForDelivery
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPreparing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerAppScreen(
    onSwitchRole: (AppRole) -> Unit
) {
    val repository = BrotherMartRepository
    val products by repository.products.collectAsState()
    val categories by repository.categories.collectAsState()
    val cart by repository.cart.collectAsState()
    val wishlist by repository.wishlist.collectAsState()
    val orders by repository.orders.collectAsState()
    val customer by repository.currentCustomer.collectAsState()
    val selectedArea by repository.selectedArea.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val banners by repository.banners.collectAsState()
    val appliedCoupon by repository.appliedCoupon.collectAsState()
    val settings by repository.storeSettings.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Categories, 2: Orders, 3: Wishlist, 4: Profile
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var detailedProduct by remember { mutableStateOf<Product?>(null) }
    var showCartSheet by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showAreaSelector by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var orderPlacedSuccess by remember { mutableStateOf<Order?>(null) }

    val customerUnreadNotifs = notifications.count { it.targetRole == AppRole.CUSTOMER && !it.isRead }
    val cartItemCount = cart.values.sumOf { it.quantity }

    Scaffold(
        topBar = {
            BrotherMartHeader(
                currentRole = AppRole.CUSTOMER,
                selectedArea = selectedArea,
                cartItemCount = cartItemCount,
                unreadNotificationCount = customerUnreadNotifs,
                onRoleChange = onSwitchRole,
                onAreaClick = { showAreaSelector = true },
                onCartClick = { showCartSheet = true },
                onNotificationClick = {
                    showNotificationsSheet = true
                    repository.markNotificationsAsRead(AppRole.CUSTOMER)
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreenPrimary,
                        selectedTextColor = DarkGreenPrimary,
                        indicatorColor = LimeGreenLight
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.GridView, contentDescription = "Categories") },
                    label = { Text("Categories", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreenPrimary,
                        selectedTextColor = DarkGreenPrimary,
                        indicatorColor = LimeGreenLight
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Orders") },
                    label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreenPrimary,
                        selectedTextColor = DarkGreenPrimary,
                        indicatorColor = LimeGreenLight
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Wishlist") },
                    label = { Text("Wishlist", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreenPrimary,
                        selectedTextColor = DarkGreenPrimary,
                        indicatorColor = LimeGreenLight
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreenPrimary,
                        selectedTextColor = DarkGreenPrimary,
                        indicatorColor = LimeGreenLight
                    )
                )
            }
        },
        containerColor = SoftGreenBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> CustomerHomeScreen(
                    products = products,
                    categories = categories,
                    banners = banners,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategoryFilter,
                    wishlist = wishlist,
                    cart = cart,
                    onSearchChange = { searchQuery = it },
                    onCategorySelect = {
                        selectedCategoryFilter = if (selectedCategoryFilter == it) null else it
                    },
                    onProductClick = { detailedProduct = it },
                    onAddToCart = { repository.addToCart(it, 1) },
                    onUpdateCartQty = { pId, delta -> repository.updateCartQuantity(pId, delta) },
                    onToggleWishlist = { repository.toggleWishlist(it.id) },
                    onBannerClick = { banner ->
                        banner.couponCode?.let {
                            repository.applyCoupon(it)
                            showCartSheet = true
                        }
                    },
                    onNavigateToCategories = { selectedTab = 1 }
                )
                1 -> CustomerCategoriesScreen(
                    categories = categories,
                    products = products,
                    wishlist = wishlist,
                    cart = cart,
                    onProductClick = { detailedProduct = it },
                    onAddToCart = { repository.addToCart(it, 1) },
                    onUpdateCartQty = { pId, delta -> repository.updateCartQuantity(pId, delta) },
                    onToggleWishlist = { repository.toggleWishlist(it.id) }
                )
                2 -> CustomerOrdersScreen(
                    orders = orders,
                    onReorder = { order ->
                        order.items.forEach { item ->
                            products.find { it.id == item.productId }?.let {
                                repository.addToCart(it, item.quantity)
                            }
                        }
                        showCartSheet = true
                    }
                )
                3 -> CustomerWishlistScreen(
                    wishlistProductIds = wishlist,
                    products = products,
                    cart = cart,
                    onProductClick = { detailedProduct = it },
                    onAddToCart = { repository.addToCart(it, 1) },
                    onRemoveWishlist = { repository.toggleWishlist(it) },
                    onStartShopping = { selectedTab = 0 }
                )
                4 -> CustomerProfileScreen(
                    customer = customer,
                    ordersCount = orders.size,
                    settings = settings,
                    onSwitchRole = onSwitchRole,
                    onViewOrders = { selectedTab = 2 },
                    onViewWishlist = { selectedTab = 3 }
                )
            }

            // Floating View Cart Bar if Cart has items
            if (cartItemCount > 0 && !showCartSheet && !showCheckoutDialog) {
                val totalAmount = cart.values.sumOf { it.total }
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable { showCartSheet = true }
                        .testTag("floating_cart_bar"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreenPrimary),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(LimeGreenAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartItemCount.toString(),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "$cartItemCount Items Added",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "₹${totalAmount.toInt()}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "View Cart",
                                color = LimeGreenAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "View Cart",
                                tint = LimeGreenAccent,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Product Detail Dialog
    detailedProduct?.let { product ->
        ProductDetailDialog(
            product = product,
            isInWishlist = wishlist.contains(product.id),
            cartQuantity = cart[product.id]?.quantity ?: 0,
            onDismiss = { detailedProduct = null },
            onAddToCart = { qty -> repository.addToCart(product, qty) },
            onUpdateQty = { delta -> repository.updateCartQuantity(product.id, delta) },
            onToggleWishlist = { repository.toggleWishlist(product.id) },
            onBuyNow = {
                repository.addToCart(product, 1)
                detailedProduct = null
                showCheckoutDialog = true
            }
        )
    }

    // Cart Bottom Sheet
    if (showCartSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCartSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            CartSheetContent(
                cart = cart,
                appliedCoupon = appliedCoupon,
                settings = settings,
                onUpdateQty = { pId, delta -> repository.updateCartQuantity(pId, delta) },
                onRemoveItem = { repository.removeFromCart(it) },
                onApplyCoupon = { code -> repository.applyCoupon(code) },
                onRemoveCoupon = { repository.removeCoupon() },
                onCheckout = {
                    showCartSheet = false
                    showCheckoutDialog = true
                },
                onClose = { showCartSheet = false }
            )
        }
    }

    // Checkout Dialog
    if (showCheckoutDialog) {
        CheckoutDialog(
            customer = customer,
            cart = cart,
            appliedCoupon = appliedCoupon,
            settings = settings,
            serviceAreas = BrotherMartRepository.SERVICE_AREAS,
            onDismiss = { showCheckoutDialog = false },
            onPlaceOrder = { name, phone, address, landmark, area, instructions, paymentMethod ->
                val newOrder = repository.placeOrder(
                    customerName = name,
                    customerPhone = phone,
                    address = address,
                    landmark = landmark,
                    area = area,
                    instructions = instructions,
                    paymentMethod = paymentMethod
                )
                showCheckoutDialog = false
                orderPlacedSuccess = newOrder
                selectedTab = 2 // Switch to Orders tab
            }
        )
    }

    // Success Order Placement Modal
    orderPlacedSuccess?.let { placedOrder ->
        AlertDialog(
            onDismissRequest = { orderPlacedSuccess = null },
            confirmButton = {
                Button(
                    onClick = { orderPlacedSuccess = null },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Track Order Now")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = DarkGreenPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Order Placed!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Thank you! Your order #${placedOrder.id} has been received.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total: ₹${placedOrder.grandTotal.toInt()} • ${placedOrder.paymentMethod.label}",
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Delivery to: ${placedOrder.deliveryArea}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your 4-Digit Delivery Security OTP: ${placedOrder.otpCode}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFFC62828)
                    )
                }
            }
        )
    }

    // Area Selector Dialog
    if (showAreaSelector) {
        AlertDialog(
            onDismissRequest = { showAreaSelector = false },
            title = { Text("Select Delivery Area", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Brother Mart currently serves the following Bishalgarh zones:",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BrotherMartRepository.SERVICE_AREAS.forEach { area ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    repository.setSelectedArea(area)
                                    showAreaSelector = false
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (selectedArea == area) DarkGreenPrimary else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = area,
                                fontWeight = if (selectedArea == area) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedArea == area) DarkGreenPrimary else Color.Black,
                                fontSize = 14.sp
                            )
                            if (selectedArea == area) {
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Default.Check, contentDescription = null, tint = DarkGreenPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAreaSelector = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Notifications Bottom Sheet
    if (showNotificationsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationsSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notifications", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { showNotificationsSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val customerNotifs = notifications.filter { it.targetRole == AppRole.CUSTOMER }
                if (customerNotifs.isEmpty()) {
                    Text("No notifications right now.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(customerNotifs) { notif ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = SoftGreenBackground)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(notif.message, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// 1. HOME SCREEN COMPONENT
// -------------------------------------------------------------
@Composable
fun CustomerHomeScreen(
    products: List<Product>,
    categories: List<Category>,
    banners: List<PromotionalBanner>,
    searchQuery: String,
    selectedCategory: String?,
    wishlist: Set<String>,
    cart: Map<String, CartItem>,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onUpdateCartQty: (String, Int) -> Unit,
    onToggleWishlist: (Product) -> Unit,
    onBannerClick: (PromotionalBanner) -> Unit,
    onNavigateToCategories: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val filteredProducts = products.filter { product ->
        val matchesSearch = searchQuery.isBlank() ||
                product.nameEn.contains(searchQuery, ignoreCase = true) ||
                product.nameBn.contains(searchQuery, ignoreCase = true) ||
                product.category.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null || product.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }

    val recommendedProducts = products.filter { it.isFeatured || it.category == "Desi Chicken" || it.category == "Desi Duck" }
    val bestSellers = products.filter { it.isBestSeller }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Search Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input"),
                    placeholder = {
                        Text(
                            "Search for products (e.g. Desi chicken, rice, eggs)...",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = DarkGreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search", tint = Color.Gray)
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Promotional Banners Carousel
        if (searchQuery.isBlank() && selectedCategory == null) {
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(banners) { banner ->
                        Card(
                            modifier = Modifier
                                .width(310.dp)
                                .height(130.dp)
                                .clickable { onBannerClick(banner) },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(banner.primaryColorHex),
                                                Color(banner.secondaryColorHex)
                                            )
                                        )
                                    )
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth(0.72f)) {
                                    Surface(
                                        color = LimeGreenAccent,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = banner.badge,
                                            color = DarkGreenHeader,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = banner.title,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = banner.subtitle,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Coupon chip at bottom-right
                                banner.couponCode?.let { code ->
                                    Surface(
                                        modifier = Modifier.align(Alignment.BottomEnd),
                                        color = Color.White.copy(alpha = 0.9f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Discount,
                                                contentDescription = null,
                                                tint = DarkGreenPrimary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = code,
                                                color = DarkGreenPrimary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Category Pills
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenHeader
                    )
                    TextButton(onClick = onNavigateToCategories) {
                        Text("See All", color = DarkGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory.equals(cat.name, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelect(cat.name) },
                            label = {
                                Text(
                                    text = "${cat.name} (${cat.nameBn})",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DarkGreenPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color.Black
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) DarkGreenPrimary else Color(0xFFDDE6DF)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }
        }

        // If user is searching or has filtered a category, show matching products
        if (searchQuery.isNotBlank() || selectedCategory != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedCategory != null) "Category: $selectedCategory" else "Search Results",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenHeader
                    )
                    Text(
                        text = "${filteredProducts.size} items found",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            items(filteredProducts.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductCard(
                        product = pair[0],
                        isInWishlist = wishlist.contains(pair[0].id),
                        cartQuantity = cart[pair[0].id]?.quantity ?: 0,
                        onClick = { onProductClick(pair[0]) },
                        onAddToCart = { onAddToCart(pair[0]) },
                        onUpdateQty = { delta -> onUpdateCartQty(pair[0].id, delta) },
                        onToggleWishlist = { onToggleWishlist(pair[0]) },
                        modifier = Modifier.weight(1f)
                    )
                    if (pair.size > 1) {
                        ProductCard(
                            product = pair[1],
                            isInWishlist = wishlist.contains(pair[1].id),
                            cartQuantity = cart[pair[1].id]?.quantity ?: 0,
                            onClick = { onProductClick(pair[1]) },
                            onAddToCart = { onAddToCart(pair[1]) },
                            onUpdateQty = { delta -> onUpdateCartQty(pair[1].id, delta) },
                            onToggleWishlist = { onToggleWishlist(pair[1]) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            // "Special Offers / Recommended" Section
            item {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⭐ Recommended For You",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreenHeader
                            )
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        items(recommendedProducts) { product ->
                            ProductCard(
                                product = product,
                                isInWishlist = wishlist.contains(product.id),
                                cartQuantity = cart[product.id]?.quantity ?: 0,
                                onClick = { onProductClick(product) },
                                onAddToCart = { onAddToCart(product) },
                                onUpdateQty = { delta -> onUpdateCartQty(product.id, delta) },
                                onToggleWishlist = { onToggleWishlist(product) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }

            // "Best Sellers" Grid Section
            item {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "🔥 Best Sellers in Bishalgarh",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenHeader,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            items(bestSellers.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductCard(
                        product = pair[0],
                        isInWishlist = wishlist.contains(pair[0].id),
                        cartQuantity = cart[pair[0].id]?.quantity ?: 0,
                        onClick = { onProductClick(pair[0]) },
                        onAddToCart = { onAddToCart(pair[0]) },
                        onUpdateQty = { delta -> onUpdateCartQty(pair[0].id, delta) },
                        onToggleWishlist = { onToggleWishlist(pair[0]) },
                        modifier = Modifier.weight(1f)
                    )
                    if (pair.size > 1) {
                        ProductCard(
                            product = pair[1],
                            isInWishlist = wishlist.contains(pair[1].id),
                            cartQuantity = cart[pair[1].id]?.quantity ?: 0,
                            onClick = { onProductClick(pair[1]) },
                            onAddToCart = { onAddToCart(pair[1]) },
                            onUpdateQty = { delta -> onUpdateCartQty(pair[1].id, delta) },
                            onToggleWishlist = { onToggleWishlist(pair[1]) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Trust Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = DarkGreenPrimary)
                            Text("Fast Delivery", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("30 Mins", fontSize = 10.sp, color = Color.Gray)
                        }
                        Divider(modifier = Modifier.height(30.dp).width(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LimeGreenAccent)
                            Text("100% Fresh", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Village Desi", fontSize = 10.sp, color = Color.Gray)
                        }
                        Divider(modifier = Modifier.height(30.dp).width(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.QrCode, contentDescription = null, tint = DarkGreenPrimary)
                            Text("Easy Pay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("UPI / COD", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// REUSABLE PRODUCT CARD
// -------------------------------------------------------------
@Composable
fun ProductCard(
    product: Product,
    isInWishlist: Boolean,
    cartQuantity: Int,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    onUpdateQty: (Int) -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Visual + Wishlist icon + Discount Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentAlignment = Alignment.Center
            ) {
                ProductVisual(
                    iconType = product.iconType,
                    size = 72.dp,
                    shapeRadius = 14.dp
                )

                // Discount tag
                if (product.discountPercent > 0) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopStart),
                        color = Color(0xFFC62828),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${product.discountPercent}% OFF",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                // Wishlist Icon
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isInWishlist) Color(0xFFE53935) else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // English & Bengali Titles
            Text(
                text = product.nameEn,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.nameBn,
                fontSize = 11.sp,
                color = DarkGreenPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.unit,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            // Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${product.effectivePrice.toInt()}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = DarkGreenHeader
                )
                if (product.discountPrice > 0 && product.discountPrice < product.price) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "₹${product.price.toInt()}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart / Quantity Controller
            if (cartQuantity > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkGreenPrimary),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { onUpdateQty(-1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = cartQuantity.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    IconButton(
                        onClick = { onUpdateQty(1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            } else {
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = LimeGreenAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ADD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. CATEGORIES SCREEN
// -------------------------------------------------------------
@Composable
fun CustomerCategoriesScreen(
    categories: List<Category>,
    products: List<Product>,
    wishlist: Set<String>,
    cart: Map<String, CartItem>,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onUpdateCartQty: (String, Int) -> Unit,
    onToggleWishlist: (Product) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()?.name ?: "Desi Chicken") }
    val categoryProducts = products.filter { it.category.equals(selectedCategory, ignoreCase = true) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left rail: Categories list
        Column(
            modifier = Modifier
                .width(100.dp)
                .fillMaxSize()
                .background(Color(0xFFEAF2EB))
                .verticalScroll(rememberScrollState())
        ) {
            categories.forEach { cat ->
                val isSelected = cat.name.equals(selectedCategory, ignoreCase = true)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = cat.name }
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProductVisual(iconType = cat.iconKey, size = 38.dp, shapeRadius = 10.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = cat.name,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) DarkGreenPrimary else Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = cat.nameBn,
                        fontSize = 9.sp,
                        color = if (isSelected) DarkGreenPrimary else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
                Divider(color = Color(0xFFD6E2D8), thickness = 0.5.dp)
            }
        }

        // Right Content: Products in selected category
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = selectedCategory,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenHeader
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (categoryProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products currently in this category.", color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(categoryProducts) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onProductClick(product) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProductVisual(iconType = product.iconType, size = 64.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.nameEn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(product.nameBn, fontSize = 11.sp, color = DarkGreenPrimary)
                                    Text(product.unit, fontSize = 10.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "₹${product.effectivePrice.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DarkGreenHeader
                                        )
                                        if (product.discountPrice > 0) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "₹${product.price.toInt()}",
                                                fontSize = 10.sp,
                                                color = Color.Gray,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }
                                    }
                                }

                                val qty = cart[product.id]?.quantity ?: 0
                                if (qty > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(DarkGreenPrimary)
                                    ) {
                                        IconButton(onClick = { onUpdateCartQty(product.id, -1) }, modifier = Modifier.size(28.dp)) {
                                            Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                        Text(qty.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        IconButton(onClick = { onUpdateCartQty(product.id, 1) }, modifier = Modifier.size(28.dp)) {
                                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { onAddToCart(product) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("ADD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. ORDERS SCREEN (LIVE TRACKING)
// -------------------------------------------------------------
@Composable
fun CustomerOrdersScreen(
    orders: List<Order>,
    onReorder: (Order) -> Unit
) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("No orders placed yet.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Order fresh groceries and Desi chicken to see live status!", color = Color.Gray, fontSize = 12.sp)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                Text(
                    text = "My Orders & Live Tracking",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreenHeader,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            items(orders) { order ->
                OrderCard(order = order, onReorder = { onReorder(order) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onReorder: () -> Unit
) {
    var expandedTracking by remember { mutableStateOf(order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Order ID + Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Order #${order.id}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = DarkGreenHeader)
                    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.createdAt))
                    Text(dateStr, fontSize = 11.sp, color = Color.Gray)
                }

                val (badgeBg, badgeText, badgeColor) = when (order.status) {
                    OrderStatus.PENDING -> Triple(Color(0xFFFFF3E0), "Placed", Color(0xFFE65100))
                    OrderStatus.CONFIRMED -> Triple(Color(0xFFE1F5FE), "Confirmed", Color(0xFF0288D1))
                    OrderStatus.PREPARING -> Triple(Color(0xFFF3E5F5), "Preparing", Color(0xFF7B1FA2))
                    OrderStatus.READY_FOR_PICKUP -> Triple(Color(0xFFEDE7F6), "Ready", Color(0xFF512DA8))
                    OrderStatus.ASSIGNED -> Triple(Color(0xFFE8F5E9), "Assigned", Color(0xFF2E7D32))
                    OrderStatus.PICKED_UP -> Triple(Color(0xFFFFF8E1), "Picked Up", Color(0xFFF57F17))
                    OrderStatus.OUT_FOR_DELIVERY -> Triple(Color(0xFFFFE0B2), "Out for Delivery", Color(0xFFE65100))
                    OrderStatus.DELIVERED -> Triple(Color(0xFFE8F5E9), "Delivered", Color(0xFF2E7D32))
                    OrderStatus.CANCELLED -> Triple(Color(0xFFFFEBEE), "Cancelled", Color(0xFFC62828))
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(10.dp))

            // Items List
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}x ${item.productName} (${item.unit})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text("₹${item.total.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total (${order.paymentMethod.label})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "₹${order.grandTotal.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreenPrimary
                )
            }

            // Delivery OTP for verification
            if (order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF9FBE7),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery OTP (Share upon arrival):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text(order.otpCode, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DarkGreenPrimary)
                    }
                }
            }

            // Assigned Delivery Partner Info if assigned
            order.deliveryPartnerName?.let { partnerName ->
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = SoftGreenBackground,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = DarkGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Delivery Partner: $partnerName", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            order.deliveryPartnerPhone?.let {
                                Text("Contact: $it", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expand / Collapse Live Tracking Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { expandedTracking = !expandedTracking },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (expandedTracking) "Hide Tracking Status ▲" else "Live Tracking Status ▼",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenPrimary
                    )
                }

                OutlinedButton(
                    onClick = onReorder,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Reorder", fontSize = 11.sp)
                }
            }

            // Detailed Live Stepper
            AnimatedVisibility(visible = expandedTracking) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    val steps = listOf(
                        OrderStatus.PENDING,
                        OrderStatus.CONFIRMED,
                        OrderStatus.PREPARING,
                        OrderStatus.READY_FOR_PICKUP,
                        OrderStatus.ASSIGNED,
                        OrderStatus.OUT_FOR_DELIVERY,
                        OrderStatus.DELIVERED
                    )

                    steps.forEachIndexed { index, step ->
                        val isDone = order.status.stepIndex >= step.stepIndex
                        val isCurrent = order.status == step

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCurrent -> LimeGreenAccent
                                            isDone -> DarkGreenPrimary
                                            else -> Color(0xFFDCDCDC)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step.display,
                                fontSize = 12.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isDone) DarkGreenHeader else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. WISHLIST SCREEN
// -------------------------------------------------------------
@Composable
fun CustomerWishlistScreen(
    wishlistProductIds: Set<String>,
    products: List<Product>,
    cart: Map<String, CartItem>,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onRemoveWishlist: (String) -> Unit,
    onStartShopping: () -> Unit
) {
    val wishlistProducts = products.filter { wishlistProductIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("My Wishlist", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(10.dp))

        if (wishlistProducts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Your Wishlist is Empty", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Explore fresh Desi chicken & organic grocery to save.", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartShopping,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                    ) {
                        Text("Start Shopping")
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(wishlistProducts) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onProductClick(product) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductVisual(iconType = product.iconType, size = 60.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.nameEn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(product.nameBn, fontSize = 11.sp, color = DarkGreenPrimary)
                                Text("₹${product.effectivePrice.toInt()} (${product.unit})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            IconButton(onClick = { onRemoveWishlist(product.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray)
                            }
                            Button(
                                onClick = { onAddToCart(product) },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("ADD", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. PROFILE SCREEN
// -------------------------------------------------------------
@Composable
fun CustomerProfileScreen(
    customer: com.example.data.Customer,
    ordersCount: Int,
    settings: com.example.data.StoreSettings,
    onSwitchRole: (AppRole) -> Unit,
    onViewOrders: () -> Unit,
    onViewWishlist: () -> Unit
) {
    var walletBal by remember { mutableStateOf(customer.walletBalance) }
    var showAddMoneyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(DarkGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customer.name.take(2).uppercase(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(customer.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(customer.phone, fontSize = 12.sp, color = Color.Gray)
                    Text(customer.email, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Wallet Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGreenPrimary),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Wallet, contentDescription = null, tint = LimeGreenAccent, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Brother Mart Wallet", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        Text("₹${walletBal.toInt()}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = { showAddMoneyDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreenAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Add Money", color = DarkGreenHeader, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Links
        Text("Account Options", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                ProfileOptionRow(icon = Icons.Default.ShoppingBag, title = "My Orders ($ordersCount)", onClick = onViewOrders)
                Divider(color = Color(0xFFF0F0F0))
                ProfileOptionRow(icon = Icons.Default.Favorite, title = "My Wishlist", onClick = onViewWishlist)
                Divider(color = Color(0xFFF0F0F0))
                ProfileOptionRow(
                    icon = Icons.Default.LocationOn,
                    title = "Saved Address: ${customer.deliveryArea}",
                    subtitle = "${customer.address}, ${customer.landmark}",
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text("Store Support & Info", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(settings.storeName, fontWeight = FontWeight.Bold, color = DarkGreenPrimary, fontSize = 14.sp)
                Text(settings.tagline, fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("📍 ${settings.address}", fontSize = 11.sp)
                Text("⏰ Hours: ${settings.openingHours}", fontSize = 11.sp)
                Text("📞 Phone/WhatsApp: ${settings.phone}", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text("Switch Panel / Demo Role", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onSwitchRole(AppRole.DELIVERY_PARTNER) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🛵 Delivery Partner", fontSize = 11.sp)
            }
            OutlinedButton(
                onClick = { onSwitchRole(AppRole.ADMIN) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("⚙️ Admin Panel", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }

    if (showAddMoneyDialog) {
        AlertDialog(
            onDismissRequest = { showAddMoneyDialog = false },
            title = { Text("Top Up Wallet") },
            text = { Text("Add ₹500 instant cashback credit to your Brother Mart wallet?") },
            confirmButton = {
                Button(
                    onClick = {
                        walletBal += 500.0
                        showAddMoneyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Add ₹500")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMoneyDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ProfileOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = DarkGreenPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            subtitle?.let {
                Text(it, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// -------------------------------------------------------------
// 6. PRODUCT DETAIL DIALOG
// -------------------------------------------------------------
@Composable
fun ProductDetailDialog(
    product: Product,
    isInWishlist: Boolean,
    cartQuantity: Int,
    onDismiss: () -> Unit,
    onAddToCart: (Int) -> Unit,
    onUpdateQty: (Int) -> Unit,
    onToggleWishlist: () -> Unit,
    onBuyNow: () -> Unit
) {
    var selectedQty by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header image + close
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProductVisual(iconType = product.iconType, size = 90.dp)
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(product.nameEn, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(product.nameBn, fontSize = 13.sp, color = DarkGreenPrimary, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Unit: ${product.unit} • In Stock (${product.stock} left)",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldStar, modifier = Modifier.size(16.dp))
                    Text(" ${product.rating} (${product.reviewsCount} reviews)", fontSize = 11.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(product.description, fontSize = 12.sp, color = Color(0xFF424242))

                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹${product.effectivePrice.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DarkGreenHeader)
                    if (product.discountPrice > 0 && product.discountPrice < product.price) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("₹${product.price.toInt()}", fontSize = 13.sp, textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(color = LimeGreenLight, shape = RoundedCornerShape(4.dp)) {
                            Text("${product.discountPercent}% OFF", color = DarkGreenHeader, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quantity selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Select Quantity:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEEEEEE))
                    ) {
                        IconButton(
                            onClick = { if (selectedQty > 1) selectedQty-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }
                        Text(selectedQty.toString(), fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(
                            onClick = { selectedQty++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons: Add to Cart & Buy Now
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onAddToCart(selectedQty)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add to Cart", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onBuyNow,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Buy Now", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    )
}

// -------------------------------------------------------------
// 7. CART SHEET CONTENT
// -------------------------------------------------------------
@Composable
fun CartSheetContent(
    cart: Map<String, CartItem>,
    appliedCoupon: com.example.data.Coupon?,
    settings: com.example.data.StoreSettings,
    onUpdateQty: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onApplyCoupon: (String) -> String?,
    onRemoveCoupon: () -> Unit,
    onCheckout: () -> Unit,
    onClose: () -> Unit
) {
    var couponInput by remember { mutableStateOf("") }
    var couponError by remember { mutableStateOf<String?>(null) }

    val items = cart.values.toList()
    val subtotal = items.sumOf { it.total }
    val deliveryFee = if (subtotal >= settings.freeDeliveryThreshold || subtotal == 0.0) 0.0 else settings.deliveryFee
    val discount = if (appliedCoupon != null && subtotal >= appliedCoupon.minOrderAmount) {
        if (appliedCoupon.discountPercent > 0) {
            minOf((subtotal * appliedCoupon.discountPercent / 100.0), appliedCoupon.maxDiscount)
        } else {
            appliedCoupon.fixedDiscount
        }
    } else 0.0
    val grandTotal = maxOf(0.0, subtotal + deliveryFee - discount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Shopping Cart (${items.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenHeader)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SoftGreenBackground)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductVisual(iconType = item.product.iconType, size = 48.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.product.nameEn, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("₹${item.product.effectivePrice.toInt()} × ${item.quantity}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Text("₹${item.total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onUpdateQty(item.product.id, -1) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                                Text(item.quantity.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { onUpdateQty(item.product.id, 1) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Coupon Code Row
            if (appliedCoupon == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponInput,
                        onValueChange = {
                            couponInput = it
                            couponError = null
                        },
                        placeholder = { Text("Enter coupon (e.g. BMART20, FREESHIP)", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val err = onApplyCoupon(couponInput)
                            couponError = err
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply")
                    }
                }
                couponError?.let {
                    Text(it, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            } else {
                Surface(
                    color = LimeGreenLight,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Coupon applied: ${appliedCoupon.code} (-₹${discount.toInt()})", color = DarkGreenHeader, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = onRemoveCoupon, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Remove coupon", tint = DarkGreenHeader)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bill Summary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7FAF7), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal", fontSize = 12.sp)
                    Text("₹${subtotal.toInt()}", fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Delivery Charge", fontSize = 12.sp)
                    Text(if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}", fontSize = 12.sp, color = if (deliveryFee == 0.0) DarkGreenPrimary else Color.Black)
                }
                if (discount > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount", fontSize = 12.sp, color = DarkGreenPrimary)
                        Text("-₹${discount.toInt()}", fontSize = 12.sp, color = DarkGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DarkGreenHeader)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("checkout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Proceed to Checkout • ₹${grandTotal.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// -------------------------------------------------------------
// 8. CHECKOUT DIALOG
// -------------------------------------------------------------
@Composable
fun CheckoutDialog(
    customer: com.example.data.Customer,
    cart: Map<String, CartItem>,
    appliedCoupon: com.example.data.Coupon?,
    settings: com.example.data.StoreSettings,
    serviceAreas: List<String>,
    onDismiss: () -> Unit,
    onPlaceOrder: (name: String, phone: String, address: String, landmark: String, area: String, instructions: String, paymentMethod: PaymentMethod) -> Unit
) {
    var name by remember { mutableStateOf(customer.name) }
    var phone by remember { mutableStateOf(customer.phone) }
    var address by remember { mutableStateOf(customer.address) }
    var landmark by remember { mutableStateOf(customer.landmark) }
    var selectedArea by remember { mutableStateOf(customer.deliveryArea) }
    var instructions by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.UPI) }

    val subtotal = cart.values.sumOf { it.total }
    val deliveryFee = if (subtotal >= settings.freeDeliveryThreshold) 0.0 else settings.deliveryFee
    val discount = if (appliedCoupon != null && subtotal >= appliedCoupon.minOrderAmount) {
        if (appliedCoupon.discountPercent > 0) minOf((subtotal * appliedCoupon.discountPercent / 100.0), appliedCoupon.maxDiscount) else appliedCoupon.fixedDiscount
    } else 0.0
    val total = maxOf(0.0, subtotal + deliveryFee - discount)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onPlaceOrder(name, phone, address, landmark, selectedArea, instructions, selectedPayment)
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                modifier = Modifier.testTag("confirm_place_order_button")
            ) {
                Text("Place Order (₹${total.toInt()})", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = {
            Text("Confirm Delivery & Order", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Delivery Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Delivery Area (Service Zone):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(serviceAreas) { area ->
                        FilterChip(
                            selected = selectedArea == area,
                            onClick = { selectedArea = area },
                            label = { Text(area, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Delivery Instructions (Optional)") },
                    placeholder = { Text("E.g. Ring bell, leave at door") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Payment Method:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                PaymentMethod.values().forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPayment = method }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPayment == method,
                            onClick = { selectedPayment = method }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(method.label, fontSize = 12.sp)
                    }
                }

                if (selectedPayment == PaymentMethod.UPI) {
                    Surface(
                        color = SoftGreenBackground,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            "Pay instantly via GooglePay, PhonePe, Paytm or UPI ID: ${settings.upiId}",
                            fontSize = 11.sp,
                            color = DarkGreenPrimary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    )
}
