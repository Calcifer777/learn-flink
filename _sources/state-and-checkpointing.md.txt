# State and checkpointing

Use cases:
- search for certain event patterns happened so far
- train ML model over a stream of data points
- manage historic data, allowing efficient acces to past events
- achieve fault-tolerance through checkpointing. A state helps in restarting the system from the failure point
- Dynamic rescaling of jobs
- Stateful transformations

## Checkpoints

A checkpoint marks a specific point in each of the input streams along with the corresponding state for each of the operators. 

In case of a program failure (due to machine-, network-, or software failure), Flink stops the distributed streaming dataflow. The system then restarts the operators and resets them to the latest successful checkpoint. The input streams are reset to the point of the state snapshot. Any records that are processed as part of the restarted parallel dataflow are guaranteed to not have affected the previously checkpointed state.

Flink's algorithm for taking the application checkpoints is called asynchronous barrier snapshotting (see [here](resources.html#streaming-concepts)).

## Barriers

These barriers are special records injected into the data stream by Flink under the hood. 

A barrier separates the records in the data stream into the set of records that goes into the current snapshot, and the records that go into the next snapshot. Multiple barriers from different snapshots can be in the stream at the same time, which means that various snapshots may happen concurrently.

When an intermediate operator has received a barrier for snapshot n from all of its input streams, it emits a barrier for snapshot n into all of its outgoing streams. Once a sink operator (the end of a streaming DAG) has received the barrier n from all of its input streams, it acknowledges that snapshot n to the checkpoint coordinator. After all sinks have acknowledged a snapshot, it is considered completed.

## State Backend

When operators contain any form of state, this state must be part of the snapshots as well. Because the state of a snapshot may be large, it is stored in a configurable state backend.

### State backed types
- Memory (default): stores the state's data internally as objects on the Java heap
- Filesystem: stores the state's data internally into the configured filesystem (e.g. HDFS)
- RockDB: stores the in-flight data into RockDB. Upon checkpointing the same database is written to a filesystem.

## Checkpointing types

### Aligned vs Unaligned

- Aligned checkpointing
    - operators wait for the barrier of each input datastream before forwarding it to the next step of the job graph.
    - all input buffers with records in the checkpoint must be processed when the barrier is forwarded
- Unaligned checkpointing:
    - Operators react on the first barrier that is stored in its input buffers. They immediately forwards the barrier to their downstream operator by adding it to the end of the output buffers.
    - The operator marks all overtaken records to be stored asynchronously and creates a snapshot of its own state
    - the state of the operator now includes also the state of its buffers

### Synchronous vs Asynchronous

- Synchronous: Flink operators will stop processing new records while a checkpoint is being written
- Asynchronous: Flink operators will not stop processing new records while the checkpoint is being written. Asynchronous checkpointing requires a *managed state* and a state backend that supports this feature (e.g. RockDB)

### Full vs Incremental

With incremental checkpointing, Flink nodes not write the full state for every checkpoint for consecutive states; it only save the state difference between two consecutive checkpoints.

## Savepoints

Savepoints are manually triggered checkpoints, which take a snapshot of the program and write it out to a state backend. They rely on the regular checkpointing mechanism for this.

Savepoints are similar to checkpoints except that they are triggered by the user and don’t automatically expire when newer checkpoints are completed.

### State types

- Operator state: state is bound to one operator instance; there is one state per operator instance
- Keyed state: one state per oprrator instance per each key
- Managed state: controlled by Flink, saved in hash tables
- Raw state: controlled by the operators, in their own data structures