package konjetic.sofanbaapp.helper

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.cardview.widget.CardView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.network.model.TeamResponseData

class TeamHelper {

    fun CardView.editLogoColor(team : TeamResponseData){
        val teamName = team.name.lowercase().replace("\\s".toRegex(), "_")
        val colorName = resources.getString(R.string.color_name_template, teamName)
        this.setCardBackgroundColor(resources.getColor(resources.getIdentifier(colorName, "color", context.packageName)))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun ImageView.editLogoImg(team: TeamResponseData){

        val conference = if(team.conference == "East"){
            "eastern"
        } else{
            "west"
        }

        val name = team.full_name.lowercase().replace("\\s".toRegex(), "_")
        val url = resources.getString(R.string.photo_name_team_template, conference, name)
        this.load(resources.getDrawable(resources.getIdentifier(url, "drawable", context.packageName)))
    }

    fun TeamData.mapToResponse() : TeamResponseData{
        return TeamResponseData(
            this.id,
            this.abbreviation,
            this.city,
            this.conference,
            this.division,
            this.full_name,
            this.name
        )
    }
}