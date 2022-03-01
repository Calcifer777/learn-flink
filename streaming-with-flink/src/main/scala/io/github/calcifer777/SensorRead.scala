package io.github.calcifer777

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

object ToyApp {
  
  val lorem = """|
  |Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
  |incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis
  |nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
  |Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore
  |eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt
  |in culpa qui officia deserunt mollit anim id est laborum.
  |""".stripMargin('|').mkString("")

  def atLeastOne(t: (String, Int)): Boolean = t._2 > 1

  case class Word(s: String, c: Int)

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val text = env.fromElements(lorem)

    val counts = text
      .flatMap { _.toLowerCase.split("\\W+") }
      .map { Word.apply(_, 1) }
      .groupBy("s")
      .sum("c")
      .filter( w => w.c > 1)
    counts.print()
      
    val counts2 = text
      .flatMap { _.toLowerCase.split("\\W+") }
      .map { (_, 1) }
      .groupBy(0)
      .sum(1)
      .filter( _ match { case (s: String, c: Int) => c > 1})
    counts2.print()

  }
}
