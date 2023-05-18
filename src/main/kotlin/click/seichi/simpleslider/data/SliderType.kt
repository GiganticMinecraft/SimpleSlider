package click.seichi.simpleslider.data

import click.seichi.simpleslider.SimpleSlider
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

/**
 * Sliderの種類をまとめたEnum
 * @param plate 感圧板の種類（基準となるブロック）
 * @param foundation [plate]の1つY座標を下にした座標のブロック
 */
enum class SliderType(val plate: Material, val foundation: Material) {
    IRON(Material.IRON_CHESTPLATE, Material.IRON_BLOCK) {
        override fun giveEffectToPlayer(player: Player) {/* Nothing to do */ }
    },
    GOLD(Material.GOLDEN_CHESTPLATE, Material.GOLD_BLOCK) {
        override fun giveEffectToPlayer(player: Player) {/* Nothing to do */ }
    },
    EMERALD(Material.IRON_CHESTPLATE, Material.EMERALD_BLOCK) {
        override fun giveEffectToPlayer(player: Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 60, 0))
        }
    },
    DIAMOND(Material.IRON_CHESTPLATE, Material.DIAMOND_BLOCK) {
        override fun giveEffectToPlayer(player: Player) {
            // LEVITATIONは負の値のレベルを指定すると降下するようになる
            player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20 * 3, 0))
            object : BukkitRunnable() {
                override fun run() {
                    player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20 * 3, -2))
                }
            }.runTaskLater(SimpleSlider.INSTANCE, 20 * 3)
        }
    },
    NETHER_QUARTZ(Material.IRON_CHESTPLATE, Material.QUARTZ_BLOCK) {
        override fun giveEffectToPlayer(player: Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 60, 2))
        }
    };

    abstract fun giveEffectToPlayer(player: Player)

    // ref. https://qiita.com/urado/items/c88f54d4d9dc21eaf2b8

    companion object {
        /**
         * 指定されたBlockとY座標1マイナスしたBlockで、それぞれSliderTypeを満たしているか
         * @param block 感圧板が設置されていると想定される座標
         * @return Boolean 基本的にはSliderTypeを満たすブロック群ならtrue、そうでないならfalse。ただし、指定されたBlockからY座標を1マイナスしたBlockがnullならfalse
         */
        fun isSlider(block: Block): Boolean {
            val foundation = block.location.apply { y -= 1 }.block
            return values().any { it.plate == block.type && it.foundation == foundation.type }
        }

        /**
         * 指定されたブロックとY座標1マイナスしたBlockで構成されたSliderのTypeを取得する
         * @param block 感圧板が設置されていると想定される座標
         * @return 当てはまるSliderTypeがあれば[SliderType]、ないか、1Y座標下のブロックが取得できなければnull
         */
        fun getSliderType(block: Block): SliderType? {
            val foundation = block.location.apply { y -= 1 }.block
            return values().find { it.plate == block.type && it.foundation == foundation.type }
        }
    }
}