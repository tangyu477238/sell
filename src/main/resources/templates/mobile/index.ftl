<!DOCTYPE html>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport">
    <title>车票预订</title>
    <link rel="stylesheet" type="text/css" href="/sell/css/yuding.css">
    <link type="text/css" rel="stylesheet" charset="UTF-8" href="/sell/css/translateelement.css">
    <script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>

    <script>
        $(document).ready(function () {
            $("#route,#time").change(function () {
                var q = document.getElementById("route").value;
                var w = document.getElementById("time").value;
                var uid = $('#uid').val();
                $('#txtHint').load("/sell/ticket/queryBanci", {uid: uid, route: q, time: w});
                $('#txtHint').html('');
            });
            var q = document.getElementById("route").value;
            var w = document.getElementById("time").value;
            var uid = $('#uid').val();

            $('#txtHint').load("/sell/ticket/queryBanci", {uid: uid, route: q, time: w});


        });

        function jianyan() {
            var r = $('#route').val();
            var t = $('#time').val();
            var y = $('#moment').val();
            if (r !== '') {
                if (t !== '') {
                    if (y !== '') {
                        return true;
                    } else {
                        alert('亲，日期（或时间或路线）一定要选哦（笑脸）');
                        return false;
                    }
                } else {
                    alert('亲，日期（或时间或路线）一定要选哦（笑脸）');
                    return false;
                }
            } else {
                alert('亲，日期（或时间或路线）一定要选哦（笑脸）');
                return false;
            }
        }
    </script>

</head>


<body>
<div class="content">

    <div class="main">
        <form name="form" action="/sell/ticket/cseat" onsubmit="return jianyan(this)" method="get">

            <input type="hidden" id="uid" name="uid"  value="${uid}">

            <select name="route" id="route" class="address">
                <#--<option value="">请选择乘车路线</option>-->
                <#list plList as pl>
                    <option value="${pl.id}">${pl.fromStation}---${pl.toStation}</option>
                </#list>
            </select>
            <select name="time" id="time" class="address">
                <#--<option value="">请选择乘车日期</option>-->
                <!--	<option value="">乘车日期</option>-->
                <#--<option value="${day1}">${day1}</option>-->
                <#--<option value="${day2}">${day2}</option>-->
                <#--<option value="${day3}">${day3}</option>-->

                <#list daylist as day>
                    <option value="${day}">${day}</option>
                </#list>

            </select>
            <br>


            <br>
            <div id="txtHint">

            </div>


            <div class="button">
                <input type="submit" name="submit" style=" background:none; border:none; color:#fff; width:100%; height:100%;" value="下一步"></div>
            <div style="color:red; padding:10px;">
                   温馨提示：班次如购买错误，系统无法实现退还，请您注意选择正确的班次！
            </div>
        </form>

    </div>

</div>


</body>
</html>