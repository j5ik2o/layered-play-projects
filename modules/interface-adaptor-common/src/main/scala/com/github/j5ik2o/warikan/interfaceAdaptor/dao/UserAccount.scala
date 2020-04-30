package com.github.j5ik2o.warikan.interfaceAdaptor.dao
package slick {
  import com.github.j5ik2o.warikan.interfaceAdatpor.dao.slick.SlickDaoSupport

  trait UserAccountComponent extends SlickDaoSupport {
    import profile.api._

    case class UserAccountRecord(
        id: String,
        status: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        createdAt: java.time.Instant,
        updatedAt: Option[java.time.Instant]
    ) extends SoftDeletableRecord

    case class UserAccounts(tag: Tag)
        extends TableBase[UserAccountRecord](tag, "user_account")
        with SoftDeletableTableSupport[UserAccountRecord] {
      def id: Rep[String]                           = column[String]("id")
      def status: Rep[String]                       = column[String]("status")
      def email: Rep[String]                        = column[String]("email")
      def password: Rep[String]                     = column[String]("password")
      def firstName: Rep[String]                    = column[String]("first_name")
      def lastName: Rep[String]                     = column[String]("last_name")
      def createdAt: Rep[java.time.Instant]         = column[java.time.Instant]("created_at")
      def updatedAt: Rep[Option[java.time.Instant]] = column[Option[java.time.Instant]]("updated_at")
      def pk                                        = primaryKey("pk", (id))
      override def * =
        (id, status, email, password, firstName, lastName, createdAt, updatedAt) <> (UserAccountRecord.tupled, UserAccountRecord.unapply)
    }

    object UserAccountDao extends TableQuery(UserAccounts)

  }

}
