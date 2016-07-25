/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.saver.gamemaps;

import game.saver.ClassMap;
import game.saver.GameData;
import game.saver.GameGraph;
import game.saver.Quarry;
import game.saver.interfaces.Flushable;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author MrInformatic
 */
public class GameShortMap<T extends GameData> implements Map<Short,T>,Flushable{
    private HashMap<Short,T> values = new HashMap<>();
    private GameGraph graph;
    private ClassMap classMap;
    private File file;
    
    public GameShortMap(GameGraph graph,ClassMap classMap,File file){
        super();
        this.file = file;
        this.classMap = classMap;
        this.graph = graph;
        if(file.exists()){
            try {
                Quarry quarry = new Quarry(file, "rw");
                int lenght = quarry.readInt();
                for(int i=0;i<lenght;i++){
                    values.put(quarry.readShort(),(T)graph.get(quarry.readInt()));
                }
            } catch (Exception ex) {

            }  
        }
    }
    
    public void flush(){
        try {
            Quarry quarry = new Quarry(file, "rw");
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
