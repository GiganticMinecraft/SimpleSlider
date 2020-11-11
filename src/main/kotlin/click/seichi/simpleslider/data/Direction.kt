package click.seichi.simpleslider.data

import org.bukkit.entity.Player

enum class Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH;

    companion object {
        /**
         * [player]の見ている方向を返す
         *
         * @return [Direction]、判定できなければnull
         * @see <a href="http://bukkit.org/threads/solved-player-direction.72789/">Bukkit Forum: Player Direction</a?
         */
        fun getCardinalDirection(player: Player): Direction {
            val rotation = (player.location.yaw - 90) % 360.toDouble().also { if (it < 0) it.plus(360.0) }

            return when {
                0 <= rotation && rotation < 45.0 -> NORTH
                45.0 <= rotation && rotation < 135.0 -> EAST
                135.0 <= rotation && rotation < 225.0 -> SOUTH
                225.0 <= rotation && rotation < 315.0 -> WEST
                315.0 <= rotation && rotation < 360.0 -> NORTH
                else -> throw IllegalStateException("Failed to handle the player(${player.name})'s yaw.")
            }
        }
    }
}