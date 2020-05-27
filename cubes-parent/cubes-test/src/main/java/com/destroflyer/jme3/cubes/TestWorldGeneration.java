package com.destroflyer.jme3.cubes;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class TestWorldGeneration extends SimpleApplication{

    public static void main(String[] args){
        Logger.getLogger("").setLevel(Level.SEVERE);
        TestWorldGeneration app = new TestWorldGeneration();
        app.start();
    }

    public TestWorldGeneration(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - World Generation");
    }

    @Override
    public void simpleInitApp(){
        CubesTestAssets.registerAssets(assetManager);
        CubesTestAssets.registerBlocks();
        CubesTestAssets.initializeEnvironment(this);
        
        BlockTerrainControl blockTerrain = new BlockTerrainControl(CubesTestAssets.getSettings(this), new Vector3Int(50, 1, 50));
        
        SimplexNoise noise = new SimplexNoise(421);
        SimplexNoise noiseTrees = new SimplexNoise(100);
        int width = 800;
        int depth = 800;
        int height = 100;
        double scalePlane = 170;
        double scaleHeight = 70;
        int waterHeight = 28;
        Vector3Int location = new Vector3Int();
        Vector3Int size = new Vector3Int(1, 1, 1);
        int freeYCounter = 0;
        int treeY;
        for(int x=0;x<width;x++){
            for(int z=0;z<depth;z++){
                freeYCounter = 0;
                for(int y=0;y<height;y++){
                    double value = noise.noise(x / scalePlane, y / scaleHeight, z / scalePlane) - 2*((double) y / height) + 1;
                    if(value > 0.2){
                        location.set(x, y, z);
                        blockTerrain.setBlock(location, CubesTestAssets.BLOCK_GRASS);
                        freeYCounter = 0;
                    }
                    else if(y < waterHeight){
                        location.set(x, y, z);
                        blockTerrain.setBlock(location, CubesTestAssets.BLOCK_WATER);
                        freeYCounter = 999999;
                    }
                    else{
                        freeYCounter++;
                        if((freeYCounter == 5) && (noiseTrees.noise(x, z) > 0.8)){
                            treeY = (y - 4);
                            location.set(x - 1, treeY + 2, z - 1);
                            size.set(3, 1, 3);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x - 1, treeY + 3, z - 1);
                            size.set(3, 1, 3);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x - 2, treeY + 3, z);
                            size.set(5, 1, 1);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x, treeY + 3, z - 2);
                            size.set(1, 1, 5);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x - 1, treeY + 4, z- 1);
                            size.set(3, 1, 3);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x, treeY + 5, z);
                            size.set(1, 1, 1);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_LEAFS);
                            location.set(x, treeY, z);
                            size.set(1, 4, 1);
                            blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_WOOD);
                        }
                    }
                }
            }
        }
        location.set(0, 0, 0);
        size.set(width, 1, depth);
        blockTerrain.setBlockArea(location, size, CubesTestAssets.BLOCK_STONE);
        
        Node terrainNode = new Node();
        terrainNode.addControl(blockTerrain);
        terrainNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(terrainNode);
        
        cam.setLocation(new Vector3f(-64, 187, -55));
        cam.lookAtDirection(new Vector3f(0.64f, -0.45f, 0.6f), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(300);
        cam.setFrustumFar(9999999);
    }
}
