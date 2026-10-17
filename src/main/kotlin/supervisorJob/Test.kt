package supervisorJob

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.milliseconds


private val parentJob = SupervisorJob()
private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    println("Exception caught")
}
private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
private val scope = CoroutineScope(parentJob + exceptionHandler + dispatcher)

fun main() {

    scope.launch {
        longOperation(3000,1)
        error("")
    }

    scope.launch {
        longOperation(4000,2)
    }

}

private suspend fun longOperation(timeMillis: Long, number : Int){
    println("Started $number")
    delay(timeMillis.milliseconds)
    println("Finished $number")
}