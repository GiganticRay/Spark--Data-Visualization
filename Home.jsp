<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link href= "../bootstrap-table/bootstrap-table.css"  rel="stylesheet" type="text/css" />
<link href= "../bootstrap-3.3.7-dist/css/bootstrap.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="../bootstrap-3.3.7-dist/js/bootstrap.js"></script>
<script type="text/javascript" src="../bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript" src="../js/My.js"></script>

</head>
<body>
	<div class="panel">
    <div class="panel-body" style="padding-bottom: 1px;">
        <form class="form-inline">
		  <div class="form-group">
		      <label for="searchMovie_Id">请输入电影ID</label>
		      <input type="text" class="form-control" name="searchString" id="searchMovie_Id" placeholder="1">
		  </div>
		  <div class="form-group">
		      <label for="searchMovie_Name">请输入电影名儿</label>
	    	  <input type="text" class="form-control" id="searchMovie_Name" placeholder="Her">
		  </div>
		  <div class="form-group">
		      <label for="searchMovie_Year">请输入电影上映年份</label>
	    	  <input type="text" class="form-control" id="searchMovie_Year" placeholder="2013">
		  </div>
		  <div class="form-group">
		      <label for="searchMovie_Type">请输入电影类型</label>
	    	  <input type="text" class="form-control" id="searchMovie_Type" placeholder="Comedy|Romance, 以‘|’分割">
		  </div>
		  <button type="button" class="btn btn-primary btn-w-m" id="queryBtn">
              <span class="glyphicon glyphicon-search"></span> 搜索
          </button>
		</form>
    </div>
</div>
<div class="ibox-content">
    <table id="myTable"></table>
</div>
</body>
</html>