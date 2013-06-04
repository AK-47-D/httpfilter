function findObj(theObj, theDoc){ 
	var p, i, foundObj; 
	if(!theDoc) theDoc = document; 
	if( (p = theObj.indexOf("?")) > 0 && parent.frames.length) {    
		theDoc = parent.frames[theObj.substring(p+1)].document;    
		theObj = theObj.substring(0,p); 
	} 
	if(!(foundObj = theDoc[theObj]) && theDoc.all) 
		foundObj = theDoc.all[theObj]; 
	for (i=0; !foundObj && i < theDoc.forms.length; i++)     
		foundObj = theDoc.forms[i][theObj]; 
	for(i=0; !foundObj && theDoc.layers && i < theDoc.layers.length; i++)     
		foundObj = findObj(theObj,theDoc.layers[i].document); 
	if(!foundObj && document.getElementById) 
		foundObj = document.getElementById(theObj);    
	return foundObj;
}

//���һ����������д��
function showResponse(response){ //��ȡ���һ�е��кţ������txtTRLastIndex�ı����� 
	paramArray = new Array;
	paramArray = response.split("&");
	//alert(paramArray);
	var responseFrame = findObj("responseFrame",document);
	var rownum = responseFrame.rows.length;
	// ɾ����
	for( j = 0; j < rownum; j++){
		//alert(rownum + "==" + j);
		paramFrame.deleteRow(0);
	}
	for( i = 0; i < paramArray.length; i++){
		AddOneRow("'" + response + "'");
	}
}

function AddOneRow(content){
	var txtTRLastIndex = findObj("txtTRLastIndex",document);
	var rowID = parseInt(txtTRLastIndex.value);
	// alert(rowID);
	var responseFrame = findObj("responseFrame",document);
	// �����
	var newTR = responseFrame.insertRow(responseFrame.rows.length);
	newTR.id = "Response" + rowID;
		
	// �����:key
	var newNameTD=newTR.insertCell(0);
	// ���������
	newNameTD.innerHTML = content;
}

//���һ��������д��
function showParams(params){ //��ȡ���һ�е��кţ������txtTRLastIndex�ı����� 	
	if(null != params){
		paramArray = new Array;
		paramArray = params.split("&");
		var paramFrame = findObj("paramFrame",document);
		var rownum = paramFrame.rows.length;
		// ɾ����
		for( j = 0; j < rownum; j++){
			paramFrame.deleteRow(0);
		}
		for( i = 0; i < paramArray.length; i++){
			AddOneParamRow("'" + paramArray[i].split("=")[0] + "'", "'" + paramArray[i].split("=")[1] + "'");
		}
	} else {
		AddOneParamRow(null, null);
	}
	
}

// ���һ��������
function AddOneParamRow(key, value){ // ��ȡ���һ�е��кţ������txtTRLastIndex�ı�����
	var txtTRLastIndex = findObj("txtTRLastIndex",document);
	var rowID = parseInt(txtTRLastIndex.value);
	var paramFrame = findObj("paramFrame",document);
	// �����
	var newTR = paramFrame.insertRow(paramFrame.rows.length);
	newTR.id = "SignItem" + rowID;
		
	// �����:key
	var keyTD=newTR.insertCell(0);
	keyTD.setAttribute("width","10px");
	keyTD.setAttribute("style","font-weight: bold;");
	// ���������
	if(key == null){
		keyTD.innerHTML = "<input name='key" + rowID + "' id='key" + rowID + "' type='text' value='please input key' style='color:#ffDD00;font-family:����; width:98%;'></input>";
	}else{
		keyTD.innerHTML = "<input name='key" + rowID + "' id='key" + rowID + "' type='text' value=" + key + "style='width:98%;padding:1;'></input>";
	}	
	
	// �����:value
	var valueTD=newTR.insertCell(1);
	valueTD.setAttribute("style","font-weight: bold;padding:2;");
	// ���������
	if(value == null){
		valueTD.innerHTML = "<input name='value" + rowID + "' id='value" + rowID + "' type='text' value='please input value' style='color:#ffDD00;font-family:����;width:98%;padding:1;'></input>";
	}else{
		valueTD.innerHTML = "<input name='value" + rowID + "' id='value" + rowID + "' type='text' value=" + value + "  style='width:98%;padding:1;'></input>";
	}
	
	// �����:ɾ����ť
	var newDeleteTD=newTR.insertCell(2);
	newDeleteTD.setAttribute("width","10px");
	// ���������
	newDeleteTD.innerHTML = "<div align='center' style='color: red;font-weight: bold; margin: 5px;' onclick=\"DeleteSignRow('SignItem" + rowID + "')\">delete</div>";
	
	// ���к��ƽ���һ��
	txtTRLastIndex.value = (rowID + paramArray.length).toString() ;
}
//ɾ��ָ����
function DeleteSignRow(rowid){
	//alert(rowid);
	var paramFrame = findObj("paramFrame",document);
	var signItem = findObj(rowid,document);
	
	//��ȡ��Ҫɾ�����е�Index
	var rowIndex = signItem.rowIndex;
	//ɾ��ָ��Index����
	paramFrame.deleteRow(rowIndex);
	
	//����������ţ����û����ţ���һ��ʡ��
	//for(i=rowIndex;i<paramFrame.rows.length;i++){
	//	paramFrame.rows[i].cells[0].innerHTML = i.toString();
	//}
}

//ɾ������
function DeleteRows(rowid){
	//alert(rowid);
	var paramFrame = findObj("paramFrame",document);
	var signItem = findObj("'SignItem" + 0 +"'",document);
		
	//��ȡ��Ҫɾ�����е�Index
	var rowIndex = signItem.rowIndex;
//	alert("rowIndex="+rowIndex);
	for( k = (rowIndex-1); k>paramFrame.rows.length; k++){
//		alert("k=" + k);
		alert("paramFrame.rows.length" + paramFrame.rows.length);
		//ɾ��ָ��Index����
		paramFrame.deleteRow(rowIndex)
	}
	//����������ţ����û����ţ���һ��ʡ��
	//for(i=rowIndex;i<paramFrame.rows.length;i++){
	//	paramFrame.rows[i].cells[0].innerHTML = i.toString();
	//}
}


var _index = 0; _uripre = ""; _domain = "";
function switchSysBar(){
	//alert(index);
	var reqList = document.getElementsByClassName("item");
	for(i = 0; i < reqList.length; i++){
		reqList[i].style.backgroundColor = "#FFFFFF";
	}
	reqList[_index].style.backgroundColor = "#0099FF";
	if (switchPoint.innerText==3){
		switchPoint.innerText=4
		document.all("frmTitle").style.display="none"
	}else{
		switchPoint.innerText=3
		document.all("frmTitle").style.display=""
	}
}

function showSysBar(index, params, response, domain, uripre){
	_index = index;
	_domain = domain;
	_uripre = uripre;
	//alert(params);
	var reqList = document.getElementsByClassName("item");
	for(i = 0; i < reqList.length; i++){
		reqList[i].style.backgroundColor = "#FFFFFF";
	}
	reqList[index].style.backgroundColor = "#0099FF";
	document.all("frmTitle").style.display=""
		
	//alert(params);
	showParams(params);
	showResponse(response);
	//alert(params);
}

function dismissSysBar(index, params, response, domain, uripre){
	_index = index;
	_domain = domain;
	_uripre = uripre;
	//alert(index);
	var reqList = document.getElementsByClassName("item");
	for(i = 0; i < reqList.length; i++){
		reqList[i].style.backgroundColor = "#FFFFFF";
	}
	reqList[index].style.backgroundColor = "#0099FF";
	document.all("frmTitle").style.display="none"
	showParams(params);
	showResponse(response);
}

	function getTab(flag) {  
	  var elList, i;
	  elList = document.getElementsByTagName("li");
	  for (i = 0; i < elList.length; i++){
		 elList[i].className ="";
	  }
	  elList[flag].className ="Selected";
	  elList[flag].blur();

	}

	function goto(flag){
		if("0"==flag){
			document.all("paramFrame").style.display=""
			document.all("responseFrame").style.display="none"
			document.all("headerFrame").style.display="none"
		} else if("1"==flag){
			document.all("paramFrame").style.display="none"
			document.all("responseFrame").style.display=""
			document.all("headerFrame").style.display="none"
		} else if("2" == flag){
			document.all("paramFrame").style.display="none"
			document.all("responseFrame").style.display="none"
			document.all("headerFrame").style.display=""
		}
		getTab(parseInt(flag));
	}
	
	function saveParams(){
		var content = new Object();
		content.domain = _domain;
		content.uri = _uripre; 
		var keyCount = $("input[id^=key]").length;
	    var valCount = $("input[id^=value]").length;
	    var newParams = "";
	    for(var i = 0; i < keyCount; i++){
	    	if(i == keyCount-1){
	    		var kv = $("input[id^=key]")[i].value + "=" + $("input[id^=value]")[i].value;
	    	} else {
	    		var kv = $("input[id^=key]")[i].value + "=" + $("input[id^=value]")[i].value + "&";
	    	}
	    	newParams += kv
	    }
	    content.param = newParams;
	    var json = JSON.stringify(content);
//	    alert(json);

//		var domainjson = eval('('+str+')');//new Object();
//		domainjson[_domain] = json;
//	   
//	    var domainjson = JSON.stringify(domainjson);
//	    alert(domainjson);
	}
	
