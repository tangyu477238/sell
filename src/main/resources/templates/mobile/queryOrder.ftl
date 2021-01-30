<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>订单查看</title>
    <script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>
    <style type="text/css">
        * {
            margin: 0px;
            padding: 0px;
            font-family: "微软雅黑"
        }

        a {
            text-decoration: none;
        }

        .header {
            width: 100%;
            height: 50px;
            background: #F3F3F3;
            text-align: center;
        }

        .header a {
            color: #888888;
            font-size: 13px;
            line-height: 50px;
        }

        .content .main {
            width: 88%;
            height: 250px;
            margin: 0 auto;
            margin-top: 20px;
            border-radius: 4px;
        }

        .content .main_header {
            height: 42px;
            width: 100%;
            background: #68A1E8;
            text-align: center;
            line-height: 42px;
            color: #fff;
            font-size: 13px;
            border-top-left-radius: 6px;
            border-top-right-radius: 6px;


        }

        .content .main_header img {
            width: 39px;
            height: 21px;
            margin-top: 10px;
        }



        .main_info {
            padding: 5px;
            font-size: 13px;
        }

        .tableleft {
            color: #909090;
        }
        .tableright {
            font-size: 26px;
            color: red;
        }

        td {
            height: 25px;
        }


        .read span {
            font-size: 10px;
            color: red;
        }


    </style>
    <script language="JavaScript">
        $(document).ready(function () {
            var num = Math.floor(Math.random() * 10) + 1;
            if (num > 5){
                $("#imageId").attr("src","/sell/img/guanggao1.jpg");
            } else {
                $("#imageId").attr("src","/sell/img/guanggao2.jpg");
            }

        });




    </script>

</head>
<body>
<!--
    <div class="header"><a class="btn" href="">返回上一步</a></div>
    -->
<form name="form" action=""  method="get">


</form>
<div class="content">
    <div class="main" style="margin-bottom: 3em;">
        <div style="width: 100%;height: 20em;margin-bottom: 20px; text-align: center;">
            <img id="imageId" src="${qrcode}" style="height: 20em;">
        </div>

        <div class="main_header" style="background-color: ${lpColor}">
            <span style="float:left; margin-left:10px;font-size: 26px;">${sod.fromStation}</span>
            <img src="/sell/img/carlogo.png">
            <span style="float:right;margin-right:10px;font-size: 26px;">${sod.toStation}</span>
        </div>

        <div class="main_info">
            <table>
                <tr>
                    <td class="tableleft">乘客姓名：</td>
                    <td>${sod.userName}</td>
                </tr>
                <tr>
                    <td class="tableleft">手&nbsp;&nbsp;机&nbsp;&nbsp;号：</td>
                    <td>${sod.userMobile}</td>
                </tr>
                <tr>
                    <td class="tableleft">乘车日期：</td>
                    <td class="tableright">${sod.bizDate} ${sod.bizTime}</td>
                </tr>
                <tr>
                    <td class="tableleft">预订时间：</td>
                    <td >${createTime}</td>
                </tr>
                <tr>
                    <td class="tableleft">已选座位：</td>
                    <td class="tableright">${sod.info}</td>
                </tr>
                <tr>
                    <td class="tableleft">单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
                    <td>${sod.price}RMB</td>
                </tr>
                <tr>
                    <td class="tableleft">乘客人数：</td>
                    <td class="tableright">${sod.num}人</td>
                </tr>
                <tr>
                    <td class="tableleft">应付金额：</td>
                    <td>${sod.amout}RMB</td>
                </tr>
                <tr>
                    <td class="tableleft">备&nbsp;&nbsp;&nbsp;注：</td>
                    <td> ${sod.remark}</td>
                </tr>
            </table>
        </div>


        <div id="aaaa" style="text-align: right;min-height: 2em;margin-top: 0.3em;margin-right: 1em;margin-bottom: 1.5em;">
            <a href="javascript:location.href='/sell/seatYudingOrder/yudingguize?uid=${sod.createUser}'">每天购票烦?试试自动出票</a>
        </div>


    </div>
</div>
<script>

</script>
</body>
</html>