package io.github.calcifer777.flink.streaming

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.api.common.functions._

object DataStreamOps {

  case class Transaction(
    date: String,
    month: String,
    cat: String,
    item: String,
    value: Int,
  )

  case class Number(value: Long, counter: Int)

  def main(args: Array[String]): Unit = {
    val flink = StreamExecutionEnvironment.getExecutionEnvironment()

    /*
    val path = getClass().getResource("/sales.csv").getPath()
    val sales = flink.readTextFile(path)
      .map(_.split(","))
      .map(r => Transaction.apply(r(0), r(1), r(2), r(3), r(4).toInt))

    // Reduce: avg transaction per month
    val monthlySales = sales
      .map(r => (r.month, r.value, 1))
      .returns(Types.TUPLE[(String, Int, Int)])  // provide TypeInformation explicitly
      .keyBy(0)
      .reduce( (l, r) => (l._1, l._2+r._2, l._3+r._3))
      .map((r: (String, Int, Int)) => (r._1, r._2.toDouble / r._3))
      .returns(Types.TUPLE[(String, Double)])  // provide TypeInformation explicitly
    monthlySales.print()
    */

    // Fold: accumulator and input types in reduce operation are different

    // Min, MinBy: min returns the minimum value, whereas minBy returns the
    // element that has the minimum value in this field

    // Split: split incoming stream into multiple streams (deprecated, use filter)
    // https://stackoverflow.com/a/57186264/7702100


    // Iterate: applies the same operation to a DS until a condition is met
    val nums = flink
      .fromSequence(0, 5)
      .map(Number(_, 0))
      .returns(Types.CASE_CLASS[Number])

    nums.print()

    flink.execute()
  
  }

}