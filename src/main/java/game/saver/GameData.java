/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 MrInformatic.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package game.saver;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public abstract class GameData implements Seriable{    
    private int id = -1;
    private ArrayList<GameData> childs;
    private Graphable<GameData> graph;
    
    public GameData(int childcount){
        childs = new ArrayList<>(childcount);
    }
    
    public GameData(){
        childs = new ArrayList<>();
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void readChilds(RandomAccessFile quarry,GameData[] data){
        try {
            childs = new ArrayList<>(quarry.readInt());
            for(int i=0;i<childs.size();i++){
                childs.add(data[quarry.readInt()]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeChilds(RandomAccessFile quarry){
        if(!childs.isEmpty()){
            try {
                quarry.writeInt(this.getId());
                quarry.writeInt(this.childs.size());
                for(GameData child : childs){
                    quarry.writeInt(child.getId());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /*public void setChilds(ArrayList<GameData> childs){
        this.childs = childs;
        setChilds(childs.toArray(new GameData[childs.size()]));
    }
    
    public ArrayList<GameData> getChilds(){
        return childs;
    }*/
    
    public abstract void setChilds(ArrayList<GameData> childs);
    
    public ArrayList<GameData> getChilds(){
        return childs;
    }
    
    public void setGameGraph(GameGraph graph){
        this.graph = graph;
        for(GameData child : childs){
            graph.add(child);
        }
    }
    
    protected void addChild(GameData child){
        childs.add(child);
        if(graph!=null){
            graph.add(child);
        }
    }
    
    protected void removeChild(int index){
        GameData child = childs.remove(index);
        if(graph!=null){
            graph.remove(child.getId());
        }
    }
}
