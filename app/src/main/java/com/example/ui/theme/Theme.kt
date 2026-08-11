package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
      primary = SleekIndigo,
      secondary = SleekEmerald,
      tertiary = SleekAmber,
      background = Color(0xFF0F172A),
      surface = Color(0xFF1E293B),
      onPrimary = Color.White,
      onBackground = Color(0xFFF8FAFC),
      onSurface = Color(0xFFF8FAFC)
  )

private val LightColorScheme =
  lightColorScheme(
      primary = SleekIndigo,
      onPrimary = Color.White,
      primaryContainer = SleekIndigoContainer,
      onPrimaryContainer = SleekOnIndigoContainer,
      secondary = SleekEmerald,
      onSecondary = Color.White,
      secondaryContainer = SleekEmeraldContainer,
      onSecondaryContainer = SleekEmerald,
      tertiary = SleekAmber,
      onTertiary = Color.White,
      tertiaryContainer = SleekAmberContainer,
      onTertiaryContainer = SleekAmber,
      background = SleekBackground,
      onBackground = SleekTextPrimary,
      surface = SleekSurface,
      onSurface = SleekTextPrimary,
      surfaceVariant = SleekSurfaceVariant,
      onSurfaceVariant = SleekTextSecondary,
      outline = SleekOutline,
      outlineVariant = Color(0xFFCBD5E1)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
