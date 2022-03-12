package io.github.calcifer777.flink.batch

import org.apache.flink.api.scala._
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.util.Collector

object CustomFunctions {
  
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val path = getClass().getResource("/words.txt").getPath()
    val ds: DataSet[String] = env.readTextFile(path)

    val out = ds
      .flatMap(Tokenize())
      .filter(StartsWith("n"))
      
    out.print()

  }

  case class Tokenize() extends FlatMapFunction[String, String] {
    def flatMap(value: String, out: Collector[String]): Unit = {
      value.split(" ").foreach(out.collect)
    }

  }

  case class StartsWith(s: String) extends FilterFunction[String] {
    def filter(value: String): Boolean = value.toLowerCase().startsWith(s)
  }
}
