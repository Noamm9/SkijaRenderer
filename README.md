# SkijaRenderer

Skija-based rendering library for Fabric `26.1.2`.

The library is aimed at client-side HUD, menu, and overlay rendering. It wraps the Skija frame setup used by
Minecraft's picture-in-picture GUI path and adds a small set of drawing, text, image, and coordinate helpers.

## Requirements

- Minecraft `26.1.2`
- Fabric Loader `0.18.4+`
- Fabric API `0.149.1+26.1.2`
- Fabric Language Kotlin `1.13.11+kotlin.2.3.21`
- Java `25`

## Main API

[`Skija`](src/main/kotlin/com/github/noamm9/skijarenderer/skia/Skija.kt) is the drawing entry point.

```kotlin
Skija.rect(20, 20, 120, 40, Color(20, 20, 20, 180), 8f)
Skija.hollowRect(20, 20, 120, 40, 1f, Color.WHITE, 8f)
Skija.line(20, 70, 180, 70, 2f, Color(255, 255, 255, 180))
Skija.circle(200, 70, 10f, Color(80, 200, 255))
Skija.gradientRect(
    20, 90, 160, 42,
    Color(73, 140, 255),
    Color(88, 235, 180),
    SkijaGradient.LEFT_RIGHT,
    10f
)
Skija.dropShadow(20, 150, 180, 60, 20f, 6f, 12f)
```

Transforms and clipping:

```kotlin
Skija.push()
Skija.translate(40, 30)
Skija.scale(1.25f, 1.25f)

Skija.pushScissor(0, 0, 120, 60)
Skija.rect(0, 0, 120, 60, Color(0, 0, 0, 120), 8f)
Skija.popScissor()

Skija.pop()
```

## Text

[`SkijaText`](src/main/kotlin/com/github/noamm9/skijarenderer/helpers/SkijaText.kt) wraps common text drawing and strips
Minecraft formatting codes before measuring or rendering.

```kotlin
SkijaText.draw("Hello", 24f, 24f, Color.WHITE, 16f)
SkijaText.draw("Centered", 120f, 50f, Color.WHITE, 14f, align = SkijaText.Align.CENTER)
SkijaText.draw("Shadow", 24f, 72f, Color.WHITE, 14f, shadow = true)
SkijaText.drawGradient(
    "Gradient",
    24f,
    96f,
    Color.WHITE,
    Color(255, 220, 120),
    18f
)
```

## Images

[`SkijaImage`](src/main/kotlin/com/github/noamm9/skijarenderer/skia/SkijaImage.kt) and `Skija.createImage(...)` support:

- classpath resources
- absolute or relative file paths
- SVG input

```kotlin
val icon = Skija.createImage("assets/skijarenderer/icon.png")
val svg = Skija.createImage("assets/skijarenderer/demo-badge.svg")

Skija.image(icon, 20f, 20f, 32f, 32f, 8f)
Skija.image(svg, 60f, 20f, 32f, 32f)

Skija.deleteImage(icon)
Skija.deleteImage(svg)
```

Notes:

- `createImage(...)` is reference-counted. If you keep the returned handle, call `deleteImage(...)` when you are done.
- `image(path, ...)` is fine for stable assets, but it caches by path.
- Loading images from disk inside a render loop is a bad idea. Preload them.

## GuiGraphics Integration

[`SkijaPIP`](src/main/kotlin/com/github/noamm9/skijarenderer/skia/SkijaPIP.kt) exposes a `GuiGraphics.drawSkija { ... }`
extension.
It respects the current `GuiGraphics` transform and scissor state, and it draws in normal Minecraft GUI coordinates.

```kotlin
override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)

    guiGraphics.drawSkija {
        Skija.rect(0, 0, 220, 120, Color(15, 18, 24, 210), 14f)
        Skija.text("Skija inside GuiGraphics", 16f, 18f, 16f, Color.WHITE)
    }
}
```

## Demo Screen

Press `F8` in-game to open the built-in demo screen. It exercises:

- primitive shapes
- gradients
- text helpers
- scissor clipping
- translated and scaled hover math
- PNG and SVG image loading

## Mouse Coordinates

[`MouseStack`](src/main/kotlin/com/github/noamm9/skijarenderer/helpers/MouseStack.kt) mirrors simple 2D GUI transforms so
hover checks can use local coordinates.

```kotlin
val mouse = MouseStack()

mouse.push()
mouse.translate(panelX, panelY)
mouse.scale(1.5f)

val local = mouse.toLocal(screenMouseX, screenMouseY)
val hovered = local.first in 0.0 .. 120.0 && local.second in 0.0 .. 40.0

mouse.pop()
```

## License

Unlicense. See [`LICENSE.txt`](LICENSE.txt).