package de.xQuixi.DesasterGames.Lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Hologram {
private static final double distance = 0.23;
private List<String> lines = new ArrayList<String>();
private List<Integer> ids = new ArrayList<Integer>();
private boolean showing = false;
public Hologram(String... lines) {
this.lines.addAll(Arrays.asList(lines));
}
 
public void show(Player p, Location loc) {
if (showing == true) {
try {
throw new Exception("Is already showing!");
} catch (Exception e) {
e.printStackTrace();
}
}
Location first = loc.clone().add(0, (this.lines.size() / 2) * distance, 0);
for (int i = 0; i < this.lines.size(); i++) {
ids.addAll(showLine(p, first.clone(), this.lines.get(i)));
first.subtract(0, distance, 0);
}
showing = true;
}
 
@SuppressWarnings("deprecation")
public void destroy() {
if (showing == false) {
try {
throw new Exception("Isn't showing!");
} catch (Exception e) {
e.printStackTrace();
}
}
int[] ints = new int[this.ids.size()];
for (int j = 0; j < ints.length; j++) {
ints[j] = ids.get(j);
}
PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
for (Player player : Bukkit.getOnlinePlayers()) {
((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
}
showing = false;
}
 
private static List<Integer> showLine(Player p, Location loc, String text) {
WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
EntityWitherSkull skull = new EntityWitherSkull(world);
skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
((CraftWorld) loc.getWorld()).getHandle().addEntity(skull);
 
EntityHorse horse = new EntityHorse(world);
horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
horse.setAge(-1700000);
horse.setCustomName(text);
horse.setCustomNameVisible(true);
PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
nmsPlayer.playerConnection.sendPacket(packedt);
 
PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
nmsPlayer.playerConnection.sendPacket(pa);
return Arrays.asList(skull.getId(), horse.getId());
}
}