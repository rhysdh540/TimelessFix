package dev.rdh.timelessfix.mixin.allocation_rate;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LayerArmorBase.class)
abstract class LayerArmorBaseMixin {
	@Unique private static final Map<ItemArmor.ArmorMaterial, ResourceLocation[]> timelessFix$armorTextures = new IdentityHashMap<>();

	@Overwrite
	private ResourceLocation getArmorResource(ItemArmor armor, boolean leggings, String type) {
		ItemArmor.ArmorMaterial material = armor.getArmorMaterial();
		ResourceLocation[] textures = timelessFix$armorTextures.get(material);
		if (textures == null) {
			textures = new ResourceLocation[4];
			timelessFix$armorTextures.put(material, textures);
		}

		int index = (leggings ? 2 : 0) + (type == null ? 0 : 1);
		ResourceLocation texture = textures[index];
		if (texture == null) {
			String path = "textures/models/armor/" + material.getName() + "_layer_" + (leggings ? 2 : 1) + (type == null ? "" : "_" + type) + ".png";
			texture = new ResourceLocation(path);
			textures[index] = texture;
		}

		return texture;
	}
}
