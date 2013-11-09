package com.mraof.minestuck.network;

import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.util.ClientEditHandler;

import cpw.mods.fml.common.network.Player;

public class ServerEditPacket extends MinestuckPacket {
	
	String target;
	int posX, posZ;
	
	public ServerEditPacket() {
		super(Type.SERVER_EDIT);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		if(data.length == 0)
			return new byte[0];
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.write((data[0].toString()+"\n").getBytes());
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		if(data.length == 0)
			return this;
		ByteArrayDataInput input = ByteStreams.newDataInput(data);
		target = input.readLine();
		posX = input.readInt();
		posZ = input.readInt();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		ClientEditHandler.onClientPackage(target, posX, posZ); 
	}

}