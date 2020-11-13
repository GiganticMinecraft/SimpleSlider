package click.seichi.simpleslider.util

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location

object WorldGuard {
    private val INSTANCE =
            WorldGuardPlugin.inst() ?: throw IllegalStateException("WorldGuardPluginが見つかりませんでした。")

    fun getRegions(location: Location): Set<ProtectedRegion> =
            INSTANCE.getRegionManager(location.world).getApplicableRegions(location).regions
}