package io.github.calcifer777.flink.table

import org.apache.flink.streaming.api.scala._

import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.sources.CsvTableSource
import org.apache.flink.table.api.DataTypes
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.api.common.RuntimeExecutionMode

object BatchDemo extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setRuntimeMode(RuntimeExecutionMode.BATCH)
  val tableEnv = StreamTableEnvironment.create(env)

  val path = getClass().getResource("/sales.csv").getPath()
  val source = CsvTableSource
    .builder()
    .path(path)
    .fieldDelimiter(",")
    .field("date", DataTypes.DATE())
    .field("month", DataTypes.STRING())
    .field("category", DataTypes.STRING())
    .field("product", DataTypes.STRING())
    .field("amount", DataTypes.INT())
    .build()
  val t = tableEnv.fromTableSource(source)
  tableEnv.registerTable("t", t)

  val q = """|
             | SELECT product, SUM(amount) as total_amount
             | FROM t
             | WHERE category = 'Category2'
             | GROUP BY product
             |""".stripMargin
  val pipeline = tableEnv.sqlQuery(q)
  pipeline.printSchema()
  tableEnv
    .toDataStream(pipeline)
    .print()

  env.execute("Batch demo")
}
