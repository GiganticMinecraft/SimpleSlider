package click.seichi.simpleslider

import org.bukkit.Bukkit

object Logger {
    private val logger = Bukkit.getServer().logger
    fun info(msg: String) = logger.info(msg)
}