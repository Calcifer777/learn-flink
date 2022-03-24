# Datastream API

## Batch vs Streaming modes

The DataStream API supports different runtime execution modes from which you can choose depending on the requirements of your use case and the characteristics of your job.  There is the “classic” execution behavior of the DataStream API, which we call STREAMING execution mode.  Additionally, there is a batch-style execution mode that we call BATCH execution mode. 

By enabling BATCH execution, we allow Flink to apply additional optimizations that we can only do when we know that our input is bounded.

Boundedness is a property of a data source that tells us whether all the input coming from that source is known before execution or whether new data will show up, potentially indefinitely.

Checkpointing and any operations that depend on checkpointing, as well as iterations do not work in batch mode.

### Difference in tasks execution between batch and streaming mode

## Watermarking

Watermarking is a mechanism to measure progress of event time in Flink.

In order to work with event time, Flink needs to know the events timestamps, meaning each element in the stream needs to have its event timestamp assigned. This is usually done by accessing/extracting the timestamp from some field in the element by using a `TimestampAssigner`. For some sources (e.g. Kafka), event time is already configured in the message.

There are two places in Flink applications where a WatermarkStrategy can be used:
- directly on sources: preferred, allows watermarking optimization
- after non-source operation (`stream.assignTimestampAndWatermarks(WatermarkStrategy)`))

For idle sources, define a custom `WatermarkStrategy` to emit watermarks to flush pending buffers after a time window:
```scala
WatermarkStrategy
  .forBoundedOutOfOrderness[(Long, String)](Duration.ofSeconds(20))
  .withIdleness(Duration.ofMinutes(1))
```

### Watermarks generators

There are two different styles of watermark generation:
- periodic: observes stream events and generates watermarks periodically (possibly depending on the stream elements, or purely based on processing time)
    - Monotonously Increasing Timestamps (`WatermarkStrategy.forMonotonousTimestamps()`)
    - Fixed Amount of Lateness (`WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10))`)
- punctuated: observes the stream of events and emits a watermark whenever it sees a special element that carries watermark information

### Watermarking a Kafka source

With Kafka-partition-aware watermark generation, watermarks are generated inside the Kafka consumer, per Kafka partition, and the per-partition watermarks are merged in the same way as watermarks are merged on stream shuffles.

Each consumer instance tracks the maximum timestamp within each partition, and take as its watermark the minimum of these maximums, less the configured bounded out-of-orderness. Idle partitions will be ignored, if you configure it to do so.

```scala
val kafkaSource = new FlinkKafkaConsumer[MyType]("myTopic", schema, props)
kafkaSource.assignTimestampsAndWatermarks(
  WatermarkStrategy
    .forBoundedOutOfOrderness(Duration.ofSeconds(20)))

val stream: DataStream[MyType] = env.addSource(kafkaSource)
```

## State and fault tolerance