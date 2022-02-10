package net.ludocrypt.limlib.impl.world;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableObject;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.sound.LiminalTravelSound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class LiminalSoundRegistry {

	// I can't parameterize this
	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends LiminalTravelSound>> LIMINAL_TRAVEL_SOUND = (SimpleRegistry<Codec<? extends LiminalTravelSound>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "liminal_travel_sound_codec")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<LiminalTravelSound> OVERRIDE_TRAVEL_SOUND = FabricRegistryBuilder.createSimple(LiminalTravelSound.class, new Identifier("limlib", "liminal_travel_sound")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static Codec<? extends LiminalTravelSound> register(Identifier id, Codec<? extends LiminalTravelSound> sound) {
		return Registry.register(LIMINAL_TRAVEL_SOUND, id, sound);
	}

	public static LiminalTravelSound registerOverride(Identifier id, LiminalTravelSound sound) {
		return Registry.register(OVERRIDE_TRAVEL_SOUND, id, sound);
	}

	public static Optional<SoundEvent> getCurrent(ServerWorld from, ServerWorld to) {
		MutableObject<Optional<SoundEvent>> mutableSound = new MutableObject<Optional<SoundEvent>>(Optional.of(SoundEvents.BLOCK_PORTAL_TRAVEL));

		Optional<LiminalTravelSound> toTravelSound = ((DimensionTypeAccess) to.getDimension()).getLiminalEffects().getTravel();
		Optional<LiminalTravelSound> fromTravelSound = ((DimensionTypeAccess) from.getDimension()).getLiminalEffects().getTravel();

		if (fromTravelSound.isPresent()) {
			fromTravelSound.get().hookSound(from, to, mutableSound);
		}

		if (toTravelSound.isPresent()) {
			toTravelSound.get().hookSound(from, to, mutableSound);
		}

		List<LiminalTravelSound> list = Lists.newArrayList(OVERRIDE_TRAVEL_SOUND.iterator());
		Collections.sort(list, (a, b) -> Integer.compare(a == null ? 1000 : a.priority(), b == null ? 1000 : b.priority()));
		for (LiminalTravelSound sound : list) {
			sound.hookSound(from, to, mutableSound);
		}

		return mutableSound.getValue();
	}

}
