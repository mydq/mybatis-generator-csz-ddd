package org.multiplexing.streams;

import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @Author: csz
 * @Date: 2018/10/19 15:34
 */
public class SplitUtil {

    public static String formatLogLine(String line){
        try {
            String s = line.split("]")[2];
            String delete = StringUtils.delete(s, "[");
            return StringUtils.trimAllWhitespace(delete);
        }catch (Exception e){
            throw new LineFormatException("this line is format error"+ line);
        }
    }

    public static void main(String[] args) throws Exception{
        Stream<String> lines = Files.lines(Paths.get("D:\\a.txt"), Charset.forName("gbk"));
        lines.map(line->{
            try {
                return SplitUtil.formatLogLine(line);
            }catch (LineFormatException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }).forEach(System.out::println);


//        String test = "[13016][13028][16:58:50.532]G.AuthAgent: is arm(default) architecture ";
//        String s = formatLogLine(test);
//        System.out.println(s);

    }


}
