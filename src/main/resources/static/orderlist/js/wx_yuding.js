var routeArray = new Array();
var dateArray = new Array();
var timeArray = new Array();
var stationArray = new Array();

var route=''; //传值  
var date=''; //传值  
var time=''; //传值
var routeStation=''; //传值

var routeObj;
var holiday = 1;

var ydsl=0;
var sysl=0;

(function($) {
    appcan.button("#nav-left", "btn-act",
    function() {});
    appcan.button("#nav-right", "btn-act",
    function() {});
    appcan.button("#Button_footer", "ani-act",
    function() {});
    
    appcan.ready(function() {
        
        
        date = formatDate(new Date().getTime());
        var datestr ='<div id ="date1" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="setHoliday(1)">工作日</div>'
        +'<div id ="date0" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="setHoliday(0)">周末/节假日</div>';
        $("#date").html(datestr);
        
        setHoliday(1);//设置默认值
        
        uexWindow.toast('1','5',"数据加载中...",'');
        var url = mesPath+'/verification/cktikcetCzyNew?uid=1';
        reqAJAX(url,function(res){   
            uexWindow.closeToast();
            // var res = '{"timelist":["19:10","7:10"],"plList":[{"id":1,"fromStation":"宏发","toStation":"幸福誉"},{"id":2,"fromStation":"幸福誉","toStation":"宏发"}]}';r tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
            var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
            if(!isDefine(tmp.plList)){
                uexWindow.toast('1','5','没有记录',2000);
                return;
            }
            
            var arrs2 = new Array();
            arrs2 = tmp.plList;
            var routestr = '';
            for (var i=0; i < arrs2.length; i++) {
                routeObj = new Object();
                routeObj.id = arrs2[i].id;
                routeObj.name = arrs2[i].fromStation+"--"+arrs2[i].toStation;
                routeArray.push(routeObj);
                routestr = routestr + '<div id="route'+routeObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth" onclick="setRoute('+routeObj.id+')">'+routeObj.name+'</div>';
                
            };
            
            $("#route").html(routestr);
            
            setRoute(routeArray[0].id);
        })
    })

})($);


function setRoute(routeId) {
    route = routeId;
    getBanci();
}   

function setHoliday (flag) {
    if(flag==0){
        addBorderCss($("#date0"));
        removeBorderCss($("#date1"));
    }else {
        addBorderCss($("#date1"));
        removeBorderCss($("#date0"));
    }
    holiday = flag;
    getBanci();
}    

    
function addBorderCss(obj){
    obj.css("border-bottom","0.15em solid #108DE9");
}

function removeBorderCss(obj){
    obj.css("border-bottom","0.15em solid #FFFFFF");
}


function getBanci() {
    $("#time").html('');
    $("#routeStation").html('');
    time = '';
    routeStation = '';
    
    
    for (var i=0; i < routeArray.length; i++) {
        removeBorderCss($("#route"+routeArray[i].id));
    }
    addBorderCss($("#route"+route));
    
    
    var url = mesPath+'/seatYudingOrder/shikebiao?route='+route+'&holiday='+holiday+'&uid=opiQiuKBtqGU6pf67fpGOHDBefXk';
    uexWindow.toast('1','5',"数据加载中...",'');
    reqAJAX(url,function(res){   
        //alert(res)
        uexWindow.closeToast();
        var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
        if(!isDefine(tmp.timelist)){
            uexWindow.toast('1','5','没有记录',2000);
            return;
        }
        var arrs = new Array();
        arrs  = tmp.timelist;
        timeArray = new Array();
        var timeObj;
        
        var timestr = '';
        for (var i=0; i < arrs.length; i++) {
            timeObj = new Object();
            timeObj.id = i;
            timeObj.name = arrs[i];
            timeArray.push(timeObj);
            timestr = timestr+'<div id="time'+timeObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth20" onclick="setTime('+timeObj.id+')">'+timeObj.name+'</div>';
        };
        $("#time").html(timestr);
        
        $("#qujian").html(tmp.startDate+'至'+tmp.endDate+'(共'+tmp.ydsl+'天,剩余月票'+tmp.sysl+')');

        ydsl = tmp.ydsl;
        sysl = tmp.sysl;
    })
     
}
 
function setTime(id){
    time = timeArray[id].name;
    for (var i=0; i < timeArray.length; i++) {
        removeBorderCss($("#time"+timeArray[i].id));
    }
    addBorderCss($("#time"+id));
}
    
function setStation(id){
    routeStation = stationArray[id].name;
    for (var i=0; i < stationArray.length; i++) {
        removeBorderCss($("#routeStation"+i));
    }
    addBorderCss($("#routeStation"+id));
    
}    
   
   

function yuding() {
    if (route=='') {
        alert("请选择线路！");
        return ;
    }
    if (time=='') {
        alert("请选择班次！");
        return ;
    }
    
    if(sysl<ydsl){
        alert("月票不足");
        return ;
    }
  
    
    var url = mesPath+'/seatYudingOrder/yudingOrder?route='+route+'&workday='+holiday+'&time='+time+'&uid=opiQiuKBtqGU6pf67fpGOHDBefXk';
    uexWindow.toast('1','5',"数据加载中...",'');
    reqAJAX(url,function(res){ 
        uexWindow.closeToast();
        var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
        if(tmp.code!=0){
            alert(tmp.msg)
            return;
        }
        alert("预定成功");
        
        getBanci();
    })
     
}
    
   
