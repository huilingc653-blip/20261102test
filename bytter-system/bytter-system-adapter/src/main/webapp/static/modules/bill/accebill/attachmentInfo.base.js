var btUpload,btUpload2;
    bt.use(['btUpload'], function() {
    	$ = bt.jquery;
    	uploader = bt.btUpload.init({
            elem: "#uploader",
            auto: false,
    		isExpand: $("#attVoisExpand").val() === 'true'||false,
            hasDelete: $("#attVohasDelete").val() === 'true'||false,
            hasUpload: $("#attVohasUpload").val() === 'true'||false,
            gid: $("#attachmentId").val(),
    		success:function(data){
    			opAttachment();
    		}
        });
        btUpload2 = bt.btUpload.init({
            elem: "#uploader2",
            auto: false,
            isExpand:true,
            hasDelete:false,
            hasUpload:false,
            gid: $("#attachmentId2").val(),
            success:function(data){
                opAttachment2();
            }
        });
    });
    
    function opAttachment(){
    	var attachmentData=uploader.getData();
    	var attachmentNum=attachmentData.length;
    	var attachmentId=uploader.getGroupId();
    	if(!attachmentId){
    		attachmentId=$("#attachmentId").val();
    	}
    	var id=$("#attVoid").val();
    	var vvid=$("#attVovvid").val();
    	var briefId=$("#attVobriefId").val();
    	var data={
    		'attachmentId':attachmentId,
    		'attachmentNum':attachmentNum,
    		'vvid':vvid,
    		'id':id,
    		'briefId':briefId
    	}
    	$.ajax({
        	type: 'POST',
        	url: basePath+"/bill/accebill/abbillattachment/opInfo", 
        	data:data,	
        	async: false, 
        	dataType:'json',
        	success: function(result){
            	if(!result.success){
            		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            	}
          	}
    	});
    }
function opAttachment2(){
    var attachmentData=uploader2.getData();
    var attachmentNum=attachmentData.length;
    var attachmentId=uploader2.getGroupId();
    if(!attachmentId){
        attachmentId=$("#attachmentId2").val();
    }
    var id=$("#attVoid2").val();
    var vvid=$("#attVovvid2").val();
    var briefId=$("#attVobriefId2").val();
    var data={
        'attachmentId':attachmentId,
        'attachmentNum':attachmentNum,
        'vvid':vvid,
        'id':id,
        'briefId':briefId
    }
    $.ajax({
        type: 'POST',
        url: basePath+"/bill/accebill/abbillattachment/opInfo",
        data:data,
        async: false,
        dataType:'json',
        success: function(result){
            if(!result.success){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            }
        }
    });
}