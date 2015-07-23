package com.aohas.library.util;

import java.io.*;
import java.util.List;

public class ArrayListCopyUtil {
	public static List copyBySerialize(List src) throws IOException,ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        List dest = (List)in.readObject();
        return dest;
	}
}
