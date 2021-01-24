//var configIp="192.168.16.12";
//var configPort="8090";
var configIp="gzjhqc.vip";
var configPort="80";
var configApp="/sell";
var mesPath="http://"+configIp+":"+configPort+configApp;

var mesImgPath="http://dj.ctoffice.net:80/mobile/headPhoto/";

var mesTrainPath="https://kyfw.12306.cn/otn";
// 
// //ajaxRqu
// window.AJAX = {
    // callBack: {},
    // index: 1,
    // get: function(url, succCall, errCall, timeout){
        // var id = this.index++;
        // this.callBack[id] = [succCall, errCall];
        // uexXmlHttpMgr.open(id, 'get', url, (timeout || 0));
        // this._send(id);
    // },
    // post: function(url, data, succCall, errCall, timeout){
        // var id = this.index++;
        // this.callBack[id] = [succCall, errCall];
        // uexXmlHttpMgr.open(id, 'post', url, (timeout || 0));
        // if (data) {
            // for (var k in data) {
                // uexXmlHttpMgr.setPostData(id, 0, k, data[k]);
            // }
        // }
        // this._send(id);
    // },
    // _send: function(id){
		// uexXmlHttpMgr.onData = this.onData;
        // uexXmlHttpMgr.send(id);
    // },
    // onData: function(inOpCode, inStatus, inResult, requestCode, response){
		// var that = AJAX, callBack = that.callBack[inOpCode] || [];
        // if (inStatus == -1) {
            // callBack[1] && callBack[1]();
        // }else  if (inStatus == 1) {
            // callBack[0] && callBack[0](inResult);
        // }
    // }
// };





//
//
//function dorpDownUpdate(){  
//	//uexWindow.toast('1','5','下拉...',2000);
//	uexWindow.setBounceParams('0', '{"levelText":"上次更新：'+Now()+'"}');
//    uexWindow.resetBounceView('0');  
//}  
//  
//function pullUpUpdate(){  
//    //uexWindow.toast('1','5','上拉...',2000);
//    uexWindow.resetBounceView('1');  
//}  
//  

function setPageBounce(downcb, upcb){  
        var s = ['0', '0'];  
        uexWindow.onBounceStateChange = function (type,status){  
                if(downcb && type==0 && status==2) downcb();  
                if(upcb && type==1 && status==2) upcb();  
        }  
        uexWindow.setBounce("1");  
        if(downcb){  
                s[0] = '1';  
                uexWindow.setBounceParams(0,  
                {"imagePath":"res://arrow.png","textColor":"#333","pullToReloadText":"上拉即可刷新","releaseToReloadText":"松开即可刷新","loadingText":"加载中，请稍等"});  
                uexWindow.notifyBounceEvent("0","1");  
				uexWindow.setBounceParams('0', '{"levelText":"上次更新：'+getTodayTime()+'"}');
        }  
        if(upcb){  
                s[1] = '1';  
//              uexWindow.setBounceParams(1,  
//              {"imagePath":"res://arrow.png","textColor":"#333","pullToReloadText":"上拉刷新","releaseToReloadText":"释放刷新","loadingText":"加载中，请稍等"});  
                uexWindow.notifyBounceEvent("1","1");
        }  
        uexWindow.showBounceView("0","#f2f0eb",s[0]);  
		uexWindow.showBounceView("1",'#f2f0eb','0');
//      uexWindow.showBounceView("1","#333",s[1]);  
}  

