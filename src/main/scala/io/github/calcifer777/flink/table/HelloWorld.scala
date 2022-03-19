package io.github.calcifer777.flink.table

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api._
import org.apache.flink.table.api.{ EnvironmentSettings, TableEnvironment }
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.api.{
  DataTypes,
  EnvironmentSettings,
  Schema,
  TableDescriptor
}
import org.apache.flink.connector.datagen.table.DataGenConnectorOptions
import scala.annotation.meta.field

object HelloWorld extends App {

  val settings = EnvironmentSettings
    .newInstance()
    .inStreamingMode()
    .build()
  val tableEnv = TableEnvironment.create(settings)

  // Register source table
  val sourceSchema = Schema
    .newBuilder()
    .column("field0", DataTypes.STRING())
    .build()
  val sourceTableDescriptor = TableDescriptor
    .forConnector("datagen")
    .schema(sourceSchema)
    .option[java.lang.Long](DataGenConnectorOptions.ROWS_PER_SECOND, 1.toLong)
    .build()
  tableEnv.createTemporaryTable("sourceTable", sourceTableDescriptor)

  // Register sink table
  val sinkSchema = Schema
    .newBuilder()
    .column("field0", DataTypes.STRING())
    .column("field1", DataTypes.INT())
    .build()
  val sinkTableDescriptor = TableDescriptor
    .forConnector("print")
    .schema(sinkSchema)
    .build()
  tableEnv.createTemporaryTable("sinkTable", sinkTableDescriptor)

  // Processing
  val sourceTable = tableEnv.from("sourceTable")
  val query = """|
  | SELECT field0, 1 as field1
  | FROM sourceTable
  """.stripMargin
  val pipeline: Table = tableEnv.sqlQuery(query)
  print(pipeline.explain())
  pipeline.insertInto("sinkTable")

  // Execute
  tableEnv.execute("Table HelloWorld")

}
