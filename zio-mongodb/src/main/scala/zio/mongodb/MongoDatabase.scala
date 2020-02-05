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

import com.mongodb.reactivestreams.client.{ MongoDatabase => MongoDatabaseDB }
import zio.{ Task, ZIO }
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
