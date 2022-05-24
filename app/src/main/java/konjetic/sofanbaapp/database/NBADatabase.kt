package konjetic.sofanbaapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.database.entity.TeamData

@Database(entities = [PlayerData::class, PlayerFavoriteImg::class, TeamData::class], version = 4, exportSchema = false)
abstract class NBADatabase : RoomDatabase() {

    abstract fun getNBADao(): NBADao

    companion object{
        private var instance: NBADatabase? = null

        fun getDatabase(context: Context) : NBADatabase?{
            if(instance == null){
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                NBADatabase::class.java,
                "NBADatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}