# Datastream API

## Batch vs Streaming modes

The DataStream API supports different runtime execution modes from which you can choose depending on the requirements of your use case and the characteristics of your job.  There is the “classic” execution behavior of the DataStream API, which we call STREAMING execution mode.  Additionally, there is a batch-style execution mode that we call BATCH execution mode. 

By enabling BATCH execution, we allow Flink to apply additional optimizations that we can only do when we know that our input is bounded.

Boundedness is a property of a data source that tells us whether all the input coming from that source is known before execution or whether new data will show up, potentially indefinitely.

Checkpointing and any operations that depend on checkpointing, as well as iterations do not work in batch mode.

## Difference in tasks execution