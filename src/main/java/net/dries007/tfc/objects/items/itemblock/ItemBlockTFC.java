/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.objects.items.itemblock;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

public class ItemBlockTFC extends ItemBlock implements IItemSize
{
    private final Size size;
    private final Weight weight;

    public ItemBlockTFC(Block block)
    {
        super(block);

        if (block instanceof IItemSize)
        {
            size = ((IItemSize) block).getSize(new ItemStack(block));
            weight = ((IItemSize) block).getWeight(new ItemStack(block));
        }
        else
        {
            size = Size.VERY_SMALL;
            weight = Weight.HEAVY;
        }
    }

    @Override
    public Size getSize(@Nonnull ItemStack stack)
    {
        return size;
    }

    @Override
    public Weight getWeight(@Nonnull ItemStack stack)
    {
        return weight;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return getStackSize(stack);
    }
}
