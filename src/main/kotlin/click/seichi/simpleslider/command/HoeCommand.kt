package click.seichi.simpleslider.command

import click.seichi.simpleslider.Logger
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HoeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Logger.info("${ChatColor.RED}このコマンドはゲーム内から実行してください。")
            return true
        }

        val itemStack = ItemStack(Material.WOOD_HOE, 1)
        val inventory = sender.inventory ?: return true
        if (inventory.firstEmpty() == -1) {
            sender.world.dropItemNaturally(sender.location, itemStack)
        } else {
            inventory.addItem(itemStack)
        }

        return true
    }
}