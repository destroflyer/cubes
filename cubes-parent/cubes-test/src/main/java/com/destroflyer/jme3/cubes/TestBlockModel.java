package com.destroflyer.jme3.cubes;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.destroflyer.jme3.cubes.models.BlockModel;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class TestBlockModel extends SimpleApplication{

    public static void main(String[] args){
        Logger.getLogger("").setLevel(Level.SEVERE);
        TestBlockModel app = new TestBlockModel();
        app.start();
    }

    public TestBlockModel(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - BlockModel");
    }

    @Override
    public void simpleInitApp(){
        CubesTestAssets.registerAssets(assetManager);
        CubesTestAssets.registerBlocks();

        BlockTerrainControl blockTerrain = new BlockTerrainControl(CubesTestAssets.getSettings(this), new Vector3Int(11, 1, 10));
        //new BlockModel("Models/castle/castle.j3o", new Block[]{CubesTestAssets.BLOCK_GRASS}).addToBlockTerrain(blockTerrain, new Vector3Int(), new Vector3Int(128, 100, 144));
        new BlockModel("Models/dragon/dragon.j3o", new Block[]{CubesTestAssets.BLOCK_GRASS}).addToBlockTerrain(blockTerrain, new Vector3Int(), new Vector3Int(112, 128, 128));
        //new BlockModel("Models/golem/golem.j3o", new Block[]{CubesTestAssets.BLOCK_GRASS}).addToBlockTerrain(blockTerrain, new Vector3Int(), new Vector3Int(176, 256, 160));
        Node terrainNode = new Node();
        terrainNode.addControl(blockTerrain);
        rootNode.attachChild(terrainNode);
        
        cam.setLocation(new Vector3f(-3, 88, 100));
        cam.lookAtDirection(new Vector3f(0.44f, -0.35f, -0.83f), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(300);
    }
}
