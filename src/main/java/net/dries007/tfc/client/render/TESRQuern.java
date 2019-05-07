/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import net.dries007.tfc.objects.te.TEQuern;

public class TESRQuern extends TileEntitySpecialRenderer<TEQuern>
{
    @Override
    public void render(TEQuern te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            ItemStack input = cap.getStackInSlot(TEQuern.SLOT_INPUT);
            ItemStack output = cap.getStackInSlot(TEQuern.SLOT_OUTPUT);
            ItemStack handstone = cap.getStackInSlot(TEQuern.SLOT_HANDSTONE);

            if (!output.isEmpty())
            {
                for (int i = 0; i < output.getCount(); i++)
                {
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                    GlStateManager.enableBlend();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                    GlStateManager.pushMatrix();
                    switch (i)
                    {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        {
                            GlStateManager.translate(x + 0.125, y + 0.625, z + 0.125 + (0.046875 * i));
                            GlStateManager.rotate(75, 1, 0, 0);
                            break;
                        }
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        {
                            GlStateManager.translate(x + 0.125 + (0.046875 * (i - 16)), y + 0.625, z + 0.875);
                            GlStateManager.rotate(90, 0, 1, 0);
                            GlStateManager.rotate(75, 1, 0, 0);
                            break;
                        }
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        {
                            GlStateManager.translate(x + 0.875, y + 0.625, z + 0.875 - (0.046875 * (i - 32)));
                            GlStateManager.rotate(180, 0, 1, 0);
                            GlStateManager.rotate(75, 1, 0, 0);
                            break;
                        }
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                        {
                            GlStateManager.translate(x + 0.875 - (0.046875 * (i - 48)), y + 0.625, z + 0.125);
                            GlStateManager.rotate(270, 0, 1, 0);
                            GlStateManager.rotate(75, 1, 0, 0);
                            break;
                        }
                        default:
                        {
                            GlStateManager.translate(x + 0.5, y + 1.0, z + 0.5);
                            GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
                        }
                    }
                    GlStateManager.scale(0.125, 0.125, 0.125);

                    IBakedModel outputModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(output, te.getWorld(), null);
                    outputModel = ForgeHooksClient.handleCameraTransforms(outputModel, ItemCameraTransforms.TransformType.FIXED, false);

                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    Minecraft.getMinecraft().getRenderItem().renderItem(output, outputModel);

                    GlStateManager.popMatrix();
                    GlStateManager.disableRescaleNormal();
                    GlStateManager.disableBlend();
                }
            }

            if (!handstone.isEmpty())
            {
                int rotationTicks = te.getRotationTimer();
                double center = (rotationTicks > 0) ? 0.497 + (te.getWorld().rand.nextDouble() * 0.006) : 0.5;

                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                GlStateManager.enableBlend();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + center, y + 0.75, z + center);

                if (rotationTicks > 0)
                {
                    GlStateManager.rotate((rotationTicks - partialTicks) * 4, 0, 1, 0);
                }

                IBakedModel handstoneModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(handstone, te.getWorld(), null);
                handstoneModel = ForgeHooksClient.handleCameraTransforms(handstoneModel, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(handstone, handstoneModel);

                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }

            if (!input.isEmpty())
            {
                double height = (handstone.isEmpty()) ? 0.75 : 0.875;
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                GlStateManager.enableBlend();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5, y + height, z + 0.5);
                GlStateManager.rotate(45, 0, 1, 0);

                IBakedModel inputModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(input, te.getWorld(), null);
                inputModel = ForgeHooksClient.handleCameraTransforms(inputModel, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(input, inputModel);

                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }
}
