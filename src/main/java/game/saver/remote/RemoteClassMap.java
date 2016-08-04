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

import game.saver.ClassMap;
import game.saver.Quarry;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class RemoteClassMap extends ClassMap implements Remoteable{
    private Quarry quarry;
    
    public RemoteClassMap(Quarry quarry){
        this.quarry = quarry;
    }
    
    @Override
    public void addClass(Class c){
        if(!classtoid.containsKey(c)){
            if(quarry.writeable()){
                quarry.writeInt(0);
                quarry.writeInt(nextid);
                quarry.writeString(c.getName());
            }
            classtoid.put(c,nextid);
            idtoclass.put(nextid,c);
            nextid++;
        }
    }
    
    @Override
    public void clear(){
        super.clear();
        if(quarry.writeable()){
            quarry.writeInt(0);
            quarry.writeInt(-1);
        }
    }
    
    @Override
    public void update(){
        try {
            if(quarry.readable()){
                int id = quarry.readInt();
                if(id==-1){
                    clear();
                }else{
                    Class c = Class.forName(quarry.readString());
                    idtoclass.put(id, c);
                    classtoid.put(c, id);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
