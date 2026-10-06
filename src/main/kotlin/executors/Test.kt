package executors

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

fun main() {

    val executorService = Executors.newCachedThreadPool()

    repeat(100_000){
        executorService.execute {
            processImage(Image(it))
        }
    }

}

private fun processImage(image:Image){
    println("Image ${image.id} is processing...")
    Thread.sleep(1000)
    println("Image ${image.id} processed!")

}

private data class Image(val id: Int)