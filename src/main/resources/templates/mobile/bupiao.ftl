<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>补票</title></head>
<link rel="stylesheet" type="text/css" href="/sell/css/yuding.css">
<script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>
<script>
    $(document).ready(function () {
        $("#route").change(function () {
            var uid = $('#uid').val();
            var route = document.getElementById("route").value;
            $('#txtHint').load("/sell/ticket/queryBanciBp", {uid: uid, route: route});
            $('#txtHint').html('');
        });
        var route = document.getElementById("route").value;
        if (route > 0) {
            var uid = $('#uid').val();
            $('#txtHint').load("/sell/ticket/queryBanciBp", {uid: uid, route: route});
        }
    });

    function jianyan() {
        var r = $('#route').val();
        var y = $('#moment').val();
        if (y == '' || r == '') {
            alert('亲，日期（或时间或路线）一定要选哦（笑脸）');
            return false;
        }
        return true;
    }
</script>
<body>
<div class="content">
    <div class="main">
        <form name="form" action="/sell/ticket/cordBupiao" onSubmit="return jianyan(this)" method="get">

            <input type="hidden" id="uid" name="uid"  value="${uid}">

            <select name="route" id="route" class="address">
                <#list plList as pl>
                    <option value="${pl.id}"> ${pl.fromStation}---${pl.toStation}</option>
                </#list>
            </select>
            <div>日期：${day}</div>

            <br>

            <div id="txtHint">

            </div>


            <br>
            <input name="act" type="hidden" value="confirm">
            <p style="color:red;">温馨提示：发车前后10分钟可以补票，如购买错误，系统无法实现退还，请向当值司机确认有空位后才补票。</p>
            <div class="button">
                <input type="submit" name="submit"  style=" background:none; border:none; color:#fff; width:100%; height:100%;"
                       value="下一步">
            </div>
        </form>
    </div>
</div>
</body>
</html>