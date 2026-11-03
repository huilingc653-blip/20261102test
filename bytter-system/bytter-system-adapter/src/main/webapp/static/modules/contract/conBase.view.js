var divId="#conAttachmen";
setTimeout("initAttachmenId('"+divId+"')", 1000 )
if($("#changeNum")&&$("#changeNum").val()>0){
	for(var i=1;i<=$("#changeNum").val();i++){
		var divId="#changeAttachmen-"+i;
		setTimeout("initAttachmenId('"+divId+"')", 1000 )
	}
}
if($("#terminateNum")&&$("#performanceNum").val()>0){
	for(var i=1;i<=$("#performanceNum").val();i++){
		var divId="#performanceAttachmen-"+i;
		setTimeout("initAttachmenId('"+divId+"')", 1000 )
	}
}
if($("#terminateNum")&&$("#terminateNum").val()>0){
	for(var i=1;i<=$("#terminateNum").val();i++){
		var divId="#terminateAttachmen-"+i;
		setTimeout("initAttachmenId('"+divId+"')", 1000 )
	}
}
if(!divIdAll){
	var divIdAll="1";
}
var initAttachmenId=function(divId){
	if($(divId)&&divIdAll.indexOf(divId)<0){
		divIdAll+="----"+divId;
		var attachmenId=$(divId).attr("attachmentId");
		if(attachmenId){
			bt.use(['jquery','element','btUpload'], function(){
				$ = bt.jquery;
				bt.btUpload.init({
	            elem: divId,
	            auto: false,
	    		isExpand: false,
	            hasDelete: false,
	            hasUpload: false,
	            gid:attachmenId,
	    		success:function(data){
	    		}
	   	 		});
			});
		}
	}
}
