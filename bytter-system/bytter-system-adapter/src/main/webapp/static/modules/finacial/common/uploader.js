var uploader;
bt.use(['btUpload'], function(){
	uploader=bt.btUpload.init(getParams(null,''));
});
//双击行上传控件渲染事件
function uploaderRender(type,result) {
	var attachmentId='';
	if (result)attachmentId=result.accbook.attachmentId;
	uploader.redraw(getParams(type,attachmentId));
}
function getParams(type,attachmentId) {
	var num=-1;
	if (type){
		num=type.indexOf('Check');
	}else {
		type=$("#opType").val();
		if (type){
			num=type.indexOf('Check');
		}
	}
	var params={};
	params["elem"]="#uploader";
	params["auto"]=false;
	params["isExpand"]=true;
	params["gid"]=attachmentId;

	if(num>-1){//复核界面只能看不能删除、不能上传
		params["hasDelete"]=false;
		params["hasUpload"]=false;
	}
	return params;
}
