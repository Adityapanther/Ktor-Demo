package com.example

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class OrderRouteTests{
    private lateinit var test:TestApplication

    @Before
    fun start(){
        test = TestApplication{
            application {
                configureRouting()
                configureSerialization()
            }
        }
    }
    @Test
     fun testGetOrder(){
        suspend {
            test.client.get("/order/2020-04-06-01").apply {
                println("response.......>${status}-------${bodyAsText()}")
                assertEquals(
                    """{"number":"2020-04-06-01","contents":[{"item":"Ham Sandwich","amount":2,"price":5.5},{"item":"Water","amount":1,"price":1.5},{"item":"Beer","amount":3,"price":2.3},{"item":"Cheesecake","amount":1,"price":3.75}]}""",
                    bodyAsText()
                )
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }

    @After
    fun close(){
        test.client.close()
    }
}