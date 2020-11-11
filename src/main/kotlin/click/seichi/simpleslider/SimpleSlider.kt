package click.seichi.simpleslider

import click.seichi.simpleslider.command.HoeCommand
import org.bukkit.plugin.java.JavaPlugin

class SimpleSlider : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: SimpleSlider
            private set
    }

    override fun onEnable() {
        INSTANCE = this

        getCommand("hoe").executor = HoeCommand()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}