package aws.s3.models

import java.util.Date

import play.api.libs.ws._

import scala.concurrent.Future
import scala.xml._

import aws.core._
import aws.core.Types._
import aws.core.parsers.Parser

import aws.s3.S3._
import aws.s3.S3.HTTPMethods._
import aws.s3.S3Parsers._

import scala.concurrent.ExecutionContext.Implicits.global

import aws.s3.S3.Parameters.Permisions.Grantees._

case class Tag(name: String, value: String)
object Tag {
  import Http._

  def delete(bucketname: String) =
    Http.delete[Unit](Some(bucketname), subresource = Some("tagging"))

  def get(bucketname: String) =
    Http.get[Seq[Tag]](Some(bucketname), subresource = Some("tagging"))

  def create(bucketname: String, tags: Tag*) = {
    val b =
      <Tagging>
        <TagSet>
          {
            for (t <- tags) yield
            <Tag>
              <Key>{ t.name }</Key>
              <Value>{ t.value }</Value>
            </Tag>
          }
        </TagSet>
      </Tagging>
    val ps = Seq(Parameters.MD5(b.mkString))
    put[Node, Unit](Some(bucketname), body = b, subresource = Some("tagging"), parameters = ps)
  }
}