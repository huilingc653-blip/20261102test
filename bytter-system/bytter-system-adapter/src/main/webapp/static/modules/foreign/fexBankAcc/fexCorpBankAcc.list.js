var datatable,dicts,selectTypes;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','selectType','upload','dicts'], function(){
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

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    /**
     * 交易类型切换：未选时查全部产品(-1)，选中时按交易类型查产品（不含产品组）
     */
    tranTypeCallback = function(data){
        var tranType = data && data.value;
        if(bt.empty(tranType)){
            tranType = '-1';
        }
        selectTypes.get('eqs_proId').attr({
            "data-url": basePath + "/foreign/fexproduct/listProduct/" + tranType,
            "data-defaultvalue": ""
        });
    }

    bt.corp.init();
    selectTypes = bt.selectType.init();// 下拉框
    bt.moneyType.init();
    bt.dateType.init();
    dicts = bt.dicts.init();
    //产品下拉已在 VM 配置 listProduct/-1 + data-defaultvalue，由 selectType.init 进页加载即可；
    //此处再调 tranTypeCallback 会并发二次请求，selectType 用 append 渲染会导致选项重复

    // 生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/foreign/fexcorpbankacc/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('foreign:fex:corp:bankacc:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
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
            {data:"tranType",className:'center',bSortable:false,
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
            {data:"cashFlowType",className:'center',bSortable:false,
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
            {data:"cdSign",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (bt.empty(full.cdSign)) {
                        return bt.lang('common.placeholder.all');
                    } else {
                        return bt.dict("fexCorp.cdSign",full.cdSign);
                    }
                }
            },
            {data:"status",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return bt.dict("fex.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    if(bt.isResource('foreign:fex:corp:bankacc:apply') && full.status!=11 && full.status!=95 && full.status!=-3){//更新和删除权限
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('foreign:fex:corp:bankacc:apply') && full.status==95){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="makeInvalid(this)">'+bt.lang("table.fexForeign.invalid")+'</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("netbank.button.change")+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('foreign:fex:corp:bankacc:apply') && full.status==-3){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="makeEnable(this)">'+bt.lang("table.fexForeign.enable")+'</a><span class="text-explode"></span>';
                    }
                    if(bt.isResource('foreign:fex:corp:bankacc:query')){//查看
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

/**
 * 新增
 */
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/foreign/fexcorpbankacc/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorpbankacc/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

/**
 * 编辑
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcorpbankacc/edit?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexcorpbankacc/edit?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

/**
 * 删除行
 * @param el
 */
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/foreign/fexcorpbankacc/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 作废
 * @param el
 */
function makeInvalid(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('fex.message.confirm.invalid'),function(){
        $.post(basePath + "/foreign/fexcorpbankacc/makeInvalid/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 启用
 * @param el
 */
function makeEnable(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('fex.message.confirm.enable'),function(){
        $.post(basePath + "/foreign/fexcorpbankacc/makeEnable/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 查看
 * @param el
 */
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorpbankacc/view?id=" +data.id
    });
}
