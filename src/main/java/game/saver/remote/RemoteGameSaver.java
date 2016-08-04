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
package game.saver.remote;

import game.saver.GameData;
import game.saver.GameGraph;
import game.saver.GameSaver;
import game.saver.Quarry;
import game.saver.gamemaps.GameByteMap;
import game.saver.gamemaps.GameDoubleMap;
import game.saver.gamemaps.GameFloatMap;
import game.saver.gamemaps.GameIntMap;
import game.saver.gamemaps.GameLongMap;
import game.saver.gamemaps.GameMap;
import game.saver.gamemaps.GameShortMap;
import game.saver.gamemaps.GameStringMap;
import game.saver.interfaces.Seriable;
import game.saver.remote.gamemaps.RemoteGameByteMap;
import game.saver.remote.gamemaps.RemoteGameDoubleMap;
import game.saver.remote.gamemaps.RemoteGameFloatMap;
import game.saver.remote.gamemaps.RemoteGameIntMap;
import game.saver.remote.gamemaps.RemoteGameLongMap;
import game.saver.remote.gamemaps.RemoteGameMap;
import game.saver.remote.gamemaps.RemoteGameShortMap;
import game.saver.remote.gamemaps.RemoteGameStringMap;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MrInformatic
 */
public class RemoteGameSaver extends GameSaver implements Remoteable,Seriable{
    private RemoteClassMap remoteClassMap;
    private LinkedList<Remoteable> remotegameMaps = new LinkedList<>();
    private Quarry quarry;
    
    public RemoteGameSaver(Quarry quarry){
        this.quarry = quarry;
        remoteClassMap = new RemoteClassMap(quarry);
    }
    
    @Override
    public <K extends Seriable,T extends GameData> Map<K,T> getnewGameMap(Class<K> k,Class<T> t){
        RemoteGameMap<K,T> gameMap = new RemoteGameMap<>(quarry,remoteClassMap);
        gameMap.setKeyClass(k);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<String,T> getnewGameStringMap(Class<T> t){
        RemoteGameStringMap<T> gameMap = new RemoteGameStringMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Integer,T> getnewGameIntMap(Class<T> t){
        RemoteGameIntMap<T> gameMap = new RemoteGameIntMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Byte,T> getnewGameByteMap(Class<T> t){
        RemoteGameByteMap<T> gameMap = new RemoteGameByteMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Short,T> getnewGameShortMap(Class<T> t){
        RemoteGameShortMap<T> gameMap = new RemoteGameShortMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Long,T> getnewGameLongMap(Class<T> t){
        RemoteGameLongMap<T> gameMap = new RemoteGameLongMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Float,T> getnewGameFloatMap(Class<T> t){
        RemoteGameFloatMap<T> gameMap = new RemoteGameFloatMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }
    
    @Override
    public <T extends GameData> Map<Double,T> getnewGameDoubleMap(Class<T> t){
        RemoteGameDoubleMap<T> gameMap = new RemoteGameDoubleMap<>(quarry,remoteClassMap);
        gameMap.setGameGraph(graph);
        gameMap.read(quarry);
        gameMaps.add(gameMap);
        remotegameMaps.add(gameMap);
        return gameMap;
    }

    @Override
    public void update() {
        int id = quarry.readInt();
        if(id==0){
            remoteClassMap.update();
        }else{
            remotegameMaps.get(id-1).update();
        }
    }
}
