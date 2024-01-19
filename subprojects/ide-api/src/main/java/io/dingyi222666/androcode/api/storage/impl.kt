package io.dingyi222666.androcode.api.storage

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.io.File

class JSONStorage(
    private val path: File
) : Storage {

    private val objectMap = mutableMapOf<String, JsonElement>()

    private var isLoading = false

    private fun write() {
        if (path.exists()) {
            path.delete()
        }
        path.outputStream().bufferedWriter().write(Json.encodeToString(objectMap))
    }


    private fun checkLoad() {
        if (!isLoading) {
            isLoading = true
            reload()
            isLoading = false
        }
    }

    override fun getStringOrNull(key: String): String? {
        checkLoad()

        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        if (valuePrimitive.isString) {
            return valuePrimitive.content
        }

        return null
    }

    override fun getString(key: String, defaultValue: String): String {
        return getStringOrNull(key) ?: defaultValue
    }

    override fun getString(key: String): String {
        return getStringOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a boolean or null")
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        checkLoad()

        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        return valuePrimitive.booleanOrNull
    }

    override fun getBoolean(key: String): Boolean {
        return getBooleanOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a boolean")
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getBooleanOrNull(key) ?: defaultValue
    }


    override fun getLongOrNull(key: String): Long? {
        checkLoad()

        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        return valuePrimitive.longOrNull
    }

    override fun getInt(key: String): Int {
        return getIntOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a int")
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return getIntOrNull(key) ?: defaultValue
    }

    override fun getIntOrNull(key: String): Int? {
        checkLoad()

        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        return valuePrimitive.intOrNull
    }

    override fun getLong(key: String): Long {
        return getLongOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a long")
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return getLongOrNull(key) ?: defaultValue
    }

    override fun getFloatOrNull(key: String): Float? {
        checkLoad()
        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        return valuePrimitive.floatOrNull
    }

    override fun getFloat(key: String): Float {
        return getFloatOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a float")
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return getFloatOrNull(key) ?: defaultValue
    }

    override fun getDoubleOrNull(key: String): Double? {
        checkLoad()

        val value = objectMap[key] ?: return null

        val valuePrimitive = value.jsonPrimitive

        return valuePrimitive.doubleOrNull
    }

    override fun <T : Any> getArray(
        key: String,
        mappingFunction: (jsonElement: JsonElement) -> T
    ): Array<T> {
        return getArrayOrNull(key, mappingFunction)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a array")
    }

    override fun <T : Any> getArray(
        key: String,
        defaultValue: Array<T>,
        mappingFunction: (jsonElement: JsonElement) -> T
    ): Array<T> {
        return getArrayOrNull(key, mappingFunction) ?: defaultValue
    }

    override fun <T : Any> getArrayOrNull(
        key: String,
        mappingFunction: (jsonElement: JsonElement) -> T?
    ): Array<T>? {
        val value = objectMap[key] ?: return null

        val resultList = mutableListOf<T>()

        for (element in value.jsonArray) {
            val mappedElement = mappingFunction(element) ?: return null

            resultList.add(mappedElement)

        }

        return Array<Any>(resultList.size)
        { resultList[it] } as Array<T>
    }

    override fun <T : Any> getObject(
        key: String,
        mappingFunction: (jsonElement: JsonElement) -> T
    ): T {
        val value = objectMap[key]
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a object")

        return mappingFunction(value)
    }

    override fun <T : Any> getObjectOrNull(
        key: String,
        mappingFunction: (jsonElement: JsonElement?) -> T?
    ): T? {
        val value = objectMap[key] ?: return null

        return mappingFunction(value)
    }


    override fun getRaw(key: String): JsonElement {
        checkLoad()

        return objectMap[key] ?: JsonNull
    }

    override fun getNull(key: String): JsonNull {
        checkLoad()

        val value = objectMap[key] ?: return JsonNull

        if (value !is JsonNull) {
            throw NullPointerException("The content $value is not a null")
        }

        return JsonNull
    }

    override fun setObject(key: String, value: JsonElement): Boolean {
        objectMap[key] = value

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setNull(key: String): Boolean {
        objectMap[key] = JsonNull

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setBoolean(key: String, value: Boolean): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setLong(key: String, value: Long): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setFloat(key: String, value: Float): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setDouble(key: String, value: Double): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setInt(key: String, value: Int): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun <T : Any> setArray(
        key: String,
        value: Array<T>,
        mappingFunction: (value: T) -> JsonElement
    ): Boolean {

        val mappedArray = value.map(mappingFunction)

        objectMap[key] = JsonArray(mappedArray)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun setString(key: String, value: String): Boolean {
        objectMap[key] = JsonPrimitive(value)

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun remove(key: String): Boolean {
        objectMap.remove(key) ?: return false

        return kotlin.runCatching {
            write()
        }.isSuccess
    }

    override fun dispose() {

    }

    override fun getDouble(key: String): Double {
        return getDoubleOrNull(key)
            ?: throw NullPointerException("The content ${objectMap[key].toString()} is not a double")
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return getDoubleOrNull(key) ?: defaultValue
    }

    override fun reload() {


        if (!path.exists()) {
            objectMap.clear()
            return
        }

        val rawText = path.inputStream().bufferedReader().readText()

        val json = Json.parseToJsonElement(rawText)


        objectMap.putAll(json.jsonObject.toMap())

    }

}

