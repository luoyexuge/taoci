package com.iclick.word2vec
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
import org.apache.spark.mllib.feature.{ Word2Vec, Word2VecModel }

import org.apache.spark.mllib.linalg.{ SparseVector => SV }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

object Word2VecTest {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val sc = new SparkContext("local", "mysql")
    val sqlContext = new SQLContext(sc)
    val path = "D:\\SPARKCONFALL\\Spark机器学习数据\\20news-bydate-train\\*"
    val rdd = sc.wholeTextFiles(path).cache()
    val xxx = rdd.map {
      case (file, text) => file.split("/").takeRight(2).head
    }.map(n => (n, 1)).reduceByKey(_ + _).collect().sortBy(_._2).mkString("\n")
    println("文章主题的数目")
    println(xxx)
    val newsgroups = rdd.map { case (file, text) => file.split("/").takeRight(2).head }
    println("分词数目")
    val text = rdd.map { case (file, text) => text }

    val whieteSpaceSplit = text.flatMap { t => t.split(" ") }.map(_.toLowerCase())
    println(whieteSpaceSplit.distinct().count)
    println(whieteSpaceSplit.sample(true, 0.3, 42).take(100).mkString(","))

    println("改进分词")
    val nonWordSplit = text.flatMap(t => t.split("""\W+""").map(_.toLowerCase))
    println(nonWordSplit.distinct.count)
    println(nonWordSplit.distinct.sample(true, 0.3, 42).take(100).mkString(","))
    val regex = """[^0-9]*""".r
    val filterNumbers = nonWordSplit.filter(token => regex.pattern.matcher(token).matches)
    println(filterNumbers.distinct.count)
    println(filterNumbers.distinct.sample(true, 0.3, 42).take(100).mkString(","))

    println("移除停用词")
    val tokenCounts = filterNumbers.map(t => (t, 1)).reduceByKey(_ + _)
    val oreringDesc = Ordering.by[(String, Int), Int](_._2)
    //println(tokenCounts.top(20)(oreringDesc).mkString("\n"))

    val stopwords = Set(
      "the", "a", "an", "of", "or", "in", "for", "by", "on", "but", "is", "not", "with", "as", "was", "if",
      "they", "are", "this", "and", "it", "have", "from", "at", "my", "be", "that", "to")
    val tokenCountsFilteredStopwords = tokenCounts.filter { case (k, v) => !stopwords.contains(k) }
    //println(tokenCountsFilteredStopwords.top(20)(oreringDesc).mkString("\n"))

    val tokenCountsFilteredSize = tokenCountsFilteredStopwords.filter { case (k, v) => k.size >= 2 }
    println(tokenCountsFilteredSize.top(20)(oreringDesc).mkString("\n"))

    println("移除低频词")
    val oreringAsc = Ordering.by[(String, Int), Int](-_._2)
    //println(tokenCountsFilteredSize.top(20)(oreringAsc).mkString("\n"))

    val rareTokens = tokenCounts.filter { case (k, v) => v < 2 }.map { case (k, v) => k }.collect.toSet
    val tokenCountsFilteredAll = tokenCountsFilteredSize.filter { case (k, v) => !rareTokens.contains(k) }
    println(tokenCountsFilteredAll.top(20)(oreringAsc).mkString("\n"))

    def tokenize(line: String): Seq[String] = {
      line.split("""\W+""")
        .map(_.toLowerCase)
        .filter(token => regex.pattern.matcher(token).matches)
        .filterNot(token => stopwords.contains(token))
        .filterNot(token => rareTokens.contains(token))
        .filter(token => token.size >= 2)
        .toSeq
    }
    //println(text.flatMap(doc => tokenize(doc)).distinct.count)
    val tokens = text.map(doc => tokenize(doc))
    println(tokens.first.take(20))

    println("训练模型")
    val dim = math.pow(2, 18).toInt
    val hashingTF = new HashingTF(dim)
    val tf = hashingTF.transform(tokens)
    tf.cache()

    val v = tf.first.asInstanceOf[SV]
    println(v.size)

    println(v.size)
    println(v.values.size)
    println(v.values.take(10).toSeq)
    println(v.indices.take(10).toSeq)

