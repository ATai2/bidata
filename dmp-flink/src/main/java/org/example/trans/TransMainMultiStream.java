package org.example.trans;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.util.Collection;

public class TransMainMultiStream {
    public static void mainTest(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String inputPath = TransMainMultiStream.class.getClassLoader().getResource("sensor.txt").getPath();
        DataStream<String> stringDataSource = env.readTextFile(inputPath);
        DataStream<SensorReadings> map = stringDataSource.map((MapFunction<String, SensorReadings>) s -> {
            String[] split = s.split(",");
            SensorReadings sensorReadings = new SensorReadings();
            sensorReadings.setId(split[0]);
            sensorReadings.setTimeStamp(Long.parseLong(split[1]));
            sensorReadings.setTemperature(Double.valueOf(split[2]));
            return sensorReadings;
        });

        KeyedStream<SensorReadings, Tuple2<String, Double>> keyedStream = map
                .keyBy(new KeySelector<SensorReadings, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2 getKey(SensorReadings sensorReadings) throws Exception {
                        return new Tuple2<String, Double>(sensorReadings.id, sensorReadings.temperature);
                    }
                });
        keyedStream.window(TumblingEventTimeWindows.of(Time.minutes(1)));
        keyedStream.print();

        env.execute();


    }

    public static class SensorReadings {
        private String id;
        private long timeStamp;
        private Double temperature;


        public SensorReadings() {
        }

        public SensorReadings(String id, long timeStamp, Double temperature) {
            this.id = id;
            this.timeStamp = timeStamp;
            this.temperature = temperature;
        }

        @Override
        public String toString() {
            return "SensorReadings{" +
                    "id='" + id + '\'' +
                    ", timeStamp=" + timeStamp +
                    ", temperature=" + temperature +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
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