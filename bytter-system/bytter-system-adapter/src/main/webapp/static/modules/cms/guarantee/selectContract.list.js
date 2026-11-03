var dateTypes,dicts,selectTypes,moneyTypes,guaContractInit,parameters,moneyFun;
var contractIds="";
var contractCodes="";
var guaWayNames="";
var guaWayCodes="";
bt.use(['form','jquery','dateType','selectType','dicts','moneyType','selectType','guaContract'],function(){
    $ = bt.jquery,form = bt.form();
	dateTypes=bt.dateType.init();
	dicts = bt.dicts.init();//字典
	selectTypes=bt.selectType.init();
	moneyTypes = bt.moneyType.init();//金额
	moneyFun = bt.moneyType.getFun();
	parameters=new Object();
	guaContractInit=bt.guaContract.init();
	
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
});
bt.guaContractBeforeClick=function(){
	var corpId=$("#corpId").val();
	var startDate = $("#startDate").val();//担保开始日期
	var endDate = $("#endDate").val();//担保结束日期
    var curId=$("#curId").val();
	parameters.corpId=corpId;
	parameters.startDate=startDate;
	parameters.endDate=endDate;
    parameters.curId=curId;
	parameters.parentIframe=window.name;
	bt.guaContract.set(parameters);
	return true;
}
//担保合同信息
function guaContractCallback(data,dom){
	var str1="";
    $.each(data, function(index, item){
    	if(bt.empty(data)){
    		return;
		}else if(!$("#"+item.id).length){//判断是否已经存在了相应的的行
            str1 +='<tr id="'+item.id+'">'+
                '<td style="text-align:center">'+item.contractCode+'</td>'+
                '<td style="text-align:center">'+item.guaNo+'</td>'+
                '<td style="text-align:center">'+item.guaPerson+'</td>'+
                '<td style="text-align:center">'+item.guaTypeName+'</td>'+
                '<td style="display:none ">'+item.guaWayCode+'</td>'+
                '<td style="text-align:center">'+item.startDate+'</td>'+
                '<td style="text-align:center">'+item.endDate+'</td>'+
                '<td style="text-align:center">'+item.curName+'</td>'+
                '<td style="text-align:right">'+bt.money.format(item.guaMoney)+'</td>';
            str1+='<td style="text-align:center"><button type="button" class="layui-btn del">'+bt.lang("common.button.delete")+'</button></td>'+
                +'</tr>'

        }
    });
    $("#guaContract").append(str1);
}


/**
 * 删除相应的行
 */
$("#infoTable").on('click','tr .del',function(){
	$(this).parents('tr').remove();
});

/**
 * 拼接数据
 */
function sureButton(){
	$('#infoTable tbody tr').each(function(i){ 
		var currentId = $(this).attr('id');
		var contractCode="";
        var guaWayName="";
		if(currentId!=undefined){
            $(this).children('td').each(function(j){
                if(j==0){
                    contractCode=$(this).text();
					if (i == 0) {
						contractCodes = contractCode
					}else{
                    	contractCodes+=","+contractCode;
					}
                }
                if(j==3){//取担保方式
                	guaWayName=$(this).text();
                    guaWayNames+=guaWayName+"、";
				}
                if(j==4){//取担保方式code
                    guaWayCodes+=$(this).text()+",";
                }

            });
			if (i == 0) {
				contractIds = currentId
			}else{
				contractIds += "," + currentId;
			}
		 }
	});
	submitData();
}

/**
 * 将数据回写给前一个页面
 */
var submitData = function(){
	var parentWindow = top.guaContractParentWindow || bt.navtab.getParentDom();
	if(parentWindow){
		parentWindow.updateInfo(contractIds,contractCodes,guaWayNames,guaWayCodes)
	}
	if(top.guaContractParentWindow){
		var index = top.layer.getFrameIndex(window.name);
		top.layer.close(index);
	}else if(bt.navtab){
		bt.navtab.closeCurrent();
	}
}


