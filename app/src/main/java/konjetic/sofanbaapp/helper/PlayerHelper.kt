package konjetic.sofanbaapp.helper

import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.network.model.PlayersResponseData

class PlayerHelper {

    fun getFullName(player: PlayersResponseData): String {
        return "${player.first_name} ${player.last_name}"
    }

    fun getFullNamePD(player: PlayerData): String {
        return "${player.first_name} ${player.last_name}"
    }
}
