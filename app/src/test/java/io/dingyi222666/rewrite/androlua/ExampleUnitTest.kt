package io.dingyi222666.rewrite.androlua

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
      println(::a.javaClass)
        assertEquals(4, 2 + 2)
    }
}

fun a() {
    println("a")
}