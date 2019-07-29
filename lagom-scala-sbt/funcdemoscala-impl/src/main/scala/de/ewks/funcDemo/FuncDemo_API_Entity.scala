package de.ewks.funcDemo

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import de.ewks.funcDemo.api.API_Information
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq


class FuncDemoScalaEntity extends PersistentEntity {
  override type Command = FuncDemoScalaCommand[_]
  override type Event = FuncDemoScalaEvent
  override type State = FuncDemoScalaState

  override def initialState: FuncDemoScalaState = FuncDemoScalaState(List(API_Information("http://192.168.2.113:8081", "rest")))


  override def behavior: Behavior = {
    case FuncDemoScalaState(api) => Actions().onCommand[Add_API_Message, Done] {

      case (Add_API_Message(newApi), ctx, state) =>
        ctx.thenPersist(
          API_Added_Event(newApi)
        ) {
          _ =>
            ctx.reply(Done)
        }

    }.onReadOnlyCommand[All_APIs.type, List[API_Information]] {
      case (All_APIs, ctx, state) =>

        ctx.reply(state.apis)
    }.onEvent {
      case (API_Added_Event(new_api), state) =>
        FuncDemoScalaState(new_api :: state.apis)

    }
  }
}

case class FuncDemoScalaState(apis: List[API_Information])

object FuncDemoScalaState {
  implicit val format: Format[FuncDemoScalaState] = Json.format
}

sealed trait FuncDemoScalaEvent extends AggregateEvent[FuncDemoScalaEvent] {
  def aggregateTag = FuncDemoScalaEvent.Tag
}

object FuncDemoScalaEvent {
  val Tag = AggregateEventTag[FuncDemoScalaEvent]
}

/**
  * An event that represents the adding of an API
  */
case class API_Added_Event(api: API_Information) extends FuncDemoScalaEvent

object API_Added_Event {

  /**
    * Format for the greeting message changed event.
    *
    * Events get stored and loaded from the database, hence a JSON format
    * needs to be declared so that they can be serialized and deserialized.
    */
  implicit val format: Format[API_Added_Event] = Json.format
}

/**
  * This interface defines all the commands that the HelloWorld entity supports.
  */
sealed trait FuncDemoScalaCommand[R] extends ReplyType[R]

/**
  * A command to add API.
  *
  * It has a reply type of [[Done]], which is sent back to the caller
  * when all the events emitted by this command are successfully persisted.
  */
case class Add_API_Message(api: API_Information) extends FuncDemoScalaCommand[Done]

object Add_API_Message {
  implicit val format: Format[Add_API_Message] = Json.format

}


/**
  */
case object All_APIs extends FuncDemoScalaCommand[List[API_Information]]


/**
  * Akka serialization, used by both persistence and remoting, needs to have
  * serializers registered for every type serialized or deserialized. While it's
  * possible to use any serializer you want for Akka messages, out of the box
  * Lagom provides support for JSON, via this registry abstraction.
  *
  * The serializers are registered here, and then provided to Lagom in the
  * application loader.
  */
object FuncDemoScalaSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[API_Added_Event],
    JsonSerializer[Add_API_Message],
    JsonSerializer[FuncDemoScalaState]
  )
}