import scala.xml.*
import com.mchange.feedletter.ItemContent
import com.mchange.feedletter.style.ComposeInfo

def itemProvenance( item : Elem ) : Option[String] =
  ( item \ "provenance" )
    .flatMap( _ \ "link" )
    .filter( _ \@ "rel" == "via" )
    .map( _ \@ "href" )
    .headOption

def itemContentProvenance( itemContent : ItemContent ) : Option[String] = itemProvenance( (itemContent.rssElem \\ "item").head.asInstanceOf[Elem] )

val LongformSomeHrefs : Set[Option[String]] =
  Set("https://www.interfluidity.com/feed","https://drafts.interfluidity.com/feed/index.rss","https://tech.interfluidity.com/feed/index.rss").map(Some.apply)

def interfluidityLongformMicro( multiple : ComposeInfo.Multiple ) : (Seq[ItemContent],Seq[ItemContent]) =
  multiple.contentsSeq.partition( ic => LongformSomeHrefs( itemContentProvenance(ic) ) )

