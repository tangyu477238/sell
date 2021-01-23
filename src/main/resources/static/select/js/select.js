var routeArray = new Array();
var dateArray = new Array();
var timeArray = new Array();
var stationArray = new Array();

var route=''; //传值  
var date=''; //传值  
var time=''; //传值
var routeStation=''; //传值

var username = '';
var password = '';
    
var routeObj;

(function($) {
    appcan.button("#nav-left", "btn-act",
    function() {
        uexWindow.close(-1);
    });
    appcan.button("#nav-right", "btn-act",
    function() {});
    
    
    
    appcan.ready(function() {
        username = getstorage("username");
        password = getstorage("password");
        
        
        var liststr=getBtnstr("乘车人数","history()");
        liststr=liststr+getBtnstr("黑名单","backlist()");
        liststr=liststr+getBtnstr("晚点通知","wandian()");
        liststr=liststr+getBtnstr("上传定位","shangchuan()");
        
        $("#list").html(liststr);
        
        
        if(username=='admin'){
           var listadminstr = getBtnstr("取消班车","cancel()");
           listadminstr=listadminstr+getBtnstr("售票参数","days()");
           listadminstr=listadminstr+getBtnstr("锁定全部座位","lockAll()");
           listadminstr=listadminstr+getBtnstr("关闭定位","guanbi()");
           
           $("#listadmin").html(listadminstr);
           
           var listadminstr2 = getBtnstr("新增班车","addBan()");
           listadminstr2=listadminstr2+getBtnstr("删除班车","delBan()");
           listadminstr2=listadminstr2+getBtnstr("查询班车表","queryBan()");
           listadminstr2=listadminstr2+getBtnstr("延时放票时间","deal()");
           $("#listadmin2").html(listadminstr2);
        }
    })

    appcan.ready(function() {
        
        var datestr ='';
        for(var i=0;i<3;i++){
            datestr = datestr+'<div id ="date'+i+'" class="ubb-01 ulev-app2 t-gra-63 swidth3" onclick="getBcDate('+i+');getBanci();">'+getNextNumDay(i)+'</div>';
        }
        $("#date").html(datestr);
        
        
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
                routestr = routestr + '<div id="route'+routeObj.id+'" class="ubb-01 ulev-app2 t-gra-63 swidth" onclick="getRoute('+routeObj.id+');getBanci();">'+routeObj.name+'</div>';
            };
            
            $("#route").html(routestr);
            getRoute(arrs2[0].id);
            getBcDate(0);
            getBanci();
        })
    })

})($);


function getBtnstr(name,fun){
    var str1 = "<div class=\"ub-f1 ub ub-ver ub-ac umar-b3\" onclick=\""+fun+"\">\n" +
                "    <div class=\"uwh-ele2 uc-a-ele1 c-wh ub ub-ac ub-pc\">\n" +
                "        <div class=\"btn_wh1 ub-img eleIcon"+getMathRandom(1,8)+"\"></div>\n" +
                "    </div>\n" +
                "    <div class=\"umar-at1 t-gra7 tx-c ulev-1\">\n" +
                "        "+name+"\n" +
                "    </div>\n" +
                "</div>";
    return str1;
} 
    

    
function addBorderCss(obj){
    obj.css("border-bottom","0.15em solid #108DE9");
}

function removeBorderCss(obj){
    obj.css("border-bottom","0.15em solid #FFFFFF");
}




function getBcDate(dateId){

    for (var i=0; i < 3; i++) {
        removeBorderCss($("#date"+i));
    }
    addBorderCss($("#date"+dateId));
    date = getNextNumDay(dateId);
}


function getRoute(routeId) {
   
    for (var i=0; i < routeArray.length; i++) {
        removeBorderCss($("#route"+routeArray[i].id));
    }
    addBorderCss($("#route"+routeId));
    route = routeId;
}
 
function getBanci(){
    $("#time").html('');
    time = '';
    var url = mesPath+'/verification/cktikcetTimeNew?route='+route+'&bizDate='+date;
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
        
        //setShangchedian(route);
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
    $("#routeStation").html('');
    routeStation = '';
    
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
    var routeName =  $("#route"+route).html(); 
   
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
    //if (routeStation=='') {
    //    alert("请选择上车地点！");
    //    return ;
    //}
    setstorage("routeName",routeName);
    setstorage("seachRouteId",route);
    setstorage("seachDate",date);
    setstorage("seachTime",time);
    
    openwin("select_seat","select_seat.html",'2');  
    
}    

//取消通知
function cancel() {
    sendData('sendCancelMsg')
} 

//锁定全部座位
function lockAll() {
    sendData('lockAll')
} 

function sendData(method){
    var routeName =  $("#route"+route).html(); 
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
    
    if(username!='admin'||password==''){
        alert("无权限操作")
    }
    
    var msg = "您确定要继续操作吗？"; 
    if (confirm(msg)==true){ 
        var url = mesPath+'/verification/'+method+'?route='+route+'&bizDate='+date+'&time='+time+'&username='+username+'&password='+password;
        uexWindow.toast('1','5',"数据加载中...",'');
        reqAJAX(url,function(res){   
    
            uexWindow.closeToast();
            var tmp=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res; 
            if(tmp.code!=0){
                uexWindow.toast('1','5','操作失败',2000);
                return;
            }
            uexWindow.toast('1','5','操作成功',3000);
        })
    }  
}



//黑名单
function backlist() {
    
    openwin("backlist","backlist.html",'2');  
    
} 

//修改配置
function days() {
    
    openwin("select_days","select_days.html",'2');  
    
} 



//晚点通知
function wandian() {
    var routeName =  $("#route"+route).html(); 
   
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
    //if (routeStation=='') {
    //    alert("请选择上车地点！");
    //    return ;
    //}
    setstorage("routeName",routeName);
    setstorage("seachRouteId",route);
    setstorage("seachDate",date);
    setstorage("seachTime",time);
    
    openwin("select_wandian","select_wandian.html",'2');  
    
} 