package me.trumpetplayer2.Pyroshot.SoftDependencies;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Debug.Debug;

public class ProtocolLibHandler {
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PyroshotMain main;
    public ProtocolLibHandler() {
        main = PyroshotMain.getInstance();
        manager = ProtocolLibrary.getProtocolManager();
//        manager.addPacketListener(new PacketAdapter(main, PacketType.Play.Server.ENTITY_METADATA) {
//            @Override
//            public void onPacketSending(PacketEvent e) {
//                //Grab player, and check if using a NMS based kit (Glow) if not, we dont need to do anything, so return.
//                Player p = e.getPlayer();
//                if(!main.getPlayerStats(p).getKit().equals(Kit.GLOW)) {return;};
//                if(!main.game.isActive) {return;}
//                Debug.TellConsole(p.getName());
//                //Grab the packet to work with
//                PacketContainer packet = e.getPacket();
//                //Grab what mob is being messed with by ID
//                int mob = packet.getIntegers().read(0);
//                //Check if the entity is a player
//                Entity entity = manager.getEntityFromID(p.getWorld(), mob);
//                if(!entity.getType().equals(EntityType.PLAYER)) {return;}
//                //The entity is a player, lets modify the packet so the player glows
//                try {
//                    StructureModifier<List<WrappedDataValue>> watchableAccessor = packet.getDataValueCollectionModifier(); 
//                    List<WrappedDataValue> values = Lists.newArrayList( 
//                            new WrappedDataValue(0, Registry.get(Byte.class), (byte) 64), 
//                            new WrappedDataValue(1, Registry.get(String.class), "World")); 
//                    watchableAccessor.write(0, values); 
//                }
//                catch(Exception ex) {
//                    ex.printStackTrace();
//                }
//                
////                if(packet.getWatchableCollectionModifier().size() > 0) {
////                    if(packet.getWatchableCollectionModifier().readSafely(0) != null)
////                for (WrappedWatchableObject metadata : packet.getWatchableCollectionModifier().read(0)) {
////                    if (metadata.getIndex() == 0) {
////                        
////                    }
////                    Debug.TellConsole(metadata.getValue().toString());
////                }
////                }
//            }
//        });
    }
    
    public void getPacketInfo(PacketContainer packet) {
        Debug.TellConsole("Ints: " + packet.getIntegers().size() +
                " Bytes: " + packet.getBytes().size() +
                " Byte Arrays: " + packet.getByteArrays().size() +
                " Doubles: " + packet.getDoubles().size() +
                " Floats: " + packet.getFloat().size() +
                " Shorts: " + packet.getShorts().size() +
                " Longs: " + packet.getLongs().size() +
                " Bools: " + packet.getBooleans().size() +
                " Watchable: " + packet.getWatchableCollectionModifier().size()
                );
    }
}
