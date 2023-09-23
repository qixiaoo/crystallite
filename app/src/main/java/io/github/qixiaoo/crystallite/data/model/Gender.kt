package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

const val UNKNOWN_GENDER_VALUE = -1

enum class Gender(val value: Int) {
    @SerializedName("1")
    MALE(1),

    @SerializedName("2")
    FEMALE(2),

    @SerializedName("")
    UNKNOWN(UNKNOWN_GENDER_VALUE);

    override fun toString(): String {
        return if (value == UNKNOWN_GENDER_VALUE) {
            ""
        } else {
            value.toString()
        }
    }
}
