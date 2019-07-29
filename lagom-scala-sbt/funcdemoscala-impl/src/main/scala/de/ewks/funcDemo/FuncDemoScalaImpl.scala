package de.ewks.funcDemo

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import de.ewks.funcDemo.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class FuncDemoScalaImpl(persistentEntityRegistry: PersistentEntityRegistry) extends FuncDemoScalaService {

  def functional() = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[FuncDemoScalaEntity]("1")
    val apis = ref.ask(All_APIs)
    val apiClients = apis.map(reply => reply.map(api => new Rest_API(api.url)))

    futureinFutures(apiClients)
    //oneFuture(apiClients)
  }

  override def storeApi(): ServiceCall[API_Information, Done] = ???

  def oneFuture(apis: Future[List[Rest_API]]): Future[(Double, Double, Double)] = {
    val responses = for {
      apiCall <- apis
      response <- Future.sequence(apiCall map (api => api.getResponse))
    } yield response

    val tuple = responses.map(response => response.foldLeft(Double.MaxValue, Double.MinValue, 0.0, 0) { case ((min, max, sum, count), e) => (math.min(min, e.get.intValue()), math.max(max, e.get.intValue()), sum + e.get.intValue(), count + 1) })

    tuple map { case (min, max, sum, count) => (min, max, sum / count) }
  }

  def futureinFutures(apis: Future[List[Rest_API]]): Future[(Double, Double, Double)] = {
    val responses = apis.map(apiList => apiList.map(api => api.getResponse))
    var tuple = responses.flatMap(response => Future.foldLeft(response)(Double.MaxValue, Double.MinValue, 0.0, 0) { case ((min, max, sum, count), e) => (math.min(min, e.get.intValue()), math.max(max, e.get.intValue()), sum + e.get.intValue(), count + 1) })


    tuple map (t => (t._1, t._2, t._3 / t._4))
  }
}

