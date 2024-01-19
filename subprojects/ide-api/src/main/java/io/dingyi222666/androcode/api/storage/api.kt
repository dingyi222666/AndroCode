package io.dingyi222666.androcode.api.storage

import io.dingyi222666.androcode.api.common.Disposable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

interface Storage : Disposable {

    fun getString(key: String): String

    fun getStringOrNull(key: String): String?

    fun getString(key: String, defaultValue: String): String

    fun getBoolean(key: String): Boolean

    fun getBooleanOrNull(key: String): Boolean?

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getLong(key: String): Long

    fun getLongOrNull(key: String): Long?

    fun getLong(key: String, defaultValue: Long): Long

    fun getInt(key: String): Int

    fun getIntOrNull(key: String): Int?

    fun getInt(key: String, defaultValue: Int): Int

    fun getFloat(key: String): Float

    fun getFloatOrNull(key: String): Float?
    fun getFloat(key: String, defaultValue: Float): Float

    fun getDouble(key: String): Double

    fun getDoubleOrNull(key: String): Double?

    fun getDouble(key: String, defaultValue: Double): Double

    fun <T : Any> getArray(key: String, mappingFunction: (jsonElement: JsonElement) -> T): Array<T>

    fun <T : Any> getArrayOrNull(
        key: String,
        mappingFunction: (jsonElement: JsonElement) -> T?
    ): Array<T>?

    fun <T : Any> getArray(
        key: String,
        defaultValue: Array<T>,
        mappingFunction: (jsonElement: JsonElement) -> T
    ): Array<T>


    fun <T : Any> getObject(key: String, mappingFunction: (jsonElement: JsonElement) -> T): T

    fun <T : Any> getObjectOrNull(
        key: String,
        mappingFunction: (jsonElement: JsonElement?) -> T?
    ): T?

    fun getRaw(key: String): JsonElement

    fun getNull(key: String): JsonNull

    fun setObject(key: String, value: JsonElement): Boolean

    fun setNull(key: String): Boolean

    fun setBoolean(key: String, value: Boolean): Boolean

    fun setLong(key: String, value: Long): Boolean

    fun setFloat(key: String, value: Float): Boolean

    fun setDouble(key: String, value: Double): Boolean

    fun setInt(key: String, value: Int): Boolean

    fun <T : Any> setArray(
        key: String,
        value: Array<T>,
        mappingFunction: (value: T) -> JsonElement
    ): Boolean

    fun setString(key: String, value: String): Boolean

    fun remove(key: String): Boolean

    fun reload()
}

fun Storage.getStringArray(key: String): Array<String> {
    return getStringArrayOrNull(key)
        ?: throw NullPointerException("The content ${getRaw(key)} is not a string array")
}

fun Storage.getStringArrayOrNull(key: String): Array<String>? {
    return getArrayOrNull(key) { it.jsonPrimitive.contentOrNull }
}

fun Storage.getStringArray(key: String, defaultValue: Array<String>): Array<String> {
    return getArray(key, defaultValue) { it.jsonPrimitive.content }
}

fun Storage.getIntArray(key: String): Array<Int> {
    return getIntArrayOrNull(key)
        ?: throw NullPointerException("The content ${getRaw(key)} is not a int array")
}

fun Storage.getIntArrayOrNull(key: String): Array<Int>? {
    return getArrayOrNull(key) { it.jsonPrimitive.intOrNull }
}

fun Storage.getIntArray(key: String, defaultValue: Array<Int>): Array<Int> {
    return getArray(key, defaultValue) { it.jsonPrimitive.int }
}

fun Storage.getDoubleArray(key: String): Array<Double> {
    return getDoubleArrayOrNull(key)
        ?: throw NullPointerException("The content ${getRaw(key)} is not a double array")
}

fun Storage.getDoubleArrayOrNull(key: String): Array<Double>? {
    return getArrayOrNull(key) { it.jsonPrimitive.doubleOrNull }
}

fun Storage.getDoubleArray(key: String, defaultValue: Array<Double>): Array<Double> {
    return getArray(key, defaultValue) { it.jsonPrimitive.double }
}

fun Storage.getLongArray(key: String): Array<Long> {
    return getLongArrayOrNull(key)
        ?: throw NullPointerException("The content ${getRaw(key)} is not a long array")
}

fun Storage.getLongArrayOrNull(key: String): Array<Long>? {
    return getArrayOrNull(key) { it.jsonPrimitive.longOrNull }
}

fun Storage.getLongArray(key: String, defaultValue: Array<Long>): Array<Long> {
    return getArray(key, defaultValue) { it.jsonPrimitive.long }
}

fun Storage.getFloatArray(key: String): Array<Float> {
    return getFloatArrayOrNull(key)
        ?: throw NullPointerException("The content ${getRaw(key)} is not a float array")
}

fun Storage.getFloatArrayOrNull(key: String): Array<Float>? {
    return getArrayOrNull(key) { it.jsonPrimitive.floatOrNull }
}

fun Storage.getFloatArray(key: String, defaultValue: Array<Float>): Array<Float> {
    return getArray(key, defaultValue) { it.jsonPrimitive.float }
}


