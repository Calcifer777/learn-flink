package io.github.calcifer777.flink.batch

import org.apache.flink.streaming.api.scala._

object SocketTextStreamWordCount {

  def main(args: Array[String]): Unit = {

    val hostName = "localhost"
    val port = 9999

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream(hostName, port)
    val counts = text
      .flatMap( _.toLowerCase.split("\\W+")
      .filter( _.nonEmpty ) )
      .map { (_, 1) }
      .keyBy(0)
      .sum(1)

    counts.print()

    env.execute("Scala SocketTextStreamWordCount Example")
  }

}
