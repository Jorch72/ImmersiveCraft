package mcjty.immcraft.network;


import mcjty.immcraft.multiblock.MultiblockInfoPacketClient;
import mcjty.immcraft.multiblock.MultiblockInfoPacketServer;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

public class ImmCraftPacketHandler {
    private static int ID = 12;
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    private static Map<Integer,Class<? extends InfoPacketClient>> clientInfoPackets = new HashMap<>();
    private static Map<Integer,Class<? extends InfoPacketServer>> serverInfoPackets = new HashMap<>();
    private static Map<Class<? extends InfoPacketClient>,Integer> clientInfoPacketsToId = new HashMap<>();
    private static Map<Class<? extends InfoPacketServer>,Integer> serverInfoPacketsToId = new HashMap<>();

    public static int nextPacketID() {
        return packetId++;
    }

    private static void register(Integer id, Class<? extends InfoPacketServer> serverClass, Class<? extends InfoPacketClient> clientClass) {
        serverInfoPackets.put(id, serverClass);
        clientInfoPackets.put(id, clientClass);
        serverInfoPacketsToId.put(serverClass, id);
        clientInfoPacketsToId.put(clientClass, id);
    }

    public static Class<? extends InfoPacketServer> getServerInfoPacket(int id) {
        return serverInfoPackets.get(id);
    }

    public static Integer getServerInfoPacketId(Class<? extends InfoPacketServer> clazz) {
        return serverInfoPacketsToId.get(clazz);
    }

    public static Class<? extends InfoPacketClient> getClientInfoPacket(int id) {
        return clientInfoPackets.get(id);
    }

    public static Integer getClientInfoPacketId(Class<? extends InfoPacketClient> clazz) {
        return clientInfoPacketsToId.get(clazz);
    }

    public ImmCraftPacketHandler() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages(SimpleNetworkWrapper network) {
        INSTANCE = network;
        registerMessages();
    }

    public static void registerMessages() {
        // Server side
        INSTANCE.registerMessage(PacketSendKey.Handler.class, PacketSendKey.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketPlaceItem.Handler.class, PacketPlaceItem.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketGetInfoFromServer.Handler.class, PacketGetInfoFromServer.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketHitBlock.Handler.class, PacketHitBlock.class, nextID(), Side.SERVER);

        // Client side
        INSTANCE.registerMessage(PacketReturnInfoHandler.class, PacketReturnInfoToClient.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketPageFlip.Handler.class, PacketPageFlip.class, nextID(), Side.CLIENT);

        register(nextPacketID(), IngredientsInfoPacketServer.class, IngredientsInfoPacketClient.class);
        register(nextPacketID(), MultiblockInfoPacketServer.class, MultiblockInfoPacketClient.class);
    }
}
