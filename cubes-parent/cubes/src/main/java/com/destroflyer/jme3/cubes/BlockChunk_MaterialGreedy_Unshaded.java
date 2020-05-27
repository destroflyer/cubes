/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.jme3.cubes;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.texture.Texture;

/**
 *
 * @author Carl
 */
public class BlockChunk_MaterialGreedy_Unshaded extends Material{

    public BlockChunk_MaterialGreedy_Unshaded(AssetManager assetManager, String blockTextureFilePath){
        super(assetManager, "MatDefs/Cubes/BlockTexture.j3md");
        Texture texture = assetManager.loadTexture(blockTextureFilePath);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        setTexture("BlockMap", texture);
        getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
}
