package net.denanu.amazia.mixin;

import java.util.UUID;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.denanu.amazia.components.AmaziaEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityChangeListener;
import net.minecraft.world.entity.EntityLike;

@Mixin(Entity.class)
public class EntityMixin implements Nameable,
EntityLike,
CommandOutput,
ComponentAccess {
	@Inject(method="adjustMovementForCollisions", at=@At("HEAD"), cancellable=true)
	public void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
		if (!AmaziaEntityComponents.getCanCollide((Entity)(Object)this)) {
			cir.setReturnValue(movement);
		}; 
	}

	@Override
	public void sendMessage(Text var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldReceiveFeedback() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldTrackOutput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBroadcastConsoleToOps() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUuid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getBlockPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Box getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChangeListener(EntityChangeListener var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Stream<? extends EntityLike> streamSelfAndPassengers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<? extends EntityLike> streamPassengersAndSelf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRemoved(RemovalReason var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Text getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
