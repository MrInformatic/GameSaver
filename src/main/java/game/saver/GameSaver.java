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

import game.saver.gamemaps.GameByteMap;
import game.saver.gamemaps.GameDoubleMap;
import game.saver.gamemaps.GameFloatMap;
import game.saver.gamemaps.GameIntMap;
import game.saver.gamemaps.GameLongMap;
import game.saver.interfaces.Flushable;
import game.saver.interfaces.Seriable;
import game.saver.gamemaps.GameStringMap;
import game.saver.gamemaps.GameMap;
import game.saver.gamemaps.GameShortMap;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MrInformatic
 */
public class GameSaver implements Flushable{
    private GameGraph graph;
    private ClassMap classMap;
    private LinkedList<Flushable> gameMaps = new LinkedList<>();
    private File file;
    
    public GameSaver(File file){
        this.file = file;
        file.mkdirs();
        classMap = new ClassMap(new File(file,"class.dat"));
        graph = new GameGraph(new File(file,"data.dat"), classMap);
    }
    
    public <K extends Seriable,T extends GameData> Map<K,T> getnewGameMap(String name,Class<K> k,Class<T> t){
        GameMap<K,T> gameMap = new GameMap(graph, classMap, new File(file,name+".dat"), k);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<String,T> getnewGameStringMap(String name,Class<T> t){
        GameStringMap<T> gameMap = new GameStringMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Integer,T> getnewGameIntMap(String name,Class<T> t){
        GameIntMap<T> gameMap = new GameIntMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Byte,T> getnewGameByteMap(String name,Class<T> t){
        GameByteMap<T> gameMap = new GameByteMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Short,T> getnewGameShortMap(String name,Class<T> t){
        GameShortMap<T> gameMap = new GameShortMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Long,T> getnewGameLongMap(String name,Class<T> t){
        GameLongMap<T> gameMap = new GameLongMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Float,T> getnewGameFloatMap(String name,Class<T> t){
        GameFloatMap<T> gameMap = new GameFloatMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Double,T> getnewGameDoubleMap(String name,Class<T> t){
        GameDoubleMap<T> gameMap = new GameDoubleMap(graph, classMap, new File(file,name+".dat"));
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public void flush(){
        graph.flush();
        classMap.flush();
        for(Flushable gameMap : gameMaps){
            gameMap.flush();
        }
    }
}
