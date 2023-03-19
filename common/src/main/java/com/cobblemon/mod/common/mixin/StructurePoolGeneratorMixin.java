package com.cobblemon.mod.common.mixin;

import net.minecraft.registry.Registry;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(StructurePoolBasedGenerator.StructurePoolGenerator.class)
public abstract class StructurePoolGeneratorMixin {
    @Final
    @Mutable
    @Shadow Deque<StructurePoolBasedGenerator.ShapedPoolStructurePiece> structurePieces;

    @Final
    @Mutable
    @Shadow List<? super PoolStructurePiece> children;

    @Shadow public abstract void generatePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, boolean modifyBoundingBox, HeightLimitView world, NoiseConfig noiseConfig);

    Map<String, Integer> generatedStructureCounts;

    private static Map<String, Integer> structureMaxes;
    static {
        //Mapped using location string as key
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put("cobblemon:village_plains/village_plains_pokecenter", 1);
        aMap.put("cobblemon:village_savanna/village_savanna_pokecenter", 1);
        aMap.put("cobblemon:village_desert/village_desert_pokecenter", 1);
        aMap.put("cobblemon:village_snowy/village_snowy_pokecenter", 1);
        aMap.put("cobblemon:village_taiga/village_taiga_pokecenter", 1);

//        aMap.put("cobblemon:village_plains/village_plains_long_path", 3);
//        aMap.put("cobblemon:village_savanna/village_savanna_long_path", 3);
//        aMap.put("cobblemon:village_desert/village_desert_long_path", 3);
//        aMap.put("cobblemon:village_snowy/village_snowy_long_path", 3);
//        aMap.put("cobblemon:village_taiga/village_taiga_long_path", 3);
        structureMaxes = Collections.unmodifiableMap(aMap);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onStructurePoolGeneratorCreation(Registry registry, int maxSize, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List children, Random random, CallbackInfo ci) {
        generatedStructureCounts = new HashMap<String, Integer>();
//        System.out.println("NEW POOL GENERATOR CREATED:");
    }

    @ModifyVariable(method = "generatePiece", at = @At("STORE"), ordinal = 1)
    private Iterator<StructurePoolElement> reduceStructurePoolELementIterator(Iterator<StructurePoolElement> iterator) {
        List<StructurePoolElement> beforeList = new ArrayList<StructurePoolElement>();
        List<StructurePoolElement> reducedList = new ArrayList<StructurePoolElement>();

        while (iterator.hasNext()) {
            StructurePoolElement structure = iterator.next();
            beforeList.add(structure);
            String structurePieceLocationKey = getCobblemonOnlyLocation(structure);
            if (structurePieceLocationKey == null) {
                reducedList.add(structure);
                continue;
            }

            Integer currentlyPlacedCount = generatedStructureCounts.get(structurePieceLocationKey);
            if (currentlyPlacedCount == null) currentlyPlacedCount = 0;

            Integer maxAllowed = structureMaxes.get(structurePieceLocationKey);
            if (maxAllowed == null) {
                reducedList.add(structure);
                continue;
            }

            if (currentlyPlacedCount < maxAllowed) {
                reducedList.add(structure);
            }
        }


        System.out.println("Reduced List before: " + reducedList);

        //////
        for (String maxStructureLocationKey : structureMaxes.keySet()) {
            Integer maxAllowed = structureMaxes.get(maxStructureLocationKey);

            Integer currentlyGenerated = generatedStructureCounts.get(maxStructureLocationKey);
            if (currentlyGenerated == null) currentlyGenerated = 0;

            Integer allowedInstances = maxAllowed - currentlyGenerated;
            reducedList = removeInstanceOfLocationKeyFrom(reducedList, allowedInstances, maxStructureLocationKey);
        }
        //////

        System.out.println("Before List: " + beforeList);
        System.out.println("Reduced List: " + reducedList);
        List<StructurePoolElement> structurePiecesList = structurePieces.stream()
                .map(shapedStructurePiece -> shapedStructurePiece.piece.getPoolElement())
                .collect(Collectors.toList());
        System.out.println("Structures: " + structurePiecesList);
        System.out.println(generatedStructureCounts + "\n");

        return reducedList.iterator();
    }

    @ModifyVariable(method = "generatePiece", at = @At("STORE"), ordinal = 1)
    private PoolStructurePiece injected(PoolStructurePiece poolStructurePiece) {
        String structureLocationKey = getCobblemonOnlyLocation(poolStructurePiece.getPoolElement());
        if (structureLocationKey != null) {
            Integer currentlyGenerated = generatedStructureCounts.get(structureLocationKey);
            if (currentlyGenerated == null) currentlyGenerated = 0;
            generatedStructureCounts.put(structureLocationKey, currentlyGenerated + 1);
        }

        return poolStructurePiece;
    }

    @Inject(method = "generatePiece", at = @At("HEAD"))
    private void beforeGeneratePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, boolean modifyBoundingBox, HeightLimitView world, NoiseConfig noiseConfig, CallbackInfo ci) {
        String structureLocationKey = getCobblemonOnlyLocation(piece.getPoolElement());

        if (structureLocationKey != null && structureLocationKey.equals("cobblemon:village_snowy/village_snowy_pokecenter")) System.out.println("Before Counts" + generatedStructureCounts);

        if (structureLocationKey != null) {
            Integer currentlyGenerated = generatedStructureCounts.get(structureLocationKey);
            if (currentlyGenerated == null) currentlyGenerated = 0;
            generatedStructureCounts.put(structureLocationKey, currentlyGenerated + 1);
        }

        if (structureLocationKey != null && structureLocationKey.equals("cobblemon:village_snowy/village_snowy_pokecenter")) {
            System.out.println("Generating: cobblemon:village_snowy/village_snowy_pokecenter");
            System.out.println("After Counts: " + generatedStructureCounts + "\n");
        }

        List<StructurePoolElement> beforeStructurePiecesList = structurePieces.stream()
                .map(shapedStructurePiece -> shapedStructurePiece.piece.getPoolElement())
                .collect(Collectors.toList());

        List<StructurePoolBasedGenerator.ShapedPoolStructurePiece> reducedStructurePiecesList = structurePieces.stream().toList();

        for (String maxStructureLocationKey : structureMaxes.keySet()) {
            Integer maxAllowed = structureMaxes.get(maxStructureLocationKey);

            Integer currentlyGenerated = generatedStructureCounts.get(maxStructureLocationKey);
            if (currentlyGenerated == null) currentlyGenerated = 0;
            if (currentlyGenerated < maxAllowed) {
                continue;
            }

            //Already have max so need to remove
            reducedStructurePiecesList = reducedStructurePiecesList.stream()
                    .filter(shapedStructurePiece -> {
                        String locationKey = getCobblemonOnlyLocation(shapedStructurePiece.piece.getPoolElement());
                        if (locationKey == null) {
                            return true;
                        }

                        return !locationKey.equals(maxStructureLocationKey);
                    })
                    .collect(Collectors.toList());
        }

        structurePieces = new ArrayDeque<>(reducedStructurePiecesList);
        List<StructurePoolElement> afterStructurePiecesList = reducedStructurePiecesList.stream()
                .map(shapedStructurePiece -> shapedStructurePiece.piece.getPoolElement())
                .collect(Collectors.toList());
    }

    private static String getCobblemonOnlyLocation(StructurePoolElement structurePoolElement) {
        String location = getLocationIfAvailable(structurePoolElement);
        if (location == null) return null;

        if (!location.contains("cobblemon")) return null;

        return location;
    }

    private static String getLocationIfAvailable(StructurePoolElement structurePoolElement) {
        if (structurePoolElement instanceof SinglePoolElement) {
            SinglePoolElement singlePoolElement = (SinglePoolElement) structurePoolElement;
            if (singlePoolElement.location.left() == null) {
                return null;
            }

            return singlePoolElement.location.left().get().toString();
        } else if (structurePoolElement instanceof LegacySinglePoolElement) {
            LegacySinglePoolElement legacySinglePoolElement = (LegacySinglePoolElement) structurePoolElement;
            if (legacySinglePoolElement.location.left() == null) {
                return null;
            }

            return legacySinglePoolElement.location.left().get().toString();
        } else {
            return null;
        }
    }

    private List<StructurePoolElement> removeInstanceOfLocationKeyFrom(List<StructurePoolElement> structureList, Integer allowedNumberOfInstances, String locationKey) {
        List<StructurePoolElement> reducedList = new ArrayList<>();
        Integer instancesFound = 0;

        for (StructurePoolElement structurePoolElement: structureList) {
            String structureKey = getCobblemonOnlyLocation(structurePoolElement);
            if (structureKey == null || !structureKey.equals(locationKey)) {
                reducedList.add(structurePoolElement);
                continue;
            }

            if (instancesFound >= allowedNumberOfInstances) {
                continue;
            }

            reducedList.add(structurePoolElement);
            instancesFound++;
        }

        return  reducedList;
    }
}
