var fromObj={},selectType={};
bt.use(['form','corp','dicts','btdate','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    fromObj=form;
//    bt.select.init({
//        elem:'#voucherType',
//        name:"voucherType",
//        valueField:"id",
//        textField:"name",
//        placeholder:null,
//        search: true,
//        disabled: false,
//        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValid",
//        change:function(data,text){
//        	location.replace(basePath+"/bill/blankvou/bvprintvouopinfo/list?vouTid="+data.value);
//        }
//    });
    selectType=bt.selectType.init();
    bt.voucherTypeReplace=function(data){
    	location.replace(basePath+"/bill/blankvou/bvprintvouopinfo/list?vouTid="+data.id);
    }
  //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",//需显示日期的元素选择器
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false,
            choose: function(dates){ //选择好日期的回调
            	 //开始时间
            	 if($(item).attr("field")=="ged_opdatepop"&&$("#led_opdatepop").val()!=" "){
            		 if($("#ged_opdatepop").val()>$("#led_opdatepop").val()){
            			 parent.layer.alert(bt.lang("bill.blankvou.led_inputDate"));
            			 $("#ged_opdatepop").val(" ");
            		 }
            	 }
            	 //结束时间
            	 if($(item).attr("field")=="led_opdatepop"&&$("#ged_opdatepop").val()!=" "){
            		 if($("#led_opdatepop").val()<$("#ged_opdatepop").val()){
            			 parent.layer.alert(bt.lang("bill.blankvou.ged_inputDate"));
            			 $("#led_opdatepop").val(" ");
            		 }
            	 }
            }
        });
    });
    bt.corp.init();
    bt.dicts.init();
    
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
//        content: basePath + "/bill/blankvou/bvprintvouopinfo/view?dataId="+data.id+"&popId="+data.idpop+"&vouTid="+$("#voucherType").find('select').val()
        content: basePath + "/bill/blankvou/bvprintvouopinfo/view?dataId="+data.id+"&popId="+data.idpop+"&vouTid="+selectType.get("voucherType").val()
    });
}
var sceneNameInfo='';
var toBatchPrint=function(flag,desigeFlag,dt,sceneId,sceneName){
	sceneNameInfo=sceneName;
	if(!fromObj.verifyFun('#fromVerify')){
        return false;
    }
//	var vouTid=$("#voucherType").find('select').val();
	var vouTid=selectType.get("voucherType").val();
	var data=dt.rows('.selected').data();
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
	var rowData = data.splice(0,len);
    var ids;
    var printNo;
    for(i=0;i<len;i++){
    	if(bt.isnotResource('bill:blankvou:bvPrintVouOpinfo:print',rowData[i].recCorpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
    	var no=$("#vounopop"+rowData[i].id).val();
    	if(!no){
    		no='undefined';
    	}
    	if(ids){
    		ids=ids+","+rowData[i].id;
    		printNo=printNo+","+no;
    	}else{
    		ids=rowData[i].id;
    		printNo=no;
    	}
    }
	var isInbox=bt.jquery("#isInbox").val();
	var printData=loadChequeDtl(vouTid,ids,isInbox,printNo,sceneId);
	for(i=0;i<printData.length;i++){
		var info=printData[i];
		if(info.isMatch==0){//打印模版匹配失败
			var sceneMsg='';
			if(sceneName){
				sceneMsg=sceneName+','
			}
			layer.msg(sceneMsg+bt.lang('bill.bvprintfmt.checknoMatch',[info.index]), {icon: 2});
			return false;
		}else if(info.isMatch==2){//凭证号获取失败
			layer.msg(bt.lang('bill.bvprintfmt.getVouNoError',[info.index]), {icon: 2});
			return false;
		}else if(info.isMatch==3){//手动填写的凭证号不存在
			layer.msg(bt.lang('bill.bvprintfmt.setVouNoError',[info.index,info.vouNo]), {icon: 2});
			return false;
		}else if(info.isMatch==4){//获取的凭证号和之前的重复
			layer.msg(bt.lang('bill.bvprintfmt.vouNoRepError',[info.index,info.vouNo]), {icon: 2});
			return false;
		}
	}
	billFormatPrint(printData,flag, desigeFlag);
}

/**
 * 通过凭证类型id和套打数据ID获取凭证套打数据打印格式信息
 * @param vouTid 凭证类型id
 * @param sids 套打数据ID
 */
var loadChequeDtl=function (vouTid,sids,isInbox,printNo,sceneId){
	var action = "/bill/blankvou/bvprintvouopinfo/loadPrintFormat";
	var printData;
	bt.jquery.ajax({
		type: "POST",
		url: basePath + action,
		data: JSON.stringify({"dataIds":sids,"vouTid":vouTid,"isInbox":isInbox,"printNo":printNo,"sceneId":sceneId}),
		dataType:"json",
		async: false, 
		contentType : 'application/json;charset=utf-8', //设置请求头信息 
		success: function(result){
			printData= result;
		}
	});
	return printData;
}

/**
 * 通过凭证类型id和套打数据ID获取凭证套打数据打印格式信息
 * @param vouTid 凭证类型id
 * @param sids 套打数据ID
 */
var printOccupyVouNo=function (printData){
	var action = "/bill/blankvou/bvprintvouopinfo/printOccupyVouNo";
	var printDataNew = []; 
	for(i=0;i<printData.length;i++){
		printDataNew.push({
			"vouTid":printData[i].vouTid,
			"vouNo":printData[i].vouNo,
			"vouId":printData[i].vouId,
			"opInfoId":printData[i].opInfoId,
			"vouNo":printData[i].vouNo,
			"money":printData[i].money,
			"sceneName":sceneNameInfo,
			"servicebeanpop":printData[i].servicebeanpop
		});
	}
	var postData={"printData":printDataNew};
	postData=JSON.stringify(postData);
	bt.jquery.ajax({
    	type: "POST",
    	url: basePath + action,
       	data: postData,
       	dataType:"json",
       	async: false, 
       	contentType : 'application/json;charset=utf-8', //设置请求头信息 
    	success: function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    		if(result.success){
    			bt.datatable.refreshCurrentPage();
            }
    	}
    });
	return printData;
}

