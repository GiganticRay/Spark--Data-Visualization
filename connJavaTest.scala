package indi.leichao.sparkpro.test

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

class connJavaTest {
  
  private val conf = new SparkConf().setAppName("sinSimple Application").setMaster("local");
    
  private val sc = new SparkContext(conf);
  
  def spiltPage(offset: Int, limit: Int, searchMovie_Id: String, searchMovie_Name: String, searchMovie_Year: String,
      searchMovie_Type: String): String = {
    
    val localfilePath = "/home/ray/MyClass/Grade3/Spark/ml-latest/movies.csv"
    val logFile = localfilePath;
    
    // make an attention on setMaster
    
    val logData = sc.textFile(logFile, 2).cache();
    
    // Json: {\"rows\":[{\"movieId\":0, \"title\":\"123\", \"year\":\"2018/08/08\", \"genres\":\"xihu\"}],\"total\":100}
    var jsonContext: String = "{\"rows\":[";
    
    // create rdd
    var dataArrayRdd = logData.zipWithIndex().filter(_._2 > offset)
    .map(lines ⇒ lines._1.split(","));
    
    val searchTypeList = searchMovie_Type.split("|");
    // filter type
    for(i ← 0 until searchTypeList.length){
      dataArrayRdd = dataArrayRdd.filter(item ⇒ item(2).contains(searchTypeList(i)));
    }
    
    // get the data that meet the condition
    val dataCount = dataArrayRdd
    .filter(item ⇒ item(0).contains(searchMovie_Id))
    .filter(item ⇒ item(1).contains(searchMovie_Name))
    .filter(item ⇒ item(1).contains(searchMovie_Year))
    .filter(item ⇒ item(1).endsWith(")"))              // exclude the data which name doesn't end with )
    val total = dataCount.count();
    
    val dataArray = dataArrayRdd
    .filter(item ⇒ item(0).contains(searchMovie_Id))
    .filter(item ⇒ item(1).contains(searchMovie_Name))
    .filter(item ⇒ item(1).contains(searchMovie_Year))
    .filter(item ⇒ item(1).endsWith(")"))
    .take(limit)
    .map(item ⇒ ("{\"movieId\":" + item(0).replace("\"", "\\\"") + ",\"title\":\"" + item(1).replace("\"", "\\\""))
        + "\" ,\"year\":\"" + item(1).subSequence(item(1).lastIndexOf("(") + 1, item(1).lastIndexOf(")")) 
        + "\", \"genres\":\"" + item(2).replace("\"", "\\\"") + "\"}");
    
    for(i ← 0 until dataArray.length){
      jsonContext = jsonContext + dataArray(i) + ", "
    }
    
    jsonContext = jsonContext.dropRight(2);
    jsonContext = jsonContext + "], \"total\":" + total + "}";
    println("offset:" + offset + ", limit: " + limit);
    println(jsonContext);
    jsonContext;
  }
}