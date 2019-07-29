package de.ewks.funcDemo

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import de.ewks.funcDemo.api.FuncDemoScalaService
import play.api.libs.ws.ahc.AhcWSComponents
import play.filters.cors.CORSComponents//Import #1
import play.api.mvc.EssentialFilter//Import 2


class FuncDemoScalaLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new FuncDemoScalaApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FuncDemoScalaApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FuncDemoScalaService])
}

abstract class FuncDemoScalaApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents
    with CORSComponents {
  override val httpFilters: Seq[EssentialFilter] = Seq(corsFilter) //Step #2.b

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[FuncDemoScalaService](wire[FuncDemoScalaImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = FuncDemoScalaSerializerRegistry

  // Register the funcDemoScala persistent entity
  persistentEntityRegistry.register(wire[FuncDemoScalaEntity])
}
