#!/bin/sh

echo '1:{"name":"marco"}' | ~/kafka_2.13-3.1.0/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic names --property "parse.key=true" --property "key.separator=:"