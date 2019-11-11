<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
/* http://localhost:8080/logistics/ */
%>

<!DOCTYPE HTML>
<html>
<head>
<!-- 设置页面的 基本路径，页面所有资源引入和页面的跳转全部基于 base路径 -->
<base href="<%=basePath%>">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"  href="lib/zTree/v3/css/zTreeStyle/zTreeStyle.css">

<script type="text/javascript" src="lib/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" src="lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script>
</head>
<body>

<h1>回显(加载页面让其对应复选框选中)已经有数据</h1>
<button onclick="getCheckedIds();">获取复选框选中数据的id值</button>

<ul id="permissionTree" class="ztree"></ul>

<script type="text/javascript">
	//定义zTree的设置 
	var setting = {	
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			async: {
				enable: true,
				url:"permission/getAllPermissions.do",
				dataFilter: filter
			},
			//异步加载完毕以后回调的函数
			callback: {
				onAsyncSuccess: zTreeOnAsyncSuccess
			}
			
	};
	//异步加载完毕以后回调的函数
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	    //获取zTree树对象
		var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
		/* 
	    	回显思路
	    	
	    	1， 把当前角色的权限id 拆分成数组
	    	var permissionIds = "${role.permissionIds}";
	    	//模拟数据
	    	var permissionIds = "10,36,37,38,39,40,54,55";
	    	
	    	var permissionIdArr = permissionIds.split(",");
	    	
	    	2, 再循环数据，分别通过值获取对应的节点
	    		zTree可以根据 指定属性的值获取对应的节点
	    		如： zTree的id=10，那么久可以找到对应的节点
	    		
	    	3，让其对应找到的节点选中即可
	    		达到回显目的
	    
	    */
		var permissionIds = "10,36,37,38,40";
		var permissionIdArr = permissionIds.split(",");
		
		for(var i = 0;i<permissionIdArr.length;i++){
			//获取具体的permissionId值
			var permissionId = permissionIdArr[i];
			console.log("permissionId["+i+"]",permissionId);
			
			//zTree可以根据 指定属性的值获取对应的节点
			//通过zTree数据 id的属性对应的值获取对应的节点
			
			var node = treeObj.getNodeByParam("id", permissionId, null);
			//console.log(node);
			
			//让其选中的节点选中
			treeObj.checkNode(node, true, false);
		}
	};
	
	
	
	//重新格式化异步加载的json数据
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].id = childNodes[i].permissionId;
			childNodes[i].pId = childNodes[i].parentId;
			childNodes[i].open = true;
		}
		return childNodes;
	}
	
	$(function(){
		//初始化zTree
		$.fn.zTree.init($("#permissionTree"), setting);
	})
	
	
	//获取选中的复选框的值
	function getCheckedIds(){
		//获取zTree树对象
		var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
		
		//获取复选框选中的节点
		var nodes = treeObj.getCheckedNodes(true);
		//console.log(nodes);
		
		/* 
			获取每一个节点的 permissionId 的值，最终拼接成字符串,使用逗号作为分隔符
			如 ： 1,3,8,10...
		
		*/
		//声明一个数组，添加permissionId
		var permissionIdArr = [];
		for(var i = 0 ;i<nodes.length;i++){
			
			var node = nodes[i];
			//获取每个数据的id
			var permissionId = node.permissionId;
			//将一个个permissionId添加到数组
			permissionIdArr.push(permissionId);
		}
		
		console.log(permissionIdArr);
		
		/*
		使用js数组的join方法或者toString方法将数组转换成字符串
			var str1 = 数组.join("自定义分隔符")
		*/
		//var permissionIds = permissionIdArr.join(",");
		var permissionIds = permissionIdArr.toString();//1,2,3,5,8.。。。
		console.log(permissionIds)
	}

</script>




</body>
</html>