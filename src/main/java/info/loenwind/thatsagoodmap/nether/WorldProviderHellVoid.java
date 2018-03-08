package info.loenwind.thatsagoodmap.nether;

import javax.annotation.Nonnull;

import info.loenwind.thatsagoodmap.ThatsAGoodMapMod;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderHellVoid extends WorldProviderHell {
  @Override
  public @Nonnull IChunkGenerator createChunkGenerator() {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world))
      return new ChunkGeneratorHellVoid(world, ThatsAGoodMapMod.instance.shouldGenerateNetherFortress(world), world.getSeed());

    return new ChunkGeneratorHell(world, ThatsAGoodMapMod.instance.shouldGenerateNetherFortress(world), world.getSeed());
  }
}
