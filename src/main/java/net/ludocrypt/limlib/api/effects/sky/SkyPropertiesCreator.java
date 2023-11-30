package net.ludocrypt.limlib.api.effects.sky;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.DimensionVisualEffects;
import net.minecraft.client.render.DimensionVisualEffects.SkyType;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SkyPropertiesCreator {

	public static DimensionVisualEffects create(float cloudHeight, boolean alternateSkyColor, String skyType,
			boolean brightenLighting, boolean darkened, boolean thickFog) {
		return new DimensionVisualEffects(cloudHeight, alternateSkyColor, SkyType.valueOf(skyType), brightenLighting,
			darkened) {

			@Override
			public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
				return color;
			}

			@Override
			public boolean useThickFog(int camX, int camY) {
				return thickFog;
			}

		};
	}

}
