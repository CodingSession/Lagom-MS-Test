package de.ewks.funcDemo

import java.net.URI

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.client.{LagomClientApplication, StaticServiceLocatorComponents}
import play.api.libs.json._
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait RestMonitoringService extends Service {

  override def descriptor: Descriptor = {
    import Service._
    named("rest_metrics").withCalls(
      restCall(Method.GET,
        "/metrics",
        getMetrics _)
    )
  }

  def getMetrics: ServiceCall[NotUsed, Metrics]

}

case class Metrics(heap: Int)

object Metrics {
  implicit val reads: Reads[Metrics] =
    (JsPath \ "heap.used").read[Int].map(h => Metrics(h))


  implicit val format: Format[Metrics] = Format(reads, null)

}

class Rest_API(url: String) {
  private val clientApplication = new LagomClientApplication("rest-client")
    with StaticServiceLocatorComponents
    with AhcWSComponents {

    override def staticServiceUri = URI.create(url)
  }

  private val restMonitorService = clientApplication.serviceClient.implement[RestMonitoringService]

  def getResponse: Future[Option[Number]] = {
    restMonitorService.getMetrics.invoke() map { metrics =>
      //metrics.find(me => me.name.equals("heap")) map (m => m.value)
      Option.apply(metrics.heap)
    }
  }
}

