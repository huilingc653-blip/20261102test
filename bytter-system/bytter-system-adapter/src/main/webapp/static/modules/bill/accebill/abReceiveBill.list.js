var corp,dicts,moneyType,dateType,up,billTable;
var idsValArray=[];
bt.use(['form','datatable','moneyType','corp','select','dicts','dateType','btImport','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    dateType=bt.dateType.init();
    bt.mulselect.init();
    importExcel = function () {
        up = bt.btImport.init({
            code: "20000001",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head,data,select) {
                bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath + "/bill/accebill/abreceivebill/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if(result.success){
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abreceivebill/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:accebill:abreceivebill:register'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
            	 text: bt.lang('common.button.batchDelete'),
            	 hide:bt.isnotResource('bill:accebill:abreceivebill:delete'),
                 action: function ( e, dt, node, config ) {
                     var data = dt.rows('.selected').data();
                     deleteBatch(data);
                  }
            },{
                text: bt.lang('common.button.submit'),
                hide:bt.isnotResource('bill:accebill:abreceivebill:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	submitBatch(data,e);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },
           /* {
                text: bt.lang('common.button.exports'),
                hide:bt.isnotResource('bill:accebill:abreceivebill:exports'),
                action: function ( e, dt, node, config ) {

                }
            },*/
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
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"businessDate",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"cnAccrecords",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"enterStockName",bSortable:true},
            {data:"billSourceName",bSortable:true},
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
            {data:"attachmentNum",bSortable:false,className:'center',render: function (data, type, full, meta) {
         	   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="attachmentView(this)">'+bt.lang("bill.accebill.attachment.num",[(full.attachmentNum||0)])+'</a>';
        	   return html;
           }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('bill:accebill:abreceivebill:update',full.applyCorpId)&&full.status!='11'&&full.status!='94'){
                 html+='<span class="text-explode">|</span>';
                 html+='<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                }
                if(bt.isResource('bill:accebill:abreceivebill:delete',full.applyCorpId)&&full.status!='11'&&full.status!='94'){
                 html+='<span class="text-explode">|</span>';
                 html+='<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                }
//                html+='<span class="text-explode">|</span>';
                // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
            ]
        }
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/accebill/abreceivebill/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/add",
        end:function(){
//            datatable && datatable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abreceivebill/view?viceId="+data.viceId+"&accId="+data.accId);
        return;
    }
   layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId="+data.viceId+"&accId="+data.accId
    });
}
function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/accebill/abreceivebill/update?accId="+data.accId);
        return;
    }
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/abreceivebill/update?accId="+data.accId,
            end:function(){
//                datatable && datatable.draw();
    			bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.ajax({
        	type:'post',
        	data:{accId:data.accId},
        	url:basePath + "/bill/accebill/abreceivebill/delete",
        	success:function(result){
             layer.alert(result.msg, {icon: result.success?1:2});
        	 if(result.success){
	    		bt.datatable.refreshCurrentPage();
        	 }
        },
           dataType:'JSON'
        });
    });
}

//批量删除
function deleteBatch(data){
	var len = data.length;
	var rowData=data.splice(0,len);
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    for(var i=0;i<len;i++){
        if(bt.isnotResource('bill:accebill:abreceivebill:delete',rowData[i].applyCorpId)){
       	layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
       	 return;
       }
    }
    layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
    	url=basePath + "/bill/accebill/abreceivebill/batchDelete";
        var accIds;
        for(var i=0;i<len;i++){
        	if(accIds){
        		accIds=accIds+","+rowData[i].accId;
        	}else{
        		accIds=rowData[i].accId;
        	}
        }
        $.post(url,{"accIds":accIds}, function(result){
        	layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
    			bt.datatable.refreshCurrentPage();
             }
        },'JSON');
    });
}

