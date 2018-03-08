package info.loenwind.thatsagoodmap.voidworld;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class VoidWorldBiomeProvider extends BiomeProvider {
  private World world;

  public VoidWorldBiomeProvider(World world) {
    super(world.getWorldInfo());
    this.world = world;
  }

  @Override
  public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random rand) {
    BlockPos ret = super.findBiomePosition(x, z, range, biomes, rand);
    if (x == 0 && z == 0 && !world.getWorldInfo().isInitialized()) {
      if (ret == null) {
        ret = BlockPos.ORIGIN;
      }
    }
    return ret;
  }

}
