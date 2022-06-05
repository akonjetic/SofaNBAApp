package konjetic.sofanbaapp.helper

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.cardview.widget.CardView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.network.model.TeamResponse
import konjetic.sofanbaapp.network.model.TeamResponseData

class TeamHelper {

    fun CardView.editLogoColor(team: TeamResponseData) {
        val teamName = team.name.lowercase().replace("\\s".toRegex(), "_")
        val colorName = resources.getString(R.string.color_name_template, teamName)
        this.setCardBackgroundColor(resources.getColor(resources.getIdentifier(colorName, "color", context.packageName)))
    }

    fun getTeamColorName(name: String, context: Context): String {
        val teamName = name.lowercase().replace("\\s".toRegex(), "_")
        return context.getString(R.string.color_name_template, teamName)
    }

    fun getTeamColorNameOpacity(name: String, context: Context): String {
        val teamName = name.lowercase().replace("\\s".toRegex(), "_")
        return context.getString(R.string.color_name_50pct, teamName)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun ImageView.editLogoImg(team: TeamResponseData) {

        val conference = if (team.conference == "East") {
            "eastern"
        } else {
            "west"
        }

        val name = team.full_name.lowercase().replace("\\s".toRegex(), "_")
        val url = resources.getString(R.string.photo_name_team_template, conference, name)
        this.load(resources.getDrawable(resources.getIdentifier(url, "drawable", context.packageName)))
    }

    fun getTeamResponseData(teamId: Long, teams: TeamResponse): TeamResponseData {
        var teamRD = TeamResponseData(0, "", "", "", "", "", "")

        for (item in teams.data) {
            if (item.id == teamId) {
                teamRD = item
                break
            }
        }

        return teamRD
    }

    fun TeamData.mapToResponse(): TeamResponseData {
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

    fun TeamInfo.mapToResponse(): TeamResponseData {
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

    fun mapToTeamInfo(team: TeamResponseData): TeamInfo {
        return TeamInfo(team.id, team.abbreviation, team.city, team.conference, team.division, team.full_name, team.name)
    }
}
