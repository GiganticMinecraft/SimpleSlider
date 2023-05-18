package click.seichi.simpleslider.command

import click.seichi.simpleslider.data.OriginalHoe.getOriginalHoe
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HoeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}このコマンドはゲーム内から実行してください。")
            return true
        }

        val itemStack = getOriginalHoe()
        val inventory = sender.inventory

        if (inventory.firstEmpty() == -1) sender.world.dropItemNaturally(sender.location, itemStack)
        else inventory.addItem(itemStack)

        return true
    }
}