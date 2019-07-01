$(document).ready(function () {
  //调用函数，初始化表格
    initTable();
  //当点击查询按钮的时候执行,bootstrap-table前端分页是不能使用搜索功能，所以可以提取出来自定义搜索。后台代码，在后面给出
    $("#queryBtn").bind("click", initTable);
});

function initTable() {
    //先销毁表格
    $('#myTable').bootstrapTable('destroy');
    
    $('#myTable').bootstrapTable({
        url: "../Home/Test",//请求后台的URL（*）
        method: 'get',
        dataType: "json",
        dataField: 'rows',
        striped: true,		// 设置为 true 会有隔行变色效果
        undefinedText: "空",// 当数据为 undefined 时显示的字符
        pagination: true, 	// 设置为 true 会在表格底部显示分页条。
        showToggle: "true",	// 是否显示 切换试图（table/card）按钮
        showColumns: "true",// 是否显示 内容列下拉框
        pageNumber: 1,		// 初始化加载第一页，默认第一页
        pageSize: 10,		// 每页的记录行数（*）
        pageList: [10, 20, 30, 40],// 可供选择的每页的行数（*），当记录条数大于最小可选择条数时才会出现
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        search: false, 		// 是否显示表格搜索,bootstrap-table服务器分页不能使用搜索功能，可以自定义搜索框，上面jsp中已经给出，操作方法也已经给出
        striped : true,		// 隔行变色
        showColumns: false,	// 是否显示 内容列下拉框
        showToggle: false, 	// 是否显示详细视图和列表视图的切换按钮
        clickToSelect: true,// 是否启用点击选中行
        data_local: "zh-US",// 表格汉化
        sidePagination: "server", 	// 服务端处理分页
        queryParamsType : "limit",	// 设置为 ‘limit’ 则会发送符合 RESTFul 格式的参数.
        queryParams: function (params) {// 自定义参数，这里的参数是传给后台的，我这是是分页用的
        								// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
        								//　queryParamsType = 'limit' ,返回参数必须包含limit, offset, search, sort, order 
        								// queryParamsType = 'undefined', 返回参数必须包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
        								// 返回false将会终止请求。
            return {	// 这里的params是table提供的
                offset: params.offset,	// 从数据库第几条记录开始
                limit: params.limit,	// 找多少条
                searchMovie_Id: $("#searchMovie_Id").val(),	// 这个就是搜索框中的内容，可以自动传到后台，搜索实现在xml中体现
                searchMovie_Name: $("#searchMovie_Name").val(),
                searchMovie_Year: $("#searchMovie_Year").val(),
                searchMovie_Type: $("#searchMovie_Type").val()
            };
        },
        responseHandler: function (res) {
	　　　　　　//如果后台返回的json格式不是{rows:[{...},{...}],total:100},可以在这块处理成这样的格式
          $('#myTable').bootstrapTable('append', res);
	　　　　　　return res;
        },
        columns: [{
            field: 'movieId',
            title: '电影编号',
//            formatter: idFormatter
        }, {
            field: 'title',
            title: '电影名儿',
        }, {
            field: 'year',
            title: '上映年份'
        }, {
        	field: 'genres',
        	title: '电影类型'
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("What you search is Nothing！");
        }
    });
}
//function idFormatter(value, row, index){
//    return index+1;
//}
// button
function operateFormatter(value, row, index) {
      return '<button  type="button" onClick="showConsumeRecord('+id+')"  class="btn btn-xs btn-primary" data-toggle="modal" data-target="#consumeModal">查看</button>';
}