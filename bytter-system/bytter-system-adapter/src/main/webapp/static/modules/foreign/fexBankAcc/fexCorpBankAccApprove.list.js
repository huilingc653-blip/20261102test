var datatable,dicts;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','selectType','dicts'], function(){
    $ = bt.jquery,form = bt.form();

    /**
     * 重新渲染单位
     * @param nodes
     * @returns {*}
     */
    beforeInitCorp = function (nodes) {
        if (!bt.empty(nodes) && nodes.length>0){
            var data = {};
            data.children = null;
            data.code = null;
            data.id = 'all';
            data.corpName = bt.lang('common.placeholder.all');
            data.name = bt.lang('common.placeholder.all');;
            data.netId = null;
            data.netName = null;
            data.parentId = null;
            data.isLeaf = false;
            data.parentIds = null;
            nodes.unshift(data);
        }
        return nodes;
    }

    /**
     * 交易对手回调初始化
     * @param nodes
     */
    beforeInitCotp = function(nodes){
        if (!bt.empty(nodes) && nodes.length>0){
            var data = {};
            data.id = 'all';
            data.cotpName = bt.lang('common.placeholder.all');
            nodes.unshift(data);
        }
        return nodes;
    }

    /**
     * 产品初始化回调函数
     * @param nodes
     */
    beforeInitPro = function(nodes){
        if (!bt.empty(nodes) && nodes.length>0){
            var data = {};
            data.id = 'all';
            data.proName = bt.lang('common.placeholder.all');
            nodes.unshift(data);
        }
        return nodes;
    }

    /**
     * 币别初始化回调函数
     * @param nodes
     */
    beforeInitCur = function(nodes){
        if (!bt.empty(nodes) && nodes.length>0){
            var data = {};
            data.id = 'all';
            data.englishCode = bt.lang('common.placeholder.all');
            nodes.unshift(data);
        }
        return nodes;
    }

    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();// 下拉框

    /**
     * 交易类型回调函数
     * @param data
     */
    tranTypeCallback = function(data){
        var url = basePath + "/foreign/fexproduct/listProductAndGroup?tranType=" + data.value;
        selectTypes.get('eqs_proId').attr("data-url", url);
    }
    dicts = bt.dicts.init();

    // 生成列表
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/foreign/fexcorpbankacc/approveTables"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.corpId)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return full.corpName;
                    }
                }
            },
            {data:"",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.tranType)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return bt.dict("fexCorp.tranType",full.tranType);
                    }
                }
            },
            {data:"proName",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.proId)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return full.proName;
                    }
                }
            },
            {data:"oppCorpName",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.oppCorpId)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return full.oppCorpName;
                    }
                }
            },
            {data:"",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.cashFlowType)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return bt.dict("fexCorp.cashFlowType",full.cashFlowType);
                    }
                }
            },
            {data:"curName",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.curId)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return full.curName;
                    }
                }
            },
            {data:"",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.cdSign)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return bt.dict("fexCorp.cdSign",full.cdSign);
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
              	if($("#flowStatus").val()=='todo'){
              		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
              	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
              	return html;
              }}
        ]
    });
    bt.workFlow.beforeSubmit = function(submitData){   
    	var data = bt.datatable.getSelectData("#appTable");
        if(data.length < 1){
        	layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 2});
        	return;
        }
    	var ids = []; 
    	var taskIds = []; 
    	// 遍历行数据
    	$.each(data,function(i, tr) {
    		taskIds.push(tr.taskId);
    	});
    	bt.workFlow.taskList = taskIds;
    	return true;
    }
});

/**
 * 审核
 * @param el
 */
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/foreign/fexcorpbankacc/viewApprove?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorpbankacc/viewApprove?id=" + data.id + "&taskId=" + data.taskId
    });
}

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}