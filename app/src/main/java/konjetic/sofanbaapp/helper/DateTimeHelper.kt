package konjetic.sofanbaapp.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateTimeHelper {

    fun getDateLongFormat(dateString: String): String {
        val dateWithDay = parseTimeStamp(dateString)

        return dateWithDay.format(DateTimeFormatter.ofPattern("EE, dd MMM yyyy"))
    }

    fun getDayOfWeekLong(timestamp: String): String {
        val day = parseTimeStamp(timestamp)

        return day.format(DateTimeFormatter.ofPattern("EEEE"))
    }

    fun getDayMonth(timestamp: String): String {
        val day = parseTimeStamp(timestamp)

        return day.format(DateTimeFormatter.ofPattern("dd MMM"))
    }

    fun getDateMonthAndYearFromTimeStamp(timestamp: String): String {
        val date = parseTimeStamp(timestamp)

        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    }

    fun getMonthFromTimeStamp(timestamp: String): Int {
        val month = timestamp.substring(5, 7)

        return month.toInt()
    }

    private fun parseTimeStamp(timestamp: String): LocalDate {

        return LocalDate.parse(
            timestamp.subSequence(0, 10),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
    }
}
