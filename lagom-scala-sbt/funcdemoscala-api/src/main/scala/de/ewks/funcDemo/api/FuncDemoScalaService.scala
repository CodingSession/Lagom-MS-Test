package de.ewks.funcDemo.api


import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait FuncDemoScalaService extends Service {
  def functional(): ServiceCall[NotUsed, (Double, Double, Double)]

  def storeApi(): ServiceCall[API_Information, Done]

  override final def descriptor: Descriptor = {
    import Service._
    named("funcDemoScala")
      .withCalls(
        pathCall("/scala/functional", functional _),
        pathCall("/scala/api", storeApi _)
      )
      /*
      .withTopics(
        topic(FuncdemoscalaService.TOPIC_NAME, greetingsTopic)
          // Kafka partitions messages, messages within the same partition will
          // be delivered in order, to ensure that all messages for the same user
          // go to the same partition (and hence are delivered in order with respect
          // to that user), we configure a partition key strategy that extracts the
          // name as the partition key.
          .addProperty(
          KafkaProperties.partitionKeyStrategy,
          PartitionKeyStrategy[GreetingMessageChanged](_.name)
        )
      )
      */
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class API_Information(url: String, endPointType: String)

object API_Information{
  implicit val format: Format[API_Information] = Json.format[API_Information]
}


