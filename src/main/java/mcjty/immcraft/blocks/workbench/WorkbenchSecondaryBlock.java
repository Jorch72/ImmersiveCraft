package mcjty.immcraft.blocks.workbench;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.rendering.HandleTESR;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorkbenchSecondaryBlock extends GenericBlockWithTE<WorkbenchSecondaryTE> {

    public WorkbenchSecondaryBlock() {
        super(Material.wood, "workbench_sec", WorkbenchSecondaryTE.class);
        setHardness(1.0f);
        setStepSound(soundTypeWood);
        setHarvestLevel("axe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(WorkbenchSecondaryTE.class, new HandleTESR<>(this));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote) {
            EnumFacing left = getLeftDirection(state);
            ((EntityPlayerMP) player).theItemInWorldManager.tryHarvestBlock(pos.offset(left));
        }
    }
}
