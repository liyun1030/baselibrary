package com.ly.baselibrary

import android.os.Build
import androidx.annotation.RequiresApi
import com.common.base.bean.BaseModel
import com.common.base.bean.ServerModel
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis

object TestKotlin {
    @JvmStatic
    fun main(args: Array<String>) {
        testAsync()
    }

    lateinit var a1: String

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestTokenAsync(): CompletableFuture<BaseModel> {
        return CompletableFuture.completedFuture(BaseModel())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun createPostAsync(token: BaseModel): CompletableFuture<ServerModel> {
        return CompletableFuture.completedFuture(ServerModel())
    }

    fun processPost(post: ServerModel) {

    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun processData(post: ServerModel.Author) {
        requestTokenAsync()
            .thenCompose { token ->
                createPostAsync(token)
            }.thenAccept {
                processPost(it)
            }
    }

    fun testWhen() {
        var tag = 1;
        when (tag) {
            5 -> {
                System.out.println("55555")
            }
            else -> {
                System.out.println(tag)
            }
        }
    }

    fun testCancelAppThread() = runBlocking {
        val job = Job()
        val coroutine = List(10) { i ->
            launch(coroutineContext) {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                println("Coroutine $i is done")
            }
        }
        println("Launched ${coroutine.size} coroutines")
        delay(500L) // delay for half a second
        println("Cancelling the job!")
        job.cancelAndJoin()
    }

    fun testSwitchThread() {
        newSingleThreadContext("ctx1").use {
            newFixedThreadPoolContext(4, "ctx2").use {
                runBlocking {

                }
            }
        }
    }

    fun testAsync() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingOne() }
            val two = async {
                doSomethingTwo()
            }
            println("The answer is ${one.await() + two.await()}")
        }
    }

    suspend fun doSomethingOne(): Int {
        delay(1000)
        return 11
    }

    suspend fun doSomethingTwo(): Int {
        delay(1000)
        return 12
    }

    fun testTimeOut() = runBlocking<Any> {
        var result = withTimeoutOrNull(1000) {
            repeat(1000) {
                println("I'm sleeping $it")
                delay(500)
            }
            "Done"
        }
        println("result is $result")
    }

    fun testCoroutines() = runBlocking<Unit> {
        var job = launch {
            doWorld()
        }
        println("hello")
        job.cancelAndJoin()
        println("1111111111")
    }

    suspend fun doWorld() {
        with(Dispatchers.IO) {
            delay(1000L)
            println("World!")
        }
    }
}