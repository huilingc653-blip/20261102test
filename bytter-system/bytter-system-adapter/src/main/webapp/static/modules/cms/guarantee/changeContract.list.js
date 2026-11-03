var dateTypes,dicts,selectTypes,moneyTypes,guaContractInit,parameters,moneyFun;
var contractIds="";
var contractCodes="";
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
	bt.guaContract.set(parameters);
	return true;
}
//担保合同信息
function guaContractCallback(data,dom){
	var str1="";
    $.each(data, function(index, item){
        if(bt.empty(data)){
            return;
        }else if(!$("#"+item.id).length&&!document .getElementsByName(item.id).length){//判断是否已经存在了相应的的行
            str1 +='<tr name="'+item.id+'">'+
                '<td style="text-align:center">'+item.contractCode+'</td>'+
                '<td style="text-align:center">'+item.guaNo+'</td>'+
                '<td style="text-align:center">'+item.guaTypeName+'</td>'+
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
 * 释放担保合同
 */
$("#infoTable").on('click','tr .release',function(){
    var contractId= $(this).parents('tr').attr('id');
    var businessId=$("#businessId").val();
    parent.layer.confirm(bt.lang('message.confirm.release'),function(){
        $.post(basePath + "/cms/guarantee/guaContractChange/release?businessId=" + businessId+"&contractId="+contractId, function(result){
            if(result.success){
                $("#"+contractId).remove();//删除相应行
                parent.layer.alert(bt.lang("common.message.success"), {icon: result.success?1:2});
            }else{
                parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
            }
        },'JSON');
    });

});

/**
 * 点击确定
 */
function sureButton(){
    integratedData();
    submitData();
}

/**
 * 拼接数据
 */
var integratedData=function(){//整理页面数据
    var tmp1 = null;
    var tmp2 = null;
    $('#infoTable tbody tr').each(function(i){
        var currentId = $(this).attr('name');//只取新增的担保合同id
        if(currentId!=undefined){
            $(this).children('td').each(function(j){
                if(j==0){
                    contractCode=$(this).text();
                    if (tmp1 == null) {
                        tmp1 = contractCode;
                    } else {
                        tmp1 += "," + contractCode;
                    }
                }
            });
            if (tmp2 == null) {
                tmp2 = currentId;
            } else {
                tmp2 += "," + currentId;
            }
        }
    });
    contractIds = tmp2;
    contractCodes = tmp1;
}
/**
 * 提交调整担保
 */
var submitData = function(){
    var contractComp;
    if((!bt.empty(contractIds))||(!bt.empty(contractCodes))){
        var id=$("#businessId").val();
        var sysCode = $("#sysCode").val();
        var billCode = $("#billCode").val();
        parent.layer.confirm(bt.lang('rat.message.confirm.change'),function(){
            $.post(basePath + "/cms/guarantee/guaContractChange/submit?businessId=" + id + "&contractIds=" + contractIds + "&billCode=" + billCode + "&sysCode=" + sysCode, function (result) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.loading.hide(); //关闭加载中
                if (result.success) {
                    if (!bt.navtab) {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        var webObj = bt.navtab.getParentDom();
                        webObj.search();
                        bt.navtab.closeCurrent();
                    }
                }
            }, 'JSON');
        });
    }else{
        bt.navtab.closeCurrent();
    }
}


