<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>满座啦</title>
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

        .content {
            width: 100%;
            text-align: center;
            padding-top: 50px;
        }

        .content img {
            width: 88px;
            height: 88px;
        }

        h3 {
            margin-top: 20px;
        }

        span {
            color: #8D8D8D;
            line-height: 50px;
            font-size: 13px;
        }

        .footer {
            width: 88%;
            height: 50px;
            margin: 0 auto;
            text-align: center;
        }

        .choise {
            display: block;
            width: 100%;
            height: 45px;
            background: #F66D22;
            margin-top: 50px;
            color: #fff;
            line-height: 45px;
            border-radius: 3px;
        }
    </style>
</head>
<body>
<div class="content"><img src="/sell/img/manzuo.png">
    <h3>班车已满座了</h3><span>抱歉，请选择其他班次</span></div>
<div class="footer"><a class="choise"  href="javascript:history.go(-1)" >选择其他的班次</a></div>
</body>
</html>