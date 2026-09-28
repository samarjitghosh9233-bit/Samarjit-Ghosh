package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.AppRole
import com.example.data.BrotherMartRepository
import com.example.ui.admin.AdminAppScreen
import com.example.ui.customer.CustomerAppScreen
import com.example.ui.delivery.DeliveryAppScreen
import com.example.ui.theme.SoftGreenBackground

@Composable
fun MainAppScreen() {
    val repository = BrotherMartRepository
    val currentRole by repository.currentRole.collectAsState()

    // BackHandler: If in Delivery or Admin panel, navigating back takes the user safely to the Customer App
    BackHandler(enabled = currentRole != AppRole.CUSTOMER) {
        repository.setRole(AppRole.CUSTOMER)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SoftGreenBackground
    ) {
        Crossfade(targetState = currentRole, label = "RoleTransition") { role ->
            when (role) {
                AppRole.CUSTOMER -> {
                    CustomerAppScreen(
                        onSwitchRole = { newRole -> repository.setRole(newRole) }
                    )
                }
                AppRole.DELIVERY_PARTNER -> {
                    DeliveryAppScreen(
                        onSwitchRole = { newRole -> repository.setRole(newRole) }
                    )
                }
                AppRole.ADMIN -> {
                    AdminAppScreen(
                        onSwitchRole = { newRole -> repository.setRole(newRole) }
                    )
                }
            }
        }
    }
}
