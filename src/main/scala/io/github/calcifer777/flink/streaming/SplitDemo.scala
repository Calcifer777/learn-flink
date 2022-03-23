package io.github.calcifer777.flink.streaming

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

object SplitDemo extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val ds = env.fromSequence(0, 50)

  val outputTag = OutputTag[Long]("even")

  val out = ds process { 
    new ProcessFunction[Long, Long] {
      override def processElement(
          value: Long, 
          ctx: ProcessFunction[Long, Long]#Context, 
          out: Collector[Long]
      ): Unit = {
        out.collect(value)
        if (value % 2 == 0) {
          ctx.output(outputTag, value)
        }
      }
    }
  }

  val even = out.getSideOutput(outputTag)
  even.print()

  env.execute()

}
