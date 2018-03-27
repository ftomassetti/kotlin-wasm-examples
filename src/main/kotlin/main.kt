import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*

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
    setInterval(1000) {
        loop(canvas)
    }
}

