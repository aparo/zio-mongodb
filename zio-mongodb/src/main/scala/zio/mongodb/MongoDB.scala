package zio.mongodb

trait MongoDB {
  val mongoDB: MongoDB.Service[Any]
}

object MongoDB {
  trait Service[R] {}
}
