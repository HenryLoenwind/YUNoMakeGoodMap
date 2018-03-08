package info.loenwind.thatsagoodmap.nether;

import java.util.Random;

import javax.annotation.Nonnull;

import info.loenwind.thatsagoodmap.ThatsAGoodMapMod;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkGeneratorHellVoid extends ChunkGeneratorHell {
  private @Nonnull World world;
  private @Nonnull Random hellRNG;

  @SuppressWarnings("null")
  public ChunkGeneratorHellVoid(@Nonnull World world, boolean shouldGenNetherFortress, long seed) {
    super(world, shouldGenNetherFortress, seed);
    this.world = world;
    this.hellRNG = new Random(seed);
    this.genNetherBridge = (MapGenNetherBridge) TerrainGen.getModdedMapGen(genNetherBridge, InitMapGenEvent.EventType.NETHER_BRIDGE);
  }

  @Override
  public void populate(int x, int z) {
    if (ThatsAGoodMapMod.instance.shouldGenerateNetherFortress(world))
      genNetherBridge.generateStructure(world, hellRNG, new ChunkPos(x, z));
  }

  @SuppressWarnings("null")
  @Override
  public @Nonnull Chunk generateChunk(int x, int z) {
    ChunkPrimer data = new ChunkPrimer();

    if (ThatsAGoodMapMod.instance.shouldGenerateNetherFortress(world))
      genNetherBridge.generate(world, x, z, data);

    Chunk ret = new Chunk(world, data, x, z);
    Biome[] biomes = world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
    byte[] ids = ret.getBiomeArray();

    for (int i = 0; i < ids.length; ++i) {
      ids[i] = (byte) Biome.getIdForBiome(biomes[i]);
    }

    ret.generateSkylightMap();
    return ret;
  }
}