package com.example.ui.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRole
import com.example.data.BrotherMartRepository
import com.example.data.Order
import com.example.data.OrderStatus
import com.example.ui.common.BrotherMartHeader
import com.example.ui.theme.DarkGreenHeader
import com.example.ui.theme.DarkGreenPrimary
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LimeGreenAccent
import com.example.ui.theme.LimeGreenLight
import com.example.ui.theme.SoftGreenBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAppScreen(
    onSwitchRole: (AppRole) -> Unit
) {
    val repository = BrotherMartRepository
    val partner by repository.currentDeliveryPartner.collectAsState()
    val orders by repository.orders.collectAsState()
    val settings by repository.storeSettings.collectAsState()
    val notifications by repository.notifications.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard/Deliveries, 1: Route Map, 2: Earnings, 3: Profile
    var verifyOrderId by remember { mutableStateOf<String?>(null) }
    var actionMessage by remember { mutableStateOf<String?>(null) }

    val unreadNotifs = notifications.count { it.targetRole == AppRole.DELIVERY_PARTNER && !it.isRead }

    // Orders available or assigned to this partner
    val myActiveDeliveries = orders.filter {
        (it.deliveryPartnerId == partner.id && it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED)
    }

    val availableRequests = orders.filter {
        (it.status == OrderStatus.CONFIRMED || it.status == OrderStatus.READY_FOR_PICKUP || it.status == OrderStatus.PREPARING) &&
                (it.deliveryPartnerId == null || it.deliveryPartnerId == partner.id)
    }

    Scaffold(
        topBar = {
            BrotherMartHeader(
                currentRole = AppRole.DELIVERY_PARTNER,
                selectedArea = "Hub: Bishalgarh",
                cartItemCount = 0,
                unreadNotificationCount = unreadNotifs,
                onRoleChange = onSwitchRole,
                onAreaClick = {},
                onCartClick = {},
                onNotificationClick = {
                    repository.markNotificationsAsRead(AppRole.DELIVERY_PARTNER)
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.TwoWheeler, contentDescription = "Deliveries") },
                    label = { Text("Deliveries", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = DarkGreenPrimary, indicatorColor = LimeGreenLight)
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Live Map") },
                    label = { Text("Route Map", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = DarkGreenPrimary, indicatorColor = LimeGreenLight)
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Payment, contentDescription = "Earnings") },
                    label = { Text("Earnings", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = DarkGreenPrimary, indicatorColor = LimeGreenLight)
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = DarkGreenPrimary, indicatorColor = LimeGreenLight)
                )
            }
        },
        containerColor = SoftGreenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DeliveryDashboardTab(
                    partner = partner,
                    activeDeliveries = myActiveDeliveries,
                    newRequests = availableRequests,
                    settings = settings,
                    onToggleOnline = { repository.toggleDeliveryPartnerOnline(it) },
                    onAcceptOrder = { repository.acceptDeliveryOrder(it) },
                    onMarkPickedUp = { repository.markOrderPickedUp(it) },
                    onMarkOutForDelivery = { repository.markOrderOutForDelivery(it) },
                    onVerifyDelivery = { verifyOrderId = it },
                    onCallCustomer = { actionMessage = "Calling customer ($it)..." },
                    onWhatsAppCustomer = { actionMessage = "Opening WhatsApp chat with $it..." },
                    onOpenMap = { selectedTab = 1 }
                )
                1 -> DeliveryMapTab(
                    activeOrders = myActiveDeliveries,
                    hubAddress = settings.address,
                    onDeliverClick = { verifyOrderId = it }
                )
                2 -> DeliveryEarningsTab(partner = partner)
                3 -> DeliveryProfileTab(
                    partner = partner,
                    onSwitchRole = onSwitchRole
                )
            }
        }
    }

    // OTP / Verification Dialog
    verifyOrderId?.let { orderId ->
        var enteredOtp by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val targetOrder = orders.find { it.id == orderId }

        AlertDialog(
            onDismissRequest = { verifyOrderId = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = DarkGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verify Delivery", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Ask the customer for the 4-digit security code shown in their app to complete delivery.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Target Order: #$orderId", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    targetOrder?.let {
                        Text("Customer: ${it.customerName} (${it.deliveryArea})", fontSize = 12.sp)
                        Text("(Hint for demo testing: OTP is ${it.otpCode})", fontSize = 11.sp, color = DarkGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = {
                            enteredOtp = it
                            errorMessage = null
                        },
                        label = { Text("Enter 4-Digit OTP") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorMessage?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = repository.verifyAndCompleteDelivery(orderId, enteredOtp)
                        if (success) {
                            verifyOrderId = null
                            actionMessage = "Order #$orderId Delivered Successfully! ₹40 added to earnings."
                        } else {
                            errorMessage = "Incorrect OTP code. Please verify with customer."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                ) {
                    Text("Verify & Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = { verifyOrderId = null }) { Text("Cancel") }
            }
        )
    }

    actionMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { actionMessage = null },
            confirmButton = {
                TextButton(onClick = { actionMessage = null }) { Text("OK") }
            },
            text = { Text(msg, fontSize = 14.sp) }
        )
    }
}

// -------------------------------------------------------------
// 1. DELIVERY DASHBOARD TAB
// -------------------------------------------------------------
@Composable
fun DeliveryDashboardTab(
    partner: com.example.data.DeliveryPartner,
    activeDeliveries: List<Order>,
    newRequests: List<Order>,
    settings: com.example.data.StoreSettings,
    onToggleOnline: (Boolean) -> Unit,
    onAcceptOrder: (String) -> Unit,
    onMarkPickedUp: (String) -> Unit,
    onMarkOutForDelivery: (String) -> Unit,
    onVerifyDelivery: (String) -> Unit,
    onCallCustomer: (String) -> Unit,
    onWhatsAppCustomer: (String) -> Unit,
    onOpenMap: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Online / Offline Status Toggle Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (partner.isOnline) DarkGreenPrimary else Color(0xFF424242)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (partner.isOnline) LimeGreenAccent else Color.Red)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (partner.isOnline) "YOU ARE ONLINE" else "YOU ARE OFFLINE",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = if (partner.isOnline) "Ready to receive delivery orders in Bishalgarh" else "Go online to start receiving delivery requests",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }

                    Switch(
                        checked = partner.isOnline,
                        onCheckedChange = onToggleOnline,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LimeGreenAccent,
                            checkedTrackColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("delivery_online_toggle")
                    )
                }
            }
        }

        // Metrics Grid (Today's Orders, Earnings, Rating, Pending)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricSmallCard(
                    title = "Today's Earnings",
                    value = "₹${partner.todayEarnings.toInt()}",
                    subtitle = "Payout pending",
                    color = DarkGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricSmallCard(
                    title = "Deliveries Done",
                    value = "${partner.completedDeliveries}",
                    subtitle = "4.9 ★ Rating",
                    color = Color(0xFF0288D1),
                    modifier = Modifier.weight(1f)
                )
                MetricSmallCard(
                    title = "Active Jobs",
                    value = "${activeDeliveries.size}",
                    subtitle = "${newRequests.size} requests",
                    color = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Deliveries (In Progress)
        item {
            Text(
                text = "⚡ Active Deliveries (${activeDeliveries.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenHeader
            )
        }

        if (activeDeliveries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active delivery in progress right now.", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(activeDeliveries) { order ->
                ActiveDeliveryCard(
                    order = order,
                    hubAddress = settings.address,
                    onMarkPickedUp = { onMarkPickedUp(order.id) },
                    onMarkOutForDelivery = { onMarkOutForDelivery(order.id) },
                    onVerifyDelivery = { onVerifyDelivery(order.id) },
                    onCallCustomer = { onCallCustomer(order.customerPhone) },
                    onWhatsAppCustomer = { onWhatsAppCustomer(order.customerPhone) },
                    onOpenMap = onOpenMap
                )
            }
        }

        // New Delivery Requests
        item {
            Text(
                text = "🔔 New Delivery Requests (${newRequests.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenHeader,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        if (newRequests.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No new incoming orders right now. Check back shortly!", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(newRequests) { order ->
                NewRequestCard(
                    order = order,
                    hubAddress = settings.address,
                    onAccept = { onAcceptOrder(order.id) }
                )
            }
        }
    }
}

