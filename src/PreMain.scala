import com.mchange.feedletter.*
import com.mchange.feedletter.style.{AllUntemplates,StyleMain}

import com.mchange.feedletter.style.Customizer

val TechMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, withinTypeId : String, feedUrl : FeedUrl, content : ItemContent ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[tech notebook] ${title} ${url}"
val MainMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, withinTypeId : String, feedUrl : FeedUrl, content : ItemContent ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[New Post] ${title} ${url}"
val DraftsMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, withinTypeId : String, feedUrl : FeedUrl, content : ItemContent ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[new draft post] ${title} ${url}"
      

object PreMain:
  def main( args : Array[String] ) : Unit =
    AllUntemplates.add( UserUntemplates )
    Customizer.MastoAnnouncement.register("tech-mastonotify", TechMastonotifyCustomizer)
    Customizer.MastoAnnouncement.register("interfluidity-main-mastonotify", MainMastonotifyCustomizer)
    Customizer.MastoAnnouncement.register("drafts-mastonotify", DraftsMastonotifyCustomizer)
    val styleExec =
      sys.env.get("FEEDLETTER_STYLE") match
        case Some( s ) => s.toBoolean
        case None      => false
    if styleExec then StyleMain.main(args) else Main.main(args)

