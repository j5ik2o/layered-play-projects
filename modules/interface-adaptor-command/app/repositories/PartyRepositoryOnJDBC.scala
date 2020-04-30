package repositories

import warikan.domain.lifecycle.PartyRepository
import warikan.domain.model.{ Party, PartyId }

import scala.concurrent.Future

class PartyRepositoryOnJDBC extends PartyRepository[Future] {
  override def resolveById(id: PartyId): Future[Option[Party]] = ???

  override def store(party: Party): Future[Unit] = ???

  override def deleteById(id: PartyId): Future[Unit] = ???
}
