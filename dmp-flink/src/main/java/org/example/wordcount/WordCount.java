//package org.example.wordcount;
//
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.api.java.ExecutionEnvironment;
//import org.apache.flink.api.java.operators.AggregateOperator;
//import org.apache.flink.api.java.operators.DataSource;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.util.Collector;
//
//public class WordCount {
//    public static void mainTest(String[] args) throws Exception {
//        ExecutionEnvironment env=ExecutionEnvironment.getExecutionEnvironment();
//        String inputPath = WordCount.class.getClassLoader().getResource("hello.txt").getPath();
//        DataSource<String> stringDataSource = env.readTextFile(inputPath);
//
//        AggregateOperator<Tuple2<String, Integer>> sum = stringDataSource.flatMap(new MyFlatMapper())
//                .groupBy(0).sum(1);
//
//        sum.print();
//
//    }
//
//    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {
//
//        @Override
//        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
//            String[] words = s.split(" ");
//            for (int i = 0; i < words.length; i++) {
//                collector.collect(new Tuple2<String, Integer>(words[i], 1));
//            }
//        }
//    }
//
//}
