package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class WhitePawnEntity extends PawnEntity
{
	public WhitePawnEntity(EntityType<? extends WhitePawnEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	public String getTexture() 
	{
		return "textures/entity/prospitian_pawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}
}
