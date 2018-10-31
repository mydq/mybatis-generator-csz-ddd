package org.multiplexing.streams;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: csz
 * @Date: 2018/10/25 20:12
 */
public class Test03 {


    public static void main(String[] args) {
        while (true) {
            List<Long> list = new ArrayList<>(10);
            Test03 test03 = new Test03();
            for (int i = 0; i < 10; i++) {
                long compute = test03.compute(() -> {
                    try {
                        new Test03().run1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
                list.add(compute);
            }
            list.forEach(System.out::println);
        }
    }


    public long compute(Supplier supplier) {
        long start = System.currentTimeMillis();
        supplier.get();
        long end = System.currentTimeMillis();
        return end - start;
    }

    public void run1() throws Exception {
        Stream<String> lines = Files.lines(Paths.get("D:\\a.txt"), Charset.forName("gbk"));
        StreamForker.Results results = new StreamForker<String>(lines, true).fork("count", s -> s.flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .count()).fork("collect", s -> s.flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .limit(10).collect(Collectors.toList())).fork("order", s -> s.map(line -> {
            try {
                return SplitUtil.formatLogLine(line);
            } catch (LineFormatException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(StringUtils::isNotBlank).collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .entrySet().stream().sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue).reversed()).limit(3))
                .fork("11", s -> s.flatMap(line -> Arrays.stream(line.split(" ")))
                        .distinct()
                        .count()).fork("22", s -> s.flatMap(line -> Arrays.stream(line.split(" ")))
                        .distinct()
                        .count()).getResults();

        long count = results.get("count");
        List<String> collect = results.get("collect");
        Stream<Map.Entry<String, Integer>> order = results.get("order");

        System.out.println("count====" + count);
        System.out.println("collect.size ===" + collect.size());
        order.forEach(entry -> System.out.println(entry.getKey() + "=====" + entry.getValue()));
    }

    public void run2() throws Exception {
        long count = Files.lines(Paths.get("D:\\a.txt"), Charset.forName("gbk")).parallel().flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .count();
        System.out.println("count====" + count);

        List<String> collect = Files.lines(Paths.get("D:\\a.txt"), Charset.forName("gbk")).parallel().flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .limit(10).collect(Collectors.toList());
        System.out.println("collect.size ===" + collect.size());

        Files.lines(Paths.get("D:\\a.txt"), Charset.forName("gbk")).parallel().map(line -> {
            try {
                return SplitUtil.formatLogLine(line);
            } catch (LineFormatException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(StringUtils::isNotBlank).collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .entrySet().stream().sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue).reversed()).limit(3).forEach(entry -> System.out.println(entry.getKey() + "=====" + entry.getValue()));
    }
}
