/*
 * Copyright 2020 Alberto Paro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.mongodb

import com.mongodb.reactivestreams.client.{ MongoClients, MongoDatabase => MongoDatabaseDB }
import zio._
import zio.interop.reactiveStreams._
import zio.stream.Stream

case class MongoDBClient(connectionString: String) {
  private lazy val _client =
    Managed.make(ZIO.effect(MongoClients.create(connectionString)))(client => ZIO.effectTotal(client.close()))

  def getDatabase(name: String): Task[MongoDatabase] =
    _client.use { client =>
      ZIO.effect(MongoDatabase(client.getDatabase(name)))
    }

  /**
   * Get a list of the database names
   *
   * @mongodb.driver.manual reference/commands/listDatabases List Databases
   * @return an iterable containing all the names of all the databases
   */
  def listDatabaseNames: Stream[Throwable, String] =
    Stream.unwrap(
      _client.use { client =>
        ZIO.effect(client.listDatabaseNames().toStream(500))
      }
    )

  def listDatabaseNamesAsList: Task[List[String]] =
    _client.use { client =>
      client.listDatabaseNames().toStream(500).runCollect
    }

}
