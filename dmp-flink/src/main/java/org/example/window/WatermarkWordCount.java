package org.example.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WatermarkWordCount {
    public static void mainTest(String[] args) throws Exception {
        StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
//    --host localhost --port 7777
//    --host 192.168.31.19 --port 7777
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


//        nc -lk 7777

        DataStreamSource<String> streamSource = env.socketTextStream(host, port);
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


