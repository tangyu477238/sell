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
    <script src="http://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>
    <script src="/sell/js/jquery-1.7.2.min.js"></script>
    <script src="/sell/js/storage.js"></script>
</head>
<body>
<!--     <div class="header"><a href="#"><img src="images/go_back.png"></a><div class="header-word"><p>我的订单</p></div>
</div> -->


<div class="content">
    <img src="/sell/img/orderList-pic.gif">
</div>

<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0; width: 100%">
    <div class="orderList-tab">月票消费记录(仅显示当月之后数据)</div>
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
            <td colspan=1>剩余${pl.totalNum-pl.useNum}次/共${pl.totalNum}次</td>
            <td colspan=1>${pl.price}</td>
            <td colspan=1>${pl.createTime}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</div>

<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0; width: 100%">
    <div class="orderList-tab">预约记录(仅显示当月之后数据)</div>
</div>


<div >

    <table style="width:100%" class="altrowstable" id="yuepiao">
        <thead class="evenrowcolor">
        <th style="width:23%;">有效区间</th>
        <th style="width:23%;">出发/到达</th>
        <th style="width:17%;">类型</th>
        <th style="width:17%;">班次</th>
        <th style="width:20%;">预约天数</th>

<#--        <th style="width:23%;">有效区间</th>-->
<#--        <th style="width:23%;">出发/到达</th>-->
<#--        <th style="width:13%;">类型</th>-->
<#--        <th style="width:12%;">班次</th>-->
<#--        <th style="width:29%;">预约次数</th>-->
        </thead>
        <tbody>
        <#list ydlist as yd>
            <tr class="oddrowcolor">
                <td colspan=1>${yd.bizDate}<br>${yd.endDate}</td>
                <td colspan=1>${yd.fromStation}<br>${yd.toStation}</td>
                <td colspan=1> <#if yd.workday =="0">节假日</#if><#if yd.workday =="1">工作日</#if></td>
                <td colspan=1>${yd.bizTime}</td>
                <td colspan=1>${yd.num}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</div>




<div style="font: 14px; color: #3445a1;font-weight:600; padding: 15px 0; background: #e6fcf0;">
    <div class="orderList-tab">次票消费记录(仅显示当月之后数据)</div>
</div>

<div >
    <#--<div class="orderList-tab">次票消费记录</div>-->

    <table style="width:100%" class="altrowstable" id="alternatecolor">
        <thead class="evenrowcolor">
            <th style="width:22%;">乘车日期/时间</th>
            <th style="width:20%;">出发/到达</th>
            <th style="width:25%;">座位</th>
            <th style="width:14%;">详情</th>
            <th style="width:19%;">服务评分</th>
        </thead>
        <tbody>
        <#list sodlist as sod>
        <tr class="oddrowcolor">
            <td colspan=1 >${sod.bizDate}<br>${sod.bizTime}</td>
            <td colspan=1>${sod.fromStation}<br>${sod.toStation}</td>
            <td colspan=1>
                <#if sod.remark =="预约出票">
                    <font color="red">${sod.info}</font>
                 </#if>
                <#if sod.remark =="月票抵扣">
                    ${sod.info}
                </#if>
                <#if sod.remark =="微信支付">
                    <font color="#1e90ff">${sod.info}</font>
                </#if>
            </td>
            <td colspan=1>
                <a style="font-size:12pt;text-decoration: none;color:#333333 " href="javascript:location.href='/sell/ticket/queryOrder?orderId=${sod.id}&uid=${uid}'">查看 </a>
            </td>
            <td colspan=1>
                <#if sod.ckstate == 0 >
                    <a style="font-size:12pt;text-decoration: none; "  href="###" onclick="pingjia('${sod.bizTime}','${sod.id}','${uid}');">评价</a>
                </#if>

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



function pingjia(time,sid,uid){
    setstorage("sid", sid);
    setstorage("uid", uid);
    location.href='/sell/wx_pingjia.html'
}

function yuding(time,sid,uid){
    var msg = "确认申请后，\r\n将会有3次（"+time+"）自动出票体验，还请注意不要重复买票哦！！！";
    if (confirm(msg)==true) {
        $.ajax({
            "url": "/sell/seatYudingOrder/testYuding?orderId="+sid ,
            "data": "uid="+uid,
            "dataType": "json",
            "type": "GET",
            "success": function(data) {
                if (data.code!=0){
                    alert(data.msg)
                } else {
                    alert("提交成功")
                }

            }
        })

    }
}

function select(){
    location.href='http://gzjhqc.vip/sell/select.html'
}



(function(){
    var data;
    //获取此页面的路径
    var thisPageUrl = document.URL;
    //使用ajax请求获取到微信验证的参数
    $.ajax({
        "url": "/sell/jianyi/getJsTicket" ,
        "data": "url="+thisPageUrl,
        "dataType": "json",
        "type": "GET",
        "success": function(data) {
            wx.config({
                debug: false,
                appId: data.appId,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList:['getLocation','openLocation','checkJsApi','closeWindow']

            });
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList : ['getLocation','openLocation','closeWindow'],
                    success : function(res) {
                    }
                });


                // $('.openLocation').click(function () {
                //     //alert(1)
                //
                // })


            })
        }
    })
})()

function openLocation(id) {

    $.ajax({
        "url": "/sell/jianyi/getDriverGps" ,
        "data": "id="+id,
        "dataType": "json",
        "type": "GET",
        "success": function(data) {
            wx.openLocation({
                latitude: data.latitude,
                longitude: data.longitude,
                name: data.name, // 位置名
                address: data.address // 地址详情说明
            });
        }
    })
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