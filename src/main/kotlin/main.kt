import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*

object Style {
    val backgroundColor = "#16103f"
    val teamNumberColor = "#38335b"
    val fontColor = "#000000"
    val styles = arrayOf("#ff7616", "#f72e2e", "#7a6aea", "#4bb8f6", "#ffffff")
}

open class Layout(val rect: DOMRect)  {
    val lowerAxisLegend = 0.1
    val fieldPartHeight = 1.0 - lowerAxisLegend

    val teamNumber = 0.10
    val result = 0.20
    val fieldPartWidth = 1.0 - teamNumber - result

    val teamBackground = 0.05

    val legendPad = 50
    val teamPad = 50
    val resultPad = 40

    val teamRect = 50

    val rectLeft = rect.getInt("left")
    val rectTop = rect.getInt("top")
    val rectRight = rect.getInt("right")
    val rectBottom = rect.getInt("bottom")
    val rectWidth = rectRight - rectLeft
    val rectHeight = rectBottom - rectTop

    val fieldWidth: Int = (rectWidth.toFloat() * fieldPartWidth).toInt()
    val fieldHeight: Int = (rectHeight.toFloat() * fieldPartHeight).toInt()

    val teamWidth = (rectWidth.toFloat() * teamNumber).toInt()
    val teamOffsetX = fieldWidth
    val teamHeight = fieldHeight

    val resultWidth = (rectWidth.toFloat() * result).toInt()
    val resultOffsetX = fieldWidth + teamWidth
    val resultHeight = fieldHeight

    val legendWidth = fieldWidth
    val legendHeight = (rectWidth.toFloat() * lowerAxisLegend)
    val legendOffsetY = fieldHeight
}

fun loop(canvas: Canvas) {
    fetch("/stats.json").
            then { args: ArrayList<JsValue> ->
                val response = Response(args[0])
                response.json()
            }.
            then { args: ArrayList<JsValue> ->
                val json = args[0]
                val colors = JsArray(json.getProperty("colors"))
                assert(colors.size == Model.tupleSize)

                val tuple = arrayOf<Int>(0, 0, 0, 0, 0)
                for (i in 0 until colors.size) {
                    val color = colors[i].getInt("color")
                    val counter = colors[i].getInt("counter")
                    tuple[color - 1] = counter
                }
                Model.push(tuple)
            }.
            then { View(canvas).render() }
}

fun main(args: Array<String>) {
    val canvas = document.getElementById("myCanvas").asCanvas
    setInterval(100) {
        loop(canvas)
    }
}
