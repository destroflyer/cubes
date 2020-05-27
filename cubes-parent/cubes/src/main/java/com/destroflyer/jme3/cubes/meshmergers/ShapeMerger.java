/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.jme3.cubes.meshmergers;

import com.destroflyer.jme3.cubes.*;

/**
 *
 * @author Carl
 */
public class ShapeMerger extends MeshMerger{

    @Override
    protected void merge(BlockChunkControl blockChunk, boolean isTransparent){
        BlockTerrainControl blockTerrain = blockChunk.getTerrain();
        Vector3Int tmpLocation = new Vector3Int();
        for(int x=0;x<blockTerrain.getSettings().getChunkSizeX();x++){
            for(int y=0;y<blockTerrain.getSettings().getChunkSizeY();y++){
                for(int z=0;z<blockTerrain.getSettings().getChunkSizeZ();z++){
                    tmpLocation.set(x, y, z);
                    Block block = blockChunk.getBlock(tmpLocation);
                    if(block != null){
                        BlockShape blockShape = block.getShape(blockChunk, tmpLocation);
                        blockShape.prepare(isTransparent, positionsList, indicesList, normalsList, textureCoordinatesList);
                        blockShape.addTo(blockChunk, tmpLocation);
                    }
                }
            }
        }
    }
}
