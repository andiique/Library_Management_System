// First import the necessary libraries
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.time.Duration
import java.time.Instant

// Then define a function to read the contents of the CSV file
@JvmName ("readCSV")
public fun readCsvFile(fileName: String): MutableList<String> {
    val rows = mutableListOf<String>()
    try {
        File(fileName).forEachLine {
            rows.add(it)
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return rows
}

// Bubble sorting algorithm
@JvmName ("bubbleSort")
public fun bubbleSort(array: MutableList<String>): MutableList<String> {
    for (i in 1 until array.size) {
        for (j in 1 until array.size - 1) {
            if (array[j] > array[j + 1]) {
                val temp = array[j]
                array[j] = array[j + 1]
                array[j + 1] = temp
            }
        }
    }
    return array
}

// Main function
fun main() {
    val fileName = "src/mydata.csv"
    val csvData = readCsvFile(fileName)

    //Starting the timer
    val startTime = Instant.now()

    val sortedData = bubbleSort(csvData)

    val endTime = Instant.now()
    val duration = Duration.between(startTime, endTime)
    println("Duration of bubble sort: $duration s" )

    val newFileName = "src/bblsorted.csv"
    val fileWriter = FileWriter(newFileName)
    for (data in sortedData)
    {
        fileWriter.write(data + "\n")
    }
    fileWriter.close()
    //println(sortedData)
}
