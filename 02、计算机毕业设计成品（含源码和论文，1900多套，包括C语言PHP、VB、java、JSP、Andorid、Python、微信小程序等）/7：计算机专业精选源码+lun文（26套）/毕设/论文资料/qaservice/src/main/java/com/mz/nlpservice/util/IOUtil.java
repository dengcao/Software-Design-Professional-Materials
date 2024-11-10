package com.mz.nlpservice.util;


import java.io.*;
import java.util.UUID;

/**
 * Created by meizu on 2016/7/19.
 */
public class IOUtil {

    public static String stream2String(InputStream in, String charset) throws UnsupportedEncodingException {
        char[] data = new char[1024];
        if (charset == null || charset.equals(""))
            charset = "utf-8";
        InputStreamReader ir = new InputStreamReader(in, charset);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        try {
            while ((count = ir.read(data)) > 0) {
                sb.append(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static String getFilePath(InputStream ins) {
        UUID uuid = UUID.randomUUID();
        java.lang.String file = "./tmp/" + uuid.toString();

        try {
            inputstreamtofile(ins, new File(file));
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void inputstreamtofile(InputStream ins,File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    public static void  main(String args[]) throws UnsupportedEncodingException {
        String dd = "老师";
        InputStream is = new ByteArrayInputStream(dd.getBytes("utf-8"));
        System.out.println(stream2String(is, "utf-8"));
    }
}
