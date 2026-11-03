var dicts,selectTypes,allStr;
bt.use(['form', 'datatable','selectType','corp','dicts'], function () {
    $ = bt.jquery, form = bt.form();
    selectTypes=bt.selectType.init();
    bt.corp.init();
    dicts = bt.dicts.init();
    allStr=bt.lang("common.placeholder.all");//全部
    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }
    //产品下拉已在 VM 配置 listProduct/-1 + data-defaultvalue，由 selectType.init 进页加载即可；
    //此处再调 changeTranType 会并发二次请求，selectType 用 append 渲染会导致选项重复
    /**初始化数据表，组件:datatable;  begin*/
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexsettlefillrule/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "corpName", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.corpName)){
                        return allStr;
                    }else{
                        return full.corpName;
                    }
                }
            },//单位
            {data: "cashFlowMethod", bSortable: true,className: 'center',render:function (data, type, full, meta) {//现金流形式
                if(bt.empty(full.cashFlowMethod)){
                    return allStr;
                }else{
                    return bt.dict("cashFlow.type", full.cashFlowMethod);
                }

                }},
            {data: "tranType", bSortable: true,className: 'center',render:function (data, type, full, meta) {//交易类型
                    if(bt.empty(full.tranType)){
                        return allStr;
                    }else{
                        return bt.dict("foreign.tranGroupRule.tranType", full.tranType);
                    }
                }},
            {data: "proName", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.proName)){
                        return allStr;
                    }else{
                        return full.proName;
                    }
                }
            },//产品名称
            {data: "cotpName", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.cotpName)){
                        return allStr;
                    }else{
                        return full.cotpName;
                    }
                }
            },//交易对手名称
            {data: "countryName", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.countryName)){
                        return allStr;
                    }else{
                        return full.countryName;
                    }
                }
            },//交易对手国家
            {data: "curEnCode", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.curEnCode)){
                        return allStr;
                    }else{
                        return full.curEnCode;
                    }
                }
            },//币种
            {data: "voucherName", bSortable: true,className: 'center',
                render:function (data, type, full, meta){
                    if(bt.empty(full.voucherName)){
                        return allStr;
                    }else{
                        return full.voucherName;
                    }
                }
            },//支付方式
            {data: "cdSign", bSortable: true,className: 'center',render:function (data, type, full, meta) {//收付方向
                        if(bt.empty(full.cdSign)){
                            return allStr;
                        }else{
                            return bt.dict("receiptAndPayment.direction", full.cdSign);
                        }
                }},
            {data: "validSign", bSortable: true,className: 'center',render:function (data, type, full, meta) {
                    if(bt.empty(full.validSign)){
                        return allStr;
                    }else{
                        return bt.dict("valid_sign", full.validSign);
                    }
                }},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
    /**下拉表格  end*/
});


/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexsettlefillrule/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexsettlefillrule/add",
        end: function () {
            table && table.draw();
        }
    });
}


/**
 * 查询
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.viewDtl'), basePath + "/foreign/fexsettlefillrule/view/" + data.id);
            return;
        }
    }
}


/**
 * 更新
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexsettlefillrule/update/" + data.id);
            return;
        }
    }
}


/**
 * 删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    $.post(basePath + "/foreign/fexsettlefillrule/checkRuleUse/" + data.id, function(result){
        if(result){//规则被引用，不允许删除
            layer.alert(bt.lang('foreign.rule.inUse'));
            return;
        }else{
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                $.post(basePath + "/foreign/fexsettlefillrule/delete/" + data.id, function (result) {
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }
    },'JSON');

}

/**
 * 根据交易类型查找产品
 * @param data
 */
function changeTranType(data){
    var tranType=data.value;
    //未选择交易类型时查全部产品(-1)；选中时按交易类型查产品
    if(bt.empty(tranType) || tranType == '0' || tranType == 0){
        tranType = '-1';
    }
    //带上data-defaultvalue，确保attr重渲染时会立刻请求接口（不能依赖data-biz-name）
    selectTypes.get("eqs_proId").attr({
        "data-url": basePath + "/foreign/fexproduct/listProduct/" + tranType,
        "data-defaultvalue": ""
    });
}