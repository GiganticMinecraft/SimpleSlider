package click.seichi.simpleslider.listener

import click.seichi.simpleslider.ConfigHandler.maxDistanceOfSearching
import click.seichi.simpleslider.data.Direction
import click.seichi.simpleslider.data.Direction.*
import click.seichi.simpleslider.data.Direction.Companion.getCardinalDirection
import click.seichi.simpleslider.util.Error.*
import click.seichi.simpleslider.data.OriginalHoe.isOriginalHoe
import click.seichi.simpleslider.util.Result
import click.seichi.simpleslider.util.Result.*
import click.seichi.simpleslider.data.SliderType
import click.seichi.simpleslider.data.SliderType.Companion.getSliderType
import click.seichi.simpleslider.data.SliderType.Companion.isSlider
import click.seichi.simpleslider.util.WorldGuard.getRegions
import org.bukkit.ChatColor
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
            (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) ||
            !event.hasItem()
        ) return
        if (!event.hasItem() || !isOriginalHoe(event.item!!)) return

        val player = event.player ?: return
        val block = player.location.block ?: run {
            player.sendMessage("Failed to get block at your location.")
            return
        }
        val sliderType = getSliderType(block) ?: run {
            player.sendMessage("Where you are standing was not 'Slider'.")
            return
        }

        when (val result = searchSlider(player.location, getCardinalDirection(player), sliderType)) {
            is Ok -> {
                player.apply {
                    teleport(result.data)
                    playSound(location, Sound.BLOCK_ANVIL_FALL, 1F, 1F)
                }
                sliderType.giveEffectToPlayer(player)
                event.isCancelled = true
            }
            is Err -> {
                player.apply {
                    sendMessage("${ChatColor.RED}${result.exception.reason}")
                    playSound(player.location, Sound.BLOCK_DISPENSER_FAIL, 1F, 1F)
                }
            }
        }
    }

    private fun searchSlider(defaultLocation: Location,
                             direction: Direction,
                             sliderType: SliderType
    ): Result<Location> {
        val defaultRegions = getRegions(defaultLocation)

        when(defaultRegions.size) {
            // globalなら ConfigHandler.maxDistanceOfSearching() だけ検索する、保護の確認をしない
            0 -> for (i in 1..maxDistanceOfSearching()) {
                val nextLocation = generateNextLocation(direction, defaultLocation, i)
                // ERR: その座標にあるブロックを取得できない
                val nextLocBlock = nextLocation.block ?: return Err(BlockNotFoundException)
                // SUC: スライダーであるかつSliderTypeが同じならLocationを返し、ERR: でないならループを戻る
                if (isSlider(nextLocBlock) && sliderType == getSliderType(nextLocBlock)!!) return Ok(nextLocation)
            }
            // 保護が1つだけなら保護の境界まで検索する、保護の確認をする
            1 -> {
                val defaultRegion = defaultRegions.iterator().next()

                var i = 1
                while (true) {
                    val nextLocation = generateNextLocation(direction, defaultLocation, i)
                    // ERR: 保護が見つからない or 同じ保護で保護されていない
                    if (getRegions(nextLocation).none { it.id == defaultRegion.id }) return Err(SliderNotFoundInSameRegion)
                    // ERR: その座標にあるブロックを取得できない
                    val nextLocBlock = nextLocation.block ?: return Err(BlockNotFoundException)
                    // SUC: スライダーであるかつSliderTypeが同じなら、Locationを返す
                    // ERR: そうでないなら、カウンタをインクリメントして再びループ
                    // `getSliderType(nextLocBlock)`は、`isSlider(nextLocBlock)`の後に指定してあるのでnullにはならない
                    if (isSlider(nextLocBlock) && sliderType == getSliderType(nextLocBlock)!!) return Ok(nextLocation)
                    i++
                }
            }
            else -> return Err(RegionsDuplicatedException)
        }

        // 保護がない or 1つだけあって、Sliderが見つからない時はここにくる
        return Err(SliderNotFound)
    }
    
    private fun generateNextLocation(direction: Direction, defaultLocation: Location, num: Int) =
        defaultLocation.clone().apply {
            when (direction) {
                NORTH -> z -= num
                EAST -> x += num
                WEST -> x -= num
                SOUTH -> z += num
            }
        }
}
