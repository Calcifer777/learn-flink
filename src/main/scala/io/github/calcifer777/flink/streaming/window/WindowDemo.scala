package io.github.calcifer777.flink.streaming.window

import org.apache.flink.api.scala._

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.util.Collector

object WindowDemo {
  def main(args: Array[String]): Unit = {

    val flink = StreamExecutionEnvironment.getExecutionEnvironment
    flink.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    val words = flink
      .socketTextStream("localhost", 9999)
      .flatMap(Tokenize())

    val commonWords = words
      .map((r: String) => (r, 1))
      .returns(Types.TUPLE[(String, Int)])
      .keyBy(0)
      .window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))
      .sum(1)
    
    commonWords.print()

    flink.execute("EventTimeDemo")

  }

  case class Tokenize() extends FlatMapFunction[String, String] {
    def flatMap(value: String, out: Collector[String]): Unit = {
      value.split(" ") foreach { out.collect }
    }
  }
}
