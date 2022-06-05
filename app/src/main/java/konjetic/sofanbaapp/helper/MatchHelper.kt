package konjetic.sofanbaapp.helper

import konjetic.sofanbaapp.network.model.StatsResponse
import konjetic.sofanbaapp.network.model.StatsResponseData

class MatchHelper {

    fun getTeamStatsSeparated(stats: StatsResponse): Pair<ArrayList<StatsResponseData>, ArrayList<StatsResponseData>> {
        val home: ArrayList<StatsResponseData> = arrayListOf()
        val visitor: ArrayList<StatsResponseData> = arrayListOf()

        for (item in stats.data) {
            if (homeOrVisitorPlayerStats(item)) {
                home.add(item)
            } else {
                visitor.add(item)
            }
        }

        return Pair(home, visitor)
    }

    fun getWinner(home: Int, visitor: Int): Boolean {
        return home > visitor
    }

    private fun homeOrVisitorPlayerStats(stats: StatsResponseData): Boolean {
        return stats.team.id == stats.game.home_team_id
    }

    fun getPercentFg(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var total = 0.0
        var homePct = 0.0
        var visitorPct = 0.0

        for (item in home) {
            total += item.fg_pct
            homePct += item.fg_pct
        }
        for (item in visitor) {
            total += item.fg_pct
            visitorPct += item.fg_pct
        }

        homePct = (homePct / total) * 100

        return Pair(homePct.toInt(), 100 - (homePct.toInt()))
    }

    fun getPercentFg3(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var total = 0.0
        var homePct = 0.0
        var visitorPct = 0.0

        for (item in home) {
            total += item.fg3_pct
            homePct += item.fg3_pct
        }
        for (item in visitor) {
            total += item.fg3_pct
            visitorPct += item.fg3_pct
        }

        homePct = (homePct / total) * 100

        return Pair(homePct.toInt(), 100 - (homePct.toInt()))
    }

    fun getReb(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var homeTotal = 0
        var visitorTotal = 0

        for (item in home) {
            homeTotal += item.reb
        }
        for (item in visitor) {
            visitorTotal += item.reb
        }

        return Pair(homeTotal, visitorTotal)
    }

    fun getAst(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var homeTotal = 0
        var visitorTotal = 0

        for (item in home) {
            homeTotal += item.ast
        }
        for (item in visitor) {
            visitorTotal += item.ast
        }

        return Pair(homeTotal, visitorTotal)
    }

    fun getTov(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var homeTotal = 0
        var visitorTotal = 0

        for (item in home) {
            homeTotal += item.turnover
        }
        for (item in visitor) {
            visitorTotal += item.turnover
        }

        return Pair(homeTotal, visitorTotal)
    }

    fun getOreb(home: ArrayList<StatsResponseData>, visitor: ArrayList<StatsResponseData>): Pair<Int, Int> {
        var homeTotal = 0
        var visitorTotal = 0

        for (item in home) {
            homeTotal += item.oreb
        }
        for (item in visitor) {
            visitorTotal += item.oreb
        }

        return Pair(homeTotal, visitorTotal)
    }
}
