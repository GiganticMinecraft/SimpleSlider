package click.seichi.simpleslider.listener

import click.seichi.simpleslider.ConfigHandler.maxDistanceOfSearching
import click.seichi.simpleslider.data.Direction
import click.seichi.simpleslider.data.Direction.*
import click.seichi.simpleslider.data.Direction.Companion.getCardinalDirection
import click.seichi.simpleslider.data.OriginalHoe.isOriginalHoe
import click.seichi.simpleslider.data.SliderType
import click.seichi.simpleslider.data.SliderType.Companion.getSliderType
import click.seichi.simpleslider.data.SliderType.Companion.isSlider
import click.seichi.simpleslider.util.WorldGuard.getRegions
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerClickListener : Listener {
    @EventHandler
    fun onPlayerClickWithWoodenHoe(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND ||
                event.action != Action.RIGHT_CLICK_BLOCK || event.action != Action.RIGHT_CLICK_AIR) return
        if (!event.hasItem() || !isOriginalHoe(event.item)) return

        val player = event.player ?: return
        val block = player.location.block ?: return
        val sliderType = getSliderType(block) ?: return
        val location = searchSlider(player.location, getCardinalDirection(player), sliderType) ?: return
        player.apply {
            teleport(location)
            playSound(location, Sound.BLOCK_ANVIL_FALL, 1F, 1F)
        }
        sliderType.giveEffectToPlayer(player)
    }

    private fun searchSlider(defaultLocation: Location, direction: Direction, sliderType: SliderType): Location? {
        val defaultRegions = getRegions(defaultLocation).also { if (it.size >= 2) return null }

        // globalなら ConfigHandler.maxDistanceOfSearching() だけ検索する、保護の確認をしない
        if (defaultRegions.isEmpty()) {
            for (i in 1..maxDistanceOfSearching()) {
                val nextLocation = defaultLocation.apply {
                    when (direction) {
                        NORTH -> z -= i
                        EAST -> x += i
                        WEST -> x -= i
                        SOUTH -> z += i
                    }
                }
                // ERR: その座標にあるブロックを取得できない
                val nextLocBlock = nextLocation.block ?: return null
                // SUC: スライダーであるかつSliderTypeが同じならLocationを返し、ERR: でないならループを戻る
                if (isSlider(nextLocBlock) && sliderType == getSliderType(nextLocBlock)!!) return nextLocation
            }
        }
        // 保護が1つだけあるならその保護の中、保護の確認をする
        else if (defaultRegions.size == 1) {
            val defaultRegion = defaultRegions.iterator().next()

            var i = 0
            while (true) {
                val nextLocation = defaultLocation.apply {
                    when (direction) {
                        NORTH -> z -= i
                        EAST -> x += i
                        WEST -> x -= i
                        SOUTH -> z += i
                    }
                }
                // ERR: 保護が見つからない
                val regionOfNextLoc = getRegions(nextLocation).also { if (it.size != 1) return null }.iterator().next()
                // ERR: 保護が同じではない
                if (defaultRegion.id != regionOfNextLoc.id) return null
                // ERR: その座標にあるブロックを取得できない
                val nextLocBlock = nextLocation.block ?: return null
                // SUC: スライダーであるかつSliderTypeが同じならLocationを返し、ERR: でないならカウンタをインクリメントして再びループ
                // `getSliderType(nextLocBlock)`は、`isSlider(nextLocBlock)`の後に指定してあるのでnullにはならない
                if (isSlider(nextLocBlock) && sliderType == getSliderType(nextLocBlock)!!) return nextLocation
                i++
            }
        }

        // 保護が重なっているならnull
        return null
    }
}
