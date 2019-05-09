/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.dries007.tfc.api.util.TFCConstants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@GameRegistry.ObjectHolder(MOD_ID)
public class SoundEventRegistry
{
    /**
     * Register SoundEvents
     */
    @GameRegistry.ObjectHolder("quern.grind")
    public static final SoundEvent QUERN_GRIND = createSoundEvent("quern.grind");

    /**
     * Create a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name without the MOD_ID prefix
     * @return The SoundEvent
     */
    private static SoundEvent createSoundEvent(String soundName)
    {
        final ResourceLocation soundID = new ResourceLocation(MOD_ID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event)
        {
            event.getRegistry().registerAll(
                QUERN_GRIND
            );
        }
    }
}
