# Spark 可视化项目
## 1.项目实验介绍
> 将数据上传至HDFS，基于RDD实现对movies.csv数据文件的过滤，用户可以通过在网页上输入关键字，实现对文件进行过滤（前匹配、后匹配、模糊过滤），将结果展示在页面上。加分项:在上述基础之上，与其他文件进行结合，形成更多、更灵活的数据过滤方案的实现
## 2.项目框架选择
> spark-2.4.2-bin-hadoop2.7 + scala-2.12.8 + jdk1.8.0_191 + tomcat9 + hadoop-2.7.2 + spring-framework-4.3.9.RELEASE + bootstrap3.3.7 + bootstrap-table
## 3.项目实现细节
### 3.1.上传至hdfs
> ./hdfs dfs -put /home/ray/MyClass/Grade3/Spark/ml-latest/* /SparkFile
### 3.2.配置 spring mvc + java web 前后端分离
- 3.2.1 将tomcat配置完成
- 3.2.2 新建web项目、其中在 java Resources\src下新建 xml 文件（命名常常springmvc-servlet.xml，url默认前后缀都在这里配置），配置这个时需注意
``` xml
<context:component-scan base-package="indi.leichao.sparkwebpro.javaservice"/>， 这里是配置controller所在的package，之后运行时会在这些package下面去寻找。
```
- 3.2.3 在WEB_INF下新建web.xml文件，注意
``` xml
<param-value>classpath:springmvc-servlet.xml（java resources下面的xml配置文件的名字）</param-value>
```
- 3.2.3 java web 与 scala互通
> 新建web项目与scala项目、web的话要在 项目=》属性=》deplotment assembly=》add=》project选择scala项目引用
> 然后在web项目里面直接写代码即可
- 3.2.4 项目框架图
- ![spark项目框架](https://github.com/GiganticRay/Spark--Data-Visualization/blob/master/resource/spark%E9%A1%B9%E7%9B%AE%E6%A1%86%E6%9E%B6.png "项目框架")
### 3.3. scala rdd 分页过滤实现
> 分页时需要 logData.zipWithIndex().filter(_._2 > offset) 给每一列数据加上索引，之后通过从 java controller 传过来的页码/过滤条件等等参数完成分页
``` scala
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
```

### 3.4 完成效果
- ![spark搜索实例.png](https://github.com/GiganticRay/Spark--Data-Visualization/blob/master/resource/spark%E6%90%9C%E7%B4%A2%E5%AE%9E%E4%BE%8B.png "spark搜索实例")

### 3.5 问题探讨
- tomcat问题，重新换了项目后无法正常启动，More than one fragment错误
> 在web.xml中添加 <absolute-ordering />即可
- 项目有红叉排错：lib包可能有错、java文件可能有错、这些都不会表明出来，需要自己手动排查
- 版本配置问题
> hadoop-2.7.2 + scala-2.12.8 + apache-tomcat-9.0.21 + spark-2.4.2-bin-hadoop2.7 + spring 4.3.9.RELEASE/ (注意，用spring 5.1.8的话，导入spark后tomcat会报jackson的相关错误)
