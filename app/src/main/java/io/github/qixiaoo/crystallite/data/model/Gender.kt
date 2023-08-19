package io.github.qixiaoo.crystallite.data.model

enum class Gender(private val value: Int) {
    MALE(1),
    FEMALE(2);

    override fun toString(): String {
        return value.toString()
    }
}