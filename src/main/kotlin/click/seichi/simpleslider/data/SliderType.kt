package click.seichi.simpleslider.data

import click.seichi.simpleslider.SimpleSlider
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

enum class SliderType(val plate: Material, val foundation: Material) {
    IRON(Material.IRON_PLATE, Material.IRON_BLOCK) {
        override fun addEffect(player: Player) {/* Nothing to do */}
    },
    GOLD(Material.GOLD_PLATE, Material.GOLD_BLOCK) {
        override fun addEffect(player: Player) {/* Nothing to do */}
    },
    EMERALD(Material.IRON_PLATE, Material.EMERALD_BLOCK) {
        override fun addEffect(player: Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 60, 0), false)
        }
    },
    DIAMOND(Material.IRON_PLATE, Material.DIAMOND_BLOCK) {
        override fun addEffect(player: Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20 * 5, 0), true)
            object : BukkitRunnable() {
                override fun run() {
                    player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20 * 5, -1), true)
                }
            }.runTaskLater(SimpleSlider.INSTANCE, 20 * 5L)
        }
    },
    NETHER_QUARTZ(Material.IRON_PLATE, Material.QUARTZ_BLOCK) {
        override fun addEffect(player: Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 60, 0), false)
        }
    };

    abstract fun addEffect(player: Player)

    // ref. https://qiita.com/urado/items/c88f54d4d9dc21eaf2b8

    companion object {
        fun isSlider(plate: Material, foundation: Material)
                = values().any { it.plate == plate && it.foundation == foundation }

        fun isSlider(block: Block): Boolean {
            val foundation = block.location.apply { y -= 1 }.block ?: return false
            return isSlider(block.type, foundation.type)
        }

        fun getSliderType(block: Block): SliderType? {
            val foundation = block.location.apply { y -= 1 }.block ?: return null
            return values().find { it.plate == block.type && it.foundation == foundation.type }
        }
    }
}