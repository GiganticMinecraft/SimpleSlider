package click.seichi.simpleslider.listener

import click.seichi.simpleslider.ConfigHandler.maxDistanceOfSearching
import click.seichi.simpleslider.data.Direction
import click.seichi.simpleslider.data.Direction.*
import click.seichi.simpleslider.data.Direction.Companion.getCardinalDirection
import click.seichi.simpleslider.data.SliderType.Companion.getSliderType
import click.seichi.simpleslider.data.SliderType.Companion.isSlider
import click.seichi.simpleslider.util.WorldGuard.getRegions
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerClickListener : Listener {
    @EventHandler
    fun onPlayerClickWithWoodenHoe(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND || event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player ?: return
        val block = player.location.block ?: return
        val sliderType = getSliderType(block) ?: return
        sliderType.addEffect(player)
        val location = searchSlider(player.location, getCardinalDirection(player)) ?: return
    }

    private fun searchSlider(defaultLocation: Location, direction: Direction): Location? {
        /*
        ・そのブロックの保護情報を取得
        →取得できなければ強制終了
        →できたら次へ
        ・そのブロックが最初のスライダーと同じ保護にあるか？
        →同じ保護でなければ強制終了
        →であれば次へ
        ・そのブロックはスライダーか？
        →YES = 関数の処理を終了、その座標を返す
        →NO = 座標を移動し戻る
         */

        /*
        globalなら1万ブロックだけ検索する、保護の確認をしない
        保護があるならその保護の中、保護の確認をする
         */

        val defaultRegions = getRegions(defaultLocation).also { if (it.size >= 2) return null }
        var nextLocation = defaultLocation.clone()

        if (defaultRegions.isEmpty()) {
            for (i in 1..maxDistanceOfSearching()) {
                nextLocation = nextLocation.apply {
                    when (direction) {
                        NORTH -> z -= 1
                        EAST -> x += 1
                        WEST -> x -= 1
                        SOUTH -> z += 1
                    }
                }
                // ERR: その座標にあるブロックを取得できない
                val nextLocBlock = nextLocation.block ?: return null
                // SUC: スライダーであるならLocationを返し、ERR: でないならループを戻る
                if (isSlider(nextLocBlock)) return nextLocation
            }
        }
        else if (defaultRegions.size == 1) {
            val defaultRegion = defaultRegions.iterator().next()

            while (true) {
                nextLocation = nextLocation.apply {
                    when (direction) {
                        NORTH -> z -= 1
                        EAST -> x += 1
                        WEST -> x -= 1
                        SOUTH -> z += 1
                    }
                }
                // ERR: 保護が見つからない
                val regionOfNextLoc = getRegions(nextLocation).also { if (it.size != 1) return null }.iterator().next()
                // ERR: 保護が同じではない
                if (defaultRegion.id != regionOfNextLoc.id) return null
                // ERR: その座標にあるブロックを取得できない
                val nextLocBlock = nextLocation.block ?: return null
                // SUC: スライダーであるならLocationを返し、ERR: でないならループを戻る
                if (isSlider(nextLocBlock)) return nextLocation
            }
        }

        return null
    }
}
