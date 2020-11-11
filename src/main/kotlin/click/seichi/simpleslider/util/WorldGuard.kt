package click.seichi.simpleslider.util

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location

object WorldGuard {
    private val INSTANCE =
            WorldGuardPlugin.inst() ?: throw IllegalStateException("WorldGuardPluginが見つかりませんでした。")

    // FIXME global保護だとnullになると思う
    fun getOneRegion(location: Location): ProtectedRegion? {
        val regions = INSTANCE.getRegionManager(location.world).getApplicableRegions(location).regions
        return if (regions.size != 1) null else regions.iterator().next()
    }
}