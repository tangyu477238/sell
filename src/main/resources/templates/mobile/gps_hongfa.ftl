<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport"/>
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <title>位置信息</title>
</head>
<body>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>
<script src="/sell/js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">


    (function(){
        var data;
        //获取此页面的路径
        var thisPageUrl = location.href.split('#')[0];
        //使用ajax请求获取到微信验证的参数
        $.ajax({
            "url": "/sell/jianyi/getJsTicket" ,
            "data": "url="+thisPageUrl,
            "dataType": "json",
            "type": "GET",
            "success": function(data) {
                wx.config({
                    debug: false,
                    appId: data.appId,
                    timestamp: data.timestamp,
                    nonceStr: data.nonceStr,
                    signature: data.signature,
                    jsApiList:['getLocation','openLocation','checkJsApi','closeWindow']

                });
                wx.ready(function() {
                    wx.checkJsApi({
                        jsApiList : ['getLocation','openLocation','closeWindow'],
                        success : function(res) {
                        }
                    });
                    // wx.getLocation({
                    //     type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                    //     success: function (res) {
                    //         wx.openLocation({
                    //             latitude: res.latitude,
                    //             longitude: res.longitude,
                    //             name: '汇众大厦', // 位置名
                    //             address: '海淀区上地七街1号汇众大厦' // 地址详情说明
                    //         });
                    //     }
                    // });

                    wx.openLocation({
                        latitude:  Number('23.132287'),
                        longitude:  Number('113.331769'),
                        name: '上车点：宏发', // 位置名
                        address: '广东省广州市天河区宏发大厦西门美宜佳门口' // 地址详情说明
                    });
                })
            }
        })
    })()
 
</script>
</html>

