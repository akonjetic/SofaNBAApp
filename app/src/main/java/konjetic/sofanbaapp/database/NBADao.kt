package konjetic.sofanbaapp.database

import androidx.room.*
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.database.entity.TeamData

@Dao
interface NBADao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlayer(favoritePlayer : PlayerData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTeam(favoriteTeamData: TeamData)

    @Delete
    suspend fun deleteFavoritePlayer(favoritePlayer: PlayerData)

    @Delete
    suspend fun deleteFavoriteTeam(favoriteTeam: TeamData)



    @Query("SELECT * FROM FavoritePlayers ORDER BY position ASC")
    suspend fun getAllFavoritePlayersSorted(): List<PlayerData>

    @Query("SELECT * FROM PlayerFavoriteImg WHERE playerId = :playerId")
    suspend fun getPlayerFavoriteImg(playerId : Long) : PlayerFavoriteImg

    @Query("SELECT * FROM FavoriteTeams ORDER BY position ASC")
    suspend fun getAllFavoriteTeamsSorted(): List<TeamData>
}