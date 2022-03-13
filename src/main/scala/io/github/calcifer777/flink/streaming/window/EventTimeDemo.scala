package io.github.calcifer777.flink.streaming.window

import org.apache.flink.api.scala._

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows

object EventTimeDemo {
  def main(args: Array[String]): Unit = {

    val flink = StreamExecutionEnvironment.getExecutionEnvironment
    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val words = flink
      .socketTextStream("localhost", 9999)
      .flatMap(TokenizeWithTs())

    val commonWords = words
      .map((r: (Long, String)) => (r._1, r._2, 1))
      .returns(Types.TUPLE[(Long, String, Int)])
      .assignTimestampsAndWatermarks(ExtractTimestamp())
      .keyBy(1)
      // .window(TumblingEventTimeWindows.of(Time.seconds(2)))
      .window(SlidingEventTimeWindows.of(Time.seconds(5), Time.seconds(2)))
      .sum(2)
    
    commonWords.print()

    flink.execute("EventTimeDemo")

  }

  case class TokenizeWithTs() extends FlatMapFunction[String, (Long, String)] {
    def flatMap(value: String, out: Collector[(Long, String)]): Unit = {
      value.split(" ")
        .map((s: String) => (System.currentTimeMillis(), s))
        .foreach(out.collect)
    }
  }

  case class ExtractTimestamp() extends AscendingTimestampExtractor[(Long, String, Int)] {
    def extractAscendingTimestamp(element: (Long, String, Int)): Long = element._1
  }
}
