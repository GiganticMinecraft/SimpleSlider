package click.seichi.simpleslider.util

import org.bukkit.Bukkit

object Logger {
    private val logger = Bukkit.getServer().logger
    fun info(msg: String) = logger.info(msg)
}