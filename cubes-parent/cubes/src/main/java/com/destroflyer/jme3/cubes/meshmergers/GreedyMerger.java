/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.jme3.cubes.meshmergers;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.destroflyer.jme3.cubes.*;

/**
 *
 * @author Carl
 */
public class GreedyMerger extends MeshMerger{

    private boolean isTransparent;
    private boolean[][] handledBlocks;
    private int areaX;
    private int areaY;
    private int areaWidth;
    private int nextAreaX;
    private int nextAreaY;
    private boolean areaIsFrontSide;
    private BlockSkin_TextureLocation areaTextureLocation;
    private BlockSkin_TextureLocation currentTextureLocation;
    private boolean isCalculatingAreaHeight;

    @Override
    protected void merge(BlockChunkControl blockChunk, boolean isTransparent){
        this.isTransparent = isTransparent;
        int chunksX = blockChunk.getTerrain().getSettings().getChunkSizeX();
        int chunksY = blockChunk.getTerrain().getSettings().getChunkSizeY();
        int chunksZ = blockChunk.getTerrain().getSettings().getChunkSizeZ();
        boolean isLastChunkX = (blockChunk.getLocation().getX() == (blockChunk.getTerrain().getChunks().length - 1));
        boolean isLastChunkY = (blockChunk.getLocation().getY() == (blockChunk.getTerrain().getChunks()[0].length - 1));
        boolean isLastChunkZ = (blockChunk.getLocation().getZ() == (blockChunk.getTerrain().getChunks()[0][0].length - 1));
        merge(blockChunk, Vector3f.UNIT_X, Vector3f.UNIT_Y, Vector3f.UNIT_Z, chunksX, chunksY, chunksZ, isLastChunkZ);
        merge(blockChunk, Vector3f.UNIT_X, Vector3f.UNIT_Z, Vector3f.UNIT_Y, chunksX, chunksZ, chunksY, isLastChunkY);
        merge(blockChunk, Vector3f.UNIT_Y, Vector3f.UNIT_Z, Vector3f.UNIT_X, chunksY, chunksZ, chunksX, isLastChunkX);
    }
    
