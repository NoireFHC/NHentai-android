/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */
 
package sumimakito.android.quickkv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class QKVFSReader
{
	public static String readFileBFD(String pFileAbsPath) throws IOException {
		return bfd(pFileAbsPath);
	}
	
	public static String readFileNIO(String pFileAbsPath) throws IOException{
		return nio(pFileAbsPath);
	}
	
	private static String bfd(String pFilePath) throws FileNotFoundException,IOException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(pFilePath));
		StringBuilder sb = new StringBuilder();
		String str = null;
		while((str = bufferedReader.readLine()) != null){
			sb.append(str);
		}
		return sb.toString();
	}
	
	private static String nio(String pFilePath) throws FileNotFoundException,IOException{
		RandomAccessFile file = new RandomAccessFile(pFilePath, "r");
		FileChannel fileChannel = file.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect((int) fileChannel.size());
		fileChannel.read(buffer);
		buffer.flip();
		CharBuffer charBuffer = Charset.forName("utf-8").decode(buffer);
		file.close();
		BufferedReader bufferedReader = new BufferedReader(new StringReader(charBuffer.toString()));
		StringBuilder sb = new StringBuilder();
		String str = null;
		while((str = bufferedReader.readLine()) != null){
			sb.append(str);
		}
		return sb.toString();
	}
}
