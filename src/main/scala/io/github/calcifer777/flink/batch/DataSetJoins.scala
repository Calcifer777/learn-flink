package io.github.calcifer777.flink.batch

import org.apache.flink.api.scala._
import org.apache.flink.api.common.operators.base.JoinOperatorBase.JoinHint

object DataSetJoins {

  def main(args: Array[String]): Unit = {

    val flink = ExecutionEnvironment.getExecutionEnvironment

    val personsPath = getClass().getResource("/persons.csv").getPath()
    val locationsPath = getClass().getResource("/locations.csv").getPath()
    val persons: DataSet[(Int, String)] = flink.readCsvFile[(Int, String)](personsPath)
    val locations: DataSet[(Int, String)] = flink.readCsvFile[(Int, String)](locationsPath)

    persons
      .join(locations, JoinHint.OPTIMIZER_CHOOSES).where(0).equalTo(0)
      .apply((p, l) => (p._1, p._2, l._2))
      .print()

    def joinFunction(l: (Int, String), r: (Int, String)): (Int, String, String) = 
      (l._1, l._2, if (r == null) "n.a." else r._2)

    persons
      .leftOuterJoin(locations).where(_._1).equalTo(_._1)
      .apply(joinFunction _)
      .print()

  }
}