    private void merge(BlockChunkControl blockChunk, Vector3f axisX, Vector3f axisY, Vector3f axisZ, int chunksX, int chunksY, int chunksZ, boolean isLastChunk){
        Vector3Int axisXInt = new Vector3Int((int) axisX.getX(), (int) axisX.getY(), (int) axisX.getZ());
        Vector3Int axisYInt = new Vector3Int((int) axisY.getX(), (int) axisY.getY(), (int) axisY.getZ());
        Vector3Int axisZInt = new Vector3Int((int) axisZ.getX(), (int) axisZ.getY(), (int) axisZ.getZ());
        int maximumZ = (isLastChunk?chunksZ:(chunksZ - 1));
        for(int z=-1;z<maximumZ;z++){
            handledBlocks = new boolean[chunksX][chunksY];
            areaX = -1;
            areaY = -1;
            nextAreaX = -1;
            nextAreaY = -1;
            isCalculatingAreaHeight = false;
            int x = 0;
            int y = 0;
            while(true){
                boolean isHandled = true;
                if((x < chunksX) && (y < chunksY)){
                    isHandled = handledBlocks[x][y];
                }
                Vector3Int location = axisXInt.mult(x).addLocal(axisYInt.mult(y)).addLocal(axisZInt.mult(z));
                Vector3Int neighborLocation = location.add(axisZInt);
                Block block = blockChunk.getBlock(location);
                Block neighborBlock = blockChunk.getBlock(neighborLocation);
                boolean isFrontSide = (block == null);
                Block.Face currentFace = null;
                if(axisZ == Vector3f.UNIT_X){
                    currentFace = (isFrontSide?Block.Face.Left:Block.Face.Right);
                }
                else if(axisZ == Vector3f.UNIT_Y){
                    currentFace = (isFrontSide?Block.Face.Bottom:Block.Face.Top);
                }
                else if(axisZ == Vector3f.UNIT_Z){
                    currentFace = (isFrontSide?Block.Face.Front:Block.Face.Back);
                }
                if((block != null) && (block.getSkin(blockChunk, location, currentFace).isTransparent() != isTransparent)){
                    block = null;
                    isFrontSide = true;
                }
                if((neighborBlock != null) && (neighborBlock.getSkin(blockChunk, neighborLocation, currentFace).isTransparent() != isTransparent)){
                    neighborBlock = null;
                }
                if(isFrontSide){
                    if(neighborBlock != null){
                        currentTextureLocation = neighborBlock.getSkin(blockChunk, neighborLocation, currentFace).getTextureLocation();
                    }
                }
                else{
                    currentTextureLocation = block.getSkin(blockChunk, location, currentFace).getTextureLocation();
                }
                if((!isHandled) && isFace(block, neighborBlock, isFrontSide)){
                    if(areaX == -1){
                        areaX = x;
                        areaY = y;
                        areaIsFrontSide = isFrontSide;
                        areaTextureLocation = currentTextureLocation;
                    }
                }
                else if(areaX != -1){
                    if(isCalculatingAreaHeight){
                        int areaHeight = (y - areaY);
                        for(int i=0;i<areaWidth;i++){
                            for(int r=0;r<areaHeight;r++){
                                handledBlocks[areaX + i][areaY + r] = true;
                            }
                        }
                        addArea(axisX, axisY, axisZ, areaX, areaY, areaWidth, areaHeight, z + 1, areaTextureLocation, areaIsFrontSide);
                        x = nextAreaX - 1;
                        y = nextAreaY;
                        areaX = -1;
                        areaY = -1;
                        areaTextureLocation = null;
                        isCalculatingAreaHeight = false;
                    }
                    else{
                        areaWidth = (x - areaX);
                        nextAreaX = x;
                        nextAreaY = y;
                        if(nextAreaX >= chunksX){
                            nextAreaX = 0;
                            nextAreaY++;
                        }
                        x = areaX - 1;
                        y++;
                        isCalculatingAreaHeight = true;
                    }
                }
                x++;
                if(isCalculatingAreaHeight && (x >= (areaX + areaWidth))){
                    x = areaX;
                    y++;
                }
                else if(areaX == -1){
                    if(x >= chunksX){
                        x = 0;
                        y++;
                        if(y >= chunksY){
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private boolean isFace(Block block, Block neighborBlock, boolean isFrontSide){
        boolean isBlockExisting = (block != null);
        boolean isNeighborBlockExisting = (neighborBlock != null);
        if(isBlockExisting != isNeighborBlockExisting){
            if(areaX != -1){
                return ((isFrontSide == areaIsFrontSide) && currentTextureLocation.equals(areaTextureLocation));
            }
            return true;
        }
        return false;
    }
    
    private void addArea(Vector3f axisX, Vector3f axisY, Vector3f axisZ, int x, int y, int width, int height, int z, BlockSkin_TextureLocation textureLocation, boolean isFrontSide){
        int indicesOffset = positionsList.size();
        Vector3f zVector = axisZ.mult(z);
        Vector3f location = axisX.mult(x).addLocal(axisY.mult(y)).addLocal(zVector);
        positionsList.add(location);
        positionsList.add(axisX.mult(x + width).addLocal(axisY.mult(y)).addLocal(zVector));
        positionsList.add(axisX.mult(x).addLocal(axisY.mult(y + height)).addLocal(zVector));
        positionsList.add(axisX.mult(x + width).addLocal(axisY.mult(y + height)).addLocal(zVector));
        boolean reverseIfFrontSide = (axisZ == Vector3f.UNIT_Y);
        if(isFrontSide == reverseIfFrontSide){
            indicesList.add((short) (indicesOffset + 2));
            indicesList.add((short) (indicesOffset + 0));
            indicesList.add((short) (indicesOffset + 1));
            indicesList.add((short) (indicesOffset + 1));
            indicesList.add((short) (indicesOffset + 3));
            indicesList.add((short) (indicesOffset + 2));
        }
        else{
            indicesList.add((short) (indicesOffset + 2));
            indicesList.add((short) (indicesOffset + 1));
            indicesList.add((short) (indicesOffset + 0));
            indicesList.add((short) (indicesOffset + 1));
            indicesList.add((short) (indicesOffset + 2));
            indicesList.add((short) (indicesOffset + 3));
        }
        Vector2f textureCoordinate = new Vector2f(textureLocation.getColumn(), textureLocation.getRow());
        for(int i=0;i<4;i++){
            textureCoordinatesList.add(textureCoordinate);
        }
        Vector3f normal = (isFrontSide?axisZ:axisZ.negate());
        for(int i=0;i<4;i++){
            normalsList.add(normal.getX());
            normalsList.add(normal.getY());
            normalsList.add(normal.getZ());
        }
    }
}
