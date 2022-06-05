package konjetic.sofanbaapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import konjetic.sofanbaapp.network.model.TeamResponseData
import java.io.Serializable

@Entity(tableName = "Teams")
data class TeamInfo(
    @PrimaryKey
    @ColumnInfo(name = "teamId")
    var id: Long,
    var abbreviation: String,
    var city: String,
    var conference: String,
    var division: String,
    var full_name: String,
    var name: String
) : Serializable {
    constructor() : this(0, "", "", "", "", "", "")

    fun toTeamResponseData(): TeamResponseData {
        return TeamResponseData(
            id, abbreviation, city, conference, division, full_name, name
        )
    }
}
