package cgntablist;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		new BukkitRunnable() {

			@Override
			public void run() {
				PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			
				if (getConfig().get("header") == null) {
					getConfig().set("header", "&c{ &6This is the header&c }");
					saveConfig();
				}
				if (getConfig().get("footer") == null) {
					getConfig().set("footer", "&c{&6 This is the footer &c}");
					saveConfig();
				}
				Object header = new ChatComponentText("\n"+getConfig().getString("header").replaceAll("\\&", "§")+"\n");
				Object footer = new ChatComponentText("\n"+getConfig().getString("footer").replaceAll("\\&", "§")+"\n");
				
				try {
					Field b = packet.getClass().getDeclaredField("b");
					Field a = packet.getClass().getDeclaredField("a");
					b.setAccessible(true);
					a.setAccessible(true);
					a.set(packet, header);
					b.set(packet, footer);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					CraftPlayer craftplayer = ((CraftPlayer)player);
					craftplayer.getHandle().playerConnection.sendPacket(packet);
				}
			}
			
		}.runTaskTimerAsynchronously(this, 0, 10);
	}
}
