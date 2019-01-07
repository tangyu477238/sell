<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>购买月票成功</title>
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
    <h4>购买月票成功</h4>
    <span>本次订票信息及取票信息已经发送您的手机</span>
    <#--<p> 购票类型：订座票</p>-->
    <#--<p>订单号：${sod.orderNo}</p>-->
    <#--<p></p>-->
    <#--<p>金额:30</p>-->
    <div id="sendMsg"></div>
</div>
<div class="message">
    <#--<div class="qupiao">取票密码没有收到？</div>-->
    <#--<a href="javascript:location.reload();">再次发送</a>-->
    <#--<br>-->
    <div class="clear"></div>
    <span>1.完成订票后，您将收到短信提示，包括本次订购信息和收费密码</span>
    <br>
    <span>2.取材密码是您取材时唯一的凭证，请妥善保存</span>
</div>
<div class="shuoming">
    <h3>取票说明</h3>
    <span>1.前往乘车现场，在班车所在地自动取票终端输入购买的取票密码，即可完成取票</span>
    <br>
    <span>2.如现场终端无法打印车票，确定订票信息无误后，联系现场工作人员，协助取票</span>
    <br>
    <span>3.打印订座票后  请配合购买的原纸质票一同检票乘车。</span>
</div>
<div class="footer">
    <#--<a href="index">完成预约</a>-->
</div>
<script>
    // $('#sendMsg').load('sendMsgx' + location.search);
</script>
</body>
</html>