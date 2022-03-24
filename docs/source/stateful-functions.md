# Stateful functions

## Overview

Stateful Functions provides a framework for building event driven applications. Here, we explain important aspects of Stateful Function’s architecture.

Components:
- Event ingress
- Stateful functions
- Persisted states
- Event egress

## Advantages

[From SO](https://stackoverflow.com/a/68501834/7702100):
- Automated handling of connections, batching, back-pressuring, and retries. Even if you are using a single python function and no state, Stateful Functions has been heavily optimized to be as fast and efficient as possible with continual improvements from the community that you will get to leverage for free. StateFun has more sophisticated back pressuring and retry mechanisms in place than AsyncIO that you would need to redevelop on your own.
- Higher level APIs. StateFuns Python SDK (and others) provide well defined, typed apis that are easy to develop against. The other team you are working with will only require a few lines of glue code to integrate with StateFun while the project will handle the transport protocols for you.
- State! As the name of the project implies, stateful functions are well stateful. Python functions can maintain state and you will get Flink's exactly once guarantees out of the box.

## Resources

- [Hands-on Stateful Serverless Applications with Kubernetes and Stateful Functions](https://www.youtube.com/watch?v=iBZLcfrHZ80)