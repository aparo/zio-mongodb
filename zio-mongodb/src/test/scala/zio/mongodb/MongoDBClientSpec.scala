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

import org.scalatest._
import zio.DefaultRuntime

class MongoDBClientSpec extends FunSuite with Matchers with MongoEmbedDatabase {

  implicit val dbPort: Int = 22000

  val runtime = new DefaultRuntime {}

  test("client activities") {

    withEmbedMongoFixture(dbPort) { mongodProps =>
      val client = MongoDBClient(s"mongodb://localhost:$dbPort")
      val names = runtime.unsafeRun(client.listDatabaseNamesAsList)
      names.length should be(3L)
      names should be(List("admin", "config", "local"))
    }
  }
}
