package com.pyaanalytics

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.api.java.{JavaRDD, JavaSparkContext}
import org.deeplearning4j.berkeley.Pair
import org.deeplearning4j.models.embeddings.WeightLookupTable
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors
import org.deeplearning4j.models.glove.GloveWeightLookupTable
import org.deeplearning4j.models.word2vec.wordstore.VocabCache
import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer
import org.deeplearning4j.spark.models.glove.Glove
import org.deeplearning4j.spark.models.word2vec.{Word2Vec, Word2VecPerformer}

import org.deeplearning4j.util.SerializationUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File
import scopt.OptionParser
import scala.io.Source

object Word2vecdl4j {
  case class Word2vecdl4jConfig(abstractsFile: String = "",
                                modelFile: String = "",
                                sparkMaster: String = "local[64]")

  case class WVS extends WordVectorSerializer

  def main(args: Array[String]): Unit = {

    val parser = new OptionParser[Word2vecdl4jConfig]("Word2vecdl4j") {

      arg[String]("abstractsFile") valueName("abstractsFile") action {
        (x, c) => c.copy(abstractsFile = x)
      }


      arg[String]("modelFile") valueName("modelFile") action {
        (x, c) => c.copy(modelFile = x)
      }

      arg[String]("sparkMaster") valueName("sparkMaster") action {
        (x, c) => c.copy(sparkMaster = x)
      }
    }

    parser.parse(args, Word2vecdl4jConfig()) match {
      case Some(config) => {
        run(config)
      } case None => {
        System.exit(1)
      }
    }
  }
  def run(config: Word2vecdl4jConfig): Unit = {
    val conf = new SparkConf()
      .setAppName("w2vdl4j")
      .setMaster(config.sparkMaster)
      .set(SparkDl4jMultiLayer.AVERAGE_EACH_ITERATION, "false")
      .set(Word2VecPerformer.NEGATIVE, "0")
      .set("spark.akka.frameSize", "100")
      .set(Word2VecPerformer.VECTOR_LENGTH, "1000")

    println("Setting up Spark Context")

    val sc = new SparkContext(conf)
    val lines = sc.textFile(config.abstractsFile)
    val vec = new Word2Vec()
    val table = vec.train(lines)
    SerializationUtils.saveObject(table, new File(config.modelFile))
    println("Model saved")

    sc.stop()
  }
}
