package com.hypeapps.instasplit.core.model.textrecognition

import android.graphics.Point
import com.google.mlkit.vision.text.Text
import kotlin.math.max
import kotlin.math.min

class TextElementParser {

    companion object {
        val TOTAL_STRINGS = listOf("TOTAL", "BALANCE", "VISA")
    }

    /**
     * Get all the elements in the nested Text object as a list
     */
    private fun getElementsAsList(results: Text): List<Text.Element> {
        val res = mutableListOf<Text.Element>()
        for (block in results.textBlocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    res.add(element)
                }
            }
        }
        return res
    }

    /**
     * Pair elements together by checking if their above and below lines would contain the other's midpoint (they intersect)
     */
    private fun generatePairs(results: Text): List<Pair<Text.Element, Text.Element>> {
        val elements = getElementsAsList(results)
        val pairs = mutableListOf<Pair<Text.Element, Text.Element>>()
        for (element in elements) {
            val intersectingElements = elements.filter { it != element && element.hits(it) }
            for (intersectingElement in intersectingElements) {
                // Check if the intersectingElement also hits the original element
                if (intersectingElement.hits(element)) {
                    pairs.add(element to intersectingElement)
                }
            }
        }
        return pairs
    }

    /**
     * Process and filter out duplicate or irrelevant pairs
     */
    private fun generateFilteredPairs(results: Text): List<Pair<Text.Element, Text.Element>> {
        return generatePairs(results).filter {pair ->
            val (first, second) = pair
            val name = if (first.text.endsWith(":")) first.text.dropLast(1) else first.text
            val price = second.text.replace(",", "").toFloatOrNull()
            name in TOTAL_STRINGS && price != null
        }
    }

    /**
     * Keep only the pair with the highest price whose name is in TOTAL_STRINGS
     */
    fun getTotal(results: Text): Pair<Text.Element, Text.Element>? {
        val pairs = generateFilteredPairs(results)
        return pairs.maxByOrNull { it.second.text.replace(",", "").toFloatOrNull() ?: 0f }
    }
}

private data class Line(val slope: Float, val point: Point) {
    val yIntercept: Float get() = point.y - slope * point.x
}

/**
 * Check if the midpoint of another element is within the parallel lines drawn from this element
 */
private fun Text.Element.hits(element: Text.Element): Boolean {
    val (line1, line2) = this.getLines()
    val otherMidpoint = Point(element.boundingBox!!.centerX(), element.boundingBox!!.centerY())

    // Calculate the y-coordinate of otherMidpoint on line1 and line2
    val yOnLine1 = line1.slope * otherMidpoint.x + line1.yIntercept
    val yOnLine2 = line2.slope * otherMidpoint.x + line2.yIntercept

    // Check if the actual y-coordinate of otherMidpoint lies between yOnLine1 and yOnLine2
    return otherMidpoint.y.toFloat() in min(yOnLine1, yOnLine2)..max(yOnLine1, yOnLine2)
}

/**
 * Get the two parallel lines made by the top and bottom of this element
 */
private fun Text.Element.getLines(): Pair<Line, Line> {
    val slope = this.getSlope()
    val topPoint = this.cornerPoints!!.minByOrNull { it.y }!!
    val bottomPoint = this.cornerPoints!!.maxByOrNull { it.y }!!
    return Line(slope, bottomPoint) to Line(slope, topPoint)
}

/**
 * Get the slope of the element by averaging the slopes of the top and bottom lines
 */
fun Text.Element.getSlope(): Float {
    val cornerPoints = this.cornerPoints!!
    val sortedCornerPoints = cornerPoints.sortedBy { it.y }
    val higherYPoints = sortedCornerPoints.takeLast(2)
    val lowerYPoints = sortedCornerPoints.take(2)

    val x1 = higherYPoints[0].x
    val y1 = higherYPoints[0].y
    val x2 = higherYPoints[1].x
    val y2 = higherYPoints[1].y
    val slope1 = (y2 - y1).toFloat() / (x2 - x1).toFloat()

    val x3 = lowerYPoints[0].x
    val y3 = lowerYPoints[0].y
    val x4 = lowerYPoints[1].x
    val y4 = lowerYPoints[1].y
    val slope2 = (y4 - y3).toFloat() / (x4 - x3).toFloat()
    return (slope1 + slope2) / 2
}