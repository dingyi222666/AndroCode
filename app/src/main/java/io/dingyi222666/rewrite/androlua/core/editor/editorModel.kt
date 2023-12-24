package io.dingyi222666.rewrite.androlua.core.editor

import kotlinx.serialization.Serializable


@Serializable
data class EditorModel(
    // (line,column)
    var cursor: IntArray?,
    var path: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EditorModel

        if (cursor != null) {
            if (other.cursor == null) return false
            if (!cursor.contentEquals(other.cursor)) return false
        } else if (other.cursor != null) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cursor?.contentHashCode() ?: 0
        result = 31 * result + path.hashCode()
        return result
    }
}