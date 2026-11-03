bt.use(['form','datatable','corp','moneyType','dateType','dicts'], function(){
    $ = bt.jquery,form = bt.form();

    //单位
    bt.corp.init();

    //字典-状态
    bt.dicts.init();

    //时间
    bt.dateType.init();

    //借款金额
    bt.moneyType.init();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == "null"){
            return true;
        }
        return false;
    }


  //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        colReorder: true,
        ordering:false,
        ajax: {
            'url': basePath+"/cms/provide/abolish/tableCancellationStart"
        },
        buttons:[],
        columns: [
            {data:"corpName",bSortable:true,className:"center"},
            {data:"bankName",bSortable:true,className:"center"},
            {data:"billCode",bSortable:true,className:"center"},
            {data:"contractNo",bSortable:true,className:"center"},
            {data:"plMoney",bSortable:true,className:"textRight",
            	render:function(data,type,full,meta){
            		var plMoney = bt.money.format(full.plMoney);
            		return plMoney;
            	}},
            {data:"curName",bSortable:true,className:"center"},
            /*{data:"sendMoney",bSortable:true,className:"textRight",
            	render : function(data,type,full,meta){
            		var sendMoney = bt.money.format(full.sendMoney);
            		return sendMoney;
            	}},
            {data:"sendCurName",bSortable:true,className:"center"},*/
            {data:"plDate",bSortable:true,className:"center",
              render : function(data,type,full,meta){
                return '<div style="min-width: 70px;">'+data+'</div>';
              }},
            {data:"repayDate",bSortable:true,className:"center",
              render : function(data,type,full,meta){
                return '<div style="min-width: 70px;">'+data+'</div>';
              }},
            {data:"plTerm",bSortable:true,className:"center",
            	render : function(data,type,full,meta){
            		var term = full.plTerm + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
            		return term;
            	}},
            {data:"abolishedDate",bSortable:true,className:"center"},
            {data:"status",bSortable:true,className:"center",
            	render :function(data,type,full,meta){
            		return bt.dict('cms.bizStatus', full.bizStatus);
            	}},

            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('cms:provide:abolish:handle:edit')){
                	if(full.bizStatus == "87"){
                		html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="Handle(this)">'+bt.lang("common.button.handle")+'</a>';//办理
                    	html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="repeal(this)">'+bt.lang("common.button.repeal")+'</a>';//撤销
                	}
                	if(full.bizStatus =="88"){
                		html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="repeal(this)">'+bt.lang("common.button.repeal")+'</a>';//撤销
                	}
                }
                html +=' \<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                // if(full.id != null && full.id != ""){
            	// 	html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
            	// }
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum:false,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                }
            ]
        }
    });
});


function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/abolish/view?infoBillCode=" + data.billCode + "&struts=" + 1
    });
}

function Handle(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var msg=bt.lang('common.determine.handle');
    var relateCon=$("#relateCon").val();
    if(relateCon == "1" && !bt.empty(data.conId)){//参数设置对接合同管理，并且合同id不为空时先校验合同状态
        $.post(basePath + "/cms/provide/abolish/getConInfo?conId=" + data.conId+"&busId="+data.id, function(result){
            if(result==90){//合同已终止
                msg=bt.lang('provide.contract.hasEnd');
            }else if(result==80){//合同终止中
                layer.alert(bt.lang("provide.contract.isEnding"));
                return;
            }else if(result==70){//合同变更中
                layer.alert(bt.lang("provide.contract.isChange"));
                return;
            }
            parent.layer.confirm(msg,function(){
                $.post(basePath + "/cms/provide/abolish/handle/" + data.id, function(result){
                    bt.navtab.refreshCurrentTable();
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                },'JSON');
            });
        },'JSON');
    }else{
        parent.layer.confirm(msg,function(){
            $.post(basePath + "/cms/provide/abolish/handle/" + data.id, function(result){
                bt.navtab.refreshCurrentTable();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }


}

function repeal(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.determine.repeal'),function(){
        $.post(basePath + "/cms/provide/abolish/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);

        }
	}
}

function formatDate(data,input){
	var date = addDate(data);
	$("#date").val(date);
}


function addDate(date,days){
	if(days == undefined || days == ''){
		days = 1;
	}
	var date = new Date(date);
	date.setDate(date.getDate() + days);
	var month = date.getMonth() +1;
	var day = date.getDate();
	return date.getFullYear() + '-' + month + '-' + day;

}
