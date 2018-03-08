package info.loenwind.thatsagoodmap.overworld;

import javax.annotation.Nonnull;

import info.loenwind.thatsagoodmap.ThatsAGoodMapMod;
import info.loenwind.thatsagoodmap.voidworld.ChunkGeneratorFlatVoid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderSurfaceVoid extends WorldProviderSurface {
  @Override
  public boolean canCoordinateBeSpawn(int x, int z) {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world))
      return true;
    return super.canCoordinateBeSpawn(x, z);
  }

  @Override
  public @Nonnull BlockPos getRandomizedSpawnPoint() {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world)) {
      BlockPos spawn = new BlockPos(world.getSpawnPoint());
      spawn = world.getTopSolidOrLiquidBlock(spawn);
      return spawn;
    } else {
      return super.getRandomizedSpawnPoint();
    }
  }

  @Override
  public @Nonnull IChunkGenerator createChunkGenerator() {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world))
      return new ChunkGeneratorFlatVoid(world);
    return super.createChunkGenerator();
  }
}
