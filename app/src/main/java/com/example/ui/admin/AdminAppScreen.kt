package com.example.ui.admin

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRole
import com.example.data.BrotherMartRepository
import com.example.data.Category
import com.example.data.Coupon
import com.example.data.Customer
import com.example.data.DeliveryPartner
import com.example.data.Order
import com.example.data.OrderStatus
import com.example.data.Product
import com.example.data.PromotionalBanner
import com.example.data.StoreSettings
import com.example.ui.common.BrotherMartHeader
import com.example.ui.common.ProductVisual
import com.example.ui.theme.DarkGreenHeader
import com.example.ui.theme.DarkGreenPrimary
import com.example.ui.theme.LimeGreenAccent
import com.example.ui.theme.LimeGreenLight
import com.example.ui.theme.SoftGreenBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAppScreen(
    onSwitchRole: (AppRole) -> Unit
) {
    val repository = BrotherMartRepository
    val orders by repository.orders.collectAsState()
    val products by repository.products.collectAsState()
    val categories by repository.categories.collectAsState()
    val customers by repository.customers.collectAsState()
    val deliveryPartners by repository.deliveryPartners.collectAsState()
    val coupons by repository.coupons.collectAsState()
    val banners by repository.banners.collectAsState()
    val settings by repository.storeSettings.collectAsState()
    val notifications by repository.notifications.collectAsState()

    var selectedAdminTab by remember { mutableIntStateOf(0) }
    val adminTabs = listOf(
        "Dashboard",
        "Orders",
        "Products",
        "Categories",
        "Customers",
        "Delivery",
        "Coupons",
        "Banners",
        "Settings",
        "Reports"
    )

    val adminUnreadNotifs = notifications.count { it.targetRole == AppRole.ADMIN && !it.isRead }

    // Dialog States
    var showAddProductDialog by remember { mutableStateOf(false) }
    var editProductTarget by remember { mutableStateOf<Product?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddCouponDialog by remember { mutableStateOf(false) }
    var showAddBannerDialog by remember { mutableStateOf(false) }
    var viewInvoiceOrder by remember { mutableStateOf<Order?>(null) }
    var assignPartnerOrder by remember { mutableStateOf<Order?>(null) }

    Scaffold(
        topBar = {
            Column {
                BrotherMartHeader(
                    currentRole = AppRole.ADMIN,
                    selectedArea = "Central Store Hub",
                    cartItemCount = 0,
                    unreadNotificationCount = adminUnreadNotifs,
                    onRoleChange = onSwitchRole,
                    onAreaClick = {},
                    onCartClick = {},
                    onNotificationClick = {
                        repository.markNotificationsAsRead(AppRole.ADMIN)
                    }
                )

                // Admin Sidebar/Tab Navigation
                ScrollableTabRow(
                    selectedTabIndex = selectedAdminTab,
                    containerColor = Color.White,
                    contentColor = DarkGreenPrimary,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedAdminTab]),
                            color = DarkGreenPrimary,
                            height = 3.dp
                        )
                    }
                ) {
                    adminTabs.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedAdminTab == index,
                            onClick = { selectedAdminTab = index },
                            text = {
                                Text(
                                    text = tabTitle,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedAdminTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedAdminTab == index) DarkGreenPrimary else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        },
        containerColor = SoftGreenBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedAdminTab) {
                0 -> AdminDashboardTab(
                    orders = orders,
                    products = products,
                    customers = customers,
                    deliveryPartners = deliveryPartners,
                    onViewOrders = { selectedAdminTab = 1 },
                    onViewProducts = { selectedAdminTab = 2 }
                )
                1 -> AdminOrdersTab(
                    orders = orders,
                    deliveryPartners = deliveryPartners,
                    onUpdateStatus = { oId, st -> repository.updateOrderStatus(oId, st) },
                    onAssignPartner = { order -> assignPartnerOrder = order },
                    onViewInvoice = { order -> viewInvoiceOrder = order }
                )
                2 -> AdminProductsTab(
                    products = products,
                    categories = categories,
                    onAddProduct = { showAddProductDialog = true },
                    onEditProduct = { editProductTarget = it },
                    onDeleteProduct = { repository.deleteProduct(it) }
                )
                3 -> AdminCategoriesTab(
                    categories = categories,
                    onAddCategory = { showAddCategoryDialog = true },
                    onToggleCategory = { repository.toggleCategory(it) }
                )
                4 -> AdminCustomersTab(
                    customers = customers,
                    onToggleBlock = { repository.toggleCustomerBlock(it) }
                )
                5 -> AdminDeliveryPartnersTab(
                    partners = deliveryPartners,
                    orders = orders
                )
                6 -> AdminCouponsTab(
                    coupons = coupons,
                    onAddCoupon = { showAddCouponDialog = true },
                    onToggleCoupon = { repository.toggleCoupon(it) }
                )
                7 -> AdminBannersTab(
                    banners = banners,
                    onAddBanner = { showAddBannerDialog = true },
                    onDeleteBanner = { repository.deleteBanner(it) }
                )
                8 -> AdminSettingsTab(
                    settings = settings,
                    onSaveSettings = { repository.updateStoreSettings(it) },
                    onSwitchRole = onSwitchRole
                )
                9 -> AdminReportsTab(
                    orders = orders,
                    products = products
                )
            }
        }
    }

    // Assign Delivery Partner Dialog
    assignPartnerOrder?.let { order ->
        AlertDialog(
            onDismissRequest = { assignPartnerOrder = null },
            title = { Text("Assign Delivery Partner", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Order #${order.id} (${order.deliveryArea})", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select from active partners in Bishalgarh:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    deliveryPartners.forEach { partner ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    repository.assignDeliveryPartner(order.id, partner.id)
                                    assignPartnerOrder = null
                                },
                            colors = CardDefaults.cardColors(containerColor = SoftGreenBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(partner.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("${partner.vehicleType} (${partner.vehicleNumber})", fontSize = 11.sp, color = Color.Gray)
                                }
                                Surface(
                                    color = if (partner.isOnline) LimeGreenLight else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        if (partner.isOnline) "ONLINE" else "OFFLINE",
                                        color = if (partner.isOnline) DarkGreenHeader else Color.Red,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { assignPartnerOrder = null }) { Text("Cancel") }
            }
        )
    }

    // View / Print Invoice Dialog
    viewInvoiceOrder?.let { order ->
        AlertDialog(
            onDismissRequest = { viewInvoiceOrder = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Print, contentDescription = null, tint = DarkGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tax Invoice: #${order.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(settings.storeName, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DarkGreenPrimary)
                    Text(settings.address, fontSize = 11.sp, color = Color.Gray)
                    Text("Helpline: ${settings.phone}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Customer: ${order.customerName}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Phone: ${order.customerPhone}", fontSize = 11.sp)
                    Text("Delivery: ${order.deliveryAddress}, ${order.deliveryArea}", fontSize = 11.sp)
                    Text("Payment: ${order.paymentMethod.label} (${if (order.isPaid) "PAID" else "UNPAID"})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Billed Items:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.productName}", fontSize = 11.sp)
                            Text("₹${item.total.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 12.sp)
                        Text("₹${order.subtotal.toInt()}", fontSize = 12.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee", fontSize = 12.sp)
                        Text("₹${order.deliveryFee.toInt()}", fontSize = 12.sp)
                    }
                    if (order.discount > 0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Discount", fontSize = 12.sp, color = DarkGreenPrimary)
                            Text("-₹${order.discount.toInt()}", fontSize = 12.sp, color = DarkGreenPrimary)
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("₹${order.grandTotal.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = DarkGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Thank you for shopping at Brother Mart!", fontSize = 11.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewInvoiceOrder = null },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Close / Print")
                }
            }
        )
    }

    // Add / Edit Product Dialog
    if (showAddProductDialog || editProductTarget != null) {
        val target = editProductTarget
        ProductEditorDialog(
            initialProduct = target,
            categories = categories,
            onDismiss = {
                showAddProductDialog = false
                editProductTarget = null
            },
            onSave = { product ->
                if (target == null) {
                    repository.addProduct(product)
                } else {
                    repository.updateProduct(product)
                }
                showAddProductDialog = false
                editProductTarget = null
            }
        )
    }

    // Add Category Dialog
    if (showAddCategoryDialog) {
        var catName by remember { mutableStateOf("") }
        var catNameBn by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Add Category", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = catName,
                        onValueChange = { catName = it },
                        label = { Text("Category Name (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = catNameBn,
                        onValueChange = { catNameBn = it },
                        label = { Text("Category Name (Bengali)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (catName.isNotBlank()) {
                            repository.addCategory(
                                Category(
                                    id = "cat_${System.currentTimeMillis()}",
                                    name = catName.trim(),
                                    nameBn = catNameBn.ifBlank { catName }.trim(),
                                    iconKey = "grocery"
                                )
                            )
                            showAddCategoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategoryDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Coupon Dialog
    if (showAddCouponDialog) {
        var code by remember { mutableStateOf("") }
        var discountPct by remember { mutableStateOf("15") }
        var minOrder by remember { mutableStateOf("299") }
        var maxDisc by remember { mutableStateOf("100") }
        var desc by remember { mutableStateOf("Special discount for users") }

        AlertDialog(
            onDismissRequest = { showAddCouponDialog = false },
            title = { Text("Create Discount Coupon", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Coupon Code (e.g. FLASH25)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = discountPct, onValueChange = { discountPct = it }, label = { Text("Discount Percent (%)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = minOrder, onValueChange = { minOrder = it }, label = { Text("Min Order Amount (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = maxDisc, onValueChange = { maxDisc = it }, label = { Text("Max Discount Cap (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (code.isNotBlank()) {
                            repository.addCoupon(
                                Coupon(
                                    code = code.trim().uppercase(),
                                    discountPercent = discountPct.toIntOrNull() ?: 10,
                                    minOrderAmount = minOrder.toDoubleOrNull() ?: 199.0,
                                    maxDiscount = maxDisc.toDoubleOrNull() ?: 100.0,
                                    description = desc
                                )
                            )
                            showAddCouponDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCouponDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Banner Dialog
    if (showAddBannerDialog) {
        var title by remember { mutableStateOf("") }
        var subtitle by remember { mutableStateOf("") }
        var badge by remember { mutableStateOf("SPECIAL OFFER") }
        var couponCode by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddBannerDialog = false },
            title = { Text("Add Promotional Banner", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Banner Title") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle / Details") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = badge, onValueChange = { badge = it }, label = { Text("Badge Label") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = couponCode, onValueChange = { couponCode = it }, label = { Text("Linked Coupon Code") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            repository.addBanner(
                                PromotionalBanner(
                                    id = "b_${System.currentTimeMillis()}",
                                    title = title.trim(),
                                    subtitle = subtitle.trim(),
                                    badge = badge.trim(),
                                    couponCode = couponCode.ifBlank { null }?.trim()
                                )
                            )
                            showAddBannerDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Add Banner")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBannerDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// -------------------------------------------------------------
// 1. DASHBOARD TAB
// -------------------------------------------------------------
@Composable
fun AdminDashboardTab(
    orders: List<Order>,
    products: List<Product>,
    customers: List<Customer>,
    deliveryPartners: List<DeliveryPartner>,
    onViewOrders: () -> Unit,
    onViewProducts: () -> Unit
) {
    val totalSales = orders.filter { it.status == OrderStatus.DELIVERED }.sumOf { it.grandTotal }
    val todaySales = orders.filter { it.status == OrderStatus.DELIVERED }.sumOf { it.grandTotal } // demo day
    val pendingOrders = orders.count { it.status == OrderStatus.PENDING }
    val activeDeliveries = orders.count { it.status == OrderStatus.OUT_FOR_DELIVERY || it.status == OrderStatus.ASSIGNED }
    val completedOrders = orders.count { it.status == OrderStatus.DELIVERED }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreenPrimary)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Brother Mart Operations Center", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("Admin Control & Analytics Dashboard", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Serving: Bishalgarh, Uttar Brajapur, Brajapur, Amtali, Lalshingmura", color = LimeGreenAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Summary Metric Cards (Total Sales, Orders, Customers, Fleet)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard("Today's Sales", "₹${todaySales.toInt()}", "Direct revenue", DarkGreenPrimary, Modifier.weight(1f))
                AdminStatCard("Total Revenue", "₹${(totalSales + 12450.0).toInt()}", "All orders", Color(0xFF0288D1), Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard("Pending Orders", "$pendingOrders", "Needs confirmation", Color(0xFFE65100), Modifier.weight(1f))
                AdminStatCard("Out for Delivery", "$activeDeliveries", "On road", Color(0xFF7B1FA2), Modifier.weight(1f))
                AdminStatCard("Completed", "$completedOrders", "Delivered successfully", Color(0xFF2E7D32), Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard("Total Customers", "${customers.size}", "Registered", Color(0xFF37474F), Modifier.weight(1f))
                AdminStatCard("Active Fleet", "${deliveryPartners.count { it.isOnline }}/${deliveryPartners.size}", "Online riders", Color(0xFF00796B), Modifier.weight(1f))
                AdminStatCard("Active Catalog", "${products.size}", "Grocery items", DarkGreenPrimary, Modifier.weight(1f))
            }
        }

        // Visual Sales Performance Chart Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Weekly Sales Trend (₹ in Hundreds)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
                    Spacer(modifier = Modifier.height(10.dp))

                    val days = listOf("Mon" to 42, "Tue" to 58, "Wed" to 75, "Thu" to 63, "Fri" to 89, "Sat" to 110, "Sun" to 135)
                    val maxVal = 140

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEach { (day, amount) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("₹${amount * 10}", fontSize = 9.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height((amount * 85 / maxVal).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(if (day == "Sun" || day == "Sat") LimeGreenAccent else DarkGreenPrimary)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(day, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Quick Action Shortcuts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onViewOrders,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Manage Orders", fontSize = 12.sp)
                }

                Button(
                    onClick = onViewProducts,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Products Catalog", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 9.sp, color = Color(0xFF757575), maxLines = 1)
        }
    }
}

// -------------------------------------------------------------
// 2. ORDER MANAGEMENT TAB
// -------------------------------------------------------------
@Composable
fun AdminOrdersTab(
    orders: List<Order>,
    deliveryPartners: List<DeliveryPartner>,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onAssignPartner: (Order) -> Unit,
    onViewInvoice: (Order) -> Unit
) {
    var statusFilter by remember { mutableStateOf<OrderStatus?>(null) }
    var orderSearch by remember { mutableStateOf("") }

    val filtered = orders.filter { order ->
        val matchesStatus = statusFilter == null || order.status == statusFilter
        val matchesSearch = orderSearch.isBlank() ||
                order.id.contains(orderSearch, ignoreCase = true) ||
                order.customerName.contains(orderSearch, ignoreCase = true) ||
                order.deliveryArea.contains(orderSearch, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search & Filter header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = orderSearch,
                    onValueChange = { orderSearch = it },
                    placeholder = { Text("Search by Order ID, Customer name or Area...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkGreenPrimary) },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = statusFilter == null,
                            onClick = { statusFilter = null },
                            label = { Text("All (${orders.size})", fontSize = 11.sp) }
                        )
                    }
                    items(OrderStatus.values()) { st ->
                        val count = orders.count { it.status == st }
                        FilterChip(
                            selected = statusFilter == st,
                            onClick = { statusFilter = st },
                            label = { Text("${st.display} ($count)", fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkGreenHeader)
                                Text("Customer: ${order.customerName} (${order.customerPhone})", fontSize = 11.sp, color = Color.Gray)
                            }
                            Surface(color = SoftGreenBackground, shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    order.status.display,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = DarkGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Area: ${order.deliveryArea} • ${order.deliveryAddress}", fontSize = 11.sp)
                        Text(
                            "Items: ${order.items.joinToString(", ") { "${it.quantity}x ${it.productName}" }}",
                            fontSize = 11.sp,
                            color = Color(0xFF424242)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total: ₹${order.grandTotal.toInt()} (${order.paymentMethod.label})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkGreenPrimary)
                            order.deliveryPartnerName?.let {
                                Text("Rider: $it", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            } ?: Text("Rider: Not Assigned", fontSize = 11.sp, color = Color(0xFFE65100))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color(0xFFF0F0F0))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Admin Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (order.status == OrderStatus.PENDING) {
                                Button(
                                    onClick = { onUpdateStatus(order.id, OrderStatus.CONFIRMED) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Confirm", fontSize = 11.sp)
                                }
                            }
                            if (order.status == OrderStatus.CONFIRMED) {
                                Button(
                                    onClick = { onUpdateStatus(order.id, OrderStatus.PREPARING) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Preparing", fontSize = 11.sp)
                                }
                            }
                            if (order.status == OrderStatus.PREPARING) {
                                Button(
                                    onClick = { onUpdateStatus(order.id, OrderStatus.READY_FOR_PICKUP) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Ready Pickup", fontSize = 11.sp)
                                }
                            }

                            OutlinedButton(
                                onClick = { onAssignPartner(order) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(if (order.deliveryPartnerId != null) "Reassign" else "Assign Rider", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { onViewInvoice(order) },
                                modifier = Modifier.weight(0.8f),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Invoice", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PRODUCT MANAGEMENT TAB
// -------------------------------------------------------------
@Composable
fun AdminProductsTab(
    products: List<Product>,
    categories: List<Category>,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf<String?>(null) }

    val filtered = products.filter {
        (searchQuery.isBlank() || it.nameEn.contains(searchQuery, ignoreCase = true) || it.nameBn.contains(searchQuery, ignoreCase = true)) &&
                (selectedCat == null || it.category == selectedCat)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search catalog products...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onAddProduct,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 12.sp)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProductVisual(iconType = product.iconType, size = 50.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.nameEn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${product.nameBn} • ${product.category}", fontSize = 11.sp, color = DarkGreenPrimary)
                            Text("Price: ₹${product.price.toInt()} | Offer: ₹${product.discountPrice.toInt()} (${product.unit})", fontSize = 11.sp, color = Color.Gray)
                            Text("Stock: ${product.stock} units available", fontSize = 10.sp, color = if (product.stock > 10) Color(0xFF2E7D32) else Color.Red)
                        }

                        IconButton(onClick = { onEditProduct(product) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF0288D1), modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { onDeleteProduct(product.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. CATEGORIES TAB
// -------------------------------------------------------------
@Composable
fun AdminCategoriesTab(
    categories: List<Category>,
    onAddCategory: () -> Unit,
    onToggleCategory: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Product Categories (${categories.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenHeader)
            Button(
                onClick = onAddCategory,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Category", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ProductVisual(iconType = cat.iconKey, size = 40.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(cat.nameBn, fontSize = 11.sp, color = DarkGreenPrimary)
                            }
                        }

                        TextButton(onClick = { onToggleCategory(cat.id) }) {
                            Text(if (cat.isEnabled) "Disable" else "Enable", color = if (cat.isEnabled) Color(0xFFC62828) else DarkGreenPrimary)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. CUSTOMERS TAB
// -------------------------------------------------------------
@Composable
fun AdminCustomersTab(
    customers: List<Customer>,
    onToggleBlock: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Registered Customers (${customers.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(customers) { cust ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cust.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${cust.phone} • ${cust.deliveryArea}", fontSize = 11.sp, color = Color.Gray)
                            Text("Total Orders: ${cust.totalOrders} • Spent: ₹${cust.totalSpend.toInt()}", fontSize = 11.sp, color = DarkGreenPrimary)
                        }

                        Button(
                            onClick = { onToggleBlock(cust.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (cust.isBlocked) Color(0xFF2E7D32) else Color(0xFFC62828)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(if (cust.isBlocked) "Unblock" else "Block", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. DELIVERY PARTNERS TAB
// -------------------------------------------------------------
@Composable
fun AdminDeliveryPartnersTab(
    partners: List<DeliveryPartner>,
    orders: List<Order>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Delivery Partner Fleet", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(partners) { partner ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(partner.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${partner.phone} • ${partner.vehicleType} (${partner.vehicleNumber})", fontSize = 11.sp, color = Color.Gray)
                            }
                            Surface(
                                color = if (partner.isOnline) LimeGreenLight else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    if (partner.isOnline) "ONLINE" else "OFFLINE",
                                    color = if (partner.isOnline) DarkGreenHeader else Color.Red,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Deliveries: ${partner.completedDeliveries}", fontSize = 11.sp)
                            Text("Rating: ${partner.rating} ★", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreenPrimary)
                            Text("Earnings: ₹${partner.totalEarnings.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 7. COUPONS TAB
// -------------------------------------------------------------
@Composable
fun AdminCouponsTab(
    coupons: List<Coupon>,
    onAddCoupon: () -> Unit,
    onToggleCoupon: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Promotional Coupons", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenHeader)
            Button(
                onClick = onAddCoupon,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Create Coupon", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(coupons) { cp ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(cp.code, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = DarkGreenPrimary)
                            Text(cp.description, fontSize = 11.sp, color = Color.Gray)
                            Text("Min Order: ₹${cp.minOrderAmount.toInt()} • Max Disc: ₹${cp.maxDiscount.toInt()}", fontSize = 10.sp)
                        }
                        TextButton(onClick = { onToggleCoupon(cp.code) }) {
                            Text(if (cp.isEnabled) "Active" else "Disabled", color = if (cp.isEnabled) DarkGreenPrimary else Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. BANNERS TAB
// -------------------------------------------------------------
@Composable
fun AdminBannersTab(
    banners: List<PromotionalBanner>,
    onAddBanner: () -> Unit,
    onDeleteBanner: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Home Banners", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenHeader)
            Button(
                onClick = onAddBanner,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Banner", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(banners) { banner ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(banner.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(banner.subtitle, fontSize = 11.sp, color = Color.Gray)
                            banner.couponCode?.let {
                                Text("Linked Coupon: $it", fontSize = 10.sp, color = DarkGreenPrimary)
                            }
                        }
                        IconButton(onClick = { onDeleteBanner(banner.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 9. SETTINGS TAB
// -------------------------------------------------------------
@Composable
fun AdminSettingsTab(
    settings: StoreSettings,
    onSaveSettings: (StoreSettings) -> Unit,
    onSwitchRole: (AppRole) -> Unit
) {
    var storeName by remember { mutableStateOf(settings.storeName) }
    var phone by remember { mutableStateOf(settings.phone) }
    var address by remember { mutableStateOf(settings.address) }
    var deliveryFee by remember { mutableStateOf(settings.deliveryFee.toInt().toString()) }
    var freeThreshold by remember { mutableStateOf(settings.freeDeliveryThreshold.toInt().toString()) }
    var upiId by remember { mutableStateOf(settings.upiId) }
    var saveSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Store Configuration", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                OutlinedTextField(value = storeName, onValueChange = { storeName = it }, label = { Text("Store Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone / WhatsApp") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Store Hub Address") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = deliveryFee, onValueChange = { deliveryFee = it }, label = { Text("Standard Delivery Fee (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = freeThreshold, onValueChange = { freeThreshold = it }, label = { Text("Free Delivery Threshold (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = upiId, onValueChange = { upiId = it }, label = { Text("Store Payment UPI ID") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onSaveSettings(
                            settings.copy(
                                storeName = storeName,
                                phone = phone,
                                address = address,
                                deliveryFee = deliveryFee.toDoubleOrNull() ?: 30.0,
                                freeDeliveryThreshold = freeThreshold.toDoubleOrNull() ?: 499.0,
                                upiId = upiId
                            )
                        )
                        saveSuccess = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Save Store Settings")
                }

                if (saveSuccess) {
                    Text("Settings updated successfully! ✅", color = DarkGreenPrimary, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Switch Panel / Demo Role", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onSwitchRole(AppRole.CUSTOMER) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🛒 Customer App", fontSize = 11.sp)
            }
            OutlinedButton(
                onClick = { onSwitchRole(AppRole.DELIVERY_PARTNER) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🛵 Delivery Partner", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

// -------------------------------------------------------------
// 10. REPORTS TAB
// -------------------------------------------------------------
@Composable
fun AdminReportsTab(
    orders: List<Order>,
    products: List<Product>
) {
    val completedOrders = orders.filter { it.status == OrderStatus.DELIVERED }
    val totalRevenue = completedOrders.sumOf { it.grandTotal }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Sales & Performance Reports", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Bishalgarh Operations Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Orders Processed", fontSize = 12.sp)
                    Text("${orders.size}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Completed Deliveries", fontSize = 12.sp)
                    Text("${completedOrders.size}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkGreenPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Gross Revenue", fontSize = 12.sp)
                    Text("₹${totalRevenue.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkGreenPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text("Top Performing Categories", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                ReportCategoryRow("Desi Chicken & Duck (Meat)", "45% of total sales")
                Divider(modifier = Modifier.padding(vertical = 6.dp))
                ReportCategoryRow("Daily Kirana & Rice", "28% of total sales")
                Divider(modifier = Modifier.padding(vertical = 6.dp))
                ReportCategoryRow("Fresh Local Vegetables", "18% of total sales")
                Divider(modifier = Modifier.padding(vertical = 6.dp))
                ReportCategoryRow("Beverages & Snacks", "9% of total sales")
            }
        }
    }
}

@Composable
fun ReportCategoryRow(name: String, percentage: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(percentage, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreenPrimary)
    }
}

// -------------------------------------------------------------
// PRODUCT EDITOR DIALOG
// -------------------------------------------------------------
@Composable
fun ProductEditorDialog(
    initialProduct: Product?,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var nameEn by remember { mutableStateOf(initialProduct?.nameEn ?: "") }
    var nameBn by remember { mutableStateOf(initialProduct?.nameBn ?: "") }
    var category by remember { mutableStateOf(initialProduct?.category ?: (categories.firstOrNull()?.name ?: "Desi Chicken")) }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var price by remember { mutableStateOf(initialProduct?.price?.toInt()?.toString() ?: "100") }
    var discountPrice by remember { mutableStateOf(initialProduct?.discountPrice?.toInt()?.toString() ?: "90") }
    var unit by remember { mutableStateOf(initialProduct?.unit ?: "1 kg") }
    var stock by remember { mutableStateOf(initialProduct?.stock?.toString() ?: "20") }
    var isBestSeller by remember { mutableStateOf(initialProduct?.isBestSeller ?: false) }
    var isFeatured by remember { mutableStateOf(initialProduct?.isFeatured ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialProduct == null) "Add New Product" else "Edit Product", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(value = nameEn, onValueChange = { nameEn = it }, label = { Text("Product Name (English)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = nameBn, onValueChange = { nameBn = it }, label = { Text("Product Name (Bengali)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit (e.g. 1 kg, 500 ml, 6 pcs)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Original Price (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = discountPrice, onValueChange = { discountPrice = it }, label = { Text("Offer / Discount Price (₹)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock Quantity") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Product Description") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isBestSeller, onCheckedChange = { isBestSeller = it })
                    Text("Mark as Best Seller", fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    Text("Mark as Featured / Special", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameEn.isNotBlank()) {
                        val p = Product(
                            id = initialProduct?.id ?: "p_${System.currentTimeMillis()}",
                            nameEn = nameEn.trim(),
                            nameBn = nameBn.ifBlank { nameEn }.trim(),
                            category = category,
                            description = description,
                            price = price.toDoubleOrNull() ?: 100.0,
                            discountPrice = discountPrice.toDoubleOrNull() ?: 90.0,
                            unit = unit.trim(),
                            stock = stock.toIntOrNull() ?: 10,
                            isBestSeller = isBestSeller,
                            isFeatured = isFeatured,
                            iconType = when {
                                category.contains("chicken", ignoreCase = true) || category.contains("meat", ignoreCase = true) -> "meat"
                                category.contains("duck", ignoreCase = true) -> "duck"
                                category.contains("egg", ignoreCase = true) -> "egg"
                                category.contains("rice", ignoreCase = true) -> "rice"
                                category.contains("oil", ignoreCase = true) -> "oil"
                                category.contains("veg", ignoreCase = true) -> "vegetable"
                                category.contains("fruit", ignoreCase = true) -> "fruit"
                                else -> "grocery"
                            }
                        )
                        onSave(p)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
            ) {
                Text("Save Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
