/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.jme3.cubes;

/**
 *
 * @author Carl
 */
public class BlockSkin_TextureLocation{

    public BlockSkin_TextureLocation(int column, int row){
        this.column = column;
        this.row = row;
    }
    private int column;
    private int row;

    public int getColumn(){
        return column;
    }

    public int getRow(){
        return row;
    }
    
    public boolean equals(BlockSkin_TextureLocation blockSkin_TextureLocation){
        return ((column == blockSkin_TextureLocation.getColumn()) && (row == blockSkin_TextureLocation.getRow()));
    }
}
