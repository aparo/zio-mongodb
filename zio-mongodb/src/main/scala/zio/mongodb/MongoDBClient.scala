package zio.mongodb

import com.mongodb.reactivestreams.client.{
  MongoClients,
  MongoDatabase => MongoDatabaseDB
}
import zio._
import zio.interop.reactiveStreams._
import zio.stream.Stream

case class MongoDBClient(connectionString: String) {
  private lazy val _client =
    Managed.make(ZIO.effect(MongoClients.create(connectionString)))(client =>
      ZIO.effectTotal(client.close()))

  def getDatabase(name: String): Task[MongoDatabase] = {
    _client.use { client =>
      ZIO.effect(client.getDatabase(name))
    }
  }

  /**
    * Get a list of the database names
    *
    * @mongodb.driver.manual reference/commands/listDatabases List Databases
    * @return an iterable containing all the names of all the databases
    */
  def listDatabaseNames: Stream[Throwable, String] = {
    Stream.unwrap(
      _client.use { client =>
        ZIO.effect(client.listDatabaseNames().toStream(500))
      }
    )
  }

  def listDatabaseNamesAsList: Task[List[String]] = {
    _client.use { client =>
      client.listDatabaseNames().toStream(500).runCollect
    }
  }

}
