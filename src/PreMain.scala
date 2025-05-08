import com.mchange.feedletter.*
import com.mchange.feedletter.style.{AllUntemplates,StyleMain}

import java.time.ZoneId

val TechMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, feedUrl : FeedUrl, content : ItemContent, timeZone : ZoneId ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[tech notebook] ${title} ${url}"
val MainMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, feedUrl : FeedUrl, content : ItemContent, timeZone : ZoneId ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[New Post] ${title} ${url}"
val DraftsMastonotifyCustomizer : Customizer.MastoAnnouncement =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, feedUrl : FeedUrl, content : ItemContent, timeZone : ZoneId ) =>
    content.link.map: url =>
      val title = content.title.getOrElse("(untitled)")
      s"[new draft post] ${title} ${url}"
      
val TechBskynotifyCustomizer   : Customizer.BskyAnnouncement = TechMastonotifyCustomizer
val MainBskynotifyCustomizer   : Customizer.BskyAnnouncement = MainMastonotifyCustomizer
val DraftsBskynotifyCustomizer : Customizer.BskyAnnouncement = DraftsMastonotifyCustomizer

val TechSubjectCustomizer : Customizer.Subject =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, feedUrl : FeedUrl, contents : Seq[ItemContent], timeZone : ZoneId ) =>
    contents.size match
      case 1 =>
        val ic = contents.head
        val title = ic.title
        title match
          case Some(t) => s"[interfluidity-tech] New Entry: $t"
          case None    => s"[interfluidity-tech] New Untitled Entry"
      case n =>   
        s"[interfluidity-tech] $n new entries"

object PreMain:
  def main( args : Array[String] ) : Unit =
    // println("PreMain beginning.")
    
    AllUntemplates.add( UserUntemplates )

    Customizer.MastoAnnouncement.register("tech-mastonotify", TechMastonotifyCustomizer)
    Customizer.MastoAnnouncement.register("interfluidity-main-mastonotify", MainMastonotifyCustomizer)
    Customizer.MastoAnnouncement.register("drafts-mastonotify", DraftsMastonotifyCustomizer)

    Customizer.BskyAnnouncement.register("tech-bskynotify", TechBskynotifyCustomizer)
    Customizer.BskyAnnouncement.register("interfluidity-main-bskynotify", MainBskynotifyCustomizer)
    Customizer.BskyAnnouncement.register("drafts-bskynotify", DraftsBskynotifyCustomizer)

    Customizer.Subject.register("interfluidity-tech", TechSubjectCustomizer)

    Customizer.Contents.register("interfluidity-tech",            UpdatesLastContentsCustomizer)
    Customizer.Contents.register("interfluidity-weekly",          UpdatesLastContentsCustomizer)
    Customizer.Contents.register("interfluidity-longform-weekly", UpdatesLastContentsCustomizer)

    val styleExec =
      sys.env.get("FEEDLETTER_STYLE") match
        case Some( s ) => s.toBoolean
        case None      => false
    if styleExec then StyleMain.main(args) else Main.main(args)

    // println("PreMain terminating.")