@Composable
fun MetricSmallCard(
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
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 9.sp, color = Color(0xFF616161))
        }
    }
}

// Active Delivery Card
@Composable
fun ActiveDeliveryCard(
    order: Order,
    hubAddress: String,
    onMarkPickedUp: () -> Unit,
    onMarkOutForDelivery: () -> Unit,
    onVerifyDelivery: () -> Unit,
    onCallCustomer: () -> Unit,
    onWhatsAppCustomer: () -> Unit,
    onOpenMap: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkGreenHeader)
                    Text("Fee Earned: ₹40.00", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkGreenPrimary)
                }

                Surface(
                    color = when (order.status) {
                        OrderStatus.PICKED_UP -> Color(0xFFFFF8E1)
                        OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFFFE0B2)
                        else -> Color(0xFFE8F5E9)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.status.display,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when (order.status) {
                            OrderStatus.PICKED_UP -> Color(0xFFF57F17)
                            OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFE65100)
                            else -> DarkGreenPrimary
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(10.dp))

            // Pickup & Drop Locations
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Store, contentDescription = null, tint = DarkGreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Pickup: Brother Mart Hub", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(hubAddress, fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Drop: ${order.customerName} (${order.deliveryArea})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("${order.deliveryAddress}, ${order.landmark}", fontSize = 11.sp, color = Color.Gray)
                    if (order.deliveryInstructions.isNotBlank()) {
                        Text("Note: ${order.deliveryInstructions}", fontSize = 11.sp, color = DarkGreenPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Call / Chat / Map Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCallCustomer,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = onWhatsAppCustomer,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = onOpenMap,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map Route", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lifecycle Progression Button
            when (order.status) {
                OrderStatus.ASSIGNED, OrderStatus.CONFIRMED, OrderStatus.READY_FOR_PICKUP, OrderStatus.PREPARING -> {
                    Button(
                        onClick = onMarkPickedUp,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Mark As 'Picked Up'", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                OrderStatus.PICKED_UP -> {
                    Button(
                        onClick = onMarkOutForDelivery,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Start Delivery (Out For Delivery)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                OrderStatus.OUT_FOR_DELIVERY -> {
                    Button(
                        onClick = onVerifyDelivery,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LimeGreenAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Verify & Complete Delivery", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkGreenHeader)
                    }
                }
                else -> {}
            }
        }
    }
}

// New Request Card
@Composable
fun NewRequestCard(
    order: Order,
    hubAddress: String,
    onAccept: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Request #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Surface(
                    color = LimeGreenLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("+ ₹40 Fee", color = DarkGreenHeader, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("📍 Delivery to: ${order.customerName} (${order.deliveryArea})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("Est. Distance: ~2.6 km • ~18 mins", fontSize = 11.sp, color = Color.Gray)
            Text("Items: ${order.items.size} grocery products (₹${order.grandTotal.toInt()})", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Decline", fontSize = 12.sp, color = Color.Gray)
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Accept Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. LIVE ROUTE MAP TAB (Simulated in-app GPS & route view)
// -------------------------------------------------------------
@Composable
fun DeliveryMapTab(
    activeOrders: List<Order>,
    hubAddress: String,
    onDeliverClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Active Delivery Navigation", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenHeader)
        Text("Real-time GPS route in Bishalgarh delivery zone", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Interactive Map Simulation Canvas Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF0E2)),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                        )
                    )
                    .padding(16.dp)
            ) {
                // Background visual road lines
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Hub Marker
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = DarkGreenPrimary,
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Store, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(6.dp)) {
                            Text("Hub: Bishalgarh Central", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
                        }
                    }

                    // Route midpoint indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = LimeGreenAccent,
                            shape = CircleShape,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = DarkGreenHeader, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(6.dp)) {
                            Text("Speed: 28 km/h • 1.2 km away", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
                        }
                    }

                    // Destination Customer Marker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(color = Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (activeOrders.isNotEmpty()) "Drop: ${activeOrders.first().deliveryArea}" else "Drop: Uttar Brajapur",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFFC62828),
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeOrders.isNotEmpty()) {
            val order = activeOrders.first()
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Currently Navigating To: Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Customer: ${order.customerName} • ${order.customerPhone}", fontSize = 12.sp, color = Color.Gray)
                    Text("Address: ${order.deliveryAddress}, ${order.landmark}", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onDeliverClick(order.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreenPrimary)
                    ) {
                        Text("Arrived at Customer • Verify Delivery OTP")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    Text("Accept an order to view live turn-by-turn route.", color = Color.Gray)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. DELIVERY EARNINGS TAB
// -------------------------------------------------------------
@Composable
fun DeliveryEarningsTab(
    partner: com.example.data.DeliveryPartner
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Earnings & Payouts", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(12.dp))

        // Total Earnings Hero Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGreenPrimary),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Total Lifetime Earnings", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text("₹${partner.totalEarnings.toInt()}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pending Bank Payout", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        Text("₹${partner.pendingPayout.toInt()}", color = LimeGreenAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Completed Deliveries", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        Text("${partner.completedDeliveries} Trips", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Breakdown Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Today's Earnings", fontSize = 11.sp, color = Color.Gray)
                    Text("₹${partner.todayEarnings.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenPrimary)
                    Text("Base fee + tips", fontSize = 10.sp, color = Color.Gray)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("This Week", fontSize = 11.sp, color = Color.Gray)
                    Text("₹2,480", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreenPrimary)
                    Text("62 Trips", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Recent Payout Transactions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        val transactions = listOf(
            Triple("Trip #BM100001 Delivery Fee", "Today, 10:15 AM", "+ ₹40.00"),
            Triple("Trip #BM100002 Delivery Fee", "Today, 09:30 AM", "+ ₹40.00"),
            Triple("Weekly Bank Direct Deposit (UPI)", "Yesterday", "- ₹2,100.00"),
            Triple("Trip #BM99948 Delivery Fee", "26 Sep", "+ ₹40.00"),
            Triple("Incentive: 10 Orders Completed", "25 Sep", "+ ₹150.00")
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                transactions.forEachIndexed { idx, tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tx.first, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(tx.second, fontSize = 10.sp, color = Color.Gray)
                        }
                        Text(
                            tx.third,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tx.third.startsWith("+")) DarkGreenPrimary else Color(0xFFC62828)
                        )
                    }
                    if (idx < transactions.size - 1) {
                        Divider(color = Color(0xFFF5F5F5))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. DELIVERY PROFILE TAB
// -------------------------------------------------------------
@Composable
fun DeliveryProfileTab(
    partner: com.example.data.DeliveryPartner,
    onSwitchRole: (AppRole) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Partner Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = LimeGreenAccent, modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(partner.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = DarkGreenPrimary, modifier = Modifier.size(16.dp))
                    }
                    Text(partner.phone, fontSize = 12.sp, color = Color.Gray)
                    Text("Partner ID: ${partner.id} • Bishalgarh", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vehicle & Document Details
        Text("Vehicle & Verification", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                DetailRow("Vehicle Type", partner.vehicleType)
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                DetailRow("Vehicle Number", partner.vehicleNumber)
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                DetailRow("KYC & Driving License", "Verified ✅")
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                DetailRow("Settlement UPI ID", partner.upiId)
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                DetailRow("Partner Rating", "4.9 ★ (Top Rated)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Switch To Other Panels", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreenHeader)
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
                onClick = { onSwitchRole(AppRole.ADMIN) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("⚙️ Admin Panel", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
