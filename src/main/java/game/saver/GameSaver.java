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
import game.saver.interfaces.Seriable;
import game.saver.gamemaps.GameStringMap;
import game.saver.gamemaps.GameMap;
import game.saver.gamemaps.GameShortMap;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MrInformatic
 */
public class GameSaver implements Seriable{
    protected GameGraph graph;
    protected LinkedList<Seriable> gameMaps = new LinkedList<>();
    private Quarry quarry;
    
    public GameSaver(){
        graph = new GameGraph();
    }
    
    public <K extends Seriable,T extends GameData> Map<K,T> getnewGameMap(Class<K> k,Class<T> t){
        GameMap<K,T> gameMap = new GameMap();
        gameMap.setKeyClass(k);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<String,T> getnewGameStringMap(Class<T> t){
        GameStringMap<T> gameMap = new GameStringMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Integer,T> getnewGameIntMap(Class<T> t){
        GameIntMap<T> gameMap = new GameIntMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Byte,T> getnewGameByteMap(Class<T> t){
        GameByteMap<T> gameMap = new GameByteMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Short,T> getnewGameShortMap(Class<T> t){
        GameShortMap<T> gameMap = new GameShortMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Long,T> getnewGameLongMap(Class<T> t){
        GameLongMap<T> gameMap = new GameLongMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Float,T> getnewGameFloatMap(Class<T> t){
        GameFloatMap<T> gameMap = new GameFloatMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    public <T extends GameData> Map<Double,T> getnewGameDoubleMap(Class<T> t){
        GameDoubleMap<T> gameMap = new GameDoubleMap();
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public void write(Quarry quarry){
        graph.write(quarry);
        for(Seriable gameMap : gameMaps){
            gameMap.write(quarry);
        }
    }
    
    @Override
    public void read(Quarry quarry){
        this.quarry = quarry;
        graph.read(quarry);
    }
}
