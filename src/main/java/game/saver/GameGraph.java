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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class GameGraph implements Graphable<GameData>{
    private HashMap<Integer,GameData> gameData = new HashMap<>();
    private ClassMap classMap;
    private File file;
    private IdDispenser idDispenser = new IdDispenser();

    public GameGraph(File file,ClassMap classMap) {
        this.file = file;
        this.classMap = classMap;
        Quarry quarry = null;
        try {
            quarry = new Quarry(file, "rw");
            idDispenser.read(quarry);
            int length = quarry.readInt();
            GameData[] gameData1 = new GameData[idDispenser.getlargestId()];
            for(int i=0;i<length;i++){
                int id = quarry.readInt();
                Class c = classMap.getClassbyId(quarry.readInt());
                GameData gameData2 = (GameData)c.newInstance();
                gameData2.read(quarry);
                gameData.put(id,gameData2);
                gameData1[id] = gameData2;
            }
            for(int i=0;i<length;i++){
                gameData1[quarry.readInt()].readChilds(quarry,gameData1);
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
    
    @Override
    public GameData get(int i){
        return gameData.get(i);
    }
    
    @Override
    public void add(GameData type){
        type.setGameGraph(this);
        if(type.getId()<0){
            int id = idDispenser.next();
            type.setId(id);
            gameData.put(id,type);
            
        }
    }
    
    @Override
    public void remove(int i){
        GameData gameData1 = gameData.remove(i);
        idDispenser.add(i);
        try{
            for(GameData gameData2 : gameData1.getChilds()){
                remove(gameData2.getId());
            }
        }catch(Exception e){}
    }
    
    public void flush(){
        try {
            Quarry quarry = new Quarry(file, "rw");
            quarry.setLength(0);
            idDispenser.write(quarry);
            quarry.writeInt(gameData.size());
            for(Map.Entry<Integer,GameData> gameData1 : gameData.entrySet()){
                quarry.writeInt(gameData1.getKey());
                quarry.writeInt(classMap.getClassId(gameData1.getValue().getClass()));
                gameData1.getValue().write(quarry);
            }
            for(GameData gameData1 : gameData.values()){
                gameData1.writeChilds(quarry);
            }
            quarry.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public class IdDispenser implements Iterator<Integer>,Seriable{
        private int nextId = 0;
        private LinkedList<Integer> ids = new LinkedList<>();
        
        public int getlargestId(){
            return nextId;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            if(ids.isEmpty()){
                return nextId++;
            }else{
                return ids.poll();
            }
        }
        
        public void add(int id){
            ids.add(id);
        }

        @Override
        public void write(Quarry quarry) {
            try {
                quarry.writeInt(nextId);
                quarry.writeInt(ids.size());
                for(Integer i : ids){
                    quarry.writeInt(i);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void read(Quarry quarry) {
            try {
                nextId = quarry.readInt();
                int length = quarry.readInt();
                for(int i=0;i<length;i++){
                    ids.add(quarry.readInt());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
