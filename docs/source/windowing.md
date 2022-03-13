# Windowing

## Window types

| Type     | Based on        | Overlapping | Fixed size |
| -------- | --------------- | ----------- | ---------- |
| Tumbling | Time            | No          | Yes        |
| Sliding  | Time            | Yes         | Yes        |
| Session  | Stream activity | No          | No         |
| Global   | Triggers        | n.d.        | n.d.       |
| Custom   |                 |             |            |

## Stream time characteristics

- Processing time
    - system time of the machine that executes the task
    - best performance and no latency
    - less suitable for distributed environment
- Event time
    - time at which the event occurred at source
    - embedded in each record
    - provides consistent and deterministic results regardless of order
    - shows latency while waiting for out-of-order records
- Ingestion time
    - each record gets the source's current timestamp
    - cannot handle out-of-order events or late data

## How to

When using event-time windows, you need to extract the timestmap information from the records
with an `AscendingTimestampExtractor`.

When using global windows, you need to specify a `trigger` for
triggering each window computation.
