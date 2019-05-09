/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.objects.recipes;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.items.ItemPowder;
import net.dries007.tfc.objects.items.metal.ItemOreTFC;
import net.dries007.tfc.objects.items.rock.ItemRock;

import static net.dries007.tfc.types.DefaultMetals.*;
import static net.dries007.tfc.types.DefaultRocks.*;

// todo: jsonify and oredictionary quern recipes
@SuppressWarnings("WeakerAccess")
public class QuernRecipe
{
    private static final QuernRecipe QUERN_RECIPE = new QuernRecipe();

    public static QuernRecipe instance()
    {
        return QUERN_RECIPE;
    }

    private final Map<ItemStack, ItemStack> grindingList = Maps.newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.newHashMap();

    @SuppressWarnings("ConstantConditions")
    private QuernRecipe()
    {
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(BORAX), 1), ItemPowder.get(Powder.FLUX, 6), 0.1F);
        this.addGrindingRecipe(ItemRock.get(TFCRegistries.ROCKS.getValue(CHALK), 1), ItemPowder.get(Powder.FLUX, 2), 0.1F);
        this.addGrindingRecipe(ItemRock.get(TFCRegistries.ROCKS.getValue(DOLOMITE), 1), ItemPowder.get(Powder.FLUX, 4), 0.1F);
        this.addGrindingRecipe(ItemRock.get(TFCRegistries.ROCKS.getValue(LIMESTONE), 1), ItemPowder.get(Powder.FLUX, 2), 0.1F);
        this.addGrindingRecipe(ItemRock.get(TFCRegistries.ROCKS.getValue(MARBLE), 1), ItemPowder.get(Powder.FLUX, 2), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(KAOLINITE), 1), ItemPowder.get(Powder.KAOLINITE_POWDER, 4), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(GRAPHITE), 1), ItemPowder.get(Powder.GRAPHITE_POWDER, 4), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(SULFUR), 1), ItemPowder.get(Powder.SULFUR_POWDER, 4), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(SALTPETER), 1), ItemPowder.get(Powder.SALTPETER_POWDER, 4), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(CINNABAR), 1), new ItemStack(Items.REDSTONE, 8), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(CRYOLITE), 1), new ItemStack(Items.REDSTONE, 8), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(SYLVITE), 1), ItemPowder.get(Powder.FERTILIZER, 4), 0.1F);
        this.addGrindingRecipe(new ItemStack(Items.BONE), new ItemStack(Items.DYE, 3, 15), 0.1F);
        this.addGrindingRecipeForBlock(Blocks.BONE_BLOCK, new ItemStack(Items.DYE, 9, 15), 0.1F);
        this.addGrindingRecipe(ItemRock.get(TFCRegistries.ROCKS.getValue(ROCKSALT), 1), ItemPowder.get(Powder.SALT, 4), 0.1F);
        this.addGrindingRecipe(ItemOreTFC.get(TFCRegistries.ORES.getValue(LAPIS_LAZULI), 1), new ItemStack(Items.DYE, 3, 4), 0.1F);
    }

    public void addGrindingRecipeForBlock(Block input, ItemStack output, float experience)
    {
        this.addGrinding(Item.getItemFromBlock(input), output, experience);
    }

    public void addGrinding(Item input, ItemStack output, float experience)
    {
        this.addGrindingRecipe(new ItemStack(input, 1), output, experience);
    }

    public void addGrindingRecipe(ItemStack input, ItemStack output, float experience)
    {
        if (getGrindingResult(input) != ItemStack.EMPTY)
        {
            TerraFirmaCraft.getLog().info("Ignored grinding recipe with conflicting input: {} = {}", input, output);
            return;
        }
        this.grindingList.put(input, output);
        this.experienceList.put(output, experience);
    }

    public boolean getIsValidGrindingIngredient(ItemStack stack)
    {
        for (Map.Entry<ItemStack, ItemStack> entry : this.grindingList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return true;
            }
        }

        return false;
    }

    public ItemStack getGrindingResult(ItemStack stack)
    {
        for (Map.Entry<ItemStack, ItemStack> entry : this.grindingList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    @SuppressWarnings("unused")
    public Map<ItemStack, ItemStack> getGrindingList()
    {
        return this.grindingList;
    }

    public float getGrindingExperience(ItemStack stack)
    {
        float ret = stack.getItem().getSmeltingExperience(stack);
        if (ret != -1) return ret;

        for (Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return 0.0F;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata()) && stack2.getItemDamage() == stack1.getItemDamage();
    }
}