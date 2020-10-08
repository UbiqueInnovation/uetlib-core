package ch.ubique.backend.core.http.response

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import java.util.stream.Collectors

class DeletedResponseTest {
    @Test
    fun test() {

        // test no entries with varargs interface
        var deletedResponse: DeletedResponse = DeletedResponse.from()
        Assertions.assertEquals(emptyList<Any>(), deletedResponse.ids)

        // test no entries with list interface
        deletedResponse = DeletedResponse.from(emptyList())
        Assertions.assertEquals(emptyList<Any>(), deletedResponse.ids)

        // test single int
        val id = 1
        deletedResponse = DeletedResponse.from(1)
        Assertions.assertEquals(id, deletedResponse.ids[0])

        // test single Integer
        val idBoxed = 1
        deletedResponse = DeletedResponse.from(1)
        Assertions.assertEquals(idBoxed, deletedResponse.ids[0])

        // test array of ints with varargs
        val ids = intArrayOf(2, 3)
        deletedResponse = DeletedResponse.from(ids[0], ids[1])
        Assertions.assertIterableEquals(
                Arrays.stream(ids).boxed().collect(Collectors.toList()), deletedResponse.ids)

        // test list of Integers with varargs
        val idsBoxed = listOf(2, 3)
        deletedResponse = DeletedResponse.from(idsBoxed[0], idsBoxed[1])
        Assertions.assertIterableEquals(idsBoxed, deletedResponse.ids)

        // test list of Integers with List interface
        deletedResponse = DeletedResponse.from(idsBoxed)
        Assertions.assertIterableEquals(idsBoxed, deletedResponse.ids)
    }
}