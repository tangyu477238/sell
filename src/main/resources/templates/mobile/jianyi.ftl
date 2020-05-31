<!DOCTYPE html>
<html class="um landscape min-width-240px min-width-320px min-width-480px min-width-768px min-width-1024px">
    <head>
        <title>新增班车建议</title>
        <meta charset="utf-8">
        <meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
        <link rel="stylesheet" href="/sell/css/fonts/font-awesome.min.css">
        <link rel="stylesheet" href="/sell/css/ui-box.css">
        <link rel="stylesheet" href="/sell/css/ui-base.css">
        <link rel="stylesheet" href="/sell/css/ui-color.css">
        <link rel="stylesheet" href="/sell/css/appcan.icon.css">
        <link rel="stylesheet" href="/sell/css/appcan.control.css">
        <link rel="stylesheet" href="/sell/css/jianyi/jianyi.css">

        <script type="text/javascript" src="/sell/js/jquery-1.7.2.min.js"></script>

    <script>
        function add() {

            var uid = $('#uid').val();
            var stype = $('#stype').val();
            var fstation = $('#fstation').val();
            var tstation = $('#tstation').val();
            var banci = $('#banci').val();
            var remark = $('#remark').val();

            if (stype==''){
                alert("请填写是工作日还是节假日！")
                return;
            }
            if (fstation==''){
                alert("请填写出发地！")
                return;
            }
            if (tstation==''){
                alert("请填写到达地！")
                return;
            }
            if (banci==''){
                alert("请填写班车时间！")
                return;
            }


            if(confirm('确实要提交吗?')){
                <#--var orderId = '${sod.id}';-->
                <#--var uid = '${uid}';-->


                if (window.is__jfzf) return;
                window.is__jfzf = true;

                $.get("/sell/jianyi/add?stype=" + stype + "&fstation=" + fstation
                    +"&tstation=" + tstation+ "&uid=" + uid+ "&banci=" + banci+ "&remark=" + remark,function(res){
                    window.is__jfzf = false;
                    //var str2 = JSON.stringify(res);
                    if (res.msg=='成功') {
                        alert('提交成功!');
                        WeixinJSBridge.invoke('closeWindow',{},function(res){});
                    } else {
                        alert('提交失败:' + res.msg);
                    }
                });

            }
        }
    </script>

    </head>
    <body class="" ontouchstart>
    <form name="form"  method="get">
        <input type="hidden" id="uid" name="uid"  value="${uid}">
        <div class="bc-bg" tabindex="0" data-control="PAGE" id="Page">
            
            <div class="ub ub-ver ub-ac ub-con bc-bg" data-control="FLEXBOXVER" id="ContentFlexVer">
<div class="ub ub-ver  ub-fh ">
    <div class="uinn-a1">
			
            <div class="uba b-gra3 uc-a1 c-wh ub uinn-a7 ub-ac">
                <div class="ulev-app1 umw4">
                    类型
                </div>
                <div class="ub ub-ac umh5 ub-f1">
                    <div class="uinput ub ub-f1">
                        <input placeholder="如:工作日/节假日" type="text" class="ub-f1" id="stype">
                    </div>
                </div>
            </div>
			<div class="uba b-gra3 uc-a1 c-wh ub uinn-a7 ub-ac umar-at1">
                <div class="ulev-app1 umw4">
                    出发
                </div>
                <div class="ub ub-ac umh5 ub-f1">
                    <div class="uinput ub ub-f1">
                        <input placeholder="如:幸福誉" type="text" class="ub-f1"  id="fstation">
                    </div>
                </div>
            </div>
            <div class="uba b-gra3 uc-a1 c-wh ub uinn-a7 ub-ac umar-at1">
                <div class="ulev-app1 umw4">
                    到达
                </div>
                <div class="ub ub-ac umh5 ub-f1">
                    <div class="uinput ub ub-f1">
                        <input placeholder="如:宏发" type="text" class="ub-f1"  id="tstation">
                    </div>
                </div>
            </div>
            <div class="uba b-gra3 uc-a1 c-wh ub uinn-a7 ub-ac umar-at1">
                <div class="ulev-app1 umw4">
                    班次
                </div>
                <div class="ub ub-ac umh5 ub-f1">
                    <div class="uinput ub ub-f1">
                        <input placeholder="如：07:20" type="text" class="ub-f1"  id="banci">
                    </div>
                </div>
            </div>
            <div class="uba b-gra3 uc-a1 c-wh ub uinn-a7 ub-ac umar-at1">
                <div class="ulev-app1 umw4">
                    其他
                </div>
                <div class="ub ub-ac umh5 ub-f1">
                    <div class="uinput ub ub-f1">
                        <input placeholder="想说啥都行" type="text" class="ub-f1"  id="remark">
                    </div>
                </div>
            </div>
            <div class="ub ub-ver">
                <div class="ub ub-pe uinn-a6 ulev-4">
                    <div class="sc-text">
                        
                    </div>
                    <div class="sc-text-active">
                        
                    </div>
                </div>
                <div class="uinn-at1">
                    <div class="button ub ub-ac bc-text-head ub-pc bc-btn uc-a1 " onclick="add()" style="background-color:#F66D22">
                        提交
                    </div>
                </div>
            </div>

            <div style="color:red; padding:10px;font-size: 14px;">
                温馨提示：
                <br/>您的建议，仅作为新增班车的重要依据！
                <br/>
            </div>

    </div>
</div>
        <div class="uinn-4b"></div>
        </div>
        </div>
    </form>
    </body>
</html>