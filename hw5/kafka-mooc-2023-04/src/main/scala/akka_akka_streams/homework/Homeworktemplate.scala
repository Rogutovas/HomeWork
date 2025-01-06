package akka_akka_streams.homework

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, Inlet, Outlet, scaladsl}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip, ZipWith}


object Homeworktemplate {
  implicit val system = ActorSystem("fusion")
  implicit val materializer = ActorMaterializer()
  val graph =     GraphDSL.create(){ implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val input =  builder.add(Source(1 to 1000))
    val multiplier10 = builder.add(Flow[Int].map(x=>x*10))
    val multiplier2 = builder.add(Flow[Int].map(x=>x*2))
    val multiplier3 = builder.add(Flow[Int].map(x=>x*3))

    val output = builder.add(Sink.foreach(println))
    val broadcast = builder.add(Broadcast[Int](3))
    val zip = builder.add(ZipWith((a: Int, b: Int, c: Int) => a + b + c))

    input ~> broadcast
    broadcast.out(0) ~> multiplier10 ~> zip.in0
    broadcast.out(1) ~> multiplier2 ~> zip.in1
    broadcast.out(2) ~> multiplier3 ~> zip.in2

    zip.out ~> output
    ClosedShape
  }

  def main(args: Array[String]) : Unit ={
    RunnableGraph.fromGraph(graph).run()
  }
}