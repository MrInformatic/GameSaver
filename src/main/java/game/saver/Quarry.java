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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.RandomAccess;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrInformatic
 */
public class Quarry{
    public InputStream inputStream;
    public OutputStream outputStream;
    
    public Quarry(String file) throws FileNotFoundException {
        this(new File(file));
    }
    
    public Quarry(File file) throws FileNotFoundException {
        inputStream = new FileInputStream(file);
        outputStream = new FileOutputStream(file);
    }
    
    public void write(Seriable seriable){
        seriable.write(this);
    }
    
    public <T extends Seriable> T read(Class<T> c){
        try {
            T type = c.newInstance();
            type.read(this);
            return type;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public void writeInt(int value){
        try {
            outputStream.write(new byte[]{
                (byte)(0xff & (value >> 24)),
                (byte)(0xff & (value >> 16)),
                (byte)(0xff & (value >>  8)),
                (byte)(0xff & value)
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeLong(long value){
        try {
            outputStream.write(new byte[]{
                (byte)(0xff & (value >> 56)),
                (byte)(0xff & (value >> 48)),
                (byte)(0xff & (value >> 40)),
                (byte)(0xff & (value >> 32)),
                (byte)(0xff & (value >> 24)),
                (byte)(0xff & (value >> 16)),
                (byte)(0xff & (value >>  8)),
                (byte)(0xff & value)
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeShort(short value){
        try {
            outputStream.write(new byte[]{
                (byte)(0xff & (value >>  8)),
                (byte)(0xff & value)
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeByte(byte value){
        try {
            outputStream.write(value);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeFloat(float value){
        writeInt(Float.floatToIntBits(value));
    }
    
    public void writeDouble(double value){
        writeLong(Double.doubleToLongBits(value));
    }
    
    public void writeString(String value){
        try {
            writeInt(value.length());
            outputStream.write(value.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public byte readByte(){
        try {
            return (byte)inputStream.read();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public int readInt(){
        return (readByte() << 24) |
               (readByte() << 16) | 
               (readByte() <<  8) | 
                readByte();
    }
    
    public long readLong(){
        return (readByte() << 56) | 
               (readByte() << 48) | 
               (readByte() << 40) | 
               (readByte() << 32) | 
               (readByte() << 24) | 
               (readByte() << 16) | 
               (readByte() <<  8) | 
                readByte();
    }
    
    public short readShort(){
        return (short)((readByte() <<  8) | 
                        readByte());
    }
    
    public float readFloat(){
        return Float.intBitsToFloat(readInt());
    }
    
    public double readDouble(){
        return Double.longBitsToDouble(readLong());
    }
    
    public String readString(){
        try {
            byte[] buffer = new byte[readInt()];
            inputStream.read(buffer);
            return new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void close(){
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Quarry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
