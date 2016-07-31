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

import game.saver.interfaces.Seriable;
import game.saver.interfaces.Writeable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class ClassMap implements Seriable{
    private int nextid = 0;
    private HashMap<Integer,Class> idtoclass = new HashMap<>();
    private HashMap<Class,Integer> classtoid = new HashMap<>();
    
    public ClassMap(){
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
    public void write(Quarry quarry){
        try {
            quarry.writeInt(nextid);
            quarry.writeInt(idtoclass.size());
            int i=0;
            for(Map.Entry<Integer,Class> classes : idtoclass.entrySet()){
                quarry.writeInt(classes.getKey());
                quarry.writeString(classes.getValue().getName());
            }
            quarry.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void read(Quarry quarry) {
        try {
            nextid = quarry.readInt();
            int length = quarry.readInt();
            for(int i=0;i<length;i++){
                int id = quarry.readInt();
                Class c = Class.forName(quarry.readString());
                idtoclass.put(id,c);
                classtoid.put(c,id);
            }
            quarry.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
