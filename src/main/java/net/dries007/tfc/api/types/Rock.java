/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.types;

import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.blocks.stone.*;
import net.dries007.tfc.objects.items.rock.*;

import static net.dries007.tfc.api.types.Rock.FallingBlockType.*;
import static net.dries007.tfc.api.util.TFCConstants.MOD_ID;

/**
 * todo: document API
 */
public class Rock extends IForgeRegistryEntry.Impl<Rock>
{
    private final RockCategory rockCategory;
    private final ResourceLocation textureLocation;

    public Rock(@Nonnull ResourceLocation name, @Nonnull RockCategory rockCategory)
    {
        //noinspection ConstantConditions
        if (rockCategory == null)
            throw new IllegalArgumentException("Rock category is not allowed to be null (on rock " + name + ")");

        setRegistryName(name);
        this.rockCategory = rockCategory;
        this.textureLocation = new ResourceLocation(MOD_ID, "textures/blocks/stonetypes/raw/" + name.getPath() + ".png");
    }

    public Rock(@Nonnull ResourceLocation name, @Nonnull ResourceLocation categoryName)
    {
        //noinspection ConstantConditions
        this(name, TFCRegistries.ROCK_CATEGORIES.getValue(categoryName));
    }

    public ResourceLocation getTexture()
    {
        return textureLocation;
    }

    public RockCategory getRockCategory()
    {
        return rockCategory;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString()
    {
        return getRegistryName().getPath();
    }

    public enum ToolType
    {
        AXE(ItemRockAxe::new, " X   ", "XXXX ", "XXXXX", "XXXX ", " X   "),
        SHOVEL(ItemRockShovel::new, "XXX", "XXX", "XXX", "XXX", " X "),
        HOE(ItemRockHoe::new, "XXXXX", "   XX"),
        KNIFE(ItemRockKnife::new, "X ", "XX", "XX", "XX", "XX"),
        JAVELIN(ItemRockJavelin::new, "XXX  ", "XXXX ", "XXXXX", " XXX ", "  X  "),
        HAMMER(ItemRockHammer::new, "XXXXX", "XXXXX", "  X  ");

        private final Function<RockCategory, Item> supplier;
        private final String[] pattern;

        ToolType(@Nonnull Function<RockCategory, Item> supplier, String... pattern)
        {
            this.supplier = supplier;
            this.pattern = pattern;
        }

        public Item create(RockCategory category)
        {
            return supplier.apply(category);
        }

        public String[] getPattern()
        {
            return pattern;
        }
    }

    public enum Type
    {
        RAW(Material.ROCK, NO_FALL, false, BlockRockRaw::new), // Todo: add collapsing when broken
        SMOOTH(Material.ROCK, NO_FALL, false),
        COBBLE(Material.ROCK, FALL_HORIZONTAL, false),
        BRICKS(Material.ROCK, NO_FALL, false),
        SAND(Material.SAND, FALL_HORIZONTAL, false),
        GRAVEL(Material.SAND, FALL_HORIZONTAL, false),
        DIRT(Material.GROUND, FALL_HORIZONTAL, false),
        GRASS(Material.GRASS, FALL_HORIZONTAL, true),
        DRY_GRASS(Material.GRASS, FALL_HORIZONTAL, true),
        CLAY(Material.GRASS, FALL_VERTICAL, false),
        CLAY_GRASS(Material.GRASS, FALL_VERTICAL, true),
        FARMLAND(Material.GROUND, FALL_VERTICAL, false, BlockFarmlandTFC::new),
        PATH(Material.GROUND, FALL_VERTICAL, false, BlockPathTFC::new);

        public final Material material;
        public final boolean isGrass;

        private final FallingBlockType gravType;
        private final BiFunction<Type, Rock, BlockRockVariant> supplier;

        Type(Material material, FallingBlockType gravType, boolean isGrass)
        {
            // If no fall + no grass, then normal. If it can fall, then eiether fallable or fallable + connected (since grass always falls)
            this(material, gravType, isGrass, (gravType == NO_FALL && !isGrass) ? BlockRockVariant::new :
                (isGrass ? BlockRockVariantConnected::new : BlockRockVariantFallable::new));
        }

        Type(Material material, FallingBlockType gravType, boolean isGrass, BiFunction<Type, Rock, BlockRockVariant> supplier)
        {
            this.material = material;
            this.gravType = gravType;
            this.isGrass = isGrass;
            this.supplier = supplier;
        }

        public boolean canFall()
        {
            return gravType != NO_FALL;
        }

        public boolean canFallHorizontal()
        {
            return gravType == FALL_HORIZONTAL;
        }

        public BlockRockVariant create(Rock rock)
        {
            return supplier.apply(this, rock);
        }

        public Type getNonGrassVersion()
        {
            if (!isGrass) return this;
            switch (this)
            {
                case GRASS:
                    return DIRT;
                case DRY_GRASS:
                    return DIRT;
                case CLAY_GRASS:
                    return CLAY;
            }
            throw new IllegalStateException("Someone forgot to add enum constants to this switch case...");
        }

        public Type getGrassVersion(Type spreader)
        {
            if (!spreader.isGrass) throw new IllegalArgumentException("Non-grass can't spread.");
            switch (this)
            {
                case DIRT:
                    return spreader == DRY_GRASS ? DRY_GRASS : GRASS;
                case CLAY:
                    return CLAY_GRASS;
            }
            throw new IllegalArgumentException("You cannot get grass from rock types.");
        }
    }

    public enum FallingBlockType
    {
        NO_FALL,
        FALL_VERTICAL,
        FALL_HORIZONTAL
    }
}
