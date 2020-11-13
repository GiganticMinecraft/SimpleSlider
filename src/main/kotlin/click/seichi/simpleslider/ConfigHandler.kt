package click.seichi.simpleslider

import click.seichi.simpleslider.SimpleSlider.Companion.INSTANCE
import org.bukkit.configuration.file.FileConfiguration

/**
 * config.ymlを操作する上で必要な関数などをまとめたobject
 */
object ConfigHandler {
    private lateinit var config: FileConfiguration

    fun loadConfig() = run {
        INSTANCE.saveDefaultConfig()
        config = INSTANCE.config
    }

    fun maxDistanceOfSearching() = config.getInt("search-max-distance", 100)
}