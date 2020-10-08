package ch.ubique.backend.core.http.response

class DeletedResponse private constructor(val ids: List<Int>) {

    companion object {
        @JvmStatic
        fun from(vararg ids: Int): DeletedResponse {
            return DeletedResponse(ids.asList())
        }

        @JvmStatic
        fun from(ids: List<Int>): DeletedResponse {
            return DeletedResponse(ids)
        }
    }
}