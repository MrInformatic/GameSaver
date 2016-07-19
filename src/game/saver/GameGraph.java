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

import game.saver.GameData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philipp
 */
public class GameGraph {
    private LinkedList<GameData> gameData = new LinkedList<>();
    private ClassMap classMap;
    private File file;

    public GameGraph(File file,ClassMap classMap) {
        this.file = file;
        this.classMap = classMap;
        RandomAccessFile quarry = null;
        try {
            quarry = new RandomAccessFile(file, "rw");
            int length = quarry.readInt();
            for(int i=0;i<length;i++){
                int id = quarry.readInt();
                Class c = classMap.getClassbyId(quarry.readInt());
                GameData gameData1 = (GameData)c.newInstance();
                gameData1.read(quarry);
                gameData.add(id,gameData1);
            }
            GameData[] gameData1 = gameData.toArray(new GameData[gameData.size()]);
            for(int i=0;i<length;i++){
                int id = quarry.readInt();
                int length2 = quarry.readInt();
                GameData[] gameData2 = new GameData[length2];
                for(int n=0;n<length2;n++){
                    int t = quarry.readInt();
                    gameData2[n]= gameData1[t];
                }
                gameData1[id].setChilds(gameData2);
            }
            quarry.close();
        } catch (Exception ex) {
            if(quarry!=null){
                try {
                    quarry.close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
            //ex.printStackTrace();
        }
    }
    
    public GameData get(int i){
        return gameData.get(i);
    }
    
    public void add(GameData type){
        type.setId(gameData.size());
        gameData.add(type);
        try{
            for(GameData gameData1 : type.getChilds()){
                add(gameData1);
            }
        }catch(Exception e){}
    }
    
    public void set(int i,GameData type){
        type.setId(i);
        gameData.set(i, type);
    }
    
    public void remove(int i){
        GameData gameData1 = gameData.remove(i);
        try{
            for(GameData gameData2 : gameData1.getChilds()){
                remove(gameData2.getId());
            }
        }catch(Exception e){}
    }
    
    public void close(){
        try {
            RandomAccessFile quarry = new RandomAccessFile(file, "rw");
            quarry.setLength(0);
            quarry.writeInt(gameData.size());
            int i=0;
            for(GameData gameData1 : gameData){
                quarry.writeInt(i);
                quarry.writeInt(classMap.getClassId(gameData1.getClass()));
                gameData1.write(quarry);
                i++;
            }
            for(GameData gameData1 : gameData){
                GameData[] gameData3 = gameData1.getChilds();
                if(gameData3!=null&&gameData3.length>0){
                    quarry.writeInt(gameData1.getId());
                    quarry.writeInt(gameData3.length);
                    for(GameData gameData2 : gameData3){
                        quarry.writeInt(gameData2.getId());
                    }
                }
            }
            quarry.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
