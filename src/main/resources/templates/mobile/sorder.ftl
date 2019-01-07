<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>我的订单</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta content="width=device-width, iniial-scale=1.0,minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" id="viewport" />
    <link href="/sell/css/orderList.css" rel="stylesheet">

</head>
<body>
<!--     <div class="header"><a href="#"><img src="images/go_back.png"></a><div class="header-word"><p>我的订单</p></div>
</div> -->
<div class="content">
    <img src="/sell/img/orderList-pic.gif">
    <div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0; width: 100%">
        <div class="orderList-tab">月票消费记录</div>
    </div>
    <div class="orderList-tab">
        <table style="width:100%">
            <thead>
            <th style="width:100px;">订单编号</th>
            <th style="width:90px;">使用情况</th>
            <th style="width:60px;">金额</th>
            <th style="width:60px;">购买时间</th>
            </thead>
            <tbody>
            <#list mtulist as pl>

                <tr>
                    <td colspan=1>${pl.ptypeName}<br>(${pl.month})</td>
                    <td colspan=1>已用${pl.useNum}次/共${pl.totalNum}次</td>
                    <td colspan=1>${pl.price}</td>
                    <td colspan=1>${pl.createTime}</td>
                </tr>

            </#list>


            </tbody>
        </table>
    </div>
</div>
<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0;">
    <div class="orderList-tab">次票消费记录</div>
</div>
<div class="content">
    <div class="orderList-tab">
        <table style="width:100%">
            <thead>
            <th style="width:70px;">订单编号</th>
            <th style="width:90px;">线路</th>
            <th style="width:60px;">金额</th>
            <th style="width:60px;">订单状态</th>
            <th style="width:60px;">详情</th>

            </thead>
            <tbody>
                <#list sodlist as sod>

                <tr>
                    <td colspan=1>${sod.orderNo}</td>
                    <td colspan=1>${sod.fromStation}-->${sod.toStation}</td>
                    <td colspan=1>${sod.num}张/共${sod.amout}元</td>
                    <td colspan=1>${sod.remark}</td>
                    <td colspan=1>
                        <a style="font-size:12pt; background:none; border:none; width:100%; height:100%;" href="javascript:location.href='/sell/ticket/queryOrder?orderId=${sod.id}&uid=${uid}'">
                        查看 </a>
                    </td>
                </tr>

            </#list>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>