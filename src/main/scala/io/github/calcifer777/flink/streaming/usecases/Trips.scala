package io.github.calcifer777.flink.streaming.usecases

/*
Data is of the following schema:
# cab id, cab number plate, cab type, cab driver name, ongoing trip/not, pickup location, destination,passenger count

Using Datastream/Dataset transformations find the following for each ongoing trip.
1.) Popular destination.  | Where more number of people reach.
2.) Average number of passengers from each pickup location.
| average =  total no. of passengers from a location / no. of trips from that location.
3.) Average number of passengers for each driver. 
| average =  total no. of passengers drivers has picked / total no. of trips he made
*/

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.typeutils.Types

object Trips {
  
  def main(args: Array[String]): Unit = {

    val path = getClass().getResource("/trips.txt").getPath()

    val flink = StreamExecutionEnvironment.getExecutionEnvironment()

    val trips: SingleOutputStreamOperator[Trip] = flink
      .readTextFile(path)
      .map(ParseTrip())
    // trips.print()

    /*
    // Most popular destinations
    trips
      .filter((t: Trip) => t.destination.isDefined)
      .map((t: Trip) => (t.destination.get, 1))
      .returns(Types.TUPLE[(String, Int)])
      .keyBy(0)
      .sum(1)
      .print()
    */

    /*
    // Average number of passengers
    trips
      .filter((t: Trip) => t.start.isDefined && t.passengerCount.isDefined)
      .map((t: Trip) => (t.start.get, t.passengerCount.get, 1))
      .returns(Types.TUPLE[(String, Int, Int)])
      .keyBy(0)
      .reduce((l, r) => (l._1, l._2 + r._2, l._3 + r._3))
      .map( (t: (String, Int, Int)) => (t._1, t._2.toDouble / t._3))
      .returns(Types.TUPLE[(String, Double)])
      .map( (t: (String, Double)) => (t._1, f"${t._2}%.2f"))
      .returns(Types.TUPLE[(String, String)])
      .print()
    */

    /*
    // Average number of passengers for each driver
    trips
      .filter((t: Trip) => t.ongoingTrip && t.passengerCount.isDefined)
      .map((t: Trip) => (t.driverName, t.passengerCount.get, 1))
      .returns(Types.TUPLE[(String, Int, Int)])
      .keyBy(0)
      .reduce((l, r) => (l._1, l._2 + r._2, l._3 + r._3))
      .map( (t: (String, Int, Int)) => (t._1, t._2.toDouble / t._3))
      .returns(Types.TUPLE[(String, Double)])
      .map( (t: (String, Double)) => (t._1, f"${t._2}%.2f"))
      .returns(Types.TUPLE[(String, String)])
      .print()
    */

    flink.execute()

  }

  case class ParseTrip() extends MapFunction[String, Trip] {
    def map(value: String): Trip = {
      val fields = value.split(",")
      Trip(
        fields(0), 
        fields(1), 
        fields(2), 
        fields(3), 
        (fields(4) == "yes"), 
        if (fields(5) == "'null'") None else Some(fields(5)), 
        if (fields(6) == "'null'") None else Some(fields(6)), 
        if (fields(7) == "'null'") None else Some(fields(7).toInt), 
      )
    }
  }

  case class Trip(
    cabId: String, 
    cabNumberPlate: String, 
    cabType: String, 
    driverName: String, 
    ongoingTrip: Boolean,
    start: Option[String], 
    destination: Option[String], 
    passengerCount: Option[Int]
  )

}
