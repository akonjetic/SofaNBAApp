package konjetic.sofanbaapp.database

import androidx.room.*
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.database.entity.TeamInfo

@Dao
interface NBADao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlayer(favoritePlayer: PlayerData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTeam(favoriteTeamData: TeamData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFavoritePlayers(favoritePlayers: ArrayList<PlayerData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(teamInfo: TeamInfo)

    @Delete
    suspend fun deleteFavoritePlayer(favoritePlayer: PlayerData)

    @Delete
    suspend fun deleteFavoriteTeam(favoriteTeam: TeamData)

    @Query("DELETE FROM FavoriteTeams WHERE id = :id")
    suspend fun deleteFavoriteTeamById(id: Long)

    @Query("DELETE FROM FavoritePlayers WHERE id = :id")
    suspend fun deleteFavoritePlayerById(id: Long)

    @Query("DELETE FROM FavoritePlayers")
    suspend fun deleteAllFavoritePlayers()

    @Query("DELETE FROM FavoriteTeams")
    suspend fun deleteAllFavoriteTeams()

    @Query("SELECT * FROM FavoritePlayers ORDER BY positionDB ASC")
    suspend fun getAllFavoritePlayersSorted(): List<PlayerData>

    @Query("SELECT * FROM FavoritePlayers WHERE id = :id LIMIT 0,1")
    suspend fun checkIfFavoritePlayer(id: Long): PlayerData

    @Query("SELECT * FROM PlayerFavoriteImg WHERE playerId = :playerId")
    suspend fun getPlayerFavoriteImg(playerId: Long): PlayerFavoriteImg

    @Query("SELECT * FROM FavoriteTeams ORDER BY position ASC")
    suspend fun getAllFavoriteTeamsSorted(): List<TeamData>

    @Query("SELECT * FROM FavoriteTeams WHERE id = :id LIMIT 0,1")
    suspend fun checkIfFavoriteTeam(id: Long): TeamData

    @Query("SELECT * FROM Teams")
    suspend fun getAllTeams(): List<TeamInfo>

    @Query("SELECT * FROM Teams WHERE division LIKE :division")
    suspend fun getTeamsFromDivision(division: String): List<TeamInfo>

    @Query("SELECT * FROM Teams WHERE full_name LIKE :search")
    suspend fun getTeamsByName(search: String): List<TeamInfo>
}
