package info.loenwind.thatsagoodmap.end;

import java.util.Random;

import javax.annotation.Nonnull;

import info.loenwind.thatsagoodmap.ThatsAGoodMapMod;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.structure.MapGenEndCity;

public class ChunkGeneratorEndVoid extends ChunkGeneratorEnd {
  private @Nonnull World world;
  private @Nonnull Random endRNG;
  private @Nonnull WorldGenSpikes spikes = new WorldGenSpikes();
  private @Nonnull MapGenEndCity endCityGen = new MapGenEndCity(this);

  public ChunkGeneratorEndVoid(@Nonnull World world, long seed, @Nonnull BlockPos spawn) {
    super(world, false, seed, spawn);
    this.world = world;
    this.endRNG = new Random(seed);
  }

  @Override
  public void populate(int x, int z) {
    if (ThatsAGoodMapMod.instance.shouldBeVoid(world)) {
      WorldGenSpikes.EndSpike[] aworldgenspikes$endspike = BiomeEndDecorator.getSpikesForWorld(world);

      for (WorldGenSpikes.EndSpike worldgenspikes$endspike : aworldgenspikes$endspike) {
        if (worldgenspikes$endspike.doesStartInChunk(new BlockPos(x * 16, 0, z * 16))) {
          this.spikes.setSpike(worldgenspikes$endspike);
          this.spikes.generate(world, endRNG, new BlockPos(worldgenspikes$endspike.getCenterX(), 45, worldgenspikes$endspike.getCenterZ()));
        }
      }
    }

    if (ThatsAGoodMapMod.instance.shouldGenerateEndCities(this.world)) {
      this.endCityGen.generateStructure(world, endRNG, new ChunkPos(x, z));
    }

    if (x == 0 && z == 0) {
      // Allows exit portal to be placed correctly. DragonFightManager will take over from here...
      world.setBlockState(new BlockPos(0, 45, 0), Blocks.END_STONE.getDefaultState());
    }
  }

  @SuppressWarnings("null")
  @Override
  public @Nonnull Chunk generateChunk(int x, int z) {
    ChunkPrimer primer = new ChunkPrimer();

    if (ThatsAGoodMapMod.instance.shouldGenerateEndCities(this.world))
      this.endCityGen.generate(world, x, z, primer);

    Chunk ret = new Chunk(world, primer, x, z);
    Biome[] biomes = world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
    byte[] ids = ret.getBiomeArray();

    for (int i = 0; i < ids.length; ++i) {
      ids[i] = (byte) Biome.getIdForBiome(biomes[i]);
    }

    ret.generateSkylightMap();
    return ret;
  }

}