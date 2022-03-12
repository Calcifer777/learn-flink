package io.github.calcifer777.flink.streaming

import org.apache.flink.streaming.api.scala._

object SocketTextStreamWordCount {

  def main(args: Array[String]): Unit = {

    val flink = StreamExecutionEnvironment.getExecutionEnvironment

    val text = flink.socketTextStream("localhost", 9999)
    val counts = text
      .flatMap( _.toLowerCase.split("\\W+")
      .filter( _.nonEmpty ) )
      .map { (_, 1) }
      .keyBy(0)
      .sum(1)

    counts.print()

    flink.execute("Scala SocketTextStreamWordCount Example")
  }

}
