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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MrInformatic
 */
public class GameSaver {
    private GameGraph graph;
    private ClassMap classMap;
    private LinkedList<GameMap> gameMaps = new LinkedList<>();
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
    
    public void flush(){
        classMap.flush();
        graph.flush();
        for(GameMap gameMap : gameMaps){
            gameMap.flush();
        }
    }
}
