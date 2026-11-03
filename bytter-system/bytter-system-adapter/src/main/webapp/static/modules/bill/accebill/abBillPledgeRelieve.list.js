var idsValArray=[];
bt.use(['form','datatable','moneyType','moneynum','btdate','corp','select','dicts','element','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.mulselect.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillpledgerelieve/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('bill:accebill:abbillpledgerelieve:relieve'),
                text: bt.lang('bill.accebill.update.pledgeRelieve'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	submitBatch(data);
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {

                }
            },*/{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"cnAccrecords",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"enterStockName",bSortable:true},
            {data:"briefIdName",bSortable:true},
            {data:"billSourceName",bSortable:true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return full.blStdAmt;
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            {
                data: "transMoney", bSortable: true, className: 'textRight', width: 120,
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.vouId+'" data-key="'+full.vouId+'" data-name="'+full.vouId+'" data-biz-name="transMoney_'+full.vouId+'" />';
                    }else{
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.vouId+'" readonly="readonly" data-name="'+full.vouId+'" data-biz-name="transMoney_'+full.vouId+'" />';
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
			    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
			    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
			    return html;
			}}
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            bt.moneyType.init();
            idsValArray = []
            setSumMoney();
        },
        fixedColumns:{
        	rightColumns:2
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'10',
                    dataName:'billMoney'
                }
                ,{
                colNum:'25',
                    dataName:'transMoney'
            }
            ]
        }
    });
    bt.btdate.init({
		elem : "#startDate",
		startname : "ged_startDate",
		endname : "led_startDate"
	});

	bt.btdate.init({
    	elem:"#endDate",
    	startname:"ged_endDate",
    	endname:"led_endDate"
    });
});

// 表格输入框离开事件，更新表格汇总功能
var timeId = null
$("#appTable").on("blur",".trans-money-input",function () {
    var that = this
    if(timeId){
        clearTimeout(timeId);
        timeId = null
    }
    timeId = setTimeout(function () {
        var key = $(that).data("key");
        var tableData = $("#appTable").DataTable().rows().data()
        if(key && tableData && tableData.length){
            for (var i = 0; i < tableData.length; i++) {
                if(tableData[i].vouId === key){
                    tableData[i].transMoney = $("#transMoney_"+key).val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

bt.use([ 'btdate' ], function() {
	var date = new Date();
	var currentTime = date.Format("yyyy-MM-dd");
	var dd = bt.btdate.init({
		elem : ".date-single",
		// labelname : bt.lang('table.abVoucherinfo.Business.date'),
		name : "businessdate",
		choose : function(data) {
//			console.log(data)
		}
	});
	dd.setValue({value : currentTime})
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abreceivebill/view?vouId="+data.vouId+"&viceId="+data.viceId+"&accId="+data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId="+data.vouId+"&viceId="+data.viceId+"&accId="+data.accId
    });
}

/**
 * 进行批量提交操作
 * @param data 选择的数据
 */
function submitBatch(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len);
    // 新系统出票且分包时，交易金额重新赋值
    for (var i = 0; i < rowData.length; i++) {
        console.log("rowData")
        if (rowData[i].newStmIssuofpprInd === '1' && rowData[i].blgrdbagcrclPrmtInd === '1') {
            // 判断交易金额是否为空
            var transMoney = $("#transMoney_" + rowData[i].vouId).val()
            if (!transMoney) {
                layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                return;
            } else {
                rowData[i].transMoney = transMoney;
            }
        }
    }
    var vouIds,viceIds,accIds,transMoneys;
    var businessdate = $("[name=businessdate]").val();
    for(i = 0;i<len;i++){
    	if(bt.isnotResource('bill:accebill:abbillpledgerelieve:relieve',rowData[i].corpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
    	if(vouIds){
    		vouIds=vouIds+","+rowData[i].vouId;
    	}else{
    		vouIds=rowData[i].vouId;
    	}
    	if(viceIds){
    		viceIds=viceIds+","+rowData[i].viceId;
    	}else{
    		viceIds=rowData[i].viceId
    	}
    	if(accIds){
    		accIds = accIds+","+rowData[i].accId;

    	}else{
    		accIds = rowData[i].accId;
    	}
        if (transMoneys){
            transMoneys = transMoneys +"," + $('input[name="'+rowData[i].vouId+'"]').val();
        }else {
            transMoneys = $('input[name="'+rowData[i].vouId+'"]').val();
        }
    }
    //证书签名
    var signDataMsg = signData("解除质押提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.pledgerelieve'),function(){
        bt.jquery.ajax({
	    	type: "POST",
	    	url: basePath + "/bill/accebill/abbillpledgerelieve/batchRelieve",
	       	data:JSON.stringify({"vouId":vouIds,"viceId":viceIds,"accId":accIds,"businessDate":businessdate,"transMoneys":transMoneys}),
	       	dataType:"json",
	       	async: false,
	       	contentType : 'application/json;charset=utf-8', //设置请求头信息
	    	success: function(result){
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	    		if(result.success){
	    			bt.datatable.refreshCurrentPage();
	        	}
	    	},
	    	error: function(res){
	    	}
	    });

    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}
