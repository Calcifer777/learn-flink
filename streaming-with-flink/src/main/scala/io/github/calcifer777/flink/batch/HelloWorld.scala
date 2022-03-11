package io.github.calcifer777.flink.batch

import scala.io.Source
import org.apache.flink.api.scala._

object HelloWorld {

  def main(args: Array[String]): Unit = {
    val path = getClass().getResource("/words.txt").getPath()

    val env = ExecutionEnvironment.getExecutionEnvironment
    
    val ds: DataSet[String] = env.readTextFile(path)

    val lengths = ds
      .flatMap(_.split(" "))
      .map(r => (r.length(), 1))
      .groupBy(0)
      .sum(1)
    
    lengths.print()

  }  

}
