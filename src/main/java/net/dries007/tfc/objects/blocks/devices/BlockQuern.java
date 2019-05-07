/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.objects.blocks.devices;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import net.dries007.tfc.CommonEventHandler;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.client.TFCGuiHandler;
import net.dries007.tfc.objects.te.TEQuern;
import net.dries007.tfc.util.Helpers;

@ParametersAreNonnullByDefault
public class BlockQuern extends Block implements IItemSize
{
    private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.625D, 1D);
    private static final AxisAlignedBB QUERN_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.875D, 1D);
    private static final AxisAlignedBB HANDSTONE_AABB = new AxisAlignedBB(0.375D, 0.625D, 0.375D, 0.625D, 0.875D, 0.625D);

    public BlockQuern()
    {
        super(Material.ROCK);
        setHardness(3.0f);
        setSoundType(SoundType.STONE);
    }

    @Override
    public Size getSize(ItemStack stack)
    {
        return Size.HUGE;
    }

    @Override
    public Weight getWeight(ItemStack stack)
    {
        return Weight.HEAVY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isBlockNormalCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TEQuern teQuern = Helpers.getTE(source, pos, TEQuern.class);
        if (teQuern != null && teQuern.getHasHandstone())
        {
            return QUERN_AABB;
        }
        else
        {
            return BASE_AABB;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.DOWN)
        {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        TEQuern teQuern = Helpers.getTE(world, pos, TEQuern.class);
        if (teQuern != null && teQuern.getHasHandstone())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, HANDSTONE_AABB);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TEQuern teQuern = Helpers.getTE(world, pos, TEQuern.class);
        if (teQuern != null)
        {
            Helpers.dropInventoryItems(world, pos, teQuern.getInventory());
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);
        TEQuern teQuern = Helpers.getTE(world, pos, TEQuern.class);
        if (teQuern != null)
        {
            int rotationTimer = teQuern.getRotationTimer();
            if (rotationTimer == 0 && !playerIn.isSneaking())
            {
                if (stack.isEmpty() && facing == EnumFacing.UP && hitX > 0.2f && hitX < 0.4f && hitZ > 0.2f && hitZ < 0.4f)
                {
                    teQuern.setRotationTimer(90);
                    world.playSound(null, pos, CommonEventHandler.QUERN_GRIND, SoundCategory.BLOCKS, 1, 1);
                }
                else if (!world.isRemote)
                {
                    TFCGuiHandler.openGui(world, pos, playerIn, TFCGuiHandler.Type.QUERN);
                }
            }
            else if (!world.isRemote && playerIn.isSneaking())
            {
                ItemStackHandler inventory = teQuern.getInventory();

                if (hitX > 0.2f && hitX < 0.8f && hitZ > 0.2f && hitZ < 0.8f)
                {
                    if (hitX > 0.4f && hitX < 0.6f && hitZ > 0.4f && hitZ < 0.6f && stack.isEmpty() && !inventory.getStackInSlot(TEQuern.SLOT_INPUT).isEmpty())
                    {
                        playerIn.setHeldItem(hand, inventory.extractItem(TEQuern.SLOT_INPUT, inventory.getStackInSlot(TEQuern.SLOT_INPUT).getCount(), false));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_INPUT);
                        world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (hitX > 0.4f && hitX < 0.6f && hitZ > 0.4f && hitZ < 0.6f && !stack.isEmpty() && teQuern.isItemValid(TEQuern.SLOT_INPUT, stack))
                    {
                        playerIn.setHeldItem(hand, teQuern.insertOrSwapItem(TEQuern.SLOT_INPUT, stack));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_INPUT);
                        world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (stack.isEmpty() && !inventory.getStackInSlot(TEQuern.SLOT_HANDSTONE).isEmpty())
                    {
                        playerIn.setHeldItem(hand, inventory.extractItem(TEQuern.SLOT_HANDSTONE, inventory.getStackInSlot(TEQuern.SLOT_HANDSTONE).getCount(), false));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_HANDSTONE);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (!stack.isEmpty() && teQuern.isItemValid(TEQuern.SLOT_HANDSTONE, stack))
                    {
                        playerIn.setHeldItem(hand, teQuern.insertOrSwapItem(TEQuern.SLOT_HANDSTONE, stack));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_HANDSTONE);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);
                    }
                }
                else if ((hitX <= 0.2f || hitX >= 0.8f || hitZ <= 0.2f || hitZ >= 0.8f) && hitY >= 0.625)
                {
                    if (stack.isEmpty() && !inventory.getStackInSlot(TEQuern.SLOT_OUTPUT).isEmpty())
                    {
                        playerIn.setHeldItem(hand, inventory.extractItem(TEQuern.SLOT_OUTPUT, inventory.getStackInSlot(TEQuern.SLOT_OUTPUT).getCount(), false));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_OUTPUT);
                        world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (!stack.isEmpty() && teQuern.isItemValid(TEQuern.SLOT_OUTPUT, stack))
                    {
                        playerIn.setHeldItem(hand, teQuern.insertOrSwapItem(TEQuern.SLOT_OUTPUT, stack));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_OUTPUT);
                        world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (stack.isEmpty() && !inventory.getStackInSlot(TEQuern.SLOT_HANDSTONE).isEmpty())
                    {
                        playerIn.setHeldItem(hand, inventory.extractItem(TEQuern.SLOT_HANDSTONE, inventory.getStackInSlot(TEQuern.SLOT_HANDSTONE).getCount(), false));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_HANDSTONE);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1, 1);
                    }
                    else if (!stack.isEmpty() && teQuern.isItemValid(TEQuern.SLOT_HANDSTONE, stack))
                    {
                        playerIn.setHeldItem(hand, teQuern.insertOrSwapItem(TEQuern.SLOT_HANDSTONE, stack));
                        teQuern.setAndUpdateSlots(TEQuern.SLOT_HANDSTONE);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TEQuern();
    }
}
