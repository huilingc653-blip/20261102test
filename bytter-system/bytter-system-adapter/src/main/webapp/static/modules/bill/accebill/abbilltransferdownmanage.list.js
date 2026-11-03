var billTable={};
var idsValArray=new Array();
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    //申请单位初始化
	bt.select.init({
        elem:'#corpId',
        name:"eqs_recevieCorpId",
        valueField:"id",
        textField:"name",
        placeholder:{value:'',text:bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url:basePath+"/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId="+$("#netId").val()/*,
        change:function(data,text){
        	corpCallback(data);
        }*/
    });

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
            'url': basePath+"/bill/accebill/abbilltransferdownmanage/datatable"
        },
        buttons: [
            //下拨、下拨登记、删除、下载
            {
                 text: bt.lang('common.button.add'),
                 hide:bt.isnotResource('ab:bill:resource:transferdownmanage:opreate'),
                 action: function ( e, dt, node, config ) {
                 	add();
                 }
             },{
                 text: bt.lang('common.button.delete'),
                 hide:bt.isnotResource('ab:bill:resource:transferdownmanage:opreate'),
                 action: function ( e, dt, node, config ) {
                	 var data = dt.rows('.selected').data();
                	 batchDelete(data);
                 }
             },
            {
                text: bt.lang('common.button.submit'),
                hide:bt.isnotResource('ab:bill:resource:transferdownmanage:opreate'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    submit(data);
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	exports(data);
                }
            },*/
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
				   {data:"allocatedCode",bSortable:true},
				   {data:"applyCode",bSortable:true},
				   {data:"recordCorpName",bSortable:true,
                       render: function ( data, type, full, meta ) {
				            var recordCorpName = "";
				            if(full.recordCorpCode!=null && full.recordCorpName!=null){
                                recordCorpName = '('+full.recordCorpCode+')'+full.recordCorpName;
                            }
                         	return recordCorpName;
                         }
                     },
                   {data:"recevieCorpName",bSortable:true,
                         render: function ( data, type, full, meta ) {
                           	return '('+full.recevieCorpCode+')'+full.recevieCorpName;
                           }
                       },
                   /*{data:"billNo",bSortable:true},*/
                   {data:"recevieBankacc",bSortable:true},
                   {data:"applyMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.applyMoney);
                       }
                   },
                   {data:"allocationMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.allocationMoney);
                       }
                   },
                   {data:"applyReason",bSortable:true},
                   {data:"allocationReason",bSortable:true},
                   {data:"statusName",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                       var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   if(data.status!=11){
						   if(bt.isResource('ab:bill:resource:transferdownmanage:opreate',full.corpId)){
                               html = html+ '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                               <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
						   }
					   }
					   // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
					   return html;
				   }}
        ],
       /* fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = new Array()
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
                    colNum:'6',
                    dataName:'applyMoney'
                },
                {
                    colNum:'7',
                    dataName:'allocationMoney'
                }
            ]
        }
    });
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
   var dd = bt.btdate.init({
	   elem:".date-single",
	   // labelname:bt.lang('table.abVoucherinfo.Business.date'),
	   name:"businessdate"
   });
   dd.setValue({value:new Date().Format("yyyy-MM-dd")})
});

//中心回调函数
function netCallback(data){
	location.replace(basePath+"/bill/accebill/abbilltransferdownmanage/list?netId="+data.id);
}

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/accebill/abbilltransferdownmanage/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownmanage/add",
        end:function(){
//        	billTable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}

//批量提交
function submit(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len),trid;
    for(var i=0;i<len;i++){
    	if(bt.isnotResource('ab:bill:resource:transferdownmanage:opreate',rowData[i].corpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
    	//你选择的第{0}条数据没有选择下拨票据!
        trid = rowData[i].trid;
    	if(trid){
            var flag = false;
            $.ajax({
                type: 'POST',
                url: basePath+"/bill/accebill/abbilltransferdownmanage/checkSelectBill",
                data:{'trid':trid},
                async: false,
                dataType:'json',
                success: function(result){
                    if(result.success && !result.attributes.isSelectBill){
                        layer.alert(bt.lang('common.message.abbilltransferdownmanage.checkOrigno',[(i+1)]), {icon: 7});
                        flag = true;
                        return;
                    }
                }});
            if(flag){
                return;
            }
    	}
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){//您确定提交吗？
    	bt.loading.show();
        var trids,aids;
        for(i=0;i<len;i++){
        	if(trids){
        		trids=trids+","+rowData[i].trid;
        	}else{
        		trids=rowData[i].trid;
        	}
        	if(aids){
        		aids=aids+","+rowData[i].aid;
        	}else{
        		aids=rowData[i].aid;
        	}
        }
        $.post(basePath + "/bill/accebill/abbilltransferdownmanage/batchSubmit",{"trids":trids,"aids":aids,"businessDate":$("[name=businessdate]").val()}, function(result){
        	bt.loading.hide();
        	if(result.success){
//        		billTable.draw();
    			bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
        	}else{
        		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	}
        },'JSON');
    });
}

function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	var aid = data.aid;
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/accebill/abbilltransferdownmanage/update/"+aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownmanage/update/"+aid,
        end:function(){
//            billTable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}

function deleteColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abbilltransferdownmanage/delete/" + aid, function(result){
        	if(result.success){
//        		billTable.draw();
    			bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
        	}else{
        		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	}
        },'JSON');
    });
}

function batchDelete(data){
	 var len = data.length;
     if(!len){
         layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	 return;
     }
     var rowData = data.splice(0,len);
     for(i=0;i<len;i++){
     	if(bt.isnotResource('ab:bill:resource:transferdownmanage:opreate',rowData[i].corpId)){
     		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
     		return;
     	}
     }
	 parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){//确定批量删除?
	        var trids,aids;
	        for(i=0;i<len;i++){
	        	if(aids){
	        		trids=trids+","+rowData[i].trid;
	        		aids=aids+","+rowData[i].aid;
	        	}else{
	        		trids=rowData[i].trid;
	        		aids=rowData[i].aid;
	        	}
	        }
	        $.post(basePath + "/bill/accebill/abbilltransferdownmanage/batchDelete",{"trids":trids,"aids":aids}, function(result){
	        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
	        	if(result.success){
	    			bt.datatable.refreshCurrentPage();
	        	}
	        },'JSON');
	    });
}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abbilltransferdownmanage/view?businessId="+data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownmanage/view?businessId="+data.aid
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].aid)<0){
                    idsValArray.push(datas[i].aid);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].aid);
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
