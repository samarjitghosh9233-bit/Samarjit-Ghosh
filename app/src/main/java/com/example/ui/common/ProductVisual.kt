package com.example.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProductVisual(
    iconType: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    shapeRadius: Dp = 16.dp
) {
    val (icon, bgColors, iconTint) = when (iconType.lowercase()) {
        "meat", "poultry" -> Triple(
            Icons.Default.Restaurant,
            listOf(Color(0xFFFFEBEB), Color(0xFFFFD4D4)),
            Color(0xFFC62828)
        )
        "duck" -> Triple(
            Icons.Default.Pets,
            listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)),
            Color(0xFFE65100)
        )
        "egg" -> Triple(
            Icons.Default.Egg,
            listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3)),
            Color(0xFFF57F17)
        )
        "duck_egg" -> Triple(
            Icons.Default.Egg,
            listOf(Color(0xFFE0F2F1), Color(0xFFB2DFDB)),
            Color(0xFF00796B)
        )
        "rice" -> Triple(
            Icons.Default.Grass,
            listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8)),
            Color(0xFF558B2F)
        )
        "oil" -> Triple(
            Icons.Default.WaterDrop,
            listOf(Color(0xFFFFFDE7), Color(0xFFFFF59D)),
            Color(0xFFFBC02D)
        )
        "kirana" -> Triple(
            Icons.Default.ShoppingBag,
            listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8)),
            Color(0xFF5D4037)
        )
        "vegetable" -> Triple(
            Icons.Default.Spa,
            listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)),
            Color(0xFF2E7D32)
        )
        "fruit" -> Triple(
            Icons.Default.Fastfood,
            listOf(Color(0xFFFFF3E0), Color(0xFFFFCC80)),
            Color(0xFFEF6C00)
        )
        "beverage" -> Triple(
            Icons.Default.LocalDrink,
            listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC)),
            Color(0xFF0288D1)
        )
        else -> Triple(
            Icons.Default.LocalGroceryStore,
            listOf(Color(0xFFEDF5EE), Color(0xFFD7EBD9)),
            Color(0xFF0C5A34)
        )
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(shapeRadius))
            .background(Brush.linearGradient(bgColors)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconType,
            tint = iconTint,
            modifier = Modifier
                .fillMaxSize()
                .padding(size * 0.22f)
        )
    }
}
