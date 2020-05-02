<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>月票充值</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta content="width=device-width, iniial-scale=1.0,minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" id="viewport" />
    <link href="/sell/css/orderList.css" rel="stylesheet">
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
    <script>
        function yan() {

            var uid = $('#uid').val();

            // if(uid!='bdf9eb628f0b464dbefaaac694e680ab'
            //     &&uid!='a365c62fd4c241f6a8b277ebd495c5d3'){
            //     alert('系统维护');
            //     return ;
            // }


            var radioVal = $('input:radio[name="id"]:checked').val();
            if(radioVal==undefined) {
                alert('请选择后购买');
                return ;
            }
            var form = document.getElementById("form");
            form.submit();
        }
    </script>

</head>
<body>
<div class="content">
    <img src="/sell/img/orderList-pic.gif">

    <form id = "form" name="form" action="/sell/ticket/payMonthTick"  method="post">
        <input id="uid" ype="hidden" name="uid"  value="${uid}">
        <input type="hidden" name="month_val"  value="${month_val}">

    <div class="orderList-tab">
        <table style="width:100%">
            <thead>
            <th style="width:70px;">车票类型</th>
            <th style="width:60px;">费用</th>
            <th style="width:90px;">使用月份</th>
            </thead>

            <#list mtdlist as mtd>

                <tbody>
                    <tr>
                    <td colspan=1><input type="radio" name="id"  value="${mtd.id}">${mtd.ptypeName}</td>
                    <td colspan=1>${mtd.price}元/${mtd.totalNum}张 </td>
                    <td colspan=1>${month_name}</td>
                    </tr>
                </tbody>

            </#list>





        </table>
    </div>

    <div style=" color: red;font-weight:600; padding: 15px 0; width: 100%">
        <div class="orderList-tab" style="font-size: 12px;">每月月底最后7天可以购买下个月月票，每个班次可以使用月票支付4次，当月未使用完的车票不能在下月继续使用，所购买的月票次票均不可退票，不可改签！</div>
    </div>
	<div style=" color: red;font-weight:600; padding: 15px 0; width: 100%">
        <div class="orderList-tab" style="font-size: 12px;">温馨提示：购买完月票的乘客与购买次票一样，需提前三天抢票，谢谢合作！</div>
    </div>
    </form>
</div>

<div class="content"  >
    <div class="main" style="background: #FFFFFF;">
        <div style="width:100%" >
            <tr >
                <td >
                    <#--<a style="font-size:12pt; background:none; border:none; color:#fff; width:100%; height:100%;"-->
                       <#--href="javascript:location.href='pay?order_id=1283916&phone='+$('#phonenum').val()">-->
                        <#--<div class="zhifu">微信支付</div>-->
                    <#--</a>-->

                    <div class="bottom_a" style="background-color: ${lpColor}">
                        <font style="vertical-align: inherit;">
                            <font style="vertical-align: inherit;">
                                <button type="button"  onclick="yan();" style="border:none; background:none;width:100%;color:#fff;
                            font-size:18px;line-height:45px;cursor:pointer;" value="微信支付">微信支付</button>
                            </font>
                        </font>
                    </div>
                </td >
            </tr>
            <#--<tr>-->
                <#--<td style="background: #FFFFFF;">-->
                    <#--<a style="font-size:12pt; background:none; border:none; color:#fff; width:100%; height:100%;"-->
                       <#--href="javascript:jfzf(this, 1283916);">-->
                        <#--<div class="zhifu">积分支付</div>-->
                    <#--</a>-->
                <#--</td>-->
            <#--</tr>-->
            <tr>
            </tr>
        </div>
    </div>
</div>
</body>
</html>