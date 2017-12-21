package mcjty.immcraft.network;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.network.NetworkTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientsInfoPacketServer implements InfoPacketServer {

    private BlockPos pos;

    public IngredientsInfoPacketServer() {
    }

    public IngredientsInfoPacketServer(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = NetworkTools.readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, pos);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        List<String> ingredients = new ArrayList<>();
        List<String> missingIngredients = new ArrayList<>();
        BlockTools.getTE(null, player.getEntityWorld(), pos)
                .ifPresent(p -> p.calculateIngredients(ingredients, missingIngredients, player));
        return Optional.of(new IngredientsInfoPacketClient(pos, ingredients, missingIngredients));
    }
}
