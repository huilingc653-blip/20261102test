var billTable={};
var idsValArray=new Array();
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
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
            'url': basePath+"/bill/accebill/abbilltransferdownapply/datatable"
        },
        buttons: [
            //新增、删除、提交、下载
            {
                 text: bt.lang('common.button.add'),
                 hide:bt.isnotResource('ab_bill:resource:transferdownapply:opreate'),
                 action: function ( e, dt, node, config ) {
                 	add();
                 }
             },{
                 text: bt.lang('common.button.delete'),
                 hide:bt.isnotResource('ab_bill:resource:transferdownapply:opreate'),
                 action: function ( e, dt, node, config ) {
                	 var data = dt.rows('.selected').data();
                	 batchDelete(data);
                 }
             },
            {
                text: bt.lang('common.button.submit'),
                hide:bt.isnotResource('ab_bill:resource:transferdownapply:opreate'),
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
                   {data:"recordCorpName",bSortable:true,
                       render: function ( data, type, full, meta ) {
                          	return '('+full.recordCorpCode+')'+full.recordCorpName;
                          }
                      },
                   {data:"recevieBankacc",bSortable:true},
                   {data:"allocationMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.allocationMoney);
                       }
                   },
                   {data:"allocationReason",bSortable:true},
                   {data:"statusName",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                       var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   if(data.status==0||data.status==-1){//保存、审批打回
						   if(bt.isResource('ab_bill:resource:transferdownapply:opreate',full.corpId)){
                               html+='<span class="text-explode">|</span>';
							   html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
							   <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
						   }
					   }
                       /*html+='<span class="text-explode">|</span>';*/
					   // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum:'4',
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

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/accebill/abbilltransferdownapply/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownapply/add",
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
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
    	if(bt.isnotResource('ab_bill:resource:transferdownapply:opreate',rowData[i].corpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
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
        $.post(basePath + "/bill/accebill/abbilltransferdownapply/batchSubmit",{"trids":trids,"aids":aids,"businessDate":$("[name=businessdate]").val()}, function(result){
        	bt.loading.hide();
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
    			bt.datatable.refreshCurrentPage();
        	}
        },'JSON');
    });
}

function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	var aid = data.aid;
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/accebill/abbilltransferdownapply/update/"+aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownapply/update/"+aid,
        end:function(){
//            billTable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}

function deleteColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var ids = data.trid+","+data.aid;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abbilltransferdownapply/delete/" + ids, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
    			bt.datatable.refreshCurrentPage();
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
     	if(bt.isnotResource('ab_bill:resource:transferdownapply:opreate',rowData[i].corpId)){
     		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
     		return;
     	}
     }
	 parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){//确定批量删除?
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
	        $.post(basePath + "/bill/accebill/abbilltransferdownapply/batchDelete",{"trids":trids,"aids":aids}, function(result){
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
        		basePath + "/bill/accebill/abbilltransferdownapply/view?aid="+data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownapply/view?aid="+data.aid
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
