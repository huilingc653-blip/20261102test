var corp,dicts,moneyType,dateType;
var idsValArray=[];
bt.use(['form','datatable','moneyType','dateType','corp','select','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    moneyFun = bt.moneyType.getFun();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    dateType=bt.dateType.init();
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
            'url': basePath+"/bill/accebill/abBillDiscountAccount/datatable"
        },
        buttons: [
          {
                text: bt.lang('common.button.arrivalConfirm'),
                hide:bt.isnotResource('bill:accebill:abBillDiscountAccount:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	submitBatch(data);
                }
            },
            {
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
            {data:"cnAccrecords",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:"textRight",
            	render:function(data, type, full, meta){
            		return bt.money.format(full.billMoney);
            	}},
            {
                data: "realMoney", bSortable: false,
                render: function (data, type, full, meta) {
                    var realMoneyInput;
                    if(full.status=='11'||full.status=='94'){
                        realMoneyInput='<div id="'+(full.accId||'')+'realMoney" name="'+(full.accId||'')+'realMoney">&nbsp;&nbsp;&nbsp;'+data+'</div>';
                    }else {
                        realMoneyInput='<input class="layui-input bt-input-inline bt-biz-moneyType" data-id="'+(full.accId||'')+'realMoney" data-name="'+(full.accId||'')+'realMoney" value="'+(data||'')+'"  data-min = "0" type="text" data-maxlength="30"/>';
                    }
                    return realMoneyInput;
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"signerok",bSortable:true,
            render:function(data,type,full,meta){
            	var reInfo;
            	if(full.status=='11'||full.status=='94'){
            		reInfo='<div id="'+(full.accId||'')+'Signerok" name="'+(full.accId||'')+'Signerok">&nbsp;&nbsp;&nbsp'+data+'</div>';
            	}else{
            	    reInfo='<input id="'+(full.accId||'')+'Signerok" name="'+(full.accId||'')+'Signerok" value="'+(data||'')+'" class="layui-input" type="text" data-maxlength="30"/>';
            	}
        		   return reInfo;
            	  }},
            {data:"statusName",bSortable:true},
            {data:"briefIdName",bSortable:true},
            {data:"enterStockName",bSortable:true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
               // html+='<span class="text-explode">|</span>';
                // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        btClickRowCallback:function(data){
            if(data&&data.length&&data.length>0){
                for(var i=0;i<data.length;i++){
                    //数据选中后，收款人必填
                    if(data[i].isSelect){
                        $('#'+data[i].accId+'Signerok').attr("lay-verify","strlength");
                    }else{
                        $('#'+data[i].accId+'Signerok').attr("lay-verify","");
                    }
                }
            }
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            bt.moneyType.init()
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
//批量提交
function submitBatch(data){
	var len = data.length;
	var rowData = data.splice(0,len);
	if(!len){
		layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
		return;
	}
	//批量提交权限控制
    for(var i=0;i<len;i++){
        if(bt.isnotResource('bill:accebill:abBillDiscountAccount:apply',rowData[i].corpId)){
       	layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
       	 return;
       }
    }
	if(!bt.form().verifyFun('#dataForm')){
        return false;
    }
	if(!bt.form().verifyFun('#appTable')){
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'),function(){
    	url=basePath + "/bill/accebill/abBillDiscountAccount/batchSubmit";
        var vouIds,viceIds,accIds,signeroks,realMoney,signerokNos;
        var businessDate=$("#businessDate").val();
        for(var i=0;i<len;i++){
        	var signeroksNew=$('#'+rowData[i].accId+'Signerok').val();
        	var realMoneyNew=$('#'+rowData[i].accId+'realMoney').val();
        	if(accIds){
        		accIds=accIds+","+rowData[i].accId;
        		signeroks=signeroks+","+signeroksNew;
        		realMoney += "," + realMoneyNew;
        	}else{
        		accIds=rowData[i].accId;
        		signeroks=signeroksNew;
        		realMoney=realMoneyNew;
        	}
        }

        $.post(url,{"accIds":accIds,
        	        "signeroks":signeroks,
                    "realMoneys":realMoney,
        	        "businessDate":businessDate},function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
        		bt.datatable.refreshCurrentPage();
            }
        },'JSON');

    })
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].vouId)<0){
                    idsValArray.push(datas[i].vouId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].vouId);
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
