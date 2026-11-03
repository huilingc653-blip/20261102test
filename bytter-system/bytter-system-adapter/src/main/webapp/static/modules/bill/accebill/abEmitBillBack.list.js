var billTable={};
var idsValArray=[];
bt.use(['form','datatable','corp','dicts','moneyType','btdate','element','mulselect'], function(){

	$ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.mulselect.init();
    bt.btdate.init({
    	elem:"#startDate",
    	startname:"ged_startDate",
    	endname:"led_startDate"
    });
    bt.btdate.init({
    	elem:"#endDate",
    	startname:"ged_endDate",
    	endname:"led_endDate"

    });
    //生成列表
    billTable= bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,

        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abemitbillback/datatable"
        },
        buttons: [
		{
		    text: bt.lang('common.button.returnTicket'),
		    hide:bt.isnotResource('bill:accebill:abemitbillback:confirm'),
		    action: function ( e, dt, node, config ) {
                var data = dt.rows('.selected').data();
                returnTicket(data);
            }
		}
		]
        ,columns: [
                {data: "applyCorpName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                    }
                },
                {
                    data: "corpIdName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return '(' + full.corpCode + ')' + full.corpIdName;
                    }
                },
                {data: "busCorpName", bSortable: true},
                {data: "busPersonnel", bSortable: true},
               {data:"origNo",bSortable:true},
               {data:"billTypeName",bSortable:true},
               {data:"billFlagName",bSortable:true},
               {data:"insideSingName",bSortable:true},
               {data:"payer",bSortable:true},
               {data:"acceptor",bSortable:true},
               {data:"billMoney",bSortable:true,className:'textRight',
                   render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                   }
               },
               {data:"startDate",bSortable:true},
               {data:"endDate",bSortable:true},
               {data:"payee",bSortable:true},
               {data:"sourceName",bSortable:true},
               {data:"briefIdName",bSortable:true},
               {data:"sendSignName",bSortable:true},
               {data:"statusName",bSortable:true},
               {data:"batchNo",bSortable:true},
               {data:"newStmIssuofpprInd",bSortable:true,
                   render: function (data, type, full, meta) {

                       return full.newStmIssuofpprIndName;
                   }},
               {data:"blgrdbagcrclPrmtInd",bSortable:true,
                   render: function (data, type, full, meta) {

                       return full.blgrdbagcrclPrmtIndName;
                   }},
               {data:"sbbllRngStrtSn",bSortable:true},
               {data:"sbbllRngEndSn",bSortable:true},
               {data:"blStdAmt",bSortable:true,className:'textRight',
                   render: function ( data, type, full, meta ) {
                   if(full.newStmIssuofpprInd == 1){
                       return bt.money.format(full.blStdAmt);
                   }else {
                       return "";
                   }
                   }},
               {data:"blRmtrUnnIdNo",bSortable:true},

               {
                   data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;}
               }
        ],
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = []
            setSumMoney();
        },
        fixedColumns:{
        	rightColumns:1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'11',
                    dataName:'billMoney'
                }
            ]
        }
    });
});

bt.use(['btdate'], function() {
	var date = new Date();
	var currentTime= date.Format("yyyy-MM-dd");
	var dd = bt.btdate.init({
	   elem:".date-single",
	   name:"date"});

	   dd.setValue({value:currentTime})
	});

function returnTicket(data){
    if(!bt.form().verifyFun('#dataForm')){
        return false;
    }
	var date=document.getElementById("date");
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len);
	var ids, vids, vvids,rmk;
	for (i = 0; i < len; i++) {
		if (bt.isnotResource('bill:accebill:abemitbillback:confirm',
				rowData[i].applyCorpId)) {
			layer.alert(bt
					.lang('common.message.dataResourceCheck', [ (i + 1) ]), {
				icon : 7
			});
			return;
		}
		if(rowData[i].source == '10'){
			if(!$("#reason").val()){
		    	//付款票据的退票原因必填
		    	layer.alert(bt.lang('abemitbillback.backReasonRequired'), {icon: 7});
		     	return;
		    }
		}
		if (vids) {
			vids = vids + "," + rowData[i].vid;
		} else {
			vids = rowData[i].vid;
		}
		if (vvids) {
			vvids = vvids + "," + rowData[i].vvid;
		} else {
			vvids = rowData[i].vvid;
		}
		if (ids) {
			ids = ids + "," + rowData[i].id;
		} else {
			ids = rowData[i].id;
		}
	}
    //证书签名
    var signDataMsg = signData("未用退票提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.returnticket'),function(){
        $.post(basePath + "/bill/accebill/abemitbillback/returnTicket",{"date":date.value,"ids":ids,"vids":vids,"vvids":vvids,rmk:$("#reason").val()}, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
    			bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abemitbill/view?viceId="+data.vvid+"&accId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['930px', '550px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view?viceId="+data.vvid+"&accId="+data.id
    });
}

function exports(el){
	parent.layer.alert(bt.lang("common.message.indevelopment"));
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abemitbillback/delete/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
			bt.datatable.refreshCurrentPage();
        },'JSON');
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