<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <title>信息补充</title>
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

            text-align: left;
        }

        .header a {
            color: #888888;
            font-size: 13px;
            line-height: 50px;
        }

        .content{
            width: 86%;
            margin: 0 auto;
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


        td {
            height: 25px;
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
        .verify {
            width: 40%;
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
         

       

        function jfzf() {

			var phonenum = $('#phonenum').val();
            if (phonenum == '') {
                alert("请填入正确的手机号");
                return ;
            } else if (typeof (phonenum) == 'undefined') {
                alert("请填入正确的手机号");
                return ;
            } else if (phonenum.length != 11) {
                alert("请填入正确的手机号");
                return ;
            }


            var name = $('#name').val();
            if (name == '') {
                alert("请填入姓名");
                return ;
            }

            var verify = $('#verify').val();
            if (verify == '') {
                alert("请填入验证码");
                return ;
            }


            if (!confirm('确认要提交吗?')) {
                return;
            }

            if (window.is__jfzf) return;
            window.is__jfzf = true;

            var uid = $('#uid').val();
            $.get('/sell/ticket/saveInfo?uid=' + uid + '&verify=' + verify + '&name=' + name + '&phone=' + phonenum, function (res) {
               
                window.is__jfzf = false;
                
                if (res.msg=='成功') {
                    alert('提交成功!');
                    location.replace( '/sell/ticket/ticket?uid=' + uid)  ;
                } else {
                    alert('提交失败:' + res.msg);
                }
            })
        }


        function sendyzm() {

            var name = $('#name').val();
            var phonenum = $('#phonenum').val();
            var uid = $('#uid').val();

            if (window.is__jfzf) return;
            window.is__jfzf = true;


            $.get('/sell/ticket/sendYzm?uid=' + uid + '&name=' + name + '&phone=' + phonenum, function (res) {

                window.is__jfzf = false;

                if (res.msg=='成功') {
                    //alert('短信已发送，请注意查收!');
                    //location.replace( '/sell/ticket/ticket?uid=' + uid)  ;
                    var obj = $("#btn");
                    settime(obj);
                } else {
                    alert('发送失败:' + res.msg);
                }
            })
        }

    </script>

</head>
<body>
<!--
    <div class="header"><a class="btn" href="">返回上一步</a></div>
    -->
<div class="content">
        <input type="hidden" id = "uid" name="uid"  value="${uid}">
        
        <input type="text" class="phone"  name="name" id="name" placeholder="真实姓名">
        <input type="text" class="phone"  name="mobile" id="phonenum" placeholder="手机号">


        <div class="header">
            <input type="text" class="verify"  name="verify" id="verify" placeholder="验证码">
            <#--<a class="btn" href="javascript:sendyzm()">获取验证码</a>-->
            <input type="button" id="btn"  value="获取验证码" onclick="sendemail()" />
        </div>

        <a class="quxiao" href="javascript:jfzf()">提交</a>
   
</div>
<script>
    var countdown=60;
    function sendemail(){

        var name = $('#name').val();
        if (name == '') {
            alert("请填入姓名");
            return ;
        }

        var phonenum = $('#phonenum').val();
        if (phonenum == '') {
            alert("请填入正确的手机号");
            return ;
        } else if (typeof (phonenum) == 'undefined') {
            alert("请填入正确的手机号");
            return ;
        } else if (phonenum.length != 11) {
            alert("请填入正确的手机号");
            return ;
        }
        //发短信
        sendyzm();




    }
    function settime(obj) { //发送验证码倒计时
        if (countdown == 0) {
            obj.attr('disabled',false);
            //obj.removeattr("disabled");
            obj.val("获取验证码");
            countdown = 60;
            return;
        } else {
            obj.attr('disabled',true);
            obj.val("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
                settime(obj) }
            ,1000)
    }
</script>
</body>
</html>