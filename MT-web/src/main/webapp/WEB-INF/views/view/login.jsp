<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <meta charset="utf-8" />
    <title>用户登录</title>
    <script src="../../../static/webjars/jquery/3.3.1/jquery.min.js"></script>
    <script src="../../../static/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../../../webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
    <script src="../../../static/js/sweetalert/sweetalert2.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../../static/css/sweetalert/sweetalert2.min.css"/>
    <style>
        #from
        {
            background-color: #96b97d;
        }
        .mycenter
        {
            margin-top: 100px;
            margin-left: auto;
            margin-right: auto;
            height: 350px;
            width: 500px;
            padding: 5%;
            padding-left: 5%;
            padding-right: 5%;
        }
        .mycenter mysign
        {
            width: 440px;
        }
        .mycenter input, checkbox, button
        {
            margin-top: 2%;
            margin-left: 10%;
            margin-right: 10%;
        }
        .mycheckbox
        {
            margin-top: 10px;
            margin-left: 40px;
            margin-bottom: 10px;
            height: 10px;
        }
    </style>
</head>
<body>
<form id="from">
    <div class="mycenter">
        <div class="mysign">
            <div class="col-lg-11 text-center text-info">
                <h2>
                    请登录</h2>
            </div>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="username" name="username" placeholder="请输入账户名" required
                       autofocus />
            </div>
            <div class="col-lg-10">
            </div>
            <div class="col-lg-10">
                <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required
                       autofocus />
            </div>
            <div class="col-lg-10">
            </div>
            <div class="col-lg-10 mycheckbox checkbox">
                <%--<input type="checkbox" class="col-lg-1">记住密码</input>--%>
                <a href="http://127.0.0.1:8090/login/register" style="margin-left: 150px;">注册</a>
            </div>
            <div class="col-lg-10">
            </div>
            <div class="col-lg-10">
                <button type="button" id="btn" class="btn btn-success col-lg-12" onclick="login()">
                    登录</button>
            </div>
        </div>
    </div>
</form>
</body>
</html>

<script type="text/javascript">
    function login() {
        var loginName = $("#username").val().trim();
        var password = $("#password").val().trim();
        if(loginName==null||loginName==""){
            swal("提示：", "用户名为空!", "warning");
            return false;
        }
        if(password==null||password==""){
            swal("提示：", "密码为空!", "warning");
            return false;
        }
        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/login/logincheck" ,//url
            data:{
                'loginname' : loginName,
                'password' : password
            },
            success: function (result) {
                if (result.ret_code == 200) {
                    swal("提示:", "登录成功!","success");
                }else {
                    swal("提示：", result.ret_info, "error");
                }
                ;
            },
            error : function() {
                alert("异常！");
            }
        });
    }

</script>

<%--<script>--%>
    <%--$(document).ready(function(){--%>
        <%--$("button").click(function(){--%>
            <%--window.localtion.href="http:localhost:8090/login/register";--%>
        <%--});--%>
    <%--});--%>

<%--</script>--%>