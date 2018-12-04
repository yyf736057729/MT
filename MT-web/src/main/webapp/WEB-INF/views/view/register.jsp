<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <meta charset="utf-8" />
    <title>用户注册</title>
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
            height: 450px;
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
                    请注册</h2>
            </div>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="username" name="username" placeholder="请输入账户名" required
                       autofocus />
            </div>
            <div class="col-lg-10">
                <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required
                       autofocus />
            </div>

            <div class="col-lg-10">
                <input type="text" class="form-control" id="rename" name="rename" placeholder="姓名" required
                       autofocus />
            </div>

            <div class="col-lg-10">
                <input type="text" class="form-control" id="phone" name="phone" placeholder="手机号码" required
                       autofocus />
            </div>

            <div class="col-lg-10">
                <input type="text" class="form-control" id="idcard" name="idcard" placeholder="身份证号码" required
                       autofocus />
            </div>



            <div class="col-lg-10">
                <button type="button" id="btn" class="btn btn-success col-lg-12" onclick="register()">
                    注册</button>
            </div>
        </div>
    </div>
</form>
</body>
</html>

<script type="text/javascript">
    function register() {
        var loginName = $("#username").val().trim();
        var password = $("#password").val().trim();
        var rename = $("#rename").val().trim();
        var phone = $("#phone").val().trim();
        var idcard = $("#idcard").val().trim();
        if(loginName==null||loginName==""){
            swal({
                text: "用户名为空!",
                type: "warning",
                closeOnConfirm: false
            })
            return false;
        }
        if(password==null||password==""){
            swal({
                text: "密码为空!",
                type: "warning",
                closeOnConfirm: false
            })
            return false;
        }

        if(rename==null||rename==""){
            swal({
                text: "姓名为空!",
                type: "warning",
                closeOnConfirm: false
            })
            return false;
        }
        if(phone==null||phone==""){
            swal({
                text: "手机号码为空!",
                type: "warning",
                closeOnConfirm: false
            })
            return false;
        }else {
            var reg=/^1[34578]\d{9}$/;
            if (!reg.test(phone)) {
                swal({
                    text: "请输入正确的手机号码!",
                    type: "warning",
                    closeOnConfirm: false
                })
                return false;
            };
        }
        if(idcard==null||idcard==""){
            swal({
                text: "身份证号码为空!",
                type: "warning",
                closeOnConfirm: false
            })
            return false;
        }else {
            var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            if(reg.test(idcard) === false)
            {
                swal({
                    text: "身份证号码错误!",
                    type: "warning",
                    closeOnConfirm: false
                })
                return  false;
            }
        }
        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/login/register" ,//url
            data:{
                'loginname' : loginName,
                'password' : password,
                'rename' : rename,
                'idcard' : idcard,
                'phone' : phone
            },
            success: function (result) {
                if (result.ret_code == 200) {
                    swal({
                        text: "SUCCESS!",
                        type: "success",
                        closeOnConfirm: false
                    })
                }else {
                    swal({
                        text: result.ret_info,
                        type: "error",
                        closeOnConfirm: false
                    })
                }
                ;
            },
            error : function() {
                alert("异常！");
            }
        });
    }
</script>