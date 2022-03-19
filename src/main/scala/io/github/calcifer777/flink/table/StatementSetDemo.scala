package io.github.calcifer777.flink.table

import com.typesafe.scalalogging.LazyLogging

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.sources.CsvTableSource
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.api.common.RuntimeExecutionMode

object StatementSetDemo extends App with LazyLogging {

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setRuntimeMode(RuntimeExecutionMode.BATCH)
  val tableEnv = StreamTableEnvironment.create(env)

  // val path = getClass().getResource("/sales.csv").getPath()
  val path = "/home/calcifer/git/marco/learn-flink/src/main/resources/sales.csv" // uncomment when running in cluster mode
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

  // Console Sink
  val consoleSinkSchema = Schema
    .newBuilder()
    .column("product", DataTypes.STRING())
    .column("total_amount", DataTypes.INT())
    .build()
  val consoleSinkDescriptor = TableDescriptor
    .forConnector("print")
    .schema(consoleSinkSchema)
    .build()

  // CSV Sink
  val csvSinkDescriptor = TableDescriptor
    .forConnector("filesystem")
    .format("csv")
    .option("path", "/tmp/flink-demo-smtmset/")
    .build()

  // Pipeline
  val stmtSet = tableEnv.createStatementSet()
  val q = """|
             | SELECT product, SUM(amount) as total_amount
             | FROM t
             | WHERE category = 'Category2'
             | GROUP BY product
             |""".stripMargin
  val result: Table = tableEnv.sqlQuery(q)

  stmtSet.addInsert(consoleSinkDescriptor, result)
  stmtSet.addInsert(csvSinkDescriptor, result)

  stmtSet.execute()

}