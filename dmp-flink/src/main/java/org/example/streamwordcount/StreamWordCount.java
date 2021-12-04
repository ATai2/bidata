package org.example.streamwordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(8);
        String inputPath = StreamWordCount.class.getClassLoader().getResource("hello.txt").getPath();
        DataStreamSource<String> streamSource = env.readTextFile(inputPath);

//        fields: tuple位置
        DataStream<Tuple2<String, Integer>> sum = streamSource.flatMap(new MyFlatMapper())
                .keyBy(0)
                .sum(1);
        sum.print();
//      执行任务
        sum.executeAndCollect();

//        线程编号(默认并行度，物理机的cup Num) > (fieldKey, sumNum）
//        2> (sfesef,1)
//        3> (sfe,1)
//        9> (,1)
//        12> (classworlds,1)
//        10> (baseDirFound,1)
//        5> (asefs,1)
//        7> (sfeclassworlds,1)
//        10> (fesfs,1)
//        12> (classworlds,2)
//        9> (,2)
//        12> (classworlds,3)
//        7> (s,1)
//        12> (classworlds,4)
//        3> (sfe,2)
//        12> (classworlds,5)
//        3> (sfe,3)
//        4> (hello,1)
//        9> (,3)
//        5> (asefs,2)
//        9> (,4)
//        9> (,5)
//        12> (classworlds,6)
//        4> (hello,2)
//        12> (classworlds,7)
//        5> (asefs,3)


    }

    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            String[] words = s.split(" ");
            for (int i = 0; i < words.length; i++) {
                collector.collect(new Tuple2<String, Integer>(words[i], 1));
            }
        }
    }

}
