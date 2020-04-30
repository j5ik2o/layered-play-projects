package warikan.domain.model

import java.time.LocalDate

import warikan.infrastructure.ulid.ULID
import warikan.domain.model.amount.{ BillingAmount, PartyPaymentTypeRatios, PaymentTypeRatio, WeightedSum }
import warikan.domain.model.member.{ MemberIds, Members }

final case class PartyId(value: ULID)

/**
  * 飲み会名。
  *
  * @param value
  */
final case class PartyName(value: String) {
  require(value.nonEmpty)
}

/**
  * 飲み会。
  *
  * @param name
  * @param date
  * @param members
  * @param partyPaymentTypeRatios
  */
final case class Party(
    name: PartyName,
    date: LocalDate,
    members: Members,
    partyPaymentTypeRatios: PartyPaymentTypeRatios
) {

  def addMembers(other: Members): Party =
    copy(members = members.combine(other))

  def removeMembers(memberIds: MemberIds): Party =
    copy(members = members.removeMembers(memberIds))

  def withPaymentTypeRatios(large: PaymentTypeRatio, medium: PaymentTypeRatio, small: PaymentTypeRatio): Party =
    withPaymentTypeRatios(PartyPaymentTypeRatios(small, medium, large))

  def withPaymentTypeRatios(value: PartyPaymentTypeRatios): Party =
    copy(partyPaymentTypeRatios = value)

  def warikan(billingAmount: BillingAmount): Warikan =
    warikan(billingAmount, partyPaymentTypeRatios)

  private def weightedSum(partyPaymentTypeRatios: PartyPaymentTypeRatios): WeightedSum = {
    val ratios = members.values.map(member => partyPaymentTypeRatios.paymentTypeRatio(member.paymentType))
    WeightedSum.zero.add(ratios.head, ratios.tail: _*)
  }

  private def warikan(
      billingAmount: BillingAmount,
      partyPaymentTypeRatios: PartyPaymentTypeRatios
  ): Warikan = {
    val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatios))
    val result = members.values.map { member =>
      val ratio = partyPaymentTypeRatios.paymentTypeRatio(member.paymentType)
      member -> paymentBaseAmount.times(ratio)
    }.toMap
    Warikan(result)
  }
}

object Party {

  def apply(name: PartyName, date: LocalDate): Party =
    new Party(name, date, Members.empty, PartyPaymentTypeRatios.default)

}
