package io.dingyi222666.androcode.api.util

import com.google.common.base.Objects
import com.google.common.collect.Ordering
import java.util.Locale

/**
 * Represents, parses, and compares version numbers. Supports a couple of different schemes:   * MAJOR.MINOR.MICRO-QUALIFIER (the default).  * MAJOR.MINOR.MICRO.PATCH-QUALIFIER.
 *
 *
 * The [.parse] method handles missing parts and allows "." to be used instead of "-", and "_" to be used instead of "." for the patch number.
 *
 *
 * This class considers missing parts to be 0, so that "1.0" == "1.0.0" == "1.0.0_0".
 *
 *
 * Note that this class considers "1.2.3-something" less than "1.2.3". Qualifiers are compared lexicographically ("1.2.3-alpha" &lt; "1.2.3-beta") and case-insensitive ("1.2.3-alpha" &lt;
 * "1.2.3.RELEASE").
 *
 *
 * To check if a version number is at least "1.2.3", disregarding a potential qualifier like "beta", use `version.getBaseVersion().compareTo(VersionNumber.parse("1.2.3")) &gt;= 0`.
 */
class VersionNumber private constructor(
    val major: Int,
    val minor: Int,
    val micro: Int,
    val patch: Int,
    val qualifier: String?,
    private val scheme: AbstractScheme
) :
    Comparable<VersionNumber> {
    constructor(major: Int, minor: Int, micro: Int, qualifier: String?) : this(
        major,
        minor,
        micro,
        0,
        qualifier,
        DEFAULT_SCHEME
    )

    constructor(major: Int, minor: Int, micro: Int, patch: Int, qualifier: String?) : this(
        major,
        minor,
        micro,
        patch,
        qualifier,
        PATCH_SCHEME
    )

    val baseVersion: VersionNumber
        get() = VersionNumber(major, minor, micro, patch, null, scheme)

    override fun compareTo(other: VersionNumber): Int {
        if (major != other.major) {
            return major - other.major
        }
        if (minor != other.minor) {
            return minor - other.minor
        }
        if (micro != other.micro) {
            return micro - other.micro
        }
        if (patch != other.patch) {
            return patch - other.patch
        }
        return Ordering.natural<Comparable<*>>().nullsLast<Comparable<*>?>().compare(
            toLowerCase(
                qualifier
            ), toLowerCase(other.qualifier)
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is VersionNumber && compareTo(other) == 0
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + micro
        result = 31 * result + patch
        result = 31 * result + Objects.hashCode(qualifier)
        return result
    }

    override fun toString(): String {
        return scheme.format(this)!!
    }

    private fun toLowerCase(string: String?): String? {
        return string?.lowercase(Locale.getDefault())
    }

    interface Scheme {
        fun parse(value: String): VersionNumber

        fun format(versionNumber: VersionNumber): String
    }

    private abstract class AbstractScheme protected constructor(val depth: Int) : Scheme {
        override fun parse(value: String): VersionNumber {
            if (value.isEmpty()) {
                return UNKNOWN
            }
            val scanner = Scanner(value)

            var major = 0
            var minor = 0
            var micro = 0
            var patch = 0

            if (!scanner.hasDigit()) {
                return UNKNOWN
            }
            major = scanner.scanDigit()
            if (scanner.isSeparatorAndDigit('.')) {
                scanner.skipSeparator()
                minor = scanner.scanDigit()
                if (scanner.isSeparatorAndDigit('.')) {
                    scanner.skipSeparator()
                    micro = scanner.scanDigit()
                    if (depth > 3 && scanner.isSeparatorAndDigit('.', '_')) {
                        scanner.skipSeparator()
                        patch = scanner.scanDigit()
                    }
                }
            }

            if (scanner.isEnd) {
                return VersionNumber(major, minor, micro, patch, null, this)
            }

            if (scanner.isQualifier) {
                scanner.skipSeparator()
                return VersionNumber(
                    major, minor, micro, patch, scanner.remainder(),
                    this
                )
            }

            return UNKNOWN
        }

        private class Scanner(val str: String) {
            var pos: Int = 0

            fun hasDigit(): Boolean {
                return pos < str.length && Character.isDigit(str[pos])
            }

            fun isSeparatorAndDigit(vararg separators: Char): Boolean {
                return pos < str.length - 1 && oneOf(*separators) && Character.isDigit(
                    str[pos + 1]
                )
            }

            private fun oneOf(vararg separators: Char): Boolean {
                val current = str[pos]
                for (i in separators.indices) {
                    val separator = separators[i]
                    if (current == separator) {
                        return true
                    }
                }
                return false
            }

            val isQualifier: Boolean
                get() = pos < str.length - 1 && oneOf('.', '-')

            fun scanDigit(): Int {
                val start = pos
                while (hasDigit()) {
                    pos++
                }
                return str.substring(start, pos).toInt()
            }

            val isEnd: Boolean
                get() = pos == str.length

            fun skipSeparator() {
                pos++
            }

            fun remainder(): String? {
                return if (pos == str.length) null else str.substring(pos)
            }
        }
    }

    private class DefaultScheme : AbstractScheme(3) {
        override fun format(versionNumber: VersionNumber): String {
            return String.format(
                VERSION_TEMPLATE,
                versionNumber.major,
                versionNumber.minor,
                versionNumber.micro,
                if (versionNumber.qualifier == null) "" else "-" + versionNumber.qualifier
            )
        }

        companion object {
            private const val VERSION_TEMPLATE = "%d.%d.%d%s"
        }
    }

    private class SchemeWithPatchVersion : AbstractScheme(4) {
        override fun format(versionNumber: VersionNumber): String {
            return String.format(
                VERSION_TEMPLATE,
                versionNumber.major,
                versionNumber.minor,
                versionNumber.micro,
                versionNumber.patch,
                if (versionNumber.qualifier == null) "" else "-" + versionNumber.qualifier
            )
        }

        companion object {
            private const val VERSION_TEMPLATE = "%d.%d.%d.%d%s"
        }
    }

    companion object {
        private val DEFAULT_SCHEME = DefaultScheme()
        private val PATCH_SCHEME = SchemeWithPatchVersion()
        val UNKNOWN: VersionNumber = version(0)

        @JvmOverloads
        fun version(major: Int, minor: Int = 0): VersionNumber {
            return VersionNumber(major, minor, 0, 0, null, DEFAULT_SCHEME)
        }

        /**
         * Returns the default MAJOR.MINOR.MICRO-QUALIFIER scheme.
         */
        fun scheme(): Scheme {
            return DEFAULT_SCHEME
        }

        /**
         * Returns the MAJOR.MINOR.MICRO.PATCH-QUALIFIER scheme.
         */
        fun withPatchNumber(): Scheme {
            return PATCH_SCHEME
        }

        fun parse(versionString: String): VersionNumber {
            return DEFAULT_SCHEME.parse(versionString)
        }
    }
}