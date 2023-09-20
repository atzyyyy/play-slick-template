package models.domain

import java.util.UUID

case class Home (id: UUID, name: String)

object Home {
    val tupled = (apply: (UUID, String) => Home).tupled
    def apply(name: String): Home = apply(UUID.randomUUID(), name)
}