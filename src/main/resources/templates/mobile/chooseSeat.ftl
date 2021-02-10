<!DOCTYPE html>

<html lang="zh-CN" class="translated-ltr">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport">
    <title>在线选座</title>
    <link rel="stylesheet" type="text/css" href="/sell/css/xuan.css">
    <script>
        function yan() {
            //alert(1)
            var checked = document.getElementById('checked');
            var con = checked.innerHTML;
            if (con == '') {
                alert('请选择座位');
                return ;
            }
            var msg = "车票一经售出是不支持退票的，\r\n您仍然确定购买吗？";
            if (confirm(msg)==true) {
                var form = document.getElementById("form");
                form.submit();
            }
        }
    </script>
    <link type="text/css" rel="stylesheet" charset="UTF-8" href="/sell/css/translateelement.css">
</head>
<body>
<div class="content">
    <div class="header" style="position:absolute;top:0px;">
        <a href="javascript:history.go(-1)">
            <div class="btn" href="">
                <font style="vertical-align: inherit;">  <font style="vertical-align: inherit;">返回上一步</font> </font>
            </div>
        </a>
    </div>
    <div class="state">
        <div><i class="xuanzhon"></i><font style="vertical-align: inherit;"><font
                style="vertical-align: inherit;">选中</font></font></div>
        <div><i class="kexuan"></i><font style="vertical-align: inherit;"><font
                style="vertical-align: inherit;">可选</font></font></div>
        <div><i class="buke"></i><font style="vertical-align: inherit;"><font
                style="vertical-align: inherit;">不可选</font></font></div>
    </div>
    <style>
        .dfdsfs {
            padding-top: 15px;
            padding-left: 15px;
            width: 228px;
            height: 454px;
            background: url('/sell/img/xuanzuo.png') no-repeat scroll 0px 0px transparent;
        }

        .dfdsfs table {
            width: 212px;
            height: 400px;
            overflow: hidden;
        }
    </style>
    <div style="height:60px;"></div>
    <div align="center">
        <div id="234l32j4" class="dfdsfs" align="left" style="">

        </div>
    </div>
    <div class="beizhu"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">
        已选定：</font></font><span id="checked"></span>
    </div>

    <div id="aaaa" style="text-align: right;min-height: 2em;margin-top: 0.3em;margin-right: 1em;">
        <a href="javascript:location.href='/sell/seatYudingOrder/yudingguize?uid=${uid}'">没座了?购票麻烦?试试提前出票</a>
    </div>
    <div class="footer">
        <form id = "form" name="form" action="/sell/ticket/cord"  method="get">
            <input type="hidden" id="seat" name="seat">
            <input type="hidden" name="route" value="${route}">
            <input type="hidden" name="time" value="${time}">
            <input type="hidden" name="moment" value="${moment}">
            <input type="hidden" id="num" name="num">
            <input type="hidden" name="uid"  value="${uid}">
            <input type="hidden" name="routeStation"  value="${routeStation}">
            <input type="hidden" name="lp"  value="${lp}">

            <div class="bottom_a" style="background-color: ${lpColor}">
                <font style="vertical-align: inherit;">
                    <font style="vertical-align: inherit;">
                        <button type="button"  onclick="yan();" style="border:none; background:none;width:100%;color:#fff;
                            font-size:18px;line-height:45px;cursor:pointer;" value="确认">确认</button>
                    </font>
                </font>
            </div>
        </form>
    </div>
</div>

<script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/sell/js/catOP_wx.js"></script>
<script type="text/javascript">

//var seatlist ='${seatlist}';
//alert(seatlist)
    var busType = {
        "id": "1",
        "type": "\u8f66\u578b45\u5ea7",
        "arrangement": "\u5de62\u6392\uff0c\u53f32\u6392\uff0c12\u5217",
        "seat": "40",
        "data": '${seatlist}',
        //"data": "{\"1\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"1\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"1\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"1\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"1\u63924\u5ea7\"}],\"2\":[{\"colIndex\":\"1\",\"seatType\":\"2\",\"name\":\"\u7231\u5fc3\u5ea7\u4f4d\"},{\"colIndex\":\"2\",\"seatType\":\"2\",\"name\":\"\u7231\u5fc3\u5ea7\u4f4d\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"2\",\"name\":\"\u7231\u5fc3\u5ea7\u4f4d\"},{\"colIndex\":\"5\",\"seatType\":\"2\",\"name\":\"\u7231\u5fc3\u5ea7\u4f4d\"}],\"3\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"3\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"3\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"3\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"3\u63924\u5ea7\"}],\"4\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"4\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"4\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"4\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"4\u63924\u5ea7\"}],\"5\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"5\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"5\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"5\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"5\u63924\u5ea7\"}],\"6\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"6\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"6\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"6\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"6\u63924\u5ea7\"}],\"7\":[{\"colIndex\":\"1\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"2\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"7\u63921\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"7\u63922\u5ea7\"}],\"8\":[{\"colIndex\":\"1\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"2\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"8\u63921\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"8\u63922\u5ea7\"}],\"9\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"9\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"9\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"9\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"9\u63924\u5ea7\"}],\"10\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"10\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"10\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"10\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"10\u63924\u5ea7\"}],\"11\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"11\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"11\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"0\",\"name\":\"\u8fc7\u9053\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"11\u63923\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"11\u63924\u5ea7\"}],\"12\":[{\"colIndex\":\"1\",\"seatType\":\"1\",\"name\":\"12\u63921\u5ea7\"},{\"colIndex\":\"2\",\"seatType\":\"1\",\"name\":\"12\u63922\u5ea7\"},{\"colIndex\":\"3\",\"seatType\":\"1\",\"name\":\"12\u63923\u5ea7\"},{\"colIndex\":\"4\",\"seatType\":\"1\",\"name\":\"12\u63924\u5ea7\"},{\"colIndex\":\"5\",\"seatType\":\"1\",\"name\":\"12\u63925\u5ea7\"}],\"total\":45,\"rows\":12,\"cols\":5,\"left\":2,\"right\":2}",
        "total_seat": "51"
    };
    var maxSelected = 4;// 最多选取座位数量.
    var maxSelectedMsg = '亲，别太贪心了，一次不能预订4张以上的票哦。';
    var seatdata = '${selected}';// 已售
    var imgPath = '../img/';//sell/img/
    $(function () {
        by.initData($.parseJSON(busType.data), imgPath, seatdata, maxSelected, maxSelectedMsg);
    })
</script>
</body>
</html>