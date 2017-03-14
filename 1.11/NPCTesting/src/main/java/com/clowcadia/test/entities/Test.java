package com.clowcadia.test.entities;

import com.clowcadia.test.GuiHandler;
import com.clowcadia.test.TestModHandler;
import com.clowcadia.test.utils.Utils;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class Test extends EntityTameable{
	
	private final ItemStackHandler handler;
	private String tagStomach = "Stomach";
	private int stomach;	
	private int stomachCap = 800;

	public Test(World worldIn) {
		super(worldIn);
		Utils.getLogger().info("Test: Constructor");
		
		this.setSize(1.0F, 1.0F);
		this.handler = new ItemStackHandler(9); 
		this.stomach = 0;
		this.tasks.addTask(0, new EntityAITempt(this, 0.5d, Items.APPLE, false));
	}
	
	
	public int getStomach(){
		Utils.getLogger().info("Test: getStomach");
		
		return this.stomach;
	}
	
	public void setStomach(int value){
		Utils.getLogger().info("Test: setStomach");
		
		this.stomach = value;
	}
	
	@Override
	public boolean isAIDisabled() {
		//Utils.getLogger().info("test: isAIDisabled");
		
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		Utils.getLogger().info("Test: createChild");
		
		return null;
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
		Utils.getLogger().info("Test: processInteract");
		
		if (!this.world.isRemote)
		{			
			int basicID = this.getEntityId(); //for inputing into x/y/z in the opne gui to pass the entety id
			System.out.println("Player has interacted with the mob");	
			player.openGui(TestModHandler.instance, GuiHandler.TEST, this.world, basicID,0, 0);	
		}
		return true;
    }
	
	@Override
	public void onEntityUpdate() {
		//Utils.getLogger().info("Test: onEntityUpdate");
		
		ItemStack foodStack = handler.getStackInSlot(0);
		if (this.world != null){
			if(!this.world.isRemote){
				if(foodStack.getUnlocalizedName().equals(Items.APPLE.getUnlocalizedName()) && stomach != stomachCap){					
					foodStack.splitStack(1);
					stomach += 40;
				}				
			}				
		}			
		super.onEntityUpdate();
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		Utils.getLogger().info("Test: getCapability");
		
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.handler;		
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		Utils.getLogger().info("Test: hasCapability");
		
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		Utils.getLogger().info("Test: readEntityFromNBT");
		
		this.handler.deserializeNBT(compound.getCompoundTag("ItemStackHandler"));		
		this.stomach = compound.getInteger("Stomach");
		super.readEntityFromNBT(compound);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);		
		Utils.getLogger().info("Test: writeEntityFromNBT");
		
		compound.setTag("ItemStackHandler", this.handler.serializeNBT());
		compound.setInteger("Stomach", this.stomach);
		
		
	}	

}
