package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.ImageBase64Helper
import com.example.model.PlannerSection
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.LocalPastelPalette
import com.example.ui.theme.WarmSlateBrown

/**
 * Returns the thin Outline Icon (أيقونة بخطوط رفيعة) for each section in the Sidebar / Bottom Bar.
 */
fun sectionOutlineIcon(section: PlannerSection): ImageVector {
    return when (section) {
        PlannerSection.HOME_DASHBOARD -> Icons.Outlined.Home
        PlannerSection.SKINCARE_GLOW -> Icons.Outlined.WaterDrop
        PlannerSection.OUTFITS_HIJAB -> Icons.Outlined.Checkroom
        PlannerSection.BODY_CARE -> Icons.Outlined.Spa
        PlannerSection.FORGET_ME_NOT -> Icons.Outlined.ShoppingBag
        PlannerSection.HABIT_TRACKER -> Icons.Outlined.CheckCircle
        PlannerSection.FUTURE_YOU -> Icons.Outlined.AutoAwesome
        PlannerSection.FITNESS_NUTRITION -> Icons.Outlined.LocalCafe
    }
}

/**
 * Custom hand-illustrated "clean girl" aesthetic sticker icon for each section.
 * Uses warm slate-brown (#524A45) rounded strokes and pastel fills.
 */
@Composable
fun SectionStickerIcon(
    section: PlannerSection,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    overridePastelColor: Color? = null
) {
    val palette = LocalPastelPalette.current
    val fillPastel = overridePastelColor ?: palette.primaryPastel

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3f))
            .background(CreamSurface)
            .border(1.2.dp, fillPastel, RoundedCornerShape(size / 3f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.78f)) {
            val w = this.size.width
            val h = this.size.height
            val strokeWidth = (w * 0.072f).coerceAtLeast(2.4f)
            val outline = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
            val pastel = fillPastel
            val brown = WarmSlateBrown

            // Soft pastel sticker backing blob
            drawCircle(
                color = pastel.copy(alpha = 0.65f),
                radius = w * 0.42f,
                center = Offset(w * 0.52f, h * 0.52f)
            )

            when (section) {
                PlannerSection.HOME_DASHBOARD -> {
                    // Cozy Sanctuary Home + Heart + Sparkle
                    val roof = Path().apply {
                        moveTo(w * 0.18f, h * 0.48f)
                        lineTo(w * 0.50f, h * 0.20f)
                        lineTo(w * 0.82f, h * 0.48f)
                    }
                    val houseBody = Path().apply {
                        moveTo(w * 0.26f, h * 0.45f)
                        lineTo(w * 0.26f, h * 0.80f)
                        lineTo(w * 0.74f, h * 0.80f)
                        lineTo(w * 0.74f, h * 0.45f)
                        close()
                    }
                    drawPath(houseBody, color = pastel, style = Fill)
                    drawPath(houseBody, color = brown, style = outline)
                    drawPath(roof, color = brown, style = outline)
                    // Arched sanctuary door
                    drawRoundRect(
                        color = CreamSurface,
                        topLeft = Offset(w * 0.42f, h * 0.56f),
                        size = Size(w * 0.16f, h * 0.24f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.42f, h * 0.56f),
                        size = Size(w * 0.16f, h * 0.24f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
                        style = outline
                    )
                    drawSparkle(Offset(w * 0.78f, h * 0.22f), w * 0.09f, brown)
                }

                PlannerSection.FORGET_ME_NOT -> {
                    // Coquette Ribbon Bow + Sparkle
                    val leftLoop = Path().apply {
                        moveTo(w * 0.5f, h * 0.46f)
                        cubicTo(w * 0.2f, h * 0.22f, w * 0.14f, h * 0.62f, w * 0.5f, h * 0.52f)
                        close()
                    }
                    val rightLoop = Path().apply {
                        moveTo(w * 0.5f, h * 0.46f)
                        cubicTo(w * 0.8f, h * 0.22f, w * 0.86f, h * 0.62f, w * 0.5f, h * 0.52f)
                        close()
                    }
                    drawPath(leftLoop, color = pastel, style = Fill)
                    drawPath(rightLoop, color = pastel, style = Fill)
                    drawPath(leftLoop, color = brown, style = outline)
                    drawPath(rightLoop, color = brown, style = outline)

                    val tails = Path().apply {
                        moveTo(w * 0.46f, h * 0.52f)
                        quadraticTo(w * 0.35f, h * 0.72f, w * 0.26f, h * 0.84f)
                        moveTo(w * 0.54f, h * 0.52f)
                        quadraticTo(w * 0.65f, h * 0.72f, w * 0.74f, h * 0.84f)
                    }
                    drawPath(tails, color = brown, style = outline)

                    drawRoundRect(
                        color = CreamSurface,
                        topLeft = Offset(w * 0.43f, h * 0.41f),
                        size = Size(w * 0.14f, h * 0.15f),
                        cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.43f, h * 0.41f),
                        size = Size(w * 0.14f, h * 0.15f),
                        cornerRadius = CornerRadius(w * 0.05f, w * 0.05f),
                        style = outline
                    )
                    drawSparkle(Offset(w * 0.78f, h * 0.22f), w * 0.09f, brown)
                }

                PlannerSection.SKINCARE_GLOW -> {
                    // Serum Dropper Bottle
                    drawRoundRect(
                        color = pastel,
                        topLeft = Offset(w * 0.28f, h * 0.44f),
                        size = Size(w * 0.38f, h * 0.42f),
                        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.28f, h * 0.44f),
                        size = Size(w * 0.38f, h * 0.42f),
                        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f),
                        style = outline
                    )
                    drawRoundRect(
                        color = CreamSurface,
                        topLeft = Offset(w * 0.34f, h * 0.55f),
                        size = Size(w * 0.26f, h * 0.2f),
                        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
                    )
                    drawRoundRect(
                        color = CreamSurface,
                        topLeft = Offset(w * 0.36f, h * 0.34f),
                        size = Size(w * 0.22f, h * 0.1f),
                        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.36f, h * 0.34f),
                        size = Size(w * 0.22f, h * 0.1f),
                        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f),
                        style = outline
                    )
                    drawRoundRect(
                        color = pastel,
                        topLeft = Offset(w * 0.40f, h * 0.18f),
                        size = Size(w * 0.14f, h * 0.16f),
                        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.40f, h * 0.18f),
                        size = Size(w * 0.14f, h * 0.16f),
                        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f),
                        style = outline
                    )
                    drawSparkle(Offset(w * 0.78f, h * 0.32f), w * 0.1f, brown)
                }

                PlannerSection.BODY_CARE -> {
                    // Botanical Lotus
                    val centerPetal = Path().apply {
                        moveTo(w * 0.5f, h * 0.22f)
                        cubicTo(w * 0.68f, h * 0.42f, w * 0.64f, h * 0.72f, w * 0.5f, h * 0.78f)
                        cubicTo(w * 0.36f, h * 0.72f, w * 0.32f, h * 0.42f, w * 0.5f, h * 0.22f)
                        close()
                    }
                    val leftPetal = Path().apply {
                        moveTo(w * 0.5f, h * 0.76f)
                        cubicTo(w * 0.22f, h * 0.74f, w * 0.14f, h * 0.46f, w * 0.26f, h * 0.36f)
                        cubicTo(w * 0.36f, h * 0.42f, w * 0.42f, h * 0.58f, w * 0.5f, h * 0.76f)
                        close()
                    }
                    val rightPetal = Path().apply {
                        moveTo(w * 0.5f, h * 0.76f)
                        cubicTo(w * 0.78f, h * 0.74f, w * 0.86f, h * 0.46f, w * 0.74f, h * 0.36f)
                        cubicTo(w * 0.64f, h * 0.42f, w * 0.58f, h * 0.58f, w * 0.5f, h * 0.76f)
                        close()
                    }
                    drawPath(leftPetal, color = pastel, style = Fill)
                    drawPath(rightPetal, color = pastel, style = Fill)
                    drawPath(centerPetal, color = CreamSurface, style = Fill)
                    drawPath(leftPetal, color = brown, style = outline)
                    drawPath(rightPetal, color = brown, style = outline)
                    drawPath(centerPetal, color = brown, style = outline)
                    drawSparkle(Offset(w * 0.5f, h * 0.13f), w * 0.08f, brown)
                }

                PlannerSection.OUTFITS_HIJAB -> {
                    // Boutique Hanger + Flowing Scarf
                    val hook = Path().apply {
                        moveTo(w * 0.5f, h * 0.36f)
                        lineTo(w * 0.5f, h * 0.28f)
                        cubicTo(w * 0.5f, h * 0.16f, w * 0.64f, h * 0.18f, w * 0.58f, h * 0.26f)
                    }
                    val hangerTriangle = Path().apply {
                        moveTo(w * 0.5f, h * 0.36f)
                        lineTo(w * 0.18f, h * 0.58f)
                        lineTo(w * 0.82f, h * 0.58f)
                        close()
                    }
                    val drapedScarf = Path().apply {
                        moveTo(w * 0.32f, h * 0.52f)
                        quadraticTo(w * 0.5f, h * 0.62f, w * 0.68f, h * 0.52f)
                        lineTo(w * 0.64f, h * 0.82f)
                        quadraticTo(w * 0.5f, h * 0.76f, w * 0.36f, h * 0.82f)
                        close()
                    }
                    drawPath(drapedScarf, color = pastel, style = Fill)
                    drawPath(hangerTriangle, color = brown, style = outline)
                    drawPath(hook, color = brown, style = outline)
                    drawPath(drapedScarf, color = brown, style = outline)
                }

                PlannerSection.HABIT_TRACKER -> {
                    // Spiral Notebook + Check
                    drawRoundRect(
                        color = pastel,
                        topLeft = Offset(w * 0.24f, h * 0.20f),
                        size = Size(w * 0.54f, h * 0.64f),
                        cornerRadius = CornerRadius(w * 0.09f, w * 0.09f)
                    )
                    drawRoundRect(
                        color = brown,
                        topLeft = Offset(w * 0.24f, h * 0.20f),
                        size = Size(w * 0.54f, h * 0.64f),
                        cornerRadius = CornerRadius(w * 0.09f, w * 0.09f),
                        style = outline
                    )
                    for (i in 0..2) {
                        val y = h * (0.32f + i * 0.18f)
                        drawLine(
                            color = brown,
                            start = Offset(w * 0.18f, y),
                            end = Offset(w * 0.29f, y),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    val check = Path().apply {
                        moveTo(w * 0.40f, h * 0.52f)
                        lineTo(w * 0.48f, h * 0.60f)
                        lineTo(w * 0.64f, h * 0.42f)
                    }
                    drawPath(check, color = brown, style = outline)
                }

                PlannerSection.FUTURE_YOU -> {
                    // Dreamy Mirror & Sparkle
                    drawCircle(
                        color = pastel,
                        radius = w * 0.28f,
                        center = Offset(w * 0.5f, h * 0.44f)
                    )
                    drawCircle(
                        color = brown,
                        radius = w * 0.28f,
                        center = Offset(w * 0.5f, h * 0.44f),
                        style = outline
                    )
                    drawLine(
                        color = brown,
                        start = Offset(w * 0.5f, h * 0.72f),
                        end = Offset(w * 0.5f, h * 0.86f),
                        strokeWidth = strokeWidth * 1.2f,
                        cap = StrokeCap.Round
                    )
                    drawSparkle(Offset(w * 0.5f, h * 0.44f), w * 0.13f, brown)
                    drawSparkle(Offset(w * 0.78f, h * 0.24f), w * 0.07f, brown)
                }

                PlannerSection.FITNESS_NUTRITION -> {
                    // Iced Matcha Cup
                    val cup = Path().apply {
                        moveTo(w * 0.28f, h * 0.42f)
                        lineTo(w * 0.34f, h * 0.82f)
                        lineTo(w * 0.66f, h * 0.82f)
                        lineTo(w * 0.72f, h * 0.42f)
                        close()
                    }
                    drawPath(cup, color = pastel, style = Fill)
                    drawPath(cup, color = brown, style = outline)
                    drawLine(
                        color = brown,
                        start = Offset(w * 0.56f, h * 0.18f),
                        end = Offset(w * 0.48f, h * 0.66f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = brown,
                        start = Offset(w * 0.24f, h * 0.42f),
                        end = Offset(w * 0.76f, h * 0.42f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawSparkle(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        quadraticTo(center.x, center.y, center.x + radius, center.y)
        quadraticTo(center.x, center.y, center.x, center.y + radius)
        quadraticTo(center.x, center.y, center.x - radius, center.y)
        quadraticTo(center.x, center.y, center.x, center.y - radius)
        close()
    }
    drawPath(path, color = color, style = Fill)
}

private fun DrawScope.drawDaintyFlower(center: Offset, petalRadius: Float, petalColor: Color, outlineColor: Color) {
    val stroke = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    for (angle in 0 until 360 step 72) {
        val rad = Math.toRadians(angle.toDouble())
        val px = center.x + (petalRadius * 1.1f * kotlin.math.cos(rad)).toFloat()
        val py = center.y + (petalRadius * 1.1f * kotlin.math.sin(rad)).toFloat()
        drawCircle(color = petalColor, radius = petalRadius * 0.75f, center = Offset(px, py))
        drawCircle(color = outlineColor.copy(alpha = 0.45f), radius = petalRadius * 0.75f, center = Offset(px, py), style = stroke)
    }
    drawCircle(color = Color(0xFFF5E3B3), radius = petalRadius * 0.55f, center = center)
}

/**
 * Decorative pastel wallpaper strip for internal sections, blending the active theme palette
 * with the section's signature pastel.
 */
@Composable
fun SectionWallpaperHeader(
    section: PlannerSection,
    isArabic: Boolean,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val palette = LocalPastelPalette.current
    val activePastel = palette.primaryPastel

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = palette.cardSurface,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, activePastel.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            activePastel.copy(alpha = 0.55f),
                            palette.background.copy(alpha = 0.92f),
                            section.pastelColor.copy(alpha = 0.45f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(w, h)
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(activePastel.copy(alpha = 0.75f), Color.Transparent),
                        center = Offset(w * 0.18f, h * 0.25f),
                        radius = w * 0.45f
                    ),
                    center = Offset(w * 0.18f, h * 0.25f),
                    radius = w * 0.45f
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(section.pastelColor.copy(alpha = 0.65f), Color.Transparent),
                        center = Offset(w * 0.85f, h * 0.78f),
                        radius = w * 0.4f
                    ),
                    center = Offset(w * 0.85f, h * 0.78f),
                    radius = w * 0.4f
                )

                drawDaintyFlower(
                    center = Offset(w * 0.88f, h * 0.26f),
                    petalRadius = 9.dp.toPx(),
                    petalColor = CreamSurface,
                    outlineColor = WarmSlateBrown
                )
                drawDaintyFlower(
                    center = Offset(w * 0.12f, h * 0.78f),
                    petalRadius = 7.dp.toPx(),
                    petalColor = CreamSurface,
                    outlineColor = WarmSlateBrown
                )
                drawSparkle(
                    center = Offset(w * 0.76f, h * 0.22f),
                    radius = 6.dp.toPx(),
                    color = WarmSlateBrown.copy(alpha = 0.45f)
                )
                drawSparkle(
                    center = Offset(w * 0.92f, h * 0.68f),
                    radius = 5.dp.toPx(),
                    color = WarmSlateBrown.copy(alpha = 0.40f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionStickerIcon(
                    section = section,
                    size = 54.dp,
                    overridePastelColor = activePastel
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = section.title(isArabic),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = WarmSlateBrown
                        )
                        Surface(
                            color = activePastel,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "\u200E$itemCount\u200E",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = WarmSlateBrown,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = section.subtitle(isArabic),
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmSlateBrown.copy(alpha = 0.82f)
                    )
                }
            }
        }
    }
}

/**
 * Displays either a locally uploaded Base64 image or a bundled studio drawable asset.
 */
@Composable
fun PlannerCardImage(
    imageBase64: String?,
    presetDrawableName: String?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val decodedBitmap = remember(imageBase64) {
        ImageBase64Helper.decodeBase64ToBitmap(imageBase64)
    }
    val drawableRes = remember(presetDrawableName) {
        ImageBase64Helper.presetNameToDrawableRes(presetDrawableName)
    }

    if (decodedBitmap != null) {
        Image(
            bitmap = decodedBitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else if (drawableRes != null) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}