//批量提交
function submitBatch(data){
	var len = data.length;
	var rowData=data.splice(0,len);
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    //权限控制
    for(var i=0;i<len;i++){
    if(bt.isnotResource('bill:accebill:abreceivebill:apply',rowData[i].applyCorpId)){
    	layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    	return;
    }
   }

   var corpArray1=[];//单位不需挂资金计划
   var corpArray2=[];//单位需要挂资金计划
   var budgetOrigNos;
   //资金计划控制
   for(var i=0;i<len;i++){
           var index1=$.inArray(rowData[i].corpId,corpArray1);
           var index2=$.inArray(rowData[i].corpId,corpArray2);
           if(index1>= 0){

           }else if(index2>=0){
               $.ajax({
                   type:'post',
                   url:basePath+"/budget/fbsBudgetProcess/getBudgetProcesInfo",
                   data:{businessName:'rec',
                         processSysname:'ab',
                         billNo:rowData[i].viceId,
                         billId:rowData[i].viceId
                    },
                   async:false,
                   dataType:'json',
                   success:function(result){
                       if(!result.length){         //资金计划status为暂存
                           if(budgetOrigNos){
                               budgetOrigNos=budgetOrigNos+rowData[i].origNo+"<br>";
                           }else{
                               budgetOrigNos="<br>"+rowData[i].origNo+"<br>";
                           }
                       }
                   }
               });
           }else{
       $.ajax({
           type: 'POST',
           url: basePath+"/bill/accebill/abvoucherinfoVice/isBudget",
           data:{'corpId':rowData[i].corpId,'briefId':'6001'},
           async: false,
           dataType:'json',
           success: function(result){
               if(result.success){
                   if(result.attributes.isBudget){
                       corpArray2.push(rowData[i].corpId);
                       //判断是否挂过资金计划
                       $.ajax({
                           type:'post',
                           url:basePath+"/budget/fbsBudgetProcess/getBudgetProcesInfo",
                           data:{businessName:'rec',
                                 processSysname:'ab',
                                 billNo:rowData[i].viceId,
                                 billId:rowData[i].viceId
                           },
                           async:false,
                           dataType:'json',
                           success:function(result){
                               if(!result.length){
                                   if(budgetOrigNos){
                                       budgetOrigNos=budgetOrigNos+rowData[i].origNo+"<br>";
                                   }else{
                                       budgetOrigNos="<br>"+rowData[i].origNo+"<br>";
                                   }
                               }
                           }
                       });
                   }else{
                   corpArray1.push(rowData[i].corpId);
                   }
               }else{
                   corpArray1.push(rowData[i].corpId);
               }
           }});
       }
   }
   if(budgetOrigNos){
       layer.alert(bt.lang("accebill.receivebill.isBudget.origNoMsg",[budgetOrigNos]),{icon: 7});
       return;
   }
    var accIds;
    // {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
    var bwDataParams=[];
    for(var i=0;i<len;i++){
        if(accIds){
            accIds=accIds+","+rowData[i].accId;
        }else{
            accIds=rowData[i].accId;
        }
        bwDataParams.push({"name":rowData[i].cnAccrecords,"appNo":rowData[i].origNo,"corpId":rowData[i].corpId})
    }
    bt.loading.show();
    //对黑白名单进行验证
    var reData=externalCorpCheckBatch({"isControl":"0","params":bwDataParams});
    bt.loading.hide();
    if(reData.reCode=='3'){
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});

    }else if(reData.reCode=='2'){
        parent.layer.confirm(reData.msg,function(){
            receiveSubmit(accIds);
        });
    }else{
        receiveSubmit(accIds);
    }
 }

 function receiveSubmit(accIds){
     layer.confirm(bt.lang('common.message.confirm.submit'),function(){
         bt.loading.show();
         $.post(basePath + "/bill/accebill/abreceivebill/batchSubmit",{"accIds":accIds}, function(result){
             bt.loading.hide();
             layer.alert(result.msg, {icon: result.success?1:2});
             if(result.success){
                 bt.datatable.refreshCurrentPage();
             }
         },'JSON');
     });
 }

function attachmentView(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var isNotOp=data.status!=11?'0':'1';
    var params={
    		"id":data.accId,
    		"attachmentId":(data.attachmentId||null),
    		"vvid":(data.viceId||null),
    		"briefId":(data.briefId||null),
    		"isNotOp":isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end:function(){
        	billTable.draw();
        }
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].accId)<0){
                    idsValArray.push(datas[i].accId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].accId);
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
