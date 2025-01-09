package akka_akka_streams.homework

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith}
import akka.stream.{ActorMaterializer, ClosedShape, Materializer}
import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor

object  ConsumerApp extends App{
  implicit val system: ActorSystem = ActorSystem("consumer-sys")
  implicit val mat: Materializer  = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  LoggerFactory
    .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    .asInstanceOf[Logger]
    .setLevel(Level.ERROR)

  val config = ConfigFactory.load()
  val consumerConfig = config.getConfig("akka.kafka.consumer")
  val consumerSettings = ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)

  val source: Source[ConsumerRecord[String, String], Consumer.Control] = Consumer.plainSource(consumerSettings, Subscriptions.topics("test"))


  val graph =     GraphDSL.create(){ implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val getVal = builder.add(Flow[ConsumerRecord[String, String]]
      .map(x=>x.value().toIntOption.getOrElse(0)))
    val multiplier10 = builder.add(Flow[Int].map(x=>x*10))
    val multiplier2 = builder.add(Flow[Int].map(x=>x*2))
    val multiplier3 = builder.add(Flow[Int].map(x=>x*3))

    val output = builder.add(Sink.foreach(println))
    val broadcast = builder.add(Broadcast[Int](3))
    val zip = builder.add(ZipWith((a: Int, b: Int, c: Int) => a + b + c))

    source ~> getVal ~> broadcast.in
    broadcast.out(0) ~> multiplier10 ~> zip.in0
    broadcast.out(1) ~> multiplier2 ~> zip.in1
    broadcast.out(2) ~> multiplier3 ~> zip.in2

    zip.out ~> output
    ClosedShape
  }

  RunnableGraph.fromGraph(graph).run()
}