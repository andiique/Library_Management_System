// Import the necessary libraries
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

// Read the contents of the CSV file
@JvmName ("readCSVMrg")
fun readCsvFileMrg(fileName: String): MutableList<String> {
    val rows = mutableListOf<String>()
    var isHeader = true
    try {
        File(fileName).forEachLine {
            if (isHeader) {
                isHeader = false
            } else {
                rows.add(it)
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return rows
}

// Merge sort algorithm
@JvmName ("mergeSort")
fun mergeSort(array: MutableList<String>): MutableList<String> {
    if (array.size <= 1) return array
    val middle = array.size / 2
    val left = array.subList(0, middle)
    val right = array.subList(middle, array.size)
    return merge(mergeSort(left), mergeSort(right))
}

fun merge(left: MutableList<String>, right: MutableList<String>): MutableList<String> {
    val result = mutableListOf<String>()
    var leftIndex = 0
    var rightIndex = 0
    while (leftIndex < left.size && rightIndex < right.size) {
        if (left[leftIndex] < right[rightIndex]) {
            result.add(left[leftIndex])
            leftIndex++
        } else {
            result.add(right[rightIndex])
            rightIndex++
        }
    }
    while (leftIndex < left.size) {
        result.add(left[leftIndex])
        leftIndex++
    }
    while (rightIndex < right.size) {
        result.add(right[rightIndex])
        rightIndex++
    }
    return result
}

// Main function
fun main() {
    val fileName = "src/mydata.csv"
    val csvData = readCsvFileMrg(fileName)

    val start = Instant.now()

    val sortedData = mergeSort(csvData)
    val end = Instant.now()
    val duration = Duration.between(start, end)
    println("Duration of merge sort: $duration s" )


    val newFileName = "src/mgsorted.csv"
    val fileWriter = FileWriter(newFileName)
    for (data in sortedData)
    {
        fileWriter.write(data + "\n")
    }
    fileWriter.close()
    //println(sortedData)
}
