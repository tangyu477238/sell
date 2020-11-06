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
                initQuery();
            });

            //初始化
            initQuery();
        });


        function initQuery() {

            var q = $('#route').val();
            var w = $('#time').val();

            var uid = $('#uid').val();
            var lp = $('#lp').val();
            setStation(q); //初始化上车点数据
            $('#txtHint').html('');//先清空再重新加载
            $('#txtHint').load("/sell/ticket/queryBanci", {lp: lp, uid: uid, route: q, time: w});
        }

        function setStation(q) {
            $("#routeStation").empty();
            if(q==1){
                $("#routeStation").append("<option value='宏发'>宏发</option>");
            }else if(q==2){
                $("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
            }else if(q==16){
                $("#routeStation").append("<option value='猎德'>猎德</option><option value='珠江新城'>珠江新城</option><option value='潭村'>潭村</option>");
            }else if(q==15){
                $("#routeStation").append("<option value=''>请选择上车地点</option><option value='幸福誉第一期'>幸福誉第一期</option><option value='幸福誉第三期'>幸福誉第三期</option><option value='绿地城'>绿地城</option>");
            }else if(q==17){
                $("#routeStation").append("<option value='北部万科城'>北部万科城</option>");
            }else if(q==18){
                $("#routeStation").append("<option value='宏发'>宏发</option>");
            }
        }



        function jianyan() {

            var uid = $('#uid').val();





            // if(uid!='bdf9eb628f0b464dbefaaac694e680ab'
            //     &&uid!='a365c62fd4c241f6a8b277ebd495c5d3'){
            // 	alert('系统维护');
            // 	return false;
            // }

            var r = $('#route').val();
            var t = $('#time').val();
            var y = $('#moment').val();
            var routeStation = $('#routeStation').val();


            //if(t=='2020-06-29' && uid!='bdf9eb628f0b464dbefaaac694e680ab'
            //	 && uid!='a365c62fd4c241f6a8b277ebd495c5d3'){
            //	alert('系统紧急维护,请稍后再试!');
            //	return false;
            //}


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

        <div style="width: 100%;margin-bottom: 20px; text-align: center;color: ${lpColor}">
            <h1>${lpName}</h1>
        </div>



        <form name="form" action="/sell/ticket/cseat" onsubmit="return jianyan(this)" method="get">

            <input type="hidden" id="uid" name="uid"  value="${uid}">
            <input type="hidden" id="lp" name="lp"  value="${lp}">

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



            <div id="aaaa" style="text-align: right;min-height: 3em;margin-top: 0.5em;">
                <a href="javascript:location.href='http://gzjhqc.vip/sell/shangchedian.html'">上车点位置&地图导航</a>
            </div>

            <div class="button" style="background-color: ${lpColor};margin-top: 0.2em;">
                <input type="submit" name="submit" style=" background:none; border:none; color:#fff; width:100%; height:100%;" value="下一步"></div>
            <div style="color:red; padding:10px;font-size: 15px;">
                温馨提示：
                <br/>
                <br/>珠江新城至幸福誉方向上车点时间:猎德(18:20)，珠江新城(18:25)，谭村(18:35)！
                <br/>珠江新城专线不支持月票购票！
                <br/>
                <br/>班次如购买错误，系统无法实现退还，请您注意选择正确的班次！
                <br/>过期车票一律作废，逃票、套票弄虚做假者我司将其列入黑名单。
                <br/>为避免超载，请主动为小朋友购买车票，谢谢！

                <br/>
                <br/>6月4日起调整售票：
                <br/>每天18点可以买第3天7点和7点之前的票
                <br/>每天19点可以买第3天8点和8点之前的票
                <br/>每天20点可以买第3天全天和之前的票
            </div>
        </form>

    </div>

</div>


</body>
</html>
