package info.loenwind.thatsagoodmap.end;

import javax.annotation.Nonnull;

import info.loenwind.thatsagoodmap.ThatsAGoodMapMod;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEndVoid extends WorldProviderEnd {
  @Override
  public @Nonnull IChunkGenerator createChunkGenerator() {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world))
      return new ChunkGeneratorEndVoid(world, world.getSeed(), this.getSpawnPoint());
    return new ChunkGeneratorEnd(world, true, world.getSeed(), this.getSpawnPoint());
  }
}
