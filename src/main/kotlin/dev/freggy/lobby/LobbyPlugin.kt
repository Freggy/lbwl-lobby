package dev.freggy.lobby

import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin


class LobbyPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getPluginManager().registerEvents(CancelListener(), this)

        this.server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        val world = Bukkit.getWorld("lobby")
        world.setGameRuleValue("doFireTick", "false")
        world.setGameRuleValue("mobGriefing", "false")
        world.setGameRuleValue("doDaylightCycle", "false")
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val spawn = Location(Bukkit.getWorld("lobby"), -169.5, 101.0, 9.5)
        spawn.yaw = -90F
        spawn.pitch = 0F

        giveItem(event.player, Material.DIAMOND,"§eFlash §r§7§o<Rechtsklick>", 3)
        giveItem(event.player, Material.MAGMA_CREAM,"§eTryJump §r§7§o<Rechtsklick>", 5)

        event.player.teleport(spawn)
        event.joinMessage = null
    }

    fun giveItem(player: Player, material: Material, name: String, slot: Int) {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName = name
        item.itemMeta = meta

        player.inventory.setItem(slot, item)
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
    }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.player.itemInHand.type == Material.DIAMOND) {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("ConnectOther")
            out.writeUTF(event.player.name)
            out.writeUTF("flash01")

            event.player.sendPluginMessage(this, "BungeeCord", out.toByteArray())
            return
        }

        if (event.player.itemInHand.type == Material.MAGMA_CREAM) {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("ConnectOther")
            out.writeUTF(event.player.name)
            out.writeUTF("tryjump01")

            event.player.sendPluginMessage(this, "BungeeCord", out.toByteArray())
            return
        }

    }
}
