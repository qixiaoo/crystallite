package io.github.qixiaoo.crystallite.datastore

import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.ReadingMode

fun ReadingModeProto.toModel(): ReadingMode {
    return when (this) {
        ReadingModeProto.READING_MODE_LTR -> ReadingMode.LeftToRight
        ReadingModeProto.READING_MODE_RTL -> ReadingMode.RightToLeft
        ReadingModeProto.READING_MODE_CONTINUOUS_VERTICAL -> ReadingMode.ContinuousVertical
        ReadingModeProto.UNRECOGNIZED -> ReadingMode.LeftToRight
    }
}

fun ReadingMode.toProto(): ReadingModeProto {
    return when (this) {
        ReadingMode.LeftToRight -> ReadingModeProto.READING_MODE_LTR
        ReadingMode.RightToLeft -> ReadingModeProto.READING_MODE_RTL
        ReadingMode.ContinuousVertical -> ReadingModeProto.READING_MODE_CONTINUOUS_VERTICAL
    }
}

fun GenderProto.toModel(): Gender {
    return when (this) {
        GenderProto.GENDER_MALE -> Gender.MALE
        GenderProto.GENDER_FEMALE -> Gender.FEMALE
        GenderProto.GENDER_UNKNOWN -> Gender.UNKNOWN
        GenderProto.UNRECOGNIZED -> Gender.UNKNOWN
    }
}

fun Gender.toProto(): GenderProto {
    return when (this) {
        Gender.MALE -> GenderProto.GENDER_MALE
        Gender.FEMALE -> GenderProto.GENDER_FEMALE
        Gender.UNKNOWN -> GenderProto.GENDER_UNKNOWN
    }
}
