package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.FuelInterfaceHandle;
import mcjty.immcraft.blocks.generic.handles.OutputInterfaceHandle;
import mcjty.immcraft.blocks.generic.handles.SmeltableInterfaceHandle;
import mcjty.immcraft.varia.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Vec3;

public class FurnaceTE extends GenericInventoryTE implements ITickable {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_TOBURN = 1;
    public static final int SLOT_OUTPUT = 2;

    public static final int FURNACE_COOKTIME = 200;

    private int burnTime = 0;
    private int cookTime = 0;

    public FurnaceTE() {
        super(3);
        addInterfaceHandle(new FuelInterfaceHandle().slot(SLOT_FUEL).side(EnumFacing.SOUTH).bounds(0, 0, 1, .5f).renderOffset(new Vec3(0, 0.23, 0)));
        addInterfaceHandle(new SmeltableInterfaceHandle().slot(SLOT_TOBURN).side(EnumFacing.SOUTH).bounds(0, .5f, .5f, 1).renderOffset(new Vec3(-.2, 0.7, 0)));
        addInterfaceHandle(new OutputInterfaceHandle().slot(SLOT_OUTPUT).side(EnumFacing.SOUTH).bounds(.5f, .5f, 1, 1).renderOffset(new Vec3(.2, 0.7, 0)));
    }

    @Override
    public void update() {
        if (burnTime > 0) {
            markDirty();
            handleMelt();
            handleBurn();
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (worldObj.isRemote) {
            // If needed send a render update.
            // @todo try to check if it is really needed
            worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        EnumFacing direction = ModBlocks.furnaceBlock.worldToBlockSpace(worldObj, getPos(), side);
        if (direction == EnumFacing.UP) {
            return new int[] { SLOT_FUEL };
        } else if (direction == EnumFacing.DOWN) {
            return new int[] { SLOT_OUTPUT };
        } else {
            return new int[] { SLOT_TOBURN };
        }
    }

    private void handleBurn() {
        burnTime--;
        if (burnTime <= 0) {
            burnTime = TileEntityFurnace.getItemBurnTime(inventoryHelper.getStackInSlot(SLOT_FUEL));
            if (burnTime > 0) {
                decrStackSize(SLOT_FUEL, 1);
            }
        }
    }

    private void handleMelt() {
        if (!inventoryHelper.hasStack(SLOT_FUEL)) {
            burnTime = 0;
            markDirtyClient();
        } else if (cookTime <= 0) {
            if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                // We need to start cooking
                cookTime = FURNACE_COOKTIME;
            }
        } else {
            cookTime--;
            if (cookTime <= 0) {
                // Finished
                if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                    ItemStack tosmelt = inventoryHelper.decrStackSize(SLOT_TOBURN, 1);
                    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(tosmelt);
                    boolean undo = false;
                    if (result != null) {
                        if (!inventoryHelper.hasStack(SLOT_OUTPUT)) {
                            inventoryHelper.setInventorySlotContents(result.getMaxStackSize(), SLOT_OUTPUT, result.copy());
                        } else if (result.isItemEqual(inventoryHelper.getStackInSlot(SLOT_OUTPUT))) {
                            if (result.stackSize + inventoryHelper.getStackInSlot(SLOT_OUTPUT).stackSize <= result.getMaxStackSize()) {
                                inventoryHelper.getStackInSlot(SLOT_OUTPUT).stackSize += result.stackSize;
                            } else {
                                undo = true;
                                cookTime = 40;   // Try again
                            }
                        }
                    } else {
                        undo = true;
                    }

                    if (undo) {
                        cookTime = 40;   // Try again
                        // Error, put back our item
                        if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                            inventoryHelper.getStackInSlot(SLOT_TOBURN).stackSize++;
                        } else {
                            inventoryHelper.setStackInSlot(SLOT_TOBURN, tosmelt);
                        }
                    }
                }
            }
        }
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3 hitVec) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.flint_and_steel) {
            burnTime = TileEntityFurnace.getItemBurnTime(inventoryHelper.getStackInSlot(SLOT_FUEL));
            if (burnTime > 0) {
                decrStackSize(SLOT_FUEL, 1);
            }
            markDirtyClient();
            player.getHeldItem().damageItem(1, player);
            return true;
        } else {
            return super.onActivate(player, worldSide, side, hitVec);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        burnTime = tagCompound.getInteger("burnTime");
        cookTime = tagCompound.getInteger("cookTime");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("burnTime", burnTime)
                .set("cookTime", cookTime);
    }
}
