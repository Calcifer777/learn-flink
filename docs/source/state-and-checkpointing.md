# State and checkpointing

Use cases:
- search for certain event patterns happened so far
- train ML model over a stream of data points
- manage historic data, allowing efficient acces to past events
- achieve fault-tolerance through checkpointing. A state helps in restarting the system from the failure point
- Dynamic rescaling of jobs
- Stateful transformations

## Fault tolerance and Checkpointing

Fault tolerance in Flink ensures that in case of failures the application state will be recovered fully and the application will be restarted exactly from the failure point.

Checkpointing is to consistently draw snapshots of distributed data stream and corresponding operator state. Each drawn snapshot will hold a full application state until the checkpointed time. After the snapshot is taken, it is saved into a persistent storage, the *state backend*

Snapshots (Checkpoints) are ligts weight and do not impact much on performance.

## Algorithm

Algorithm name: synchronous barrier snapshotting

### Barriers

### State Backend

State backend determines how and where a checkpointed state is stored.

State backed types:
- Memory: stores the state's data internally as objects on the Java heap
- Filesystem: stores the state's data internally into the configured filesystem (e.g. HDFS)
- RockDB: stores the in-flight data into RockDB. Upon checkpointing the same database is written to a filesystem.

### Checkpointing types

#### Synchronous vs Asynchronous

- Synchronous: Flink operators will stop processing new records while a checkpoint is being written
- Asynchronous: Flink operators will not stop processing new records while the checkpoint is being written. Asynchronous checkpointing requires a *managed state* and a state backend that supports this feature (e.g. RockDB)

### Full vs incremental

With incremental checkpointing, Flink odes not write the full state for every checkpoint for consecutive states; it only save the state difference between two consecutive checkpoints.

### State types

- Operator state: state is bound to one operator instance; there is one state per operator instance
- Keyed state: one state per oeprator instance per each key

- Managed state: controlled by Flink, saved in hash tables
- Raw state: controlled by the operators, in their own data structures

