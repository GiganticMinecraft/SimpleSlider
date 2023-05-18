package click.seichi.simpleslider.util

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.World
import com.sk89q.worldguard.bukkit.BukkitRegionContainer
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location

/**
 * WorldGuardを利用するためのUtilをまとめたObject
 */
object WorldGuard {
    private val INSTANCE =
            WorldGuardPlugin.inst() ?: throw IllegalStateException("WorldGuardPluginが見つかりませんでした。")

    /**
     * 指定されたlocationを保護している保護領域のSetを返す
     */
    fun getRegions(location: Location): Set<ProtectedRegion> =
        BukkitRegionContainer(INSTANCE).get(BukkitWorld(location.world))
            ?.getApplicableRegions(BlockVector3.at(location.x, location.y, location.z))?.regions.orEmpty()
}