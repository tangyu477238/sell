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
            background: #F3F3F3;
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

        td {
            height: 25px;
        }

        .read {
            margin-top: 20px;
        }

        .read span {
            font-size: 10px;
            color: red;
        }

        .phone {
            width: 100%;
            height: 42px;
            margin-top: 10px;
            border: 1px solid #E0E0E0;
            border-radius: 3px;
            color: #A9A9A9;
            background: #F3F3F3;
        }

        .zhifu {
            display: block;
            width: 100%;
            height: 42px;
            text-align: center;
            line-height: 42px;
            color: #fff;
            background: #F66D22;
            margin-top: 20px;
            border-radius: 3px;
        }

        .quxiao {
            display: block;
            width: 100%;
            height: 42px;
            text-align: center;
            line-height: 42px;
            color: #fff;
            background: #EA3931;
            margin-top: 10px;
            border-radius: 3px;
        }
    </style>
    <script language="JavaScript">
        function check() {
            var phonenum = $('#phonenum').val();
            if (phonenum == '') {
                alert("请填入正确的手机号");
                return false;
            } else if (typeof (phonenum) == 'undefined') {
                alert("请填入正确的手机号");
                return false;
            } else if (phonenum.length < 11) {
                alert("请填入正确的手机号");
                return false;
            } else {
                return true;
            }
        }




    </script>

</head>
<body>
<!--
    <div class="header"><a class="btn" href="">返回上一步</a></div>
    -->
<form name="form" action=""  method="get">


</form>
<div class="content">
    <div class="main">
        <div class="main_header">
            <span style="float:left; margin-left:10px;">${sod.fromStation}</span>
            <img src="/sell/img/carlogo.png">
            <span style="float:right;margin-right:10px;">${sod.toStation}</span>
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
                    <td>${sod.bizDate} ${sod.bizTime}</td>
                </tr>
                <tr>
                    <td class="tableleft">预订时间：</td>
                    <td>${createTime}</td>
                </tr>
                <tr>
                    <td class="tableleft">已选座位：</td>
                    <td>${sod.info}</td>
                </tr>
                <tr>
                    <td class="tableleft">单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
                    <td>${sod.price}RMB</td>
                </tr>
                <tr>
                    <td class="tableleft">乘客人数：</td>
                    <td>${sod.num}人</td>
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


        <#--短信通知-->
        <#--<input type="text" class="phone" value="13552379492" name="mobile" id="phonenum" placeholder="手机号">-->


    </div>
</div>
<script>

</script>
</body>
</html>