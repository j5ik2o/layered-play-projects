package com.github.j5ik2o.warikan.interfaceAdaptor.dao
package slick {
  import com.github.j5ik2o.warikan.interfaceAdatpor.dao.slick.SlickDaoSupport

  trait UserMessageComponent extends SlickDaoSupport {
    import profile.api._

    case class UserMessageRecord(
        messageId: Long,
        userId: String,
        status: String,
        message: String,
        createdAt: java.time.Instant,
        updatedAt: Option[java.time.Instant]
    ) extends SoftDeletableRecord

    case class UserMessages(tag: Tag)
        extends TableBase[UserMessageRecord](tag, "user_message")
        with SoftDeletableTableSupport[UserMessageRecord] {
      def messageId: Rep[Long]                      = column[Long]("message_id")
      def userId: Rep[String]                       = column[String]("user_id")
      def status: Rep[String]                       = column[String]("status")
      def message: Rep[String]                      = column[String]("message")
      def createdAt: Rep[java.time.Instant]         = column[java.time.Instant]("created_at")
      def updatedAt: Rep[Option[java.time.Instant]] = column[Option[java.time.Instant]]("updated_at")
      def pk                                        = primaryKey("pk", (messageId, userId))
      override def * =
        (messageId, userId, status, message, createdAt, updatedAt) <> (UserMessageRecord.tupled, UserMessageRecord.unapply)
    }

    object UserMessageDao extends TableQuery(UserMessages)

  }

}
