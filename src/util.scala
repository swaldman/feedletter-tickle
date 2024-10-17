import scala.xml.*

import com.mchange.feedletter.*
import com.mchange.conveniences.string.*

import audiofluidity.rss.{Element,Synthetic}

def isUpdateAnnouncement( singleItemRssElem : Elem ) : Boolean =
  val synthTypeOrEmpty = (singleItemRssElem \ "channel" \ "item" \ "synthetic" \ "type").text
  Element.Iffy.Synthetic.KnownType.lenientParse( synthTypeOrEmpty ) == Some( Element.Iffy.Synthetic.KnownType.UpdateAnnouncement )

def isUpdateAnnouncement( ic : ItemContent ) : Boolean = isUpdateAnnouncement(ic.rssElemBeforeOverrides)

def itemLinks( rssElem : Elem ) : Seq[String] =
  (rssElem \ "channel" \ "item" \ "link").filter( _.prefix == null ).map( _.text.toOptionNotBlank ).flatten

def updateAnnouncementsInitialLinks( rssElem : Elem ) : Seq[String] =
  (rssElem \ "channel" \ "item" \ "synthetic" \ "update" \ "initial" \ "link").map( _.text.toOptionNotBlank ).flatten

def removeInDigestUpdateAnnouncements(nonupdates : Seq[ItemContent])( updateAnnouncements : Seq[ItemContent]) : Seq[ItemContent] =
  val linksInDigest = nonupdates.map(ic => itemLinks(ic.rssElemBeforeOverrides)).flatten.toSet
  updateAnnouncements.filter: uaic =>
    val initialLinks = updateAnnouncementsInitialLinks( uaic.rssElemBeforeOverrides )
    !initialLinks.exists( linksInDigest.contains )



