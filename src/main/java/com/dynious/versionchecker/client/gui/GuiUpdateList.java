package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.DownloadThread;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.lib.Resources;
import com.dynious.versionchecker.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiUpdateList extends GuiScroll
{
    private GuiUpdates parent;
    private int selectedIndex = -1;

    public GuiUpdateList(GuiUpdates parent, int width, int height, int top, int bottom, int left)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, 35);
        this.parent = parent;
    }

    @Override
    protected int getSize()
    {
        return UpdateHandler.getListSize();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        selectedIndex = index;
        if (doubleClick && UpdateHandler.getElement(index) != null)
        {
            parent.openInfoScreen(UpdateHandler.getElement(index));
        }
    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground()
    {
        parent.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int index, int var2, int var3, int var4, Tessellator tessellator)
    {
        Update update = UpdateHandler.getElement(index);
        if (update != null)
        {
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(update.displayName, listWidth - 10), this.left + 3, var3 + 2, 0xFFFFFF);
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(update.oldVersion + " -> " + update.newVersion, listWidth - 10), this.left + 3, var3 + 12, 0xCCCCCC);
            String info;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_ICONS);

            if (DownloadThread.isUpdating(update))
            {
                Gui.func_146110_a(this.left + listWidth - 30, var3 + 8, 0, 0, 16, 16, 48, 32);

                info = StatCollector.translateToLocal(Strings.UPDATING);
            }
            else if (update.isDownloaded())
            {
                Gui.func_146110_a(this.left + listWidth - 30, var3 + 8, 16, 0, 16, 16, 48, 32);

                if (!update.MOD_ID.equalsIgnoreCase(Reference.MOD_ID))
                {
                    info = StatCollector.translateToLocal(Strings.IS_DOWNLOADED);
                }
                else
                {
                    info = StatCollector.translateToLocal(Strings.UNABLE_TO_REMOVE_SELF);
                }
            }
            else if (update.isErrored())
            {
                Gui.func_146110_a(this.left + listWidth - 30, var3 + 8, 32, 0, 16, 16, 48, 32);

                info = StatCollector.translateToLocal(Strings.ERRORED);
            }
            else if (update.isDirectLink)
            {
                Gui.func_146110_a(this.left + listWidth - 30, var3 + 8, 16, 16, 16, 16, 48, 32);

                info = StatCollector.translateToLocal(Strings.DL_AVAILABLE);
            }
            else if (update.updateURL != null)
            {
                Gui.func_146110_a(this.left + listWidth - 30, var3 + 8, 0, 16, 16, 16, 48, 32);

                info = StatCollector.translateToLocal(Strings.LINK_TO_DL);
            }
            else
            {
                info = StatCollector.translateToLocal(Strings.CANNOT_UPDATE);
            }
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(info, listWidth - 10), this.left + 3, var3 + 22, 0xCCCCCC);
        }
    }

    @Override
    public void overlayBackground()
    {
        this.client.renderEngine.bindTexture(Gui.optionsBackground);
        GL11.glColor4f(0.25F, 0.25F, 0.25F, 1.0F);
        Gui.func_146110_a(left - 10, top - slotHeight, 0, 0, listWidth + 20, slotHeight, 32, 32);
        Gui.func_146110_a(left - 10, top + listHeight, 0, listHeight + slotHeight, listWidth + 20, slotHeight, 32, 32);
    }
}
