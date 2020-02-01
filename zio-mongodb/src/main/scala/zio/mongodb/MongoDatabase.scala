package zio.mongodb

import com.mongodb.reactivestreams.client.{MongoDatabase => MongoDatabaseDB}
import zio.{Task, ZIO}
import zio.interop.reactiveStreams._
import zio.stream._
case class MongoDatabase(mdb: MongoDatabaseDB) {

  /**
    * Drops this database.
    *
    * @return a publisher identifying when the database has been dropped
    * @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
    */
  def drop(): Task[Unit] =
    mdb.drop().toStream().runDrain

  /**
    * Gets the name of the database.
    *
    * @return the database name
    */
  def name: Task[String] = ZIO.effect(mdb.getName)

}
