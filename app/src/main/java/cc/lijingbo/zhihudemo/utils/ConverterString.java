package cc.lijingbo.zhihudemo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConverterString {

    public static InputStream String2InputStream(String name) {
        return new ByteArrayInputStream(name.getBytes());
    }

    public static String InputStream2String(InputStream input, String encode) throws IOException {
        StringBuilder builder = new StringBuilder();

        InputStreamReader reader = new InputStreamReader(input, encode);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return builder.toString();
    }

    public static String InputStream2String2(InputStream input) throws IOException {
        StringBuffer buffer = new StringBuffer();
        byte[] b = new byte[1024];
        int n;
        try {
            while ((n = input.read(b)) != -1) {
                buffer.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return buffer.toString();
    }

    public static String InputStream2String3(InputStream input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int i;
        try {
            while ((i = input.read()) != -1) {
                outputStream.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return outputStream.toString();
    }
}
