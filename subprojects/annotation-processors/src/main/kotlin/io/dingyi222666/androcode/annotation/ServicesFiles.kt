package io.dingyi222666.androcode.annotation

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


/**
 * A helper class for reading and writing Services files.
 */
internal object ServicesFiles {
    const val SERVICES_PATH = "META-INF/services"

    /**
     * Returns an absolute path to a service file given the class
     * name of the service.
     *
     * @param serviceName not `null`
     * @return SERVICES_PATH + serviceName
     */
    fun getPath(serviceName: String): String {
        return "$SERVICES_PATH/$serviceName"
    }

    /**
     * Reads the set of service classes from a service file.
     *
     * @param input not `null`. Closed after use.
     * @return a not `null Set` of service class names.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readServiceFile(input: InputStream): Set<String> {
        val serviceClasses = HashSet<String>()
        BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8)).use { reader ->
            var line: String
            while (reader.readLine().also { line = it } != null) {
                val commentStart = line.indexOf('#')
                if (commentStart >= 0) {
                    line = line.substring(0, commentStart)
                }
                line = line.trim { it <= ' ' }
                if (line.isNotEmpty()) {
                    serviceClasses.add(line)
                }
            }
            return serviceClasses
        }
    }

    /**
     * Writes the set of service class names to a service file.
     *
     * @param output not `null`. Not closed after use.
     * @param services a not `null Collection` of service class names.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeServiceFile(services: Collection<String>, output: OutputStream) {
        val writer = BufferedWriter(OutputStreamWriter(output, StandardCharsets.UTF_8))
        for (service in services) {
            writer.write(service)
            writer.newLine()
        }
        writer.flush()
    }
}