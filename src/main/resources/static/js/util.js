
//=======================================================
//	对跳转做简单兼容,仅区分IOS(0)，安卓(1)，与其他(2)
//  参数1:提交url 参数2:传递参数json格式 参数3:回调方法 参数4:失败回调
// 	post提交 data格式：reqAJAX('www.xx.cn',function(msg){..},{a:'参数1',b:'参数2'})



function reqAJAX(httpUrl,jsondata,callback,callfail){
    console.log(httpUrl)
	if (isJson(jsondata)) {
		if (typeof(eval(callfail)) == "function") {
		    ajaxPost(httpUrl,jsondata,callback,callfail);
		}else{
			ajaxPost(httpUrl,jsondata,callback,failBack);
		}
	}else if(typeof(eval(jsondata)) == "function"){
		if (typeof(eval(callback)) == "function") {
			ajaxGet(httpUrl,jsondata,callback);
		}else{
		    ajaxGet(httpUrl,jsondata,failBack);   
		}
	}
}

function failBack(){
    uexWindow.toast('0','5','加载失败','2000');
}

function ajaxGet(httpUrl,jsondata,callback){
    appcan.ajax({
        url : httpUrl,
        type : 'GET',
        offline : undefined,
        expires: 3000,
        success : jsondata, 
        error : callback
    });
}

function ajaxPost(httpUrl,jsondata,callback,callfail){
    appcan.ajax({
        url : httpUrl,
        type : 'POST',
        data : jsondata,
        offline : undefined,
        expires: 3000,
        success : callback, 
        error : callfail
    });
}



//判断是否json数据
function  isJson(obj){
    var isjson = typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
    return isjson;
}

//返回对象类型
function getType(x){
	if(x==null){
		return "null";
	}
	var t= typeof x;
	if(t!="object"){
		return t;
	}
	var c=Object.prototype.toString.apply(x);
	c=c.substring(8,c.length-1);
	if(c!="Object"){
		return c;
	}
	if(x.constructor==Object){
		return c
	}
	if("classname" in x.prototype.constructor
			&& typeof x.prototype.constructor.classname=="string"){
		return x.constructor.prototype.classname;
	}
	return "<unknown type>";
}



//====================请求等待======================================
/*******
 * 
 * 发起数据请求时，其他请求必须等待
 * 标识 waiting=0,加载中...
 * 当请求完成或者异常皆需要改回标识 waiting=1,
 * 另增加定时器启动程序10秒改回标识 waiting=1,
 * 且程序正常执行完成后 调用停止定时标识,防止重复更改 waiting=1,
 * 防止意外发生和不可预知情况未能及时改回标识
 * 启动程序默认初始化 waiting=1;
 */
var meosWaiting='';
function setWaiting(){
	//clearTimeout(meosWaiting);	
	uexWindow.toast('1','5',"数据加载中...",'');
	//meosWaiting=setTimeout("uexWindow.closeToast();", 10000);
}
//停止等待
function stopWaiting(){
	uexWindow.closeToast();
	//setTimeout("uexWindow.closeToast();", 500);
	//clearTimeout(meosWaiting);	
}

/*******
 * 检测平台转化兼容跳转
 * @param {Object} strfile 目录
 * @param {Object} strhtml 网页文件
 * @param {Object} vs  	‘0’代表导航之间的跳转；
 * 						‘1’代表主页根目录跳转到某文件夹下面的页面；
 * 						‘2’代表某个文件夹下面的页面镶嵌在根目录主页的情况（eg:accountIndex_content.html镶嵌在index.html）跳转到某文件夹下面的页面；
 * 						‘3’或‘不填’代表某文件夹下面的页面跳转到某个文件夹下面的页面
 */
function checkPlayForm(strfile,strhtml,vs){
//	setstorage('vs',vs); 
	if(/(Android)/i.test(navigator.userAgent)){
//		return strfile + "/" + strhtml;
		if(vs == 0){
			return strfile + "/" + strhtml;
		}else if(vs == 1){
			return strfile + "/" + strhtml;
		}else if(vs == 2){
			return strfile + "/" + strhtml;
		}else{
			return "../" + strfile + "/" + strhtml;
		}
	}else{
		if(vs == 0){
			return strfile + "/" + strhtml;
		}else if(vs == 1){
			return strfile+ "/" + strhtml;
		}else if(vs == 2){
			return "../" + strfile + "/" + strhtml;
		}else{
			return  "../" +strfile + "/" + strhtml;
		}
	}

}
var indexfla=false;
function getIndex(id){
	if(indexfla) return;
	indexfla = true;
	setTimeout('indexfla = false;', 1000);
	uexWindow.evaluateScript('root','0','blockmsg('+id+');');
	setTimeout("uexWindow.evaluateScript('','0','uexWindow.close(\"-1\",500);');", 1);	
}

//判断是什么系统（android or iphone）
function loadSystem(){
	if(/(Android)/i.test(navigator.userAgent)){
		
		return true;
	}else{
		
		return false;
	}

}

function mobileSystem(msg){
	if(/(Android)/i.test(navigator.userAgent)){
		
		uexWindow.toast('0','5',msg,'2000');
	}else{
		
		uexWindow.alert('',msg,'确定');
	}
}


//判断js对象或值是否为空
function isDefine(para) {
	if (typeof para == 'undefined' || para == "" || para == null
			|| para == 'undefined') {
		return false;
	} else {
		return true;
	}
}

// 判断变量是否定义
function isUndefined(variable) {
	return !isDefine(variable);
}

/***
 * 取得某个ID的值
 * @param {Object} id
 */
function getValue(id) {
	return getAttrById(id, "value");
}
/******
 * 设置某个ID的值
 * @param {Object} id
 * @param {Object} value
 */
function setValue(id, value) {
	setAttrById(id, "value", value)
}

function getAttrById(id, name) {
	if ("string" == typeof(id)) {
		var ele = $$(id);
		if (ele != null) {
			if (name != null) {
				var tmp = ele[name];
				if (tmp != null) {
					return tmp;
				}
			}
		}
	} else {
		if (id != null) {
			return id[name];
		}
	}
	return null;
}
function setAttrById(id, name, value) {
	if ("string" == typeof(id)){
		var ele = $$(id);
		if (ele != null) {
			if (name != null) {
				ele[name] = value;
			}
		}
	} else {
		if (id != null && name != null) {
			id[name] = value;
		}
	}
}

function addCookie(objName,objValue){//添加cookie
	var str = objName + "=" + escape(objValue);
	document.cookie = str;
	//log("addCookie-->objName: "+objName);
}
function getCookie(objName){//获取指定名称的cookie的值
	var arrStr = document.cookie.split("; ");
	for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		if(temp[0] == objName) return unescape(temp[1]);
	}
}
function clearCookie(){
	var arrStr = document.cookie.split("; ");
	for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		addCookie(temp[0],'');
	}
}
/******
 * 添加本地存储
 * @param {Object} objName
 * @param {Object} objValue
 */
function setstorage(objName,objValue){//添加cookie
	var storage = window.localStorage;
	if(window.localStorage){
		localStorage.setItem(objName,objValue);
	}else{
		 //log('setstorage-->This browser does NOT support localStorage');
		 addCookie(objName,objValue);
	}
}
/*******
 * 取得本地存储
 * @param {Object} objName
 */
function getstorage(objName){//获取指定名称的cookie的值
	var storage = window.localStorage;
	var result = '';
	if(window.localStorage){
		for(var i=0;i<storage.length;i++){
			if(storage.key(i)==objName)
				result = storage.getItem(storage.key(i));
		 }
	}else{
		 //log('getstorage-->This browser does NOT support localStorage');
		 getCookie(objName);
	}
	return result;
}

/***********
 * 清除所有本地存储
 * @param 
 */
function clearstorage(){
	var storage = window.localStorage;
	if(window.localStorage){
		localStorage.clear();
	}else{
		clearCookie();
	}
} 


/***********
 * 
 * 自动更新
 * 
 *********/
var upgrade=function(){
        this.flag_sdcard = 1;
        this.updateurl = '';//下载新apk文件地址
        this.filepath2 = "/sdcard/";//保存到sd卡
        this.fileName = '';//新版本文件名
        this.platform = '';//平台版本
		this.version='';
}

upgrade.prototype.check=function(){
   var that=this;
        //安卓版 ，显示下载进度 （step:7）
    uexDownloaderMgr.onStatus = function(opId, fileSize, percent, status) {
            if (status == 0) {
                    // 下载中...
                    uexWindow.toast('1', '5', '正在更新：' + percent + '%', '');
            } else if (status == 1) {// 下载完成.
                    uexWindow.closeToast();
                    uexDownloaderMgr.closeDownloader('14');//关闭下载对象
                    //alert(that.filepath2+that.fileName);
					uexWidget.installApp(that.filepath2+that.fileName);// 安装下载apk文件
            } else {
                    uexWindow.toast('1', '5', '下载出错，请关闭软件再次运行.', '');
            }
    };
        
        //安卓版 ，创建下载对象回调函数（step:6）
    uexDownloaderMgr.cbCreateDownloader = function(opId, dataType, data) {
            if (data == 0) {
					//alert(that.updateurl+" "+that.filepath2+that.fileName);
                    //updateurl是通过调用cbCheckUpdate回调后，放入全局变量的
                    uexDownloaderMgr.download('14', that.updateurl, that.filepath2+that.fileName, '0');//开始下载apk文件
            } else if (data == 1) {
                    ;
            } else {
                    ;
            }
    };
        
        //提示更新模态框按钮事件回调函数，判断用户选择更新还是取消 （step:5）
    uexWindow.cbConfirm = function(opId, dataType, data) {
                      //  alert(data);
            //调用对话框提示函数
            if (data == 0) {
               //用户点击确定按钮，进行更新
                if (platform == 0) {
                        //苹果版更新，通过浏览器加载appstore路径
                        //TODO:这个还没有试过
                        uexWidget.loadApp("", "", that.updateurl);
                } else if (platform == 1) {
                        //安卓版更新，通过创建下载对象进行下载
                        //uexDownloaderMgr.createDownloader("14");
                        uexWidget.startApp("1", "android.intent.action.VIEW", '{"data":{"mimeType":"text/html","scheme":"'+that.updateurl+'"}}');
                } else {
                        ;
                }
            } else {
                  //用户点击稍后按钮，不进行更新   
            }
    };
        
        //调用检查更新回调函数，请求成功后，弹出模态框让用户选择是否现在更新（step:4）
    uexWidget.cbCheckUpdate = function(opCode, dataType, jsonData) {
            //alert(jsonData);
			var obj = eval('(' + jsonData.replace(/#/g, "'")+ ')');
            if (obj.result == 0) {
             	var tips1 = "更新地址是：" + obj.url + "<br>文件名：" + obj.name + "<br>文件大小：" +obj.size + "<br>服务器版本号：" + obj.version;
                //alert(tips1);
                that.version=obj.version;
				that.updateurl = obj.url;
				//alert(obj.url);
                //var objname = eval('(' + obj.name+ ')');
				//that.fileName = objname.name+".apk";
				var vername = (obj.name+"").split("_");
				that.fileName = vername[0]+".apk";
				
				setstorage("version",obj.version);
				//setstorage("hcytel",objname.hcytel);
				//setstorage("hcyqq",objname.hcyqq);
				//setstorage("hcyemail",objname.hcyemail);
//				alert(objname.hcyweixin);
				//setstorage("hcyweixin",objname.hcyweixin);
				//setstorage("hcyqqs",objname.hcyqqs);
				//setstorage("hcyname",objname.hcyname);
				
				/**
				if(objname.mustupdate==2){
					uexWindow.toast('1', '5', '版本太旧，程序3秒后自动退出！', '3000');
					setTimeout(function(){
						uexWidgetOne.exit(0);
					},3500);
					return;
				}**/
				
				//var ver = that.version.split(".");
				
				if(vername[1]==0||vername[1]=='0'){//当前有更新
				//if(objname.statusupdate==1||objname.statusupdate=='1'){//当前有更新
					
					uexDevice.cbGetInfo = function(opId,dataType,data){
			                var conn = {'connectStatus':'-1'};
			                try{conn = eval('('+data+')');}catch(e){}
			               /**
			                if(conn['connectStatus']=='0'){
									//是WIFI
			                        uexWindow.toast('1', '5', '当前WIFI状态，即将自动更新...', '3000');
									setTimeout(function(){
			                                uexWindow.closeToast();
										  //用户点击确定按钮，进行更新
							                if (platform == 0) {
							                        //苹果版更新，通过浏览器加载appstore路径
							                        //TODO:这个还没有试过
							                        uexWidget.loadApp("", "", that.updateurl);
							                } else if (platform == 1) {
							                        //安卓版更新，通过创建下载对象进行下载
							                        uexDownloaderMgr.createDownloader("14");
							                } else {
							                        ;
							                }
			                        },3000);
			                }else{**/
									/**if(objname.mustupdate==1||objname.mustupdate=='1'){//是否强制更新
										var value = "立即更新";
					                    var mycars = value.split(";");
										uexWindow.confirm('更新日志', objname.messupdate.replace(/@@/g,"\r"),mycars);
									}else{**/
										var value = "更新;稍后";
					                    var mycars = value.split(";");
					                    //uexWindow.confirm('更新日志', objname.messupdate.replace(/@@/g,"\r"), mycars);
					                    uexWindow.confirm('更新提示', "有新版本程序是否更新？", mycars);
									/**}**/
			              /**  }**/
			        };
			        uexDevice.getInfo(13);
					
					
				}
            }else if (obj.result == 1) {
                    //alert("已经是最新版本");
            } else if (obj.result == 2) {
                    ;// tips = "未知错误";alert(tips);
            } else if (obj.result == 3) {
                    ;// tips = "参数错误";alert(tips);
            }
    };
        
        //检查是否已经存在sd卡的回调函数（step:3）
    uexFileMgr.cbIsFileExistByPath = function(opCode, dataType, data) {
            if (that.flag_sdcard == 0) {
                    if (data == 0) {
                            //Log('sdcard不存在，根据具体情况处理');
                    } else {
                            //执行检查更新
                            uexWidget.checkUpdate();//根据config.xml里面配置的检查更新地址发起http请求
                    }
                    that.flag_sdcard = 1;
            } 
    };
        
        //获取平台版本回调函数，确定是客户端是那个平台的客户端 （step:2）
    uexWidgetOne.cbGetPlatform = function(opId, dataType, data) {
                       // alert("step2");
            //Log('uexWidgetOne.cbGetPlatform ');
            //获取系统版本信息回调函数
            platform = data;
            //Log('platform= '+platform);
            if (data == 0) {
					setstorage("playStatus",0);
                    // 是iphone
                    uexWidget.checkUpdate();// 直接调用检查更新，检查更新地址在config.xml里面有配置
            } else if (data == 1) {
                    // 是android
					setstorage("playStatus",1);
                    that.flag_sdcard = 0;
                    uexFileMgr.isFileExistByPath('/sdcard/');//先判断是否存在sd卡，再调用checkUpdate来进行更新
            } else {
					setstorage("playStatus",2);
                    // 是平台
            }
    };
        
    uexWidgetOne.getPlatform();//获取平台版本 （step:1）
                
}

var actflag = false;
/******
* 打开窗口,如存在则强制刷新显示
 * @param {Object} winName
 * @param {Object} url
 * @param {Object} anim
 */
function openwin(winName,url,anim){
	if(actflag) return;
	actflag = true;
	setTimeout('actflag = false;', 1000);
	appcan.window.open({
                 name:winName,
                 dataType:0,
                 aniId:anim,
                 data:url,
                 extraInfo:{
                     opaque:true,
                     bgColor:""
                 }
            });
	//uexWindow.open(winName, "0",url,'10',"","","2",500);
}

/**************
 * 打开窗口,如存在则直接显示 切入方式
 * @param {Object} winName
 * @param {Object} url
 * @param {Object} anim
 */
function openNavWindow(winName,url,anim){
	uexWindow.open(winName, "0",url,anim,"","","0",500);
}

/********
 * 
 * 系统启动
 * 自动登录操作
 * 
 ********/

function autologin(){
  	var logindata = getstorage('autologin');
	if(isDefine(logindata)){
		var dataobj = JSON.parse(logindata);
		if(isDefine(dataobj)&&dataobj.username)
		{
			if(dataobj.autologin == '1')
			{
				var eusername = encodeURIComponent(dataobj.username);
				var epassword = encodeURIComponent(dataobj.password);
				var loginUrl = mesPath + "/account/loginSystem.action?user=" + eusername + "&password=" + epassword;
				setstorage('loginstatus','1');
				reqAJAX(loginUrl,function(res){
					var msg=getType(res)=='string'&&isDefine(res)?JSON.parse(res):res;	
					setstorage('loginstatus','0');
					if(isDefine(msg) && msg.messStatus == '0'){
						uid = msg.acId;
						var data = JSON.stringify(msg);
		                //log("index-->data="+data);
		                setstorage('meoslogin',data);
					}
				});
			}
		}
	}
}

/*********
 * 信息提示
 * @param {Object} msg
 */
function myalert(msg){
	uexWindow.cbConfirm = function (opId, dataType, data){
		uexWindow.close('-1',500);
	}
	uexWindow.confirm('', msg, "确定");
}
function trim(s) {
	var count = s.length;
	var st = 0; // start
	var end = count - 1; // end

	if (s == "")
		return s;
	while (st < count) {
		if (s.charAt(st) == " ")
			st++;
		else
			break;
	}
	while (end > st) {
		if (s.charAt(end) == " ")
			end--;
		else
			break;
	}
	return s.substring(st, end + 1);
}


function getLoginUID(){
	return getLoginInfor('uid');
}


function getLoginInfor(name){
	var result = '';
	var data = getstorage('meoslogin');
	if(isDefine(data))
	{
		var dataobj = JSON.parse(data);
		if(isDefine(dataobj) && dataobj.messStatus=='0')
		{
			if(name == 'uid')
				result = dataobj.acId;			
		}
	}
	return result;
}








//////////////底部导航内容处理////////////////////////

/*
 * todo set htmlele for element by id example setHtml("id", "<a href="">value</a>");
 */
function setHtml(id, html) {
	if ("string" == typeof(id)) {
		var ele = $$(id);
		if (ele != null) {
			ele.innerHTML = html == null ? "" : html;
		}
	} else if (id != null) {
		id.innerHTML = html == null ? "" : html;
	}
}







//================hashClass=======================
function hasClass(ele,cls) {
   return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
}
  
function addClass(ele,cls) {
    if (!this.hasClass(ele,cls)) ele.className += " "+cls;
}
  
 function removeClass(ele,cls) {
   if (hasClass(ele,cls)) {
           var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
     ele.className=ele.className.replace(reg,' ');
   }
}

function siblings(o){//参数o就是想取谁的兄弟节点，就把那个元素传进去
   var a=[];//定义一个数组，用来存o的兄弟元素
   var p=o.previousSibling;
   while(p){//先取o的哥哥们
        if(p.nodeType===1){
              a.push(p);
        }
        p=p.previousSibling            
   }
   a.reverse()//把顺序反转一下
    
   var n=o.nextSibling;//再取o的弟弟
   while(n){
        if(n.nodeType===1){
             a.push(n);
        }
        n=n.nextSibling;
   }
   return a//最后按从老大到老小的顺序，把这一组元素返回        
}


//单个数字配零
function getDouble(number){
  	var numbers=["0","1","2","3","4","5","6","7","8","9"];
  	for(var i=0;i<numbers.length;i++){
   		if(numbers[i]==number){
    		return "0"+numbers[i];
   		}else if(i==9){
    		return number;
   		}
	}
}
//得到当天时间   格式：年-月-日  星期
function getTodayTime(){
	
  	var days=["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
  	var today=new Date();
  	var str= (today.getYear()<1900?1900+today.getYear():today.getYear())+"年" + getDouble([today.getMonth()+1])+"月" +getDouble(today.getDate()) +"日 "+getDouble(today.getHours())+":"+getDouble(today.getMinutes());
  	return str;
  
}
String.prototype.endWith=function(str){  
	if(str==null||str==""||this.length==0||str.length>this.length)  
	  return false;  
	if(this.substring(this.length-str.length)==str)  
	  return true;  
	else  
	  return false;  
	return true;  
}  
  
String.prototype.startWith=function(str){  
	if(str==null||str==""||this.length==0||str.length>this.length)  
	  return false;  
	if(this.substr(0,str.length)==str)  
	  return true;  
	else  
	  return false;  
	return true;  
}  

//加法运算 
 function FloatAdd(arg1,arg2){   
   var r1,r2,m;   
   try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
   try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
   m=Math.pow(10,Math.max(r1,r2))   
   return (arg1*m+arg2*m)/m   
  } 
 //浮点数减法运算   
 function FloatSub(arg1,arg2){   
	 var r1,r2,m,n;   
	 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
	 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
	 m=Math.pow(10,Math.max(r1,r2));   
	 //动态控制精度长度   
	 n=(r1>=r2)?r1:r2;   
	 return ((arg1*m-arg2*m)/m).toFixed(n);   
 }    

//乘法运算 
 function FloatMul(arg1,arg2){    
  var m=0,s1=arg1.toString(),s2=arg2.toString();    
  try{m+=s1.split(".")[1].length}catch(e){}    
  try{m+=s2.split(".")[1].length}catch(e){}    
  return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)    
 }
 //除法运算
 function FloatDiv(arg1,arg2){ 
	var t1=0,t2=0,r1,r2; 
	try{t1=arg1.toString().split(".")[1].length}catch(e){} 
	try{t2=arg2.toString().split(".")[1].length}catch(e){} 
	with(Math){ 
	r1=Number(arg1.toString().replace(".","")) 
	r2=Number(arg2.toString().replace(".","")) 
	return (r1/r2)*pow(10,t2-t1); 
	} 
}     

function uescript(wn, scr){
	uexWindow.evaluateScript(wn,'0',scr);
}

function closewin(){
	uexWindow.close('-1');
}
//补零
function getZore(tzore){
	if(tzore<10){
		tzore='0'+tzore;
	}
	return tzore;
}
//今天之后几天,今天请传0
function getNextNumDay(num){
    return formatDate(new Date(new Date().getTime() + num*24*60*60*1000).getTime());
}


//把long型日期转成YYYY-MM-DD日期格式
function formatDate(longtime){ 
 	var now=new Date(longtime); 
	var year=(now.getYear()<1900)?(1900+now.getYear()):now.getYear();
 	var month=now.getMonth()+1; 
 	var date=now.getDate(); 
 	var hour=now.getHours(); 
 	var minute=now.getMinutes(); 
 	var second=now.getSeconds();
	return year+"-"+getZore(month)+"-"+getZore(date);
} 

//把long型日期转成 小时:分钟 日期格式
function formatHourMinute(longtime){ 
    var now=new Date(longtime); 
    var year=(now.getYear()<1900)?(1900+now.getYear()):now.getYear();
    var month=now.getMonth()+1; 
    var date=now.getDate(); 
    var hour=now.getHours(); 
    var minute=now.getMinutes(); 
    var second=now.getSeconds();
    return getZore(hour)+":"+getZore(minute);
} 


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//by函数接受一个成员名字符串和一个可选的次要比较函数做为参数  
//并返回一个可以用来包含该成员的对象数组进行排序的比较函数  
//当o[age] 和 p[age] 相等时，次要比较函数被用来决出高下  
//employees.sort(ArrayBy(‘age’,ArrayBy(‘name’)));  
var ArrayBy = function(name,minor){  
    return function(o,p){  
        var a,b;  
        if(o && p && typeof o === 'object' && typeof p ==='object'){  
            a = o[name];  
            b = p[name];  
            if(a === b){  
                return typeof minor === 'function' ? minor(o,p):0;  
            }  
            if(typeof a === typeof b){  
                return a < b ? -1:1;  
            }  
            return typeof a < typeof b ? -1 : 1;  
        }else{  
            thro("error");  
        }  
    }  
}  

/**  
 * Simple Map  
 *   
 *   
 * var m = new Map();  
 * m.put('key','value');  
 * ...  
 * var s = "";  
 * m.each(function(key,value,index){  
 *      s += index+":"+ key+"="+value+"/n";  
 * });  
 * alert(s);  
 *   
 * @author dewitt  
 * @date 2008-05-24  
 */  
  
 
function Map() {   
     Array.prototype.remove = function(s) {   
        for (var i = 0; i < this.length; i++) {   
            if (s == this[i])   
                this.splice(i, 1);   
        }   
    }  
    
    /** 存放键的数组(遍历用到) */  
    this.keys = new Array();   
    /** 存放数据 */  
    this.data = new Object();   
       
    /**  
     * 放入一个键值对  
     * @param {String} key  
     * @param {Object} value  
     */  
    this.put = function(key, value) {   
        if(this.data[key] == null){   
            this.keys.push(key);   
        }   
        this.data[key] = value;   
    };   
       
    /**  
     * 获取某键对应的值  
     * @param {String} key  
     * @return {Object} value  
     */  
    this.get = function(key) {   
        return this.data[key];   
    };   
       
    /**  
     * 删除一个键值对  
     * @param {String} key  
     */  
    this.remove = function(key) {   
        this.keys.remove(key);   
        this.data[key] = null;   
    };   
       
    /**  
     * 遍历Map,执行处理函数  
     *   
     * @param {Function} 回调函数 function(key,value,index){..}  
     */  
    this.each = function(fn){   
        if(typeof fn != 'function'){   
            return;   
        }   
        var len = this.keys.length;   
        for(var i=0;i<len;i++){   
            var k = this.keys[i];   
            fn(k,this.data[k],i);   
        }   
    };   
       
    /**  
     * 获取键值数组(类似Java的entrySet())  
     * @return 键值对象{key,value}的数组  
     */  
    this.entrys = function() {   
        var len = this.keys.length;   
        var entrys = new Array(len);   
        for (var i = 0; i < len; i++) {   
            entrys[i] = {   
                key : this.keys[i],   
                value : this.data[i]   
            };   
        }   
        return entrys;   
    };   
       
    /**  
     * 判断Map是否为空  
     */  
    this.isEmpty = function() {   
        return this.keys.length == 0;   
    };   
       
    /**  
     * 获取键值对数量  
     */  
    this.size = function(){   
        return this.keys.length;   
    };   
       
    /**  
     * 重写toString   
     */  
    this.toString = function(){   
        var s = "{";   
        for(var i=0;i<this.keys.length;i++,s+=','){   
            var k = this.keys[i];   
            s += k+"="+this.data[k];   
        }   
        s+="}";   
        return s;   
    };   
    
    this.toMap = function(m){
        var ms = new Map();  
        m = m.replace('{','').replace('}','');
        var mj = new Array();
        mj = m.split(',');
        for (var i=0; i < mj.length; i++) {
            if (mj[i]!='') {
                var arr = new Array();
                arr=mj[i].split('=');
                ms.put(arr[0],arr[1]);
            };
        };
        return ms;
    }
}   
  
  
function testMap(){   
    var m = new Map();   
    m.put('key1','Comtop');   
    m.put('key2','南方电网');   
    m.put('key3','景新花园');   
    alert("init:"+m);   
       
    m.put('key1','康拓普');   
    alert("set key1:"+m);   
       
    m.remove("key2");   
    alert("remove key2: "+m);   
       
    var s ="";   
    m.each(function(key,value,index){   
        s += index+":"+ key+"="+value+"/n";   
    });   
    alert(s);   
}  

String.prototype.startWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substr(0,str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
}  
String.prototype.endWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substring(this.length-str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
}  

function getMathRandom(min,max){
    var rand = Math.floor(Math.random()*(max-min+1))+min;
    return rand;
}

//JS获取url参数
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