    println("fit & transform")
    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)
    val v2 = tfidf.first.asInstanceOf[SV]
    println(v2.values.size)
    println(v2.values.take(10).toSeq)
    println(v2.indices.take(10).toSeq)

    //    分析权重
    val minMaxVals = tfidf.map { v =>
      val sv = v.asInstanceOf[SV]
      (sv.values.min, sv.values.max)
    }
    val globalMinMax = minMaxVals.reduce {
      case ((min1, max1), (min2, max2)) =>
        (math.min(min1, min2), math.max(max1, max2))
    }
    println(globalMinMax)
    //globalMinMax: (Double, Double) = (0.0,66155.39470409753)
    //常用词

    val common = sc.parallelize(Seq(Seq("you", "do", "we")))
    val tfCommon = hashingTF.transform(common)
    val tfidfCommon = idf.transform(tfCommon)
    val commonVector = tfidfCommon.first.asInstanceOf[SV]
    println(commonVector.values.toSeq)
    //不常出现的单词

    val uncommon = sc.parallelize(Seq(Seq("telescope", "legislation", "investment")))
    val tfUncommon = hashingTF.transform(uncommon)
    val tfidfUncommon = idf.transform(tfUncommon)
    val uncommonVector = tfidfUncommon.first.asInstanceOf[SV]
    println(uncommonVector.values.toSeq)

    //    
    //    4 使用模型
    //4.1 余弦相似度
    import breeze.linalg._

    val hockeyText = rdd.filter { case (file, text) => file.contains("hockey") }
    val hockeyTF = hockeyText.mapValues(doc => hashingTF.transform(tokenize(doc)))
    val hockeyTfIdf = idf.transform(hockeyTF.map(_._2))

    val hockey1 = hockeyTfIdf.sample(true, 0.1, 42).first.asInstanceOf[SV]
    val breeze1 = new SparseVector(hockey1.indices, hockey1.values, hockey1.size)

    val hockey2 = hockeyTfIdf.sample(true, 0.1, 43).first.asInstanceOf[SV]
    val breeze2 = new SparseVector(hockey2.indices, hockey2.values, hockey2.size)
    val cosineSim = breeze1.dot(breeze2) / (norm(breeze1) * norm(breeze2))
    println(cosineSim)

    val graphicsText = rdd.filter { case (file, text) => file.contains("comp.graphics") }
    val graphicsTF = graphicsText.mapValues(doc => hashingTF.transform(tokenize(doc)))
    val graphicsTfIdf = idf.transform(graphicsTF.map(_._2))
    val graphics = graphicsTfIdf.sample(true, 0.1, 42).first.asInstanceOf[SV]
    val breezeGraphics = new SparseVector(graphics.indices, graphics.values, graphics.size)
    val cosineSim2 = breeze1.dot(breezeGraphics) / (norm(breeze1) * norm(breezeGraphics))
    println(cosineSim2)

    val baseballText = rdd.filter { case (file, text) => file.contains("baseball") }
    val baseballTF = baseballText.mapValues(doc => hashingTF.transform(tokenize(doc)))
    val baseballTfIdf = idf.transform(baseballTF.map(_._2))
    val baseball = baseballTfIdf.sample(true, 0.1, 42).first.asInstanceOf[SV]
    val breezeBaseball = new SparseVector(baseball.indices, baseball.values, baseball.size)
    val cosineSim3 = breeze1.dot(breezeBaseball) / (norm(breeze1) * norm(breezeBaseball))
    println(cosineSim3)

    //4.2 学习单词与主题的映射关系
    //多分类映射
    import org.apache.spark.mllib.regression.LabeledPoint
    import org.apache.spark.mllib.classification.NaiveBayes
    import org.apache.spark.mllib.evaluation.MulticlassMetrics

    val newsgroupsMap = newsgroups.distinct.collect().zipWithIndex.toMap
    val zipped = newsgroups.zip(tfidf)
    val train = zipped.map { case (topic, vector) => LabeledPoint(newsgroupsMap(topic), vector) }
    train.cache
    //朴素贝叶斯训练
    val model = NaiveBayes.train(train, lambda = 0.1)
    //加载测试数据集
    val testPath = "D:\\SPARKCONFALL\\Spark机器学习数据\\20news-bydate-test\\*"
    val testRDD = sc.wholeTextFiles(testPath)
    val testLabels = testRDD.map {
      case (file, text) =>
        val topic = file.split("/").takeRight(2).head
        newsgroupsMap(topic)
    }
    val testTf = testRDD.map { case (file, text) => hashingTF.transform(tokenize(text)) }
    val testTfIdf = idf.transform(testTf)
    val zippedTest = testLabels.zip(testTfIdf)
    val test = zippedTest.map { case (topic, vector) => LabeledPoint(topic, vector) }
    //计算准确度和多分类加权F-指标
    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
    println(accuracy)

    val metrics = new MulticlassMetrics(predictionAndLabel)
    println(metrics.weightedFMeasure)

    //5 评估
    val rawTokens = rdd.map { case (file, text) => text.split(" ") }
    val rawTF = rawTokens.map(doc => hashingTF.transform(doc))
    val rawTrain = newsgroups.zip(rawTF).map { case (topic, vector) => LabeledPoint(newsgroupsMap(topic), vector) }
    val rawModel = NaiveBayes.train(rawTrain, lambda = 0.1)
    val rawTestTF = testRDD.map { case (file, text) => hashingTF.transform(text.split(" ")) }
    val rawZippedTest = testLabels.zip(rawTestTF)
    val rawTest = rawZippedTest.map { case (topic, vector) => LabeledPoint(topic, vector) }
    val rawPredictionAndLabel = rawTest.map(p => (rawModel.predict(p.features), p.label))
    val rawAccuracy = 1.0 * rawPredictionAndLabel.filter(x => x._1 == x._2).count() / rawTest.count()
    println(rawAccuracy)

    val rawMetrics = new MulticlassMetrics(rawPredictionAndLabel)
    println(rawMetrics.weightedFMeasure)

    println("word2Vec模型训练")
    val word2vec = new Word2Vec()
    word2vec.setSeed(42) // we do this to generate the same results each time
    val word2vecModel = word2vec.fit(tokens)
    println("寻找最相似的二十个單詞，")
    word2vecModel.findSynonyms("hockey", 20).foreach(println)
    word2vecModel.findSynonyms("legislation", 20).foreach(println)
  }

}