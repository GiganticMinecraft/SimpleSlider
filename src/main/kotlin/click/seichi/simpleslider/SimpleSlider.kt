package click.seichi.simpleslider

import click.seichi.simpleslider.command.HoeCommand
import click.seichi.simpleslider.listener.PlayerClickListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SimpleSlider : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: SimpleSlider
            private set
    }

    override fun onEnable() {
        INSTANCE = this
        ConfigHandler.loadConfig()

        Bukkit.getPluginManager().registerEvents(PlayerClickListener(), this)
        getCommand("hoe")?.setExecutor(HoeCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}