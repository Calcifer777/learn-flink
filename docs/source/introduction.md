# Introduction

Apache Flink is an open source stream processing framework for distributed,
high-performing, always available and data streaming applications

It als provides support for batch, graph, and iterative processing.

Flink's application state is re-scalable, and it is possible to add resourcs while 
an app is running.

Also, it maintains exactly once semantics.

## FLink vs Spark

|                          | Spark                                | Flink                                    |
| ------------------------ | ------------------------------------ | ---------------------------------------- |
| Starts as                | Starts as batch processing framework | Starts as streaming processing framework |
| Engine approach          | microbatching                        | windowing and checkpointing              |
| Language                 | Scala                                | Java                                     |
| Built-in memory manager  | No                                   | Yes                                      |
| Execution plan framework | DAG                                  | Controlled cyclic graph                  |