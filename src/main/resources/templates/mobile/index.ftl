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

                $("#routeStation").empty();
                if(q==1){
                    $("#routeStation").append("<option value='宏发'>宏发</option>");
                }else if(q==2){
                    $("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
                }else if(q==16){
                    $("#routeStation").append("<option value='珠江新城'>珠江新城</option>");
                }else if(q==15){
                    $("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
                }
                $('#txtHint').html('');//先清空再重新加载
                $('#txtHint').load("/sell/ticket/queryBanci", {uid: uid, route: q, time: w});
            });
            var q = document.getElementById("route").value;
			$("#routeStation").empty();
			if(q==1){
				$("#routeStation").append("<option value='宏发'>宏发</option>");
			}else if(q==2){
				$("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
			}else if(q==16){
				$("#routeStation").append("<option value='珠江新城'>珠江新城</option>");
			}else if(q==15){
				$("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
			}
            var w = document.getElementById("time").value;
            var uid = $('#uid').val();
            $('#txtHint').load("/sell/ticket/queryBanci", {uid: uid, route: q, time: w});


        });

        function jianyan() {
			
			var uid = $('#uid').val();

			if(uid!='bdf9eb628f0b464dbefaaac694e680ab'){
				alert('系统维护');
				return false;
			}
		
            var r = $('#route').val();
            var t = $('#time').val();
            var y = $('#moment').val();
            var routeStation = $('#routeStation').val();

            if (r==''){
                alert('亲，日期（或时间或路线）一定要选哦');
                return false;
            }

            if (t==''){
                alert('亲，日期（或时间或路线）一定要选哦');
                return false;
            }
            if (y==''){
                alert('亲，日期（或时间或路线）一定要选哦');
                return false;
            }
            if (routeStation==''){
                alert('亲，上车点，一定要选哦');
                return false;
            }

            return true;
        }
    </script>

</head>


<body>
<div class="content">

    <div class="main">

        <div style="width: 100%;height: 8em;margin-bottom: 20px; text-align: center;">
            <img src="/sell/img/20191015222244.jpg" style="height: 8em;">
        </div>



        <form name="form" action="/sell/ticket/cseat" onsubmit="return jianyan(this)" method="get">

            <input type="hidden" id="uid" name="uid"  value="${uid}">

            <select name="route" id="route" class="address">
                <#--<option value="">请选择乘车路线</option>-->
                <#list plList as pl>
                    <option value="${pl.id}">${pl.fromStation}---${pl.toStation}</option>
                </#list>
            </select>
            <select name="time" id="time" class="address">
                <#list daylist as day>
                    <option value="${day}">${day}</option>
                </#list>
            </select>

            <div id="txtHint">

            </div>

            <div id="station">
                <select name="routeStation" id="routeStation" class="address">

                </select>
            </div>





            <div class="button">
                <input type="submit" name="submit" style=" background:none; border:none; color:#fff; width:100%; height:100%;" value="下一步"></div>
            <div style="color:red; padding:10px;font-size: 14px;">
                   温馨提示1：
                <br/>班次如购买错误，系统无法实现退还，请您注意选择正确的班次！
                <br/>为避免超载，请主动为小朋友购买车票，谢谢！
                <br/>
                <br/>最新调整售票：
                <br/>每天19点可以买第3天7点和7点之前的票
                <br/>每天20点可以买第3天8点和8点之前的票
                <br/>每天21点可以买第3天全天和之前的票
            </div>
        </form>

    </div>

</div>


</body>
</html>
