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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class GameMap<K extends EntryPoint,T extends GameData> extends HashMap<K,T>{
    private HashMap<K,T> values = new HashMap<>();
    private GameGraph graph;
    private ClassMap classMap;
    private File file;
    
    public GameMap(GameGraph graph,ClassMap classMap,File file,Class<K> c){
        super();
        this.file = file;
        this.classMap = classMap;
        this.graph = graph;
        try {
            RandomAccessFile quarry = new RandomAccessFile(file, "rw");
            while(true){
                K key = c.newInstance();
                key.read(quarry);
                values.put(key,(T)graph.get(quarry.readInt()));
            }
        } catch (Exception ex) {
            
        }        
    }
    
    public void flush(){
        try {
            RandomAccessFile quarry = new RandomAccessFile(file, "rw");
            for(Map.Entry<K,T> value : values.entrySet()){
                value.getKey().write(quarry);
                quarry.writeInt(value.getValue().getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
