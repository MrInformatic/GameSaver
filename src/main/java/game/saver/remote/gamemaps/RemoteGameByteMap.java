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
package game.saver.remote.gamemaps;

import game.saver.GameData;
import game.saver.Quarry;
import game.saver.gamemaps.GameByteMap;
import game.saver.gamemaps.GameIntMap;
import game.saver.interfaces.Seriable;
import game.saver.remote.RemoteClassMap;
import game.saver.remote.Remoteable;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MrInformatic
 */
public class RemoteGameByteMap<T extends GameData> extends GameByteMap<T> implements Remoteable{
    private int id;
    private Quarry quarry;
    private RemoteClassMap remoteClassMap;
    
    public RemoteGameByteMap(Quarry quarry,RemoteClassMap remoteClassMap){
        this.quarry = quarry;
        this.remoteClassMap = remoteClassMap;
    }
    
    @Override
    public T put(Byte key, T value) {
        LinkedList<GameData> gameDatas = getUnaddedGameData(value);
        T type = super.put(key,value);
        sendPut(key, value, gameDatas);
        return type;
    }
    
    @Override
    public T remove(Object key) {
        quarry.writeByte((byte)1);
        quarry.writeByte((Byte)key);
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends Byte, ? extends T> m) {
        LinkedList<GameData>[] gameDatases = new LinkedList[m.size()];
        int i=0;
        for(T ms : m.values()){
            gameDatases[i] = getUnaddedGameData(ms);
            i++;
        }
        super.putAll(m);
        i=0;
        for(Map.Entry<? extends Byte, ? extends T> ms : m.entrySet()){
            sendPut(ms.getKey(),ms.getValue(),gameDatases[i]);
            i++;
        }
    }

    @Override
    public void clear() {
        quarry.writeByte((byte)2);
        super.clear();
    }
    
    private void sendPut(Byte key,T value,LinkedList<GameData> unaddedGameData){
        for(GameData gameData : unaddedGameData){
            remoteClassMap.addClass(gameData.getClass());
        }
        quarry.writeInt(id);
        quarry.writeByte((byte)0);
        quarry.writeByte(key);
        quarry.writeInt(unaddedGameData.size());
        for(GameData gameData : unaddedGameData){
            quarry.writeInt(gameData.getId());
            quarry.writeInt(remoteClassMap.getClassId(gameData.getClass()));
            quarry.write(gameData);
        }
        for(GameData gameData : unaddedGameData){
            gameData.writeChilds(quarry);
        }
        quarry.writeInt(-1);
    }
    
    private LinkedList<GameData> getUnaddedGameData(GameData gameData){
        LinkedList<GameData> result = new LinkedList<>();
        getUnaddedGameData(result,gameData);
        return result;
    }
    
    private LinkedList<GameData> getUnaddedGameData(LinkedList<GameData> list,GameData gameData){
        if(gameData.getId()==-1){
            list.add(gameData);
        }
        for(GameData gameData1 : gameData.getChilds()){
            getUnaddedGameData(list,gameData1);
        }
        return list;
    }
    
    @Override
    public void update() {
        try {
            switch(quarry.readByte()){
                case 0:
                    byte key = quarry.readByte();
                    LinkedList<GameData> gameDatas = new LinkedList<>();
                    int length = quarry.readInt();
                    for(int i=0;i<length;i++){
                        int id = quarry.readInt();
                        T value = (T)quarry.read(remoteClassMap.getClassbyId(quarry.readInt()));
                        gameDatas.add(value);
                        graph.set(id,value);
                    }
                    for(GameData gameData : gameDatas){
                        gameData.readChilds(quarry,graph);
                    }
                    break;
                case 1:
                    remove(quarry.readByte());
                    break;
                case 2:
                    clear();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setId(int id){
        this.id = id;
    }
}
