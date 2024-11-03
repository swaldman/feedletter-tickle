import scala.xml.*

import com.mchange.feedletter.*
import com.mchange.conveniences.string.*

import audiofluidity.rss.{Element,Synthetic}

import java.time.ZoneId

val UpdatesLastContentsCustomizer : Customizer.Contents =
  ( _ : SubscribableName, _ : SubscriptionManager, feedUrl : FeedUrl, itemContents : Seq[ItemContent], _ : ZoneId ) =>
    val (updateAnnouncements, rest) = itemContents.partition(isUpdateAnnouncement)
    updateAnnouncements.length match
      case 0 => itemContents // do nothing
      case _ =>
        val filteredUpdateAnnouncements = removeInDigestUpdateAnnouncements(rest)(updateAnnouncements)
        rest ++ filteredUpdateAnnouncements

