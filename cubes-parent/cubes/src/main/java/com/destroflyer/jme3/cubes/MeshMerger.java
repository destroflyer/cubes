/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.jme3.cubes;

import java.util.Iterator;
import java.util.LinkedList;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 *
 * @author Carl
 */
public abstract class MeshMerger{

    protected LinkedList<Vector3f> positionsList = new LinkedList<Vector3f>();
    protected LinkedList<Short> indicesList = new LinkedList<Short>();
    protected LinkedList<Float> normalsList = new LinkedList<Float>();
    protected LinkedList<Vector2f> textureCoordinatesList = new LinkedList<Vector2f>();

    public Mesh generateMesh(BlockChunkControl blockChunk, boolean isTransparent){
        positionsList.clear();
        indicesList.clear();
        normalsList.clear();
        textureCoordinatesList.clear();
        merge(blockChunk, isTransparent);
        return buildMesh(blockChunk.getTerrain().getSettings().getBlockSize());
    }
    
    protected abstract void merge(BlockChunkControl blockChunk, boolean isTransparent);

    private Mesh buildMesh(float blockSize){
        Vector3f[] positions = new Vector3f[positionsList.size()];
        Iterator<Vector3f> positionsIterator = positionsList.iterator();
        for(int i=0;positionsIterator.hasNext();i++){
            positions[i] = positionsIterator.next().mult(blockSize);
        }
        short[] indices = new short[indicesList.size()];
        Iterator<Short> indicesIterator = indicesList.iterator();
        for(int i=0;indicesIterator.hasNext();i++){
            indices[i] = indicesIterator.next();
        }
        Vector2f[] textureCoordinates = textureCoordinatesList.toArray(new Vector2f[0]);
        float[] normals = new float[normalsList.size()];
        Iterator<Float> normalsIterator = normalsList.iterator();
        for(int i=0;normalsIterator.hasNext();i++){
            normals[i] = normalsIterator.next();
        }
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));
        mesh.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoordinates));
        mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.updateBound();
        return mesh;
    }
}
