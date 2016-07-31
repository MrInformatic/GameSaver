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
package game.saver.gamemaps;

import game.saver.ClassMap;
import game.saver.interfaces.Flushable;
import game.saver.GameData;
import game.saver.GameGraph;
import game.saver.Quarry;
import game.saver.interfaces.Seriable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class GameMap<K extends Seriable,T extends GameData> implements Map<K,T>,Flushable{
    private HashMap<K,T> values = new HashMap<>();
    private GameGraph graph;
    private ClassMap classMap;
    private File file;
    
    public GameMap(GameGraph graph,ClassMap classMap,File file,Class<K> c){
        super();
        this.file = file;
        this.classMap = classMap;
        this.graph = graph;
        if(file.exists()){
            try {
                Quarry quarry = new Quarry(file);
                int length = quarry.readInt();
                for(int i=0;i<length;i++){
                    K key = c.newInstance();
                    key.read(quarry);
                    values.put(key,(T)graph.get(quarry.readInt()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }   
        }
    }
    
    @Override
    public void flush(){
        try {
            Quarry quarry = new Quarry(file);
            quarry.writeInt(values.size());
            for(Map.Entry<K,T> value : values.entrySet()){
                value.getKey().write(quarry);
                quarry.writeInt(value.getValue().getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return values.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return values.get(key);
    }

    @Override
    public T put(K key, T value) {
        graph.add(value);
        return values.put(key, value);
    }

    @Override
    public T remove(Object key) {
        T type = values.remove(key);
        graph.remove(type.getId());
        return type;
    }

    @Override
    public void putAll(Map<? extends K, ? extends T> m) {
        for(T type : m.values()){
            graph.add(type);
        }
        values.putAll(m);
    }

    @Override
    public void clear() {
        for(T type : values()){
            graph.remove(type.getId());
        }
        values.clear();
    }

    @Override
    public Set<K> keySet() {
        return values.keySet();
    }

    @Override
    public Collection<T> values() {
        return values.values();
    }

    @Override
    public Set<Entry<K, T>> entrySet() {
        return values.entrySet();
    }
}
