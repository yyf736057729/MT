<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="webjars/jquery/3.3.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css"/>
    <meta charset="UTF-8">
    <title>美团</title>
<script>
    // $("#inputfile").hide();
    // $("#inputfile").css("display", "");
    // document.getElementById("inputfile").style.display="inline";
    // document.getElementById("inputfile").style.display="inline";
    // $("center_apsn").click(function(){
    //     alert()
    //     $("center_apsn").css("height","20px");
    // });
</script>
</head>
<body id="body_index">
<h2>美团优惠</h2>
<form id="file_upload_id"  name="file_upload_name"
      enctype="multipart/form-data">
    <div id="file_on" class="form-group" style="">
        <div align="center">
        <span style="background: #4796d7;border: 1px solid #FFF;position:absolute;width: 5%;height: 3%;margin-left: -2.5%" class="btn btn-success fileinput-button">
                <span   id="center_apsn"  style="position:absolute;background: #4796d7; width: 5%;height: 3%;margin-left:-38%;margin-top: -4%">请选择文件</span>
                <input type="file" id="inputfile" name="files" multiple="multiple" style="margin-left: 45%;background: #4796d7;display: none" >
        </span>
        </div>

    </div>
    <button type="submit" id="add_submit" class="btn btn-default" style="margin-top: 3%;margin-left: 1%">上传</button>
    <%--<button id="excel">明细</button>--%>
    <span style="width: 10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <a href="/excel" class="btn btn-default"  style="margin-top: 3%;margin-left: 1%">导出报表</a>
    <div class="col-md-12 col-sm-12 col-xs-12">
        <div class="x_panel">
            <div class="x_title" style="height: 50px">

            </div>
            <div style="position: absolute;margin-left: 28%;width: 45%">
                <%--所有执行：<span id="countAll"></span>&nbsp;--%>
                今日执行：<span id="countByDay"></span>&nbsp;
                未执行：<span id="notCount"></span>&nbsp;
                <%--一共领取新人首单和风控中：<span id="byCount1"></span>&nbsp;--%>
                <%--今天领取新人首单和风控中：<span id="byCount"></span>&nbsp;--%>
                <%--领取新人首单：<span id="listSize"></span>&nbsp;--%>
                <%--风控中：<span id="listSize1"></span>--%>
            </div>
    <div id="x_content" class="x_content"style="margin-top: 1%">
        <div class="row">
            <div class="col-sm-12">
                <div class="card-box table-responsive">
                    <p class="text-muted font-13 m-b-30" style="margin-left: 85%">
                        最新任务前50条信息状态&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;15s自动刷新
                    </p>

                    <table id="datatable-keytable" class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <%--<th>Name</th>--%>
                            <th align="center">手机号</th>
                            <th>导入时间</th>
                            <th>领取状态信息</th>
                            <th>优惠提示</th>
                        </tr>
                        </thead>
                        <tbody  id="tbody_index">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    </div>
    </div>

</form>

    <div>

    </div>
</body>
</html>
<script>


</script>
<style>

    .fileinput-button {
        position: relative;
        display: inline-block;
        overflow: hidden;
    }

    .fileinput-button input{
        position:absolute;
        right: 0px;
        top: 0px;
        opacity: 0;
        -ms-filter: 'alpha(opacity=0)';
        font-size: 200px;
    }
    th{
        text-align:center;/** 设置水平方向居中 */
        vertical-align:middle/** 设置垂直方向居中 */
    }
    body {
        text-align: center
    }

    #divcss5 {
        margin: 0 auto;
        border: 1px solid #000;
        width: 300px;
        height: 100px
    }
</style>

<script>

    $("#excel").on("click", function () {

        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/excel", //url
            // data: formData,
            async: false,
            error: function (request) {
                alert("出错了");
            },
            success: function (data) {
                if(date == ''){

                }
                if(date != ''){
                    alert(data)
                }
            }
        });
    })
    $("#inputfile").css("display", "");
    document.getElementById("inputfile").style.display="inline";
    setInterval(function(){
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/statistic", //url
            // data: formData,
            async: false,
            error: function (request) {
            },
            success: function (data) {
                var e = eval("("+data+")");
                // console.log( e)
                // $("#countAll").html(e.count_all);
                $("#countByDay").html(e.count_by_day);
                // $("#listSize").html(e.list_size);
                // $("#listSize1").html(e.list_size1);
                // $("#byCount1").html(e.byCount1);
                // $("#byCount").html(e.byCount);
                $("#notCount").html(e.notCount);
                var find_statistics = e.find_statistics;
                var html = "";
                for (var o in find_statistics) {
                    // console.log(find_statistics[o].phone);
                   // html+="<td>"+find_statistics[o].byCount+"</td><td>"+find_statistics[o].byCount1+"</td><td>"+find_statistics[o].message+"</td><td>"+find_statistics[o].coupon_display_name+"</td></tr>";
                     html+="<tr><td>"+find_statistics[o].phone+"</td><td>"+find_statistics[o].date+"</td><td>"+find_statistics[o].message+"</td><td>"+find_statistics[o].coupon_display_name+"</td></tr>";

                }
                $("#tbody_index").html(html);
            }
        });
    }, 1500);
    // $("#x_content").fadeIn(3000);
    $("#body_index").hide();
    $("#body_index").show(1200);
    $("#file_on").show();

    $.ajax({
        type: "POST",
        contentType: false,
        processData: false,
        url: "/statistic", //url
        // data: formData,
        async: false,
        error: function (request) {
        },
        success: function (data) {
            var e = eval("("+data+")");
            // console.log( e)
            $("#countAll").html(e.count_all);
            $("#countByDay").html(e.count_by_day);
            // $("#listSize").html(e.list_size);
            // $("#listSize1").html(e.list_size1);
            // $("#byCount1").html(e.byCount1);
            // $("#byCount").html(e.byCount);
            var find_statistics = e.find_statistics;
            var html = "";
            for (var o in find_statistics) {
                // console.log(find_statistics[o].phone);
                html+="<tr><td>"+find_statistics[o].phone+"</td><td>"+find_statistics[o].date+"</td><td>"+find_statistics[o].message+"</td><td>"+find_statistics[o].coupon_display_name+"</td></tr>";
            }
            $("#tbody_index").html(html);
        }
    })

    $("#add_submit").on("click", function () {
        var formData = new FormData($("#file_upload_id")[0]);
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/batch/upload", //url
            data: formData,
            async: false,
            error: function (request) {
                alert("出错了");
            },
            success: function (data) {
                if(date == ''){

                }
                if(date != ''){
                    alert(data)
                }
            }
        });
    })
</script>

