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
</div>

<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0; width: 100%">
    <div class="orderList-tab">月票消费记录</div>
</div>


<div >

    <table style="width:100%" class="altrowstable" id="yuepiao">
        <thead class="evenrowcolor">
        <th style="width:25%;">订单编号</th>
        <th style="width:32%;">使用情况</th>
        <th style="width:20%;">金额</th>
        <th style="width:23%;">购买时间</th>
        </thead>
        <tbody>
        <#list mtulist as pl>
            <tr class="oddrowcolor">
            <td colspan=1>${pl.ptypeName}<br>(${pl.month})</td>
            <td colspan=1>已用${pl.useNum}次/共${pl.totalNum}次</td>
            <td colspan=1>${pl.price}</td>
            <td colspan=1>${pl.createTime}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</div>





<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0;">
    <div class="orderList-tab">次票消费记录</div>
</div>

<div >
    <#--<div class="orderList-tab">次票消费记录</div>-->

    <table style="width:100%" class="altrowstable" id="alternatecolor">
        <thead class="evenrowcolor">
            <th style="width:25%;">乘车日期/时间</th>
            <th style="width:25%;">出发/到达</th>
            <th style="width:30%;">座位</th>
            <th style="width:20%;">详情</th>
        </thead>
        <tbody>
        <#list sodlist as sod>
        <tr class="oddrowcolor">
            <td colspan=1>${sod.bizDate}<br>${sod.bizTime}</td>
            <td colspan=1>${sod.fromStation}-->${sod.toStation}</td>
            <td colspan=1>${sod.info}</td>
            <td colspan=1>
        <a style="font-size:12pt; background:none; border:none; width:100%; height:100%;" href="javascript:location.href='/sell/ticket/queryOrder?orderId=${sod.id}&uid=${uid}'">
            查看 </a>
            </td>
        </tr>

        <#--<tr >-->
            <#--<td colspan=1></td>-->
            <#--<td colspan=1></td>-->
            <#--<td colspan=1>${sod.remark}</td>-->
            <#--<td colspan=1></td>-->
        <#--</tr>-->
        </#list>
        </tbody>
    </table>

</div>

</body>

<script type="text/javascript">
    function altRows(id){
        if(document.getElementsByTagName){

            var table = document.getElementById(id);
            var rows = table.getElementsByTagName("tr");

            for(i = 0; i < rows.length; i++){
                if(i % 2 == 0){
                    rows[i].className = "evenrowcolor";
                }else{
                    rows[i].className = "oddrowcolor";
                }
            }
        }
    }

    window.onload=function(){
        //altRows('alternatecolor');
        //altRows('yuepiao');

    }
</script>


<style type="text/css">
    table.altrowstable {
        font-family: verdana,arial,sans-serif;
        font-size:11px;
        color:#333333;
        border-width: 1px;
        border-color: #CCC;
        border-collapse: collapse;
    }
    table.altrowstable th {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #CCC;
    }
    table.altrowstable td {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #CCC;
    }
    .oddrowcolor{
        background-color:#FFF;
    }
    .evenrowcolor{
        background-color:#F5F5F5;
    }
</style>



</html>