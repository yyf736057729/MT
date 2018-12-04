<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <meta charset="utf-8" />
    <title>用户列表</title>
    <script src="../../../static/webjars/jquery/3.3.1/jquery.min.js"></script>
    <script src="../../../static/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <%--<script src="../../../static/js/pagination/jquery.pagination.js"></script>--%>
    <%--<link href="../../../static/js/pagination/pagination.css" rel="stylesheet" type="text/css" />--%>

    <%--<script src="../../../static/js/fy/jquery.page.js"></script>--%>
    <%--<link href="../../../static/js/fy/jquery.page.css" rel="stylesheet" type="text/css" />--%>

    <link rel="stylesheet" href="../../../static/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

    <script src="../../../static/js/sweetalert/sweetalert2.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../../static/css/sweetalert/sweetalert2.min.css"/>
    <!-- 时间插件 -->
    <%--<script src="../../../static/js/bootstrap-datetimepicker/bootstrap-datepicker.js"></script>--%>
    <%--<script src="../../../static/js/bootstrap-datetimepicker/locales/bootstrap-datepicker.fr.js"></script>--%>
    <%--<link href="../../../static/css/bootstrap-datetimepicker/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css" />--%>

</head>
<style>

    /*.M-box{*/
        /*position: absolute;*/
        /*left: 30px;*/
        /*top: 20px;*/
        /*float:right;*/
    /*}*/
.row-fluid{
    height: 370px;
}
    table{
        background-color: gainsboro;
    }
</style>

<h1>用户管理</h1>
<div class="ibox-content m-b-sm border-bottom">
    <div class="row">
        <div class=" col-lg-4 col-md-6 col-sm-8 " >
            <div class="tablesearch pull-right m-t-xs">
                <div class="table-td">
                    <div class="input-group" >
                        <input type="text" class="input-sm form-control" id = "search_login" value="" placeholder="输入账号搜索">
                        <span class="input-group-btn"><button type="button" class="btn btn-sm btn-primary "onclick="search_login_name()"> 查询</button></span>
                        <div class="table-td m-l-sm pull-right">
                            <a  class="btn btn-sm btn-primary dropdown-toggle" role="button" data-toggle="collapse" href="#collapseExample" aria-expanded="false" aria-controls="collapseExample"> 高级搜索 <span class="caret"></span></a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!----高级搜索内容区---->
    <div class="collapse" id="collapseExample">
        <div class="border-top m-t-md m-b-none sidedown-box" >
            <div class="row" style="margin-right: 10px">

                <div class="col-sm-4">
                    <div class="form-group">
                        <label class="control-label" for="order_id">身份证号</label>
                        <input type="text" id="order_id" name="order_id" value="" placeholder="输入身份证号" class="form-control">
                    </div>
                </div>

                <div class="col-sm-4">
                    <div class="form-group">
                        <label class="control-label" for="order_id">手机号</label>
                        <input type="text" id="phone_id" name="order_id" value="" placeholder="输入手机号" class="form-control">
                    </div>
                </div>


                <%--<div class="input-append date form_datetime">--%>
                    <%--<input size="16" type="text" value="" readonly>--%>
                    <%--<span class="add-on"><i class="icon-th"></i></span>--%>
                <%--</div>--%>



            <div class="row">
                <div class="col-sm-4 " style="margin-top: 23px;">
                    <button type="button" class="btn btn-primary"><i class="fa fa-search"></i> 立即搜索</button>
                </div>
            </div>

        </div>
    </div>
    <!---高级搜索结束---->
</div>

</div>

<div class="container-fluid" style="margin-top: 40px;">
    <div class="row-fluid">
        <div class="span12">
            <table class="table">
                <thead>
                <tr>
                    <th>
                        编号
                    </th>
                    <th>
                        用户名
                    </th>
                    <th>
                        密码
                    </th>
                    <th>
                        手机号
                    </th>
                    <th >
                        身份证号
                    </th>
                    <%--<th>--%>
                        <%--是否删除--%>
                    <%--</th>--%>
                    <th >
                        创建时间
                    </th>
                    <th>
                        操作
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${userList}" var="user" varStatus="status">
                    <tr class="success">
                        <%--DateUtil.StringToDate4(${user.createTime});--%>
                        <td id=id${user.id}>${status.index+1}</td>
                        <td id=loginName${user.id}>${user.loginName}</td>
                        <td><input type="text" style="width: 190px;" id=loginPassword${user.id} value=${user.loginPassword}></td>
                        <td><input style="width: 190px;" id=phone${user.id} type="text" value=${user.phone}></td>
                        <td  align=center><input  style="width: 190px;" id=idcard${user.id} type="text" value=${user.idcard}></td>
                        <%--<td id=enable${user.id}>${user.enable}</td>--%>
                        <td id=createTime${user.id}> <fmt:formatDate type="both" value="${user.createTime}"/></td>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="update(${user.id})">修改</button>
                            <button type="button" class="btn btn-primary" onclick="deleteuser(${user.id})">删除</button>
                        </td><!-- 修改删除按钮 -->
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </div>
</div>
<div style="position: relative;float: right;right: 30px;">
<%--<div style="float: right;">--%>
    <div class="M-box m-style"></div>
    <form style="margin: 0px;" action="/user/list"
          method="get" id="data_form">
        <input name="current" type="hidden" id="current">
    </form>
</div>

<%--<input type="date">--%>


<script>
    $(function() {// 初始化内容

        $('.M-box').pagination({
            // jump:true,
            coping:true,
            totalData : '${totalData}',
            showData : '${pageSize}',
            current : '${current}',
            homePage:'首页',
            endPage:'末页',
            prevContent:'上页',
            nextContent:'下页',
            callback : function(api) {
                $("#current").val(api.getCurrent());
                $("#data_form").submit();
            },
        });

    });
</script>

<script>
    function deleteuser(id) {
    var login_obj = document.getElementById("loginName"+id);
    swal({
            title: "确定删除吗？",
            text: "您将删除-"+login_obj.innerText+"-用户",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            closeOnConfirm: false
        }).then(function(){
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/user/delete" ,//url
                data:{
                    'id' : id
                },
                success: function (result) {
                    if (result.ret_code == 200) {
                        swal("删除！", "用户已被删除。", "success");
                        setTimeout(2000)
                        window.location.reload();
                    }else {
                        alert(result.ret_info);
                    }
                    ;
                },
                error : function() {
                    swal("异常!");
                }
            });
        })

}

function update(id){
        var loginName = $("#loginName"+id).val();
        var loginPassword = $("#loginPassword"+id).val();
        var phone = $("#phone"+id).val();
        var idcard = $("#idcard"+id).val();

        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/user/update" ,//url
            data:{
                'id' : id,
                'loginName' : loginName,
                'loginPassword' : loginPassword,
                'phone' : phone,
                'idcard' : idcard,
            },
            success: function (result) {
                if (result.ret_code == 200) {
                    swal({
                        title: "提示：",
                        text: "修改成功! 2秒后自动关闭。",
                        timer: 2000,
                        showConfirmButton: false
                    });
                }else {
                    swal("异常!");
                } ;
            },
            error : function() {
                swal("异常!");
            }
        });
}
    function search_login_name() {
        var login_name=$("#search_login").val();
        window.location.href="http://127.0.0.1:8090/user/list?login_name="+login_name;
    }
</script>