package concurrency

import eitities.Book
import kotlinx.coroutines.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.Executors
import javax.swing.*
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.milliseconds

object Display {

    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val scope = CoroutineScope(CoroutineName("My coroutine") + dispatcher)

    private val loadButton = JButton("Load Book").apply {
        addActionListener {
            isEnabled = false
            infoArea.text = "Loading book information...\n"

            val jobs = mutableListOf<Deferred<Book>>()
            repeat(10) {
                scope.async {
                    val book = loadBook()
                    infoArea.append("Book $it: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n\n")
                    book
                }.let { job -> jobs.add(job) }
            }


            scope.launch {
                val books = jobs.awaitAll()
                println(books.joinToString(", "))
                isEnabled = true
            }
        }
    }
    private val timerLabel = JLabel("Time: 00:00")
    private val topPanel = JPanel(BorderLayout()).apply {
        add(timerLabel, BorderLayout.WEST)
        add(loadButton, BorderLayout.EAST)
    }

    private val mainFrame = JFrame("Book and Author info").apply {
        layout = BorderLayout()
        add(topPanel, BorderLayout.NORTH)
        add(JScrollPane(infoArea), BorderLayout.CENTER)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                scope.cancel()
            }
        })
        size = Dimension(400, 300)
    }

    fun show() {
        mainFrame.isVisible = true
        startTimer()
    }

    private fun longOperation() {
        val list = mutableListOf<Int>()
        repeat(300_000) {
            list.add(0, it)
        }

    }

    private suspend fun loadBook(): Book {
        withContext(Dispatchers.Default) {
            longOperation()
        }
        return Book(title = "Bamdad khomar", year = 1383, genre = "Dram")
    }

    private fun startTimer() {
        scope.launch {
            var totalSecond = 0
            while (true) {
                val minute = totalSecond / 60
                val second = totalSecond % 60
                timerLabel.text = String.format("Timer: %02d:%02d", minute, second)
                totalSecond++
                delay(1000.milliseconds)
            }
        }

    }

}