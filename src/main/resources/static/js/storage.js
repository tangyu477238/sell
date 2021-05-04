
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

