package indi.leichao.sparkwebpro.javaservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import indi.leichao.sparkpro.test.connJavaTest;
import scala.tools.nsc.transform.patmat.MatchApproximation.MatchApproximator.Test;
import shapeless.newtype;

import org.apache.spark.SparkContext;
import org.apache.spark.SparkContext.*;
import org.apache.spark.SparkConf;


@RestController
@RequestMapping("/Home")
public class HomeController {

	private connJavaTest connScala = new connJavaTest();
    
    @RequestMapping("/Test")
    public String Test(HttpServletRequest request, HttpServletResponse response){
    	int offset = Integer.parseInt(request.getParameter("offset"));
    	int limit = Integer.parseInt(request.getParameter("limit"));
    	String searchMovie_Id = request.getParameter("searchMovie_Id");
    	String searchMovie_Name = request.getParameter("searchMovie_Name");
    	String searchMovie_Year = request.getParameter("searchMovie_Year");
    	String searchMovie_Type = request.getParameter("searchMovie_Type");
    	
    	
    	String jsonContent = connScala.spiltPage(offset, limit, searchMovie_Id, searchMovie_Name, searchMovie_Year, searchMovie_Type);

//    	String text = JSON.toJSONString(obj); //序列化
//    	JSONObject vo = JSON.parseObject("{...}"); //反序列化
//    	System.out.println(request.getParameter("offset") + ", " + request.getParameter("offset") + ", " +  request.getParameter("memberId"));
    	
    	return jsonContent; 	
    }
}
