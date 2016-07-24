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

import game.saver.interfaces.Flushable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class ClassMap implements Flushable{
    private int nextid = 0;
    private HashMap<Integer,Class> idtoclass = new HashMap<>();
    private HashMap<Class,Integer> classtoid = new HashMap<>();
    private File file;
    
    public ClassMap(File file){
        this.file = file;
        RandomAccessFile quarry = null;
        try {
            quarry = new RandomAccessFile(file, "rw");
            nextid = quarry.readInt();
            while(true){
                int id = quarry.readInt();
                byte[] buffer = new byte[quarry.readInt()];
                quarry.read(buffer);
                Class c = Class.forName(new String(buffer));
                idtoclass.put(id,c);
                classtoid.put(c,id);
            }
        } catch (Exception ex) {
            if(quarry!=null){
                try {
                    quarry.close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
        
    }
    
    public int getClassId(Class c){
        if(!classtoid.containsKey(c)){
            classtoid.put(c,nextid);
            idtoclass.put(nextid,c);
            return nextid++;
        }
        return classtoid.get(c);
    }
    
    public Class getClassbyId(int id){
        return idtoclass.get(id);
    }
    
    @Override
    public void flush(){
        try {
            RandomAccessFile quarry = new RandomAccessFile(file, "rw");
            quarry.setLength(0);
            quarry.writeInt(nextid);
            int i=0;
            for(Map.Entry<Integer,Class> classes : idtoclass.entrySet()){
                quarry.writeInt(classes.getKey());
                quarry.writeInt(classes.getValue().getName().length());
                quarry.write(classes.getValue().getName().getBytes());
            }
            quarry.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
