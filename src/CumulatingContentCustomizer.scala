// Note:
//   This is not complete or usable.
//   To make it usable, we'd have to complete
//
//       createCumulationItemContent( cumulation : Synthetic.UpdateCumulation )
//
//   We also have to actually extract the update announcements from the ItemContents RSS, rather than treat the whole
//   ItemContents as UpdateAnnouncements

import scala.xml.*

import com.mchange.feedletter.*
import com.mchange.conveniences.string.*

import audiofluidity.rss.{Element,Synthetic}

import java.time.ZoneId

def feedSelfLink( singleItemRssElem : Elem ) : Option[String] =
  if ((singleItemRssElem \ "channel" \ "link").filter( _.prefix == "atom") \@ "rel").equalsIgnoreCase("self") then
    ((singleItemRssElem \ "channel" \ "link").filter( _.prefix == "atom") \@ "href").toOptionNotBlank
  else
    None

def feedSelfLink( ic : ItemContent ) : Option[String] = feedSelfLink(ic.rssElemBeforeOverrides)

def createCumulationItemContent( cumulation : Synthetic.UpdateCumulation ) : ItemContent = ???

val CumulatingContentCustomizer : Customizer.Contents =
  ( _ : SubscribableName, _ : SubscriptionManager, feedUrl : FeedUrl, itemContents : Seq[ItemContent], _ : ZoneId ) =>
    val (updateAnnouncements, rest) = itemContents.partition(isUpdateAnnouncement)
    val filteredUpdateAnnouncements = removeInDigestUpdateAnnouncements(rest)(updateAnnouncements)
    filteredUpdateAnnouncements.length match
      case 0 => rest // do nothing
      case 1 => rest ++ filteredUpdateAnnouncements // append single update announcement
      case _ =>
        val sourceFeedUrl =
          val uaFeedSelfLinks = filteredUpdateAnnouncements.map( feedSelfLink ).flatten.toSet
          uaFeedSelfLinks.size match
            case 0 => feedUrl.str
            case _ => uaFeedSelfLinks.head
        // val provenance = Synthetic.UpdateCumulation.computeProvenance( sourceFeedUrl, None, updateAnnouncements )
        // val cumulation = Synthetic.UpdateCumulation.cumulate( updateAnnouncements )
        // rest :+ createCumulationItemContent(cumulation)
        ???

