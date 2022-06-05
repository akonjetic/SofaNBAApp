package konjetic.sofanbaapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayerFavoriteImg")
data class PlayerFavoriteImg(
    @PrimaryKey
    var playerId: Long,
    var imgUrl: String,
    var imgCaption: String
) {
    constructor() : this(0, "", "")
}
