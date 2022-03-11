# Architecture

## Ecosystem

- Deployment manager
    - Local JVM
    - Cluster Standalone / Yarn
    - Cloud
- Storage
    - Files
    - Databases
    - Streams and queues
- Engine
    - Flink's runtime
- Abastraction
    -  Batch: DataSet
    -  Batch: DataStream
- Libraries
    -  Relational: Table
    -  Graph processing: Gelly
    -  Machine Learning: Flink ML
- EDA tools
    - Zeppelin

## Flink programming model

Dataset
- immutable
- does not support operations on part of DataSet
- stores list of its dependencies