package info.loenwind.thatsagoodmap.voidworld;

import javax.annotation.Nonnull;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class VoidWorldType extends WorldType {
  public VoidWorldType() {
    super("void");
  }

  @Override
  public @Nonnull BiomeProvider getBiomeProvider(@Nonnull World world) {
    return new VoidWorldBiomeProvider(world);
  }

  @Override
  public @Nonnull IChunkGenerator getChunkGenerator(@Nonnull World world, @Nonnull String generatorOptions) {
    return new ChunkGeneratorFlatVoid(world);
  }

  @Override
  public int getSpawnFuzz(@Nonnull WorldServer world, @Nonnull MinecraftServer server) {
    return 1;
  }

}
