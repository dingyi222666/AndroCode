package io.dingyi222666.rewrite.androlua.api.plugin.loader

import io.dingyi222666.rewrite.androlua.api.util.Trie
import java.io.IOException
import java.net.URL
import java.util.Enumeration
import javax.annotation.Nonnull


/**
 * A ClassLoader which hides all non-system classes, packages and resources. Allows certain non-system packages and classes to be declared as visible. By default, only the Java system classes,
 * packages and resources are visible.
 */
class FilteringClassLoader(parent: ClassLoader?, spec: Spec) : ClassLoader(parent) {
    private val packageNames: Set<String> = HashSet(spec.packageNames)
    private val packagePrefixes: TrieSet = TrieSet(spec.packagePrefixes)
    private val resourcePrefixes: TrieSet = TrieSet(spec.resourcePrefixes)
    private val resourceNames: Set<String> = HashSet(spec.resourceNames)
    private val classNames: Set<String> = HashSet(spec.classNames)
    private val disallowedClassNames: Set<String> = HashSet(spec.disallowedClassNames)
    private val disallowedPackagePrefixes: TrieSet = TrieSet(spec.disallowedPackagePrefixes)


    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        try {
            return EXT_CLASS_LOADER!!.loadClass(name)
        } catch (ignore: ClassNotFoundException) {
            // ignore
        }
        if (!classAllowed(name)) {
            throw ClassNotFoundException("$name not found.")
        }
        val cl = super.loadClass(name, false)
        if (resolve) {
            resolveClass(cl)
        }
        return cl
    }

    @Suppress("deprecation")
    override fun getPackage(name: String): Package? {
        val p = super.getPackage(name)
        return if (p == null || !allowed(p)) {
            null
        } else p
    }

    override fun getPackages(): Array<Package> {
        val packages: MutableList<Package> = ArrayList()
        for (p in super.getPackages()) {
            if (allowed(p)) {
                packages.add(p)
            }
        }
        return packages.toTypedArray<Package>()
    }

    override fun getResource(name: String): URL {
        return if (allowed(name)) {
            super.getResource(name)
        } else EXT_CLASS_LOADER!!.getResource(name)
    }

    @Throws(IOException::class)
    override fun getResources(name: String): Enumeration<URL> {
        return if (allowed(name)) {
            super.getResources(name)
        } else EXT_CLASS_LOADER!!.getResources(name)
    }

    override fun toString(): String {
        return FilteringClassLoader::class.java.getSimpleName() + "(" + parent + ")"
    }

    private fun allowed(resourceName: String): Boolean {
        return (resourceNames.contains(resourceName)
                || resourcePrefixes.find(resourceName))
    }

    private fun allowed(pkg: Package): Boolean {
        val packageName = pkg.name
        return if (disallowedPackagePrefixes.find(packageName)) {
            false
        } else (SYSTEM_PACKAGES.contains(packageName)
                || packageNames.contains(packageName)
                || packagePrefixes.find(packageName))
    }

    private fun classAllowed(className: String): Boolean {
        if (disallowedClassNames.contains(className)) {
            return false
        }
        if (classNames.contains(className)) {
            return true
        }
        return if (disallowedPackagePrefixes.find(className)) {
            false
        } else packagePrefixes.find(className) || packagePrefixes.contains("$DEFAULT_PACKAGE.") && isInDefaultPackage(
            className
        )
    }

    private fun isInDefaultPackage(className: String): Boolean {
        return !className.contains(".")
    }

    class Spec {
        val packageNames: MutableSet<String> = HashSet()
        val packagePrefixes: MutableSet<String> = HashSet()
        val resourcePrefixes: MutableSet<String> = HashSet()
        val resourceNames: MutableSet<String> = HashSet()
        val classNames: MutableSet<String> = HashSet()
        val disallowedClassNames: MutableSet<String> = HashSet()
        val disallowedPackagePrefixes: MutableSet<String> = HashSet()

        constructor()
        constructor(spec: Spec) : this(
            spec.classNames,
            spec.packageNames,
            spec.packagePrefixes,
            spec.resourcePrefixes,
            spec.resourceNames,
            spec.disallowedClassNames,
            spec.disallowedPackagePrefixes
        )

        constructor(
            classNames: Iterable<String>,
            packageNames: Iterable<String>,
            packagePrefixes: Iterable<String>,
            resourcePrefixes: Iterable<String>,
            resourceNames: Iterable<String>,
            disallowedClassNames: Iterable<String>,
            disallowedPackagePrefixes: Iterable<String>
        ) {
            addAll(this.classNames, classNames)
            addAll(this.packageNames, packageNames)
            addAll(this.packagePrefixes, packagePrefixes)
            addAll(this.resourcePrefixes, resourcePrefixes)
            addAll(this.resourceNames, resourceNames)
            addAll(this.disallowedClassNames, disallowedClassNames)
            addAll(this.disallowedPackagePrefixes, disallowedPackagePrefixes)
        }

        val isEmpty: Boolean
            /**
             * Whether or not any constraints have been added to this filter.
             *
             * @return true if no constraints have been added
             */
            get() = (classNames.isEmpty()
                    && packageNames.isEmpty()
                    && packagePrefixes.isEmpty()
                    && resourcePrefixes.isEmpty()
                    && resourceNames.isEmpty()
                    && disallowedClassNames.isEmpty()
                    && disallowedPackagePrefixes.isEmpty())

        /**
         * Marks a package and all its sub-packages as visible. Also makes resources in those packages visible.
         *
         * @param packageName the package name
         */
        fun allowPackage(packageName: String) {
            packageNames.add(packageName)
            packagePrefixes.add("$packageName.")
            resourcePrefixes.add(packageName.replace('.', '/') + '/')
        }

        /**
         * Marks a single class as visible.
         *
         * @param clazz the class
         */
        fun allowClass(clazz: Class<*>) {
            classNames.add(clazz.getName())
        }

        /**
         * Marks a single class as not visible.
         *
         * @param className the class name
         */
        fun disallowClass(className: String) {
            disallowedClassNames.add(className)
        }

        /**
         * Marks a package and all its sub-packages as not visible. Does not affect resources in those packages.
         *
         * @param packagePrefix the package prefix
         */
        fun disallowPackage(packagePrefix: String) {
            disallowedPackagePrefixes.add("$packagePrefix.")
        }

        /**
         * Marks all resources with the given prefix as visible.
         *
         * @param resourcePrefix the resource prefix
         */
        fun allowResources(resourcePrefix: String) {
            resourcePrefixes.add("$resourcePrefix/")
        }

        /**
         * Marks a single resource as visible.
         *
         * @param resourceName the resource name
         */
        fun allowResource(resourceName: String) {
            resourceNames.add(resourceName)
        }

        override fun equals(obj: Any?): Boolean {
            if (obj === this) {
                return true
            }
            if (obj == null || obj.javaClass != javaClass) {
                return false
            }
            val other = obj as Spec
            return other.packageNames == packageNames && other.packagePrefixes == packagePrefixes && other.resourceNames == resourceNames && other.resourcePrefixes == resourcePrefixes && other.classNames == classNames && other.disallowedClassNames == disallowedClassNames && other.disallowedPackagePrefixes == disallowedPackagePrefixes
        }

        override fun hashCode(): Int {
            return (packageNames.hashCode()
                    xor packagePrefixes.hashCode()
                    xor resourceNames.hashCode()
                    xor resourcePrefixes.hashCode()
                    xor classNames.hashCode()
                    xor disallowedClassNames.hashCode()
                    xor disallowedPackagePrefixes.hashCode())
        }



        companion object {
            private fun addAll(collection: MutableCollection<String>, elements: Iterable<String>) {
                for (element in elements) {
                    collection.add(element)
                }
            }
        }
    }


    companion object {
        private val EXT_CLASS_LOADER: ClassLoader? = null
        private val SYSTEM_PACKAGES: MutableSet<String> = HashSet()
        const val DEFAULT_PACKAGE = "DEFAULT"

        init {


            SYSTEM_PACKAGES.add("io.dingyi222666.rewrite.androlua.api.plugin")
            SYSTEM_PACKAGES.add("kotlin")
            SYSTEM_PACKAGES.add("android")

            try {
                registerAsParallelCapable()
            } catch (ignore: NoSuchMethodError) {
                // Not supported on Java 6
            }
        }
    }


    private class TrieSet(words: Collection<String>) :
        Iterable<String> {
        private val trie: Trie
        private val set: Set<String>

        init {
            trie = Trie.from(words)
            set = HashSet(words)
        }

        fun find(seq: CharSequence): Boolean {
            return trie.find(seq)
        }

        operator fun contains(seq: String): Boolean {
            return set.contains(seq)
        }

        @Nonnull
        override fun iterator(): Iterator<String> {
            return set.iterator()
        }
    }

}