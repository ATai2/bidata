package org.example.trans;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class TransMain {
    public static void mainTest(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String inputPath = TransMain.class.getClassLoader().getResource("hello.txt").getPath();
        DataStream<String> stringDataSource = env.readTextFile(inputPath);
        SingleOutputStreamOperator<SensorReadings> map = stringDataSource.map(new MapFunction<String, SensorReadings>() {
            @Override
            public SensorReadings map(String s) throws Exception {
                String[] split = s.split(",");
                SensorReadings sensorReadings = new SensorReadings();
                sensorReadings.setId(split[0]);
                sensorReadings.setTimeStamp(Long.parseLong(split[1]));
                sensorReadings.setTemperature(Double.valueOf(split[2]));
                return sensorReadings;
            }
        });

//        map.keyBy(new KeySelector<String, SensorReadings>() {
//            @Override
//            public SensorReadings getKey(String s) throws Exception {
//                return null;
//            }
//        });
//        AggregateOperator<Tuple2<String, Integer>> sum = stringDataSource.flatMap(new MyFlatMapper())
//                .groupBy(0).sum(1);
//        KeyedStream<String, SensorReadings> keyedStream = stringDataSource.keyBy(1);

        KeyedStream<SensorReadings, Tuple> sensorReadingsTupleKeyedStream = map.keyBy(1);
        SingleOutputStreamOperator<SensorReadings> reduce = sensorReadingsTupleKeyedStream.reduce(new ReduceFunction<SensorReadings>() {
            @Override
            public SensorReadings reduce(SensorReadings sensorReadings, SensorReadings t1) throws Exception {
                return null;
            }
        });
//        sum.print();

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