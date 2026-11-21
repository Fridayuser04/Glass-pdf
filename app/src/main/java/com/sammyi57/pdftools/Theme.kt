package com.sammyi57.pdftools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

// Cool blue is the primary accent; deep navy anchors text and navigation. Light is a clean blue-tinted
// paper surface, while dark uses layered midnight blues. Glass panels use translucent blue-grey surfaces.
object Brand {
  val tangerine = Color(0xFF3186E8) // Kept as a stable token name for existing components.
  val cherry = Color(0xFF102A43)
  val cool = Color(0xFFDCEEFF)
}

@Immutable
data class Palette(
  val bg: Color, // screen background
  val surface: Color, // grouped lists, inputs, dialogs
  val sunken: Color, // tiles behind artwork, pressed rows, search, segmented track
  val page: Color, // paper inside illustrations
  val ink: Color, // text and primary buttons
  val sub: Color,
  val faint: Color,
  val line: Color,
  val lineStrong: Color,
  val accent: Color,
  val accentSoft: Color,
  val accentInk: Color,
  val onInk: Color, // text on primary buttons
  val hero: List<Color>, // the loud surface: base, middle, glow
  val panel: Color, // the floating tab bar's glass
  val onPanel: Color,
  val onPanelSub: Color,
  val dark: Boolean,
)

val LightPalette = Palette(
  bg = Color(0xFFF3F7FC), surface = Color(0xFFFFFFFF), sunken = Color(0xFFEAF1F8), page = Color(0xFFFFFFFF),
  ink = Brand.cherry, sub = Color(0xFF63788F), faint = Color(0xFF9EAFBF), line = Color(0xFFDDE7F0),
  lineStrong = Color(0xFFC9D8E7), accent = Brand.tangerine, accentSoft = Color(0xFFDCEEFF), accentInk = Color(0xFF155AA6),
  onInk = Color(0xFFFFFFFF),
  hero = listOf(Color(0xFF102A43), Color(0xFF175C9E), Color(0xFF3186E8)),
  panel = Color(0xE6102A43), onPanel = Color(0xFFFFFFFF), onPanelSub = Color(0xFFB8D1E9),
  dark = false,
)

// Midnight blue keeps the glass surfaces distinct without losing contrast.
val DarkPalette = Palette(
  bg = Color(0xFF080F1A), surface = Color(0xFF111C2A), sunken = Color(0xFF1A2A3C), page = Color(0xFF223247),
  ink = Color(0xFFEAF4FF), sub = Color(0xFF9BB0C7), faint = Color(0xFF6E849B), line = Color(0xFF26384B),
  lineStrong = Color(0xFF354B61), accent = Color(0xFF66AEFF), accentSoft = Color(0xFF1B3551), accentInk = Color(0xFFB7D9FF),
  onInk = Color(0xFF081522),
  hero = listOf(Color(0xFF101E30), Color(0xFF174A79), Color(0xFF3186E8)),
  panel = Color(0xE6111C2A), onPanel = Color(0xFFEAF4FF), onPanelSub = Color(0xFF9BB0C7),
  dark = true,
)

// The scanner keeps its camera-style dark surface, with blue for detection and actions.
object ScanUi {
  val bg = Color(0xFF07111E)
  val panel = Color(0xFF111F30)
  val raised = Color(0xFF20344A)
  val text = Color(0xFFFFFFFF)
  val dim = Color(0xFF9BB0C7)
  val line = Color(0xFF2A4058)
  val accent = Color(0xFF66AEFF)
  val accentSoft = Color(0x3866AEFF)
  val onAccent = Color(0xFF081522)
  val scrim = Color(0x99000000)
}

val LocalPalette = staticCompositionLocalOf { LightPalette }

/** The active palette. */
val c: Palette @Composable get() = LocalPalette.current

// Poppins throughout. It runs tall, so line heights are ~1.3x to keep accents and descenders unclipped.
val Poppins = FontFamily(
  Font(R.font.poppins_regular, FontWeight.Normal),
  Font(R.font.poppins_medium, FontWeight.Medium),
  Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

object Type {
  private fun s(size: Float, weight: FontWeight, line: Float? = null, spacing: Float = 0f) = TextStyle(
    fontFamily = Poppins, fontWeight = weight, fontSize = size.sp,
    lineHeight = (line ?: (size * 1.32f)).sp, letterSpacing = spacing.sp,
  )
  val largeTitle = s(32f, FontWeight.SemiBold, 42f, -0.5f)
  val title = s(25f, FontWeight.SemiBold, 33f, -0.3f)
  val hero = s(25f, FontWeight.SemiBold, 33f, -0.4f)
  val body = s(15f, FontWeight.Normal, 22f)
  val small = s(13f, FontWeight.Normal, 19f)
  val item = s(15f, FontWeight.Medium)
  val label = s(13f, FontWeight.SemiBold)
  val button = s(15f, FontWeight.SemiBold)
  val num = s(12.5f, FontWeight.Medium)
  val med = s(13.5f, FontWeight.Medium)
  val bold = s(14f, FontWeight.SemiBold)
  val big = s(26f, FontWeight.SemiBold, 32f, -0.6f)
  val caption = s(11.5f, FontWeight.Medium, 16f, 1.4f)
}

// Deliberately not one radius for everything: small things are tighter.
object Radius {
  val tile = 10.dp
  val control = 12.dp
  val group = 16.dp
  val card = 22.dp
  val hero = 30.dp
}

enum class ThemePref { System, Light, Dark }

object ThemeState {
  private val file get() = File(appContext.filesDir, "theme.txt") // same file the Expo app used
  var pref by mutableStateOf(read())
    private set

  private fun read(): ThemePref = try {
    when (file.takeIf { it.exists() }?.readText()?.trim()) {
      "light" -> ThemePref.Light
      "dark" -> ThemePref.Dark
      else -> ThemePref.System
    }
  } catch (_: Exception) {
    ThemePref.System
  }

  fun set(p: ThemePref) {
    pref = p
    try { file.writeText(p.name.lowercase()) } catch (_: Exception) {}
  }
}
