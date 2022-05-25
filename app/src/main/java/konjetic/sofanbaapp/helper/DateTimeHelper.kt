package konjetic.sofanbaapp.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateTimeHelper {

    fun getDateLongFormat(dateString : String) : String{
        val dateWithDay = LocalDate.parse(
            dateString.subSequence(0, 10),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        return dateWithDay.format(DateTimeFormatter.ofPattern("EE, dd MMM yyyy"))


    }


}