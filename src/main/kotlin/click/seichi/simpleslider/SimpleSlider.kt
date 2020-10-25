package click.seichi.simpleslider

import click.seichi.simpleslider.command.HoeCommand
import org.bukkit.plugin.java.JavaPlugin

class SimpleSlider : JavaPlugin() {
    override fun onEnable() {
        getCommand("hoe").executor = HoeCommand()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}