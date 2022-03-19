# Architecture

## Components

### Job Manager

- Orchestrates the application runtime
- Assigns tasks to Task Manager(s)
- Coordinates checkpoints
- Handles failure recovery

A `JobManager` consists of:
- `ResourceManager`: manages resource (de)allocation, and manages task slots
- `Dispatcher`: provides a REST interface to submit Flink applications
- `JobMaster`: manages the execution of a single Flink Job

### Workers (Task Manager)

The `Worker`s execute the tasks of a dataflow, and buffer and exchange the data streams.

Each `Worker` is a JVM process, and may execute one or more subtasks in separate threads

## Runtime modes

| Running mode        | Cluster lifecycle | Resource Isolation | Other |
| ------------------- | ----------------- | ------------------ | ----- |
| Session cluster     | A long-running cluster accepts multiple job submissions. <br> The lifetyme of the cluster is not bounded to that of each Job | TaskManager slots are allocated by the ResourceManager for each job. <br> Because all jobs are sharing the same cluster, there is some competition for cluster resources | No overhead for deploying JobManager and Workers for each Job |
| Job cluster         | The available cluster manager is used to spin up a cluster for each submitted job <br> this cluster is available to that job only | A fatal error in the JobManager only affects the one job running in that Flink Job Cluster | Flink Job Clusters are more suited to large jobs that are long-running, have high-stability requirements and are not sensitive to longer startup times |
| Application cluster | a dedicated Flink cluster that only executes jobs from one Flink Application and where the `main()` method runs on the cluster rather than the client | The ResourceManager and Dispatcher are scoped to a single Flink Application, which provides a better separation of concerns than the Flink Session Cluster | A Flink Job Cluster can be seen as a “run-on-client” alternative to Flink Application Clusters |