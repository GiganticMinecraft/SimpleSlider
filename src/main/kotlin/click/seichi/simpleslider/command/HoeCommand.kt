package click.seichi.simpleslider.command

import click.seichi.simpleslider.data.OriginalHoe.getOriginalHoe
import click.seichi.simpleslider.util.Logger
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HoeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Logger.info("${ChatColor.RED}このコマンドはゲーム内から実行してください。")
            return true
        }

        val itemStack = getOriginalHoe()
        val inventory = sender.inventory ?: return true

        if (inventory.firstEmpty() == -1) sender.world.dropItemNaturally(sender.location, itemStack)
        else inventory.addItem(itemStack)

        return true
    }
}