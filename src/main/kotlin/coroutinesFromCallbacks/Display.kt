package coroutinesFromCallbacks

import eitities.Author
import eitities.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import kotlin.concurrent.thread

object Display {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    private val loadButton = JButton("Load Book").apply {
        addActionListener {
            scope.launch {
                isEnabled = false

                infoArea.text = "Loading book information...\n"

                val book = loadBook()
                infoArea.append("Book: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n")
                infoArea.append("Loading author information...\n")

                val author = loadAuthor(book)
                infoArea.append("Author: ${author.name}\nBio: ${author.bio}")
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
        size = Dimension(400, 300)
    }

    fun show() {
        mainFrame.isVisible = true
        startTimer()
    }

    private suspend fun loadBook(): Book {
        return suspendCancellableCoroutine { continuation ->
            loadBook { book ->
                continuation.resumeWith(Result.success(book))
            }
        }
    }

    private fun loadBook(callback: (Book) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Book(title = "Bamdad khomar", year = 1383, genre = "Dram"))
        }
    }

    private suspend fun loadAuthor(book: Book): Author {
        return suspendCancellableCoroutine { continuation ->
            loadAuthor(book) { author ->
                continuation.resumeWith(Result.success(author))
            }
        }
    }

    private fun loadAuthor(book: Book, callback: (Author) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Author(name = "ALi", bio = "Iranian and programing"))
        }
    }

    private fun startTimer() {
        thread {
            var totalSecond = 0
            while (true) {
                val minute = totalSecond / 60
                val second = totalSecond % 60
                timerLabel.text = String.format("Timer: %02d:%02d", minute, second)
                totalSecond++
                Thread.sleep(1000)
            }
        }

    }

}