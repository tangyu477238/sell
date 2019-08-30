<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>订单确定</title>
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
<form name="form" action="" onsubmit="yan(this)" method="get">
    <input type="hidden" id = "uid" name="uid"  value="${uid}">
    <input type="hidden" id = "orderId" name="orderId"  value="${orderNewId}">
    <input type="hidden" id = "sysl" name="sysl"  value="${sysl}">
    <input type="hidden" id = "num" name="num"  value="${sod.num}">


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
        <script>$('.main').css('height', $('.main_info').height() + 60);</script>
        <div class="read">
            <span>1.请仔细核对订单信息，车票一旦售出，无法退换</span>
            <br>
            <span>2.请您在2分钟内完成支付;如果尚未完成支付，您选择的座位将被取消</span>
        </div>
        <#--短信通知-->
        <#--<input type="text" class="phone" value="13552379492" name="mobile" id="phonenum" placeholder="手机号">-->
        <table style="width:100%">
            <tr>
                <td>
                    <a style="font-size:12pt; background:none; border:none; color:#fff; width:100%; height:100%;"
                       href="javascript:location.href='/sell/ticket/payOrder?orderId=${orderNewId}&uid=${uid}'">
                        <div class="zhifu">微信支付</div>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <a style="font-size:12pt; background:none; border:none; color:#fff; width:100%; height:100%;"
                       href="javascript:jfzf();">
                        <div class="zhifu">月票抵扣(${sysl})</div>
                    </a>
                </td>
            </tr>
            <tr>
            </tr>
        </table>
        <a class="quxiao" href="javascript:cancelOrder()">取消订单</a>
    </div>
</div>
<script>

    pushHistory();
    window.addEventListener("popstate", function(e) {
        cancelOrder();
    }, false);
    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, "title", "#");
    }


    function cancelOrder() {
        if(confirm('确实要取消该订单吗?')){

            postRuest();

        }
    }


    function postRuest(){
        var uid = $('#uid').val();
        var order = $('#orderId').val();

        if (window.is__jfzf) return;
        window.is__jfzf = true;

        $.get("/sell/ticket/delsorder?orderId=" + order + "&uid=" + uid,function(res){
            window.is__jfzf = false;
            //var str2 = JSON.stringify(res);
            if (res.msg=='成功') {
                alert('订单取消成功!');
                WeixinJSBridge.invoke('closeWindow',{},function(res){});
            } else {
                alert('取消失败:' + res.msg);
            }
        });
    }


    function jfzf() {

        if(confirm('确定月票抵扣吗?')){
            <#--var orderId = '${sod.id}';-->
            <#--var uid = '${uid}';-->

            var uid = $('#uid').val();
            var order = $('#orderId').val();

            var num = $('#num').val();
            var sysl = $('#sysl').val();

            if(parseInt(num)>parseInt(sysl)){
                alert("月票不足！")
                return ;
            }

            if(num>4){
                alert("同一班车最多只能4张月票！")
                return ;
            }

            if (window.is__jfzf) return;
            window.is__jfzf = true;

            $.get("/sell/ticket/yuepiaoOrder?orderId=" + order + "&uid=" + uid,function(res){
                window.is__jfzf = false;
                //var str2 = JSON.stringify(res);
                if (res.msg=='成功') {
                    alert('恭喜您，购票成功!');
                    WeixinJSBridge.invoke('closeWindow',{},function(res){});
                } else {
                    alert('支付失败:' + res.msg);
                }
            });

        }


    }

</script>
</body>
</html>