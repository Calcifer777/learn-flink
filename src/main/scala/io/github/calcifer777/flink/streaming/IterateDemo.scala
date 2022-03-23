package io.github.calcifer777.flink.streaming

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.api.common.typeinfo.TypeInformation

object IterateDemo extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val ds = env.fromSequence(0, 50).mapWith{case (l) => (l, 0)}

  val out = ds.iterate(loop _, 5000L)

  out.print()

  env.execute()

  def loop(ds: DataStream[(Long, Int)]): (DataStream[(Long, Int)], DataStream[(Long, Int)]) = {
    val inc = ds mapWith { case (v, cnt) => (v+1, cnt+1)}
    val large = inc filterWith { case (v, _) => v >= 42 }
    val small = inc filterWith { case (v, _) => v < 42 }
    (small, large)
  }
}
