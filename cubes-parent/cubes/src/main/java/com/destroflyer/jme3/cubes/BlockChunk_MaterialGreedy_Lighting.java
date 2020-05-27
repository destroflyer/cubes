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
public class BlockChunk_MaterialGreedy_Lighting extends Material{

    public BlockChunk_MaterialGreedy_Lighting(AssetManager assetManager, String blockTextureFilePath){
        super(assetManager, "MatDefs/Cubes/Lighting.j3md");
        Texture texture = assetManager.loadTexture(blockTextureFilePath);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        setTexture("DiffuseMap", texture);
        setFloat("AlphaDiscardThreshold", 0.2f);
        getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
}
