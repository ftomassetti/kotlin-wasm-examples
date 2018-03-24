object Model {
    val tupleSize = 5
    val styles = Style.styles

    val backLogSize = 100
    private val backLog = IntArray(backLogSize * tupleSize, {0})
    private fun offset(time: Int, color: Int) = time * tupleSize + color

    var current = 0
    var maximal = 0

    fun colors(time: Int, color: Int): Int = backLog[offset(time, color)]

    fun tuple(time: Int) = backLog.slice(time * tupleSize .. (time + 1) * tupleSize - 1)

    fun push(new: Array<Int>) {
        assert(new.size == tupleSize)

        new.forEachIndexed { index, it ->
            backLog[offset(current, index)] = it
        }
        current = (current+1) % backLogSize

        new.forEach {
            if (it > maximal) maximal = it
        }
    }
}
