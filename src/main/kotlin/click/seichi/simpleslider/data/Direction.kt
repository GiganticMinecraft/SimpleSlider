package click.seichi.simpleslider.data

import org.bukkit.entity.Player

/**
 * 方角をまとめたEnum
 */
enum class Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH;

    companion object {
        /**
         * [player]の見ている方向を返す
         *
         * @return [Direction]、判定できなければ[IllegalStateException]
         * @see <a href="http://bukkit.org/threads/solved-player-direction.72789/">Bukkit Forum: Player Direction</a?
         */
        fun getCardinalDirection(player: Player): Direction {
            val rotation = run {
                val rotation = ((player.location.yaw - 180) % 360)
                if (rotation >= 0) rotation else rotation + 360
            }.toInt()

            return when(rotation) {
                in 0 until 45 -> NORTH
                in 45 until 135 -> EAST
                in 135 until 225 -> SOUTH
                in 225 until 315 -> WEST
                in 315 until 360 -> NORTH
                else -> throw IllegalStateException("Failed to calculate the player(${player.name})'s yaw.")
            }
        }
    }
}