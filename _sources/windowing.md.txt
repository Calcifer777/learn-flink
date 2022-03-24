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

## Triggers

A trigger determines when a window is ready to be processed by a window function.

All window assigners have a default trigger.

### Built in trigger

- `EventTimeTrigger`: fires based on progress of event time
- `ProcessingTimeTrigger`: fires based on progress of processing time
- `CountTrigger`: fires when the number of elements in a indo exceeds the count specified in parameters
- `PurgeTrigger`: takes another trigger as argument and purges it after the inner one fires

## Evictors

Evictor is used to remove elements from a window after the trigger fires and before and/or after the window function is applied.

### Window lifecycle

1. Window creation
2. Trigger
3. Evictor (optional)
4. Window function
5. Evictor (optional)
6. Result

### Methods

- `evictBefore`
- `evictAfter`

### Built-in evictors

| Evictor        | Description                                                                                                                                                    | Example                                                  |
| -------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| `CountEvictor` | Keeps the user-specified number of elements from the window and discards the remaining ones                                                                    | `.evictor(CountEvictor.of(3))`                           |
| `DeltaEvictor` | Computes a delta between the last element in the window and the remaining elements, then removes the elements whose delta is greater or equal to the threshold | `.evictor(DeltaEvictor.of(treshold, new MyDeltaFunction` |
| `TimeEvictor`  | finds the `max_ts` of the records in a window and remove all records with timestamp less than `max_ts - interval`                                              | `.evictor(TimeEvictor.of(Time.of(3 TimeUnit.SECODDS)))`  |

## How to

When using event-time windows, you need to extract the timestmap information from the records
with an `AscendingTimestampExtractor`.

When using global windows, you need to specify a `trigger` for
triggering each window computation.
