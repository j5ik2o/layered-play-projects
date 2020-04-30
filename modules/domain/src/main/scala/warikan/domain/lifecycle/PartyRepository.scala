package warikan.domain.lifecycle

import warikan.domain.model.{ Party, PartyId }

trait PartyRepository[M[_]] {

  def resolveById(id: PartyId): M[Option[Party]]

  def store(party: Party): M[Unit]

  def deleteById(id: PartyId): M[Unit]

}
