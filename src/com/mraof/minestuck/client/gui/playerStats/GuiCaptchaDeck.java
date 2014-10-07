package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.inventory.ContainerCaptchaDeck;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCaptchaDeck extends GuiPlayerStatsContainer {
	
	private static final ResourceLocation guiCaptchaDeckEmpty = new ResourceLocation("minestuck", "textures/gui/captchaDeckEmpty.png");
	
	public GuiCaptchaDeck() {
		super(new ContainerCaptchaDeck(), true);
		guiWidth = 178;
		guiHeight= 54;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor) {
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiCaptchaDeckEmpty);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor) {
		drawTabTooltip(xcor, ycor);
	}
	
}