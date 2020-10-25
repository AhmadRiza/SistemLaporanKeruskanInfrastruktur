package com.gis.sistemlaporankeruskaninfrastruktur

import com.gis.sistemlaporankeruskaninfrastruktur.core.Polygon
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun convertPolygon() {

        Polygon.initialize()

        Polygon.regionList.forEach {

            println("${it.name}")

            it.polygon.sides.forEach {
                println(
                    "Point(${String.format("%.6f", it.start.x)},${
                        String.format(
                            "%.6f",
                            it.start.y
                        )
                    }),"
                )
            }

            println("\n\n\n")

        }


    }

}
