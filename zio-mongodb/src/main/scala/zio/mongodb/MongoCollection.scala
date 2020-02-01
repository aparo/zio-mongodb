package zio.mongodb

import com.mongodb.reactivestreams.client.{MongoCollection => MgCollection}

case class MongoCollection[T](internal: MgCollection[T]) {}
