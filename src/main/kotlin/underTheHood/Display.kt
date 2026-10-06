package underTheHood

import eitities.Author
import eitities.Book
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import kotlin.concurrent.thread

object Display {

    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    private val loadButton = JButton("Load Book").apply {
        addActionListener {


            loadData()
        }
    }

    private fun loadData(step: Int = 0, data: Any? = null) {
        when (step) {

            0 -> {
                loadButton.isEnabled = false
                infoArea.text = "Loading book information...\n"
                loadBook {
                    loadData(1, it)
                }
            }

            1 -> {
                val book = data!! as Book
                infoArea.append("Book: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n")
                loadAuthor(book) {
                    loadData(2, it)
                }
            }

            2 -> {

                val author = data!! as Author
                infoArea.append("Loading author information...\n")
                infoArea.append("Author: ${author.name}\nBio: ${author.bio}")
                loadButton.isEnabled = true
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

    private fun loadBook(callback: (Book) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Book(title = "Bamdad khomar", year = 1383, genre = "Dram"))
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