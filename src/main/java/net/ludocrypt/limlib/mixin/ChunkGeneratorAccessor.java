package net.ludocrypt.limlib.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {

	@Invoker
	Codec<? extends ChunkGenerator> callGetCodec();

}
