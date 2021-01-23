var routeArray = new Array();
var dateArray = new Array();
var timeArray = new Array();
var stationArray = new Array();

var route=''; //传值  
var date=''; //传值  
var time=''; //传值
var routeStation=''; //传值

var routeObj;

(function($) {
    appcan.button("#nav-left", "btn-act",
    function() {});
    appcan.button("#nav-right", "btn-act",
    function() {});
    appcan.button("#Button_footer", "ani-act",
    function() {});
    
    appcan.ready(function() {
        
        
        uexWindow.toast('1','5',"数据加载中...",'');
        var url = mesPath+'/verification/cktikcetCzyNew?uid=1';
        reqAJAX(url,function(res){   
            uexWindow.closeToast();
            // var res = '{"timelist":["19:10","7:10"],"plList":[{"id":1,"fromStation":"宏发","toStation":"幸福誉"},{"id":2,"fromStation":"幸福誉","toStation":"宏发"}]}';r tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
            var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
            if(!isDefine(tmp.plList)){
                uexWindow.toast('1','5','查询失败',2000);
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
                routestr = routestr + '<div id="route'+routeObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth" onclick="getBanci('+routeObj.id+')">'+routeObj.name+'</div>';
                
            };
            
            $("#route").html(routestr);
            date = formatDate(new Date().getTime());
            var datestr ='<div id ="date0" class="ubb-01 ulev-app2 t-gra-63 swidth3" >'+date+'</div>';
            $("#date").html(datestr);
            
            addBorderCss($("#date0"+route));
            
            getBanci(routeArray[0].id);
        })
    })

})($);


 
    

    
function addBorderCss(obj){
    obj.css("border-bottom","0.15em solid #108DE9");
}

function removeBorderCss(obj){
    obj.css("border-bottom","0.15em solid #FFFFFF");
}


function getBanci (routeId) {
    
    
    route = routeId;
    $("#time").html('');
    $("#routeStation").html('');
    time = '';
    routeStation = '';
    
    
    for (var i=0; i < routeArray.length; i++) {
        removeBorderCss($("#route"+routeArray[i].id));
    }
    addBorderCss($("#route"+route));
    
    
    var url = mesPath+'/verification/cktikcetTime?route='+route;
    uexWindow.toast('1','5',"数据加载中...",'');
    reqAJAX(url,function(res){   
        uexWindow.closeToast();
        var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
        if(!isDefine(tmp.timelist)){
            uexWindow.toast('1','5','查询失败',2000);
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
        
        setShangchedian(route);
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
    
function setShangchedian (q) {
    if(q==1){
        $("#routeStation").html('<div id="routeStation0" class="ubb-01 ulev-app2 t-gra-63 swidth3" >宏发</div>');
        routeStation = '宏发';
        addBorderCss($("#routeStation0"));
    }else if(q==2){
        setStationStr();
    }else if(q==16){
        $("#routeStation").html('<div id="routeStation0" class="ubb-01 ulev-app2 t-gra-63 swidth3" >珠江新城</div>');
        routeStation = '珠江新城';
        addBorderCss($("#routeStation0"));
    }else if(q==15){
        setStationStr();
    }else if(q==17){
        $("#routeStation").html('<div id="routeStation0" class="ubb-01 ulev-app2 t-gra-63 swidth3" >北部万科城</div>');
        routeStation = '北部万科城';
        addBorderCss($("#routeStation0"));
    }else if(q==18){
        $("#routeStation").html('<div id="routeStation0" class="ubb-01 ulev-app2 t-gra-63 swidth3" >宏发</div>');
        routeStation = '宏发';
        addBorderCss($("#routeStation0"));
    }
}    
    
function setStationStr(){
    var stationObj = new Object();
    stationObj.id = 0;
    stationObj.name = '幸福誉第一期';
    stationArray.push(stationObj);
    var routestr = '<div id="routeStation'+stationObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="setStation('+stationObj.id+')">'+stationObj.name+'</div>';
        
    stationObj = new Object();    
    stationObj.id = 1;
    stationObj.name = '幸福誉第三期';
    stationArray.push(stationObj);
    routestr = routestr + '<div id="routeStation'+stationObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="setStation('+stationObj.id+')">'+stationObj.name+'</div>';
       
    stationObj = new Object();   
    stationObj.id = 2;
    stationObj.name = '绿地城';
    stationArray.push(stationObj);
    routestr = routestr + '<div id="routeStation'+stationObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="setStation('+stationObj.id+')">'+stationObj.name+'</div>';
    
    $("#routeStation").html(routestr);
}    

    
function history() {
    //var route = $("#route").val();
    //var time = $("#time").val();
    if (route=='') {
        alert("请选择线路！");
        return ;
    }
    if (date=='') {
        alert("请选择日期！");
        return ;
    }
    
    if (time=='') {
        alert("请选择班次！");
        return ;
    }
    if (routeStation=='') {
        alert("请选择上车地点！");
        return ;
    }
    
    setstorage("seachRouteId",route);
    setstorage("seachDate",date);
    setstorage("seachTime",time);
    
    openwin("wx_select_seat","wx_select_seat.html",'2');  
    
}    
