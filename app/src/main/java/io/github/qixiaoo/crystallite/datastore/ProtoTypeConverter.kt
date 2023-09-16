package io.github.qixiaoo.crystallite.datastore

import io.github.qixiaoo.crystallite.data.model.ReadingMode

fun ReadingModeProto.toModel(): ReadingMode {
    return when (this) {
        ReadingModeProto.READING_MODE_LTR -> ReadingMode.LeftToRight
        ReadingModeProto.READING_MODE_RTL -> ReadingMode.RightToLeft
        ReadingModeProto.UNRECOGNIZED -> ReadingMode.LeftToRight
    }
}

fun ReadingMode.toProto(): ReadingModeProto {
    return when (this) {
        ReadingMode.LeftToRight -> ReadingModeProto.READING_MODE_LTR
        ReadingMode.RightToLeft -> ReadingModeProto.READING_MODE_RTL
    }
}