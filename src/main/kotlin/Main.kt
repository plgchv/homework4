package org.example

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import java.io.FileReader
import java.util.function.Supplier
import java.util.stream.Stream
import kotlin.math.ceil

@Serializable
data class Visitor (
    val name: String,
    val surname: String,
    val phone: String,
    val subscribed: Boolean,
    val favouriteBooks: List<Book>
)

@Serializable
data class Book (
    val name: String,
    val author: String,
    val publishingYear: Int,
    val isbn: String,
    val publisher: String,
)

data class Sms(var phone: String? = null, var message: String? = null)

fun main() {
    val visitors = Gson().fromJson(FileReader("src/main/resources/books.json"), Array<Visitor>::class.java).toList()
    val booksSupplier = Supplier<Stream<Book>> { visitors.stream().flatMap { it.favouriteBooks.stream() }}
    val uniqueBooksSupplier = Supplier<Stream<Book>> { booksSupplier.get().distinct() }

    println("1. Вывести список посетителей и их количество.\n" +
            "2. Вывести список и количество книг, добавленных посетителями в избранное, без повторений.\n" +
            "3. Отсортировать по году издания и вывести список книг.\n" +
            "4. Проверить, есть ли у кого-то в избранном книга автора 'Jane Austen'\n" +
            "5. Вывести максимальное число добавленных в избранное книг.\n" +
            "6. Создать класс sms-сообщения с полями: номер телефона и сообщение. Далее сгруппируйте посетителей библиотеки, согласившихся на рассылку, по категориям\n")
    print("> ")
    val choice = readln().toInt()
    when (choice) {
        1 -> {
            visitors.stream().forEach(System.out::println)
            println(visitors.stream().count())
        }
        2 -> {
            uniqueBooksSupplier.get().forEach(System.out::println)
            println(uniqueBooksSupplier.get().count())
        }
        3 -> uniqueBooksSupplier.get().sorted(Comparator.comparingInt(Book::publishingYear)).forEach(System.out::println)
        4 -> println(uniqueBooksSupplier.get().anyMatch { it.author == "Jane Austen" })
        5 -> println(visitors.stream().map { it.favouriteBooks.count() }.toList().max())
        6 -> {
            var sms: Sms
            val bookworms = mutableListOf<Sms>()
            val readmores = mutableListOf<Sms>()
            val fines = mutableListOf<Sms>()

            val favBooksAvg = ceil(booksSupplier.get().count().toDouble() / visitors.stream().count())
            visitors.stream().filter { it.subscribed }.forEach {
                sms = Sms()
                sms.phone = it.phone
                if (it.favouriteBooks.count() > favBooksAvg) {
                    sms.message = "you are a bookworm"
                    bookworms.add(sms)
                }
                else if(it.favouriteBooks.count() < favBooksAvg) {
                    sms.message = "read more"
                    readmores.add(sms)
                }
                else {
                    sms.message = ("fine")
                    fines.add(sms)
                }
            }
            println("Bookworms ")
            bookworms.stream().forEach(System.out::println)
            println("\nReadmores")
            readmores.stream().forEach(System.out::println)
            println("\nFines: ")
            fines.stream().forEach(System.out::println)
        }
        else -> println("Wrong choice. Try again")
    }
}