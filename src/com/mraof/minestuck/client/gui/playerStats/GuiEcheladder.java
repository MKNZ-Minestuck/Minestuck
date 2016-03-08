package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.lwjgl.input.Mouse;

import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEcheladder extends GuiPlayerStats
{
	
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	private static final ResourceLocation potionIcons = new ResourceLocation("textures/gui/container/inventory.png");
	
	private static final int MAX_SCROLL = Echeladder.RUNG_COUNT*14 - 154;
														//0			1			2			3			4			5			6			7			8			9			10			11			12			13			14			15			16			17			18			19
	private static final int[] backgrounds = new int[] {0xFF4FD400, 0xFFFF0000, 0xFF956C4C, 0xFF7DB037, 0xFFD8A600, 0xFF7F0000, 0xFF007F0E, 0xFF808080, 0xFF00FF21, 0xFF4800FF, 0xFF404040, 0xFFE4FF00, 0xFFDFBB6C, 0xFFCECECE, 0xFFFF0000, 0xFFC68E4D, 0xFF60E554, 0xFF88CE88, 0xFF006EBC, 0xFFF12B26};
	private static final int[] textColors  = new int[] {  0xFDFF2B,   0x404040,   0xB6FF00,   0x775716,   0xFFFFFF,   0xFF6A00,   0x0094FF,   0x3F3F3F,   0x007F7F,   0xB200FF,   0x7B9CB5,   0x6D9A00,   0x219621,   0x7F743F,   0xFF7F7F,   0xAF0A8C,   0x2A9659,   0xFFD8F2,   0xFFFFFF,   0xDAFF7F};
	
	private static final int ladderXOffset = 163, ladderYOffset = 25;
	private static final int rows = 12;
	private int scrollIndex;
	public static int lastRung = -1;
	private int fromRung;
	private boolean wasClicking, isScrolling;
	
	public GuiEcheladder()
	{
		super();
		guiWidth = 250;
		guiHeight = 197;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		scrollIndex = MathHelper.clamp_int((MinestuckPlayerData.rung - 8)*14, 0, MAX_SCROLL);
		fromRung = Math.max(lastRung, MinestuckPlayerData.rung - 4);
		lastRung = MinestuckPlayerData.rung;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		boolean mouseButtonDown = Mouse.isButtonDown(0);
		if(!wasClicking && mouseButtonDown && xcor >= xOffset + 80 && xcor < xOffset + 87 && ycor >= yOffset + 42 && ycor < yOffset + 185)
			isScrolling = true;
		else if(!mouseButtonDown)
			isScrolling = false;
		
		if(isScrolling)
		{
			scrollIndex = (int) (MAX_SCROLL*(ycor - yOffset - 179)/-130F);
			scrollIndex = MathHelper.clamp_int(scrollIndex, 0, MAX_SCROLL);
		}
		wasClicking = mouseButtonDown;
		
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		int scroll = scrollIndex % 14;
		for(int i = 0; i < rows; i++)
			drawTexturedModalRect(xOffset + 90, yOffset + 175 + scroll - i*14, 7, 242, 146, 14);
		
		Random rand = new Random(452619373);
		for(int i = 0; i < scrollIndex/14; i++)
			rand.nextInt(0xFFFFFF);
		for(int i = 0; i < rows; i++)
		{
			int y = yOffset + 177 + scroll - i*14;
			int rung = scrollIndex/14 + i;
			if(rung > Echeladder.RUNG_COUNT)
				break;
			
			int textColor = 0xFFFFFF;
			if(rung <= MinestuckPlayerData.rung)
			{
				textColor = rand.nextInt(0xFFFFFF);
				if(textColors.length > rung)
					textColor = textColors[rung];
				drawRect(xOffset + 90, y, xOffset + 236, y + 12, backgrounds.length > rung ? backgrounds[rung] : (textColor^0xFFFFFFFF));
			} else if(rung == MinestuckPlayerData.rung + 1)
			{
				int bg = rand.nextInt(0xFFFFFF)^0xFFFFFFFF;
				if(backgrounds.length > rung)
					bg = backgrounds[rung];
				else if(textColors.length > rung)
					bg = textColors[rung]^0xFFFFFFFF;
				drawRect(xOffset + 90, y + 10, xOffset + 90 + (int)(146*MinestuckPlayerData.rungProgress), y + 12, bg);
			} else rand.nextInt(0xFFFFFF);
			
			String s = StatCollector.canTranslate("echeladder.rung"+rung) ? StatCollector.translateToLocal("echeladder.rung"+rung) : "Rung "+(rung+1);
			mc.fontRendererObj.drawString(s, xOffset+ladderXOffset - mc.fontRendererObj.getStringWidth(s) / 2, y + 2, textColor);
		}
		GlStateManager.color(1,1,1);
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		this.drawTexturedModalRect(xOffset + 80, yOffset + 42 + (int) (130*(1 - scrollIndex/(float) MAX_SCROLL)), 0, 243, 7, 13);
		
		boolean gristLimit = true;
		this.mc.getTextureManager().bindTexture(potionIcons);
		this.drawTexturedModalRect(xOffset + 5, yOffset + 30, 72, 198, 18, 18);
		this.drawTexturedModalRect(xOffset + 5, yOffset + 84, 126, 198, 18, 18);
		this.mc.getTextureManager().bindTexture(icons);
		this.drawTexturedModalRect(xOffset + 6, yOffset + 139, 48, 64, 16, 16);
		this.drawTexturedModalRect(xOffset + 5, yOffset + 7, 238, 16, 18, 18);
		
		String msg = StatCollector.translateToLocal("gui.echeladder.name");
		mc.fontRendererObj.drawString(msg, xOffset + 168 - mc.fontRendererObj.getStringWidth(msg)/2, yOffset + 12, 0x404040);
		
		int attack = (int) Math.round(100*Echeladder.attackBonus(MinestuckPlayerData.rung));
		mc.fontRendererObj.drawString(StatCollector.translateToLocal("gui.echeladder.attack.name"), xOffset + 24, yOffset + 30, 0x404040);
		mc.fontRendererObj.drawString(attack+"%", xOffset + 26, yOffset + 39, 0x0094FF);
		
		double health = 10 + Echeladder.healthBoost(MinestuckPlayerData.rung)/2.0;
		mc.fontRendererObj.drawString(StatCollector.translateToLocal("gui.echeladder.health.name"), xOffset + 24, yOffset + 84, 0x404040);
		mc.fontRendererObj.drawString(String.valueOf(health), xOffset + 26, yOffset + 93, 0x0094FF);
		
		mc.fontRendererObj.drawString("=", xOffset + 25, yOffset + 12, 0x404040);
		mc.fontRendererObj.drawString(String.valueOf(MinestuckPlayerData.boondollars), xOffset + 27 + mc.fontRendererObj.getCharWidth('='), yOffset + 12, 0x0094FF);
		
		mc.fontRendererObj.drawString(StatCollector.translateToLocal("gui.echeladder.cache.name"), xOffset + 24, yOffset + 138, 0x404040);
		mc.fontRendererObj.drawString("Unlimited", xOffset + 26, yOffset + 147, 0x0094FF);
		
		fromRung = Math.max(fromRung, MinestuckPlayerData.rung - 4);
		
		String tooltip = null;
		if(fromRung < MinestuckPlayerData.rung)
		{
			rand =  new Random(452619373);
			for(int i = 0; i <= fromRung; i++)
				rand.nextInt(0xFFFFFF);
			for(int rung = fromRung + 1; rung <= MinestuckPlayerData.rung; rung++)
			{
				int index = rung - 1 - fromRung;
				int textColor = rand.nextInt(0xFFFFFF);
				if(textColors.length > rung)
					textColor = textColors[rung];
				int bg = backgrounds.length > rung ? backgrounds[rung] : (textColor^0xFFFFFFFF);
				
				String str = "+"+(Math.round(100*Echeladder.attackBonus(rung)) - Math.round(100*Echeladder.attackBonus(rung - 1)))+"%!";
				drawRect(xOffset + 5 + 32*(index%2), yOffset + 50 + 15*(index/2), xOffset + 35 + 32*(index%2), yOffset + 62 + 15*(index/2), bg);
				int strX = xOffset + 20 + 32*(index%2) - mc.fontRendererObj.getStringWidth(str)/2, strY = yOffset + 52 + 15*(index/2);
				mc.fontRendererObj.drawString(str, strX, strY, textColor);
				if(ycor >= strY && ycor < strY + mc.fontRendererObj.FONT_HEIGHT && xcor >= strX && xcor < strX + mc.fontRendererObj.getStringWidth(str))
				{
					int diff = (int) Math.round(100*Echeladder.attackBonus(rung)*Echeladder.getUnderlingDamageModifier(rung));
					diff -= Math.round(100*Echeladder.attackBonus(rung - 1)*Echeladder.getUnderlingDamageModifier(rung - 1));
					tooltip = StatCollector.translateToLocalFormatted("gui.echeladder.damageUnderling.increase", diff);
				}
				
				double d = (Echeladder.healthBoost(rung) - Echeladder.healthBoost(rung - 1))/2D;
				str = "+"+(d == 0 ? d : d+"!");
				drawRect(xOffset + 5 + 32*(index%2), yOffset + 104 + 15*(index/2), xOffset + 35 + 32*(index%2), yOffset + 116 + 15*(index/2), bg);
				strX = xOffset + 20 + 32*(index%2) - mc.fontRendererObj.getStringWidth(str)/2;
				strY = yOffset + 106 + 15*(index/2);
				mc.fontRendererObj.drawString(str, strX, strY, textColor);
				if(ycor >= strY && ycor < strY + mc.fontRendererObj.FONT_HEIGHT && xcor >= strX && xcor < strX + mc.fontRendererObj.getStringWidth(str))
				{
					int diff = (int) Math.round(1000*Echeladder.getUnderlingProtectionModifier(rung - 1));
					diff -= Math.round(1000*Echeladder.getUnderlingProtectionModifier(rung));
					tooltip = StatCollector.translateToLocalFormatted("gui.echeladder.protectionUnderling.increase", diff/10D);
				}
			}
		}
		
		drawActiveTabAndOther(xcor, ycor);
		
		if(tooltip != null)
			drawHoveringText(Arrays.asList(tooltip), xcor, ycor);
		else if(ycor >= yOffset + 39 && ycor < yOffset + 39 + mc.fontRendererObj.FONT_HEIGHT && xcor >= xOffset + 26 && xcor < xOffset + 26 + mc.fontRendererObj.getStringWidth(attack+"%"))
			drawHoveringText(Arrays.asList(StatCollector.translateToLocalFormatted("gui.echeladder.damageUnderling"), Math.round(attack*Echeladder.getUnderlingDamageModifier(MinestuckPlayerData.rung)) + "%"), xcor, ycor);
		else if(ycor >= yOffset + 93 && ycor < yOffset + 93 + mc.fontRendererObj.FONT_HEIGHT && xcor >= xOffset + 26 && xcor < xOffset + 26 + mc.fontRendererObj.getStringWidth(String.valueOf(health)))
			drawHoveringText(Arrays.asList(StatCollector.translateToLocalFormatted("gui.echeladder.protectionUnderling"), String.format("%.1f", 100*Echeladder.getUnderlingProtectionModifier(MinestuckPlayerData.rung))+"%"), xcor, ycor);
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		
		if(i != 0)
		{
			if(i > 0)
				scrollIndex += 14;
			else scrollIndex -= 14;
			scrollIndex = MathHelper.clamp_int(scrollIndex, 0, MAX_SCROLL);
		}
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		super.mouseClicked(xcor, ycor, mouseButton);
		if(mouseButton == 0&& xcor >= xOffset + 80 && xcor < xOffset + 87)
			if(ycor >= yOffset + 35 && ycor < yOffset + 42)
				scrollIndex = MathHelper.clamp_int(scrollIndex + 14, 0, MAX_SCROLL);
			else if(ycor >= yOffset + 185 && ycor < yOffset + 192)
				scrollIndex = MathHelper.clamp_int(scrollIndex - 14, 0, MAX_SCROLL);
	}
}