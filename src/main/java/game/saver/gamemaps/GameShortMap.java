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
import game.saver.GameData;
import game.saver.GameGraph;
import game.saver.Quarry;
import game.saver.interfaces.Seriable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author MrInformatic
 */
public class GameShortMap<T extends GameData> implements Map<Short,T>,Seriable{
    private HashMap<Short,T> values = new HashMap<>();
    protected GameGraph graph;
    
    public void setGameGraph(GameGraph graph){
        this.graph = graph;
    }
    
    @Override
    public void read(Quarry quarry){
        int lenght = quarry.readInt();
        for(int i=0;i<lenght;i++){
            values.put(quarry.readShort(),(T)graph.get(quarry.readInt()));
        }
    }
    
    @Override
    public void write(Quarry quarry){
        try {
            quarry.writeInt(values.size());
            for(Map.Entry<Short,T> value : values.entrySet()){
                quarry.writeShort(value.getKey());
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
    public T put(Short key, T value) {
        graph.add(value);
        return values.put(key,value);
    }

    @Override
    public T remove(Object key) {
        T type = values.remove(key);
        graph.remove(type.getId());
        return type;
    }

    @Override
    public void putAll(Map<? extends Short, ? extends T> m) {
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
    public Set<Short> keySet() {
        return values.keySet();
    }

    @Override
    public Collection<T> values() {
        return values.values();
    }

    @Override
    public Set<Map.Entry<Short, T>> entrySet() {
        return values.entrySet();
    }
}
