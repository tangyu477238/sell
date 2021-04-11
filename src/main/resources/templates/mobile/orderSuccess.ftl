<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>支付成功</title>
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

        .main {
            width: 88%;
            height: auto;
            margin: 0 auto;
            text-align: center;
            padding-top: 20px;
            margin-bottom: 20px;
        }

        .main img {
            width: 88px;
            height: 88px;
        }

        h4 {
            margin-top: 10px;
            margin-bottom: 5px;
        }

        .main span {
            font-size: 8px;
            color: #9D9D9D;
        }

        .message {
            width: 88%;
            background: #F3F3F3;
            height: 100px;
            margin-top: 20px;
            margin: 0 auto;
            padding: 10px 5px;
            border-radius: 3px;
        }

        .qupiao {
            float: left;
            line-height: 25px;
            font-size: 14px;
        }

        .message a {
            display: block;
            width: 80px;
            height: 25px;
            line-height: 25px;
            color: #fff;
            background: #F66D22;
            text-align: center;
            font-size: 13px;
            float: left;
            border-radius: 3px;
        }

        .message span {
            font-size: 10px;
            color: #A7A7A7
        }

        .clear {
            clear: both;
        }

        .shuoming {
            width: 88%;
            margin: 0 auto;
        }

        .shuoming h3 {
            margin-top: 30px;
        }

        .shuoming span {
            font-size: 10px;
            color: #505050
        }

        .footer {
            width: 88%;
            margin: 0 auto;
            text-align: center;
        }

        .footer a {
            display: block;
            width: 100%;
            height: 45px;
            line-height: 45px;
            color: #fff;
            background: #F66D22;
            margin-top: 20px;
            border-radius: 3px;
        }
    </style>
    <script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>
</head>
<body>
<!--
    <div class="header"><a class="btn" href="">返回上一步</a></div>
    -->
<div class="main">
    <img src="/sell/img/success.png">
    <h4>支付成功，但不一定出票成功！</h4>
    <h4>请在“我的订单”中查看,核实购票信息！！！！</h4>
    <#--<p> 购票类型：订座票</p>-->
    <p>订单号：${orderNo}</p>
    <#--<p></p>-->
    <#--<p>金额:30</p>-->
    <div id="sendMsg"></div>
</div>
<div class="message">
    <div class="qupiao">购票信息没有收到？</div>
    <#--<a href="javascript:location.reload();">再次发送</a>-->
    <br>
    <div class="clear"></div>
    <span style="color: red">1.因微信公众号下线模板消息功能(2021-04-30)，购票成功，请在“我的订单”中查看购票信息！！！！</span>
    <br>
    <span style="color: red"></span>
</div>
<div class="shuoming">
    <h3>乘车说明</h3>
    <span>1.前往乘车现场，展示购票订单详情即可！</span>
    <br>
    <span>2.司机可通过购票系统的座位图，查询到每位业主的购票信息，大巴的空位情况！</span>
    <br>
    <span>3.为避免不必要的麻烦，请不要使用过期或重复的截图订单详情逃票上车。</span>
</div>
<div class="footer">
    <#--<a href="index">完成预约</a>-->
</div>
<script>

pushHistory();
window.addEventListener("popstate", function(e) {
    WeixinJSBridge.invoke('closeWindow',{},function(res){});
}, false);
function pushHistory() {
    var state = {
        title: "title",
        url: "#"
    };
    window.history.pushState(state, "title", "#");
}
</script>
</body>
</html>