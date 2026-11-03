var dicts, selectType, form, moneyType;
bt.use(['form', 'dicts', 'element',"selectType", 'datatable', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();

    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    selectType = bt.selectType.init();

    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: false,
        checkbox: true,
        ignoreButtons: ['colvis', 'print', 'state', 'output'],
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                text: bt.lang("新增规则"),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang("bill.intellect.btn.moveUp"),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    moveUpOrDown(data, true);
                }
            },
            {
                text: bt.lang("bill.intellect.btn.moveDown"),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    moveUpOrDown(data, false);
                }
            },
        ],
        ajax: {
            'url': basePath + "/bill/intellect/ruleGroupDtl/datatable"
        }
        , columns: [
            {data: "serialNo", className: "center"},
            // {data: "dtlGroupCode", className: "center"},
            {data: "ruleName", className: "center"},
            {data: "ruleRmk", className: "center"},
            {
                data: null, className: "center", render: function (data, type, full, meta) {
                    var html = '';
                    // html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(\'' + data.dtlGroupCode + '\')">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="view(\'' + data.ruleId + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    form.on("submit(submit)", function (data) {
        bt.loading.show();
        $.post(basePath + "/bill/intellect/ruleGroup/update", data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.refreshParent();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                }
            }
            bt.loading.hide();
        }, 'JSON');
    });

});

/**
 * 上移或下移
 * @param data 选择的数据
 * @param flag 上移true，下移false
 */
function moveUpOrDown(data, flag){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);

    $.post(basePath + "/bill/intellect/ruleGroupDtl/moveUpOrDown", {id: rowData[0].id, moveFlag: flag, applyNo: $('#applyNo').val()}, function(result){
        datatable && datatable.draw();
        parent.layer.alert(result.msg, {icon: result.success?1:2});
    },'JSON');
}

/**
 * 编辑选票规则组明细
 */
function edit(dtlGroupCode) {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.edit"), basePath + "/bill/intellect/ruleGroupDtl/edit/" + dtlGroupCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ["850px", "530px"],
        maxmin: true,
        content: basePath + "/bill/intellect/ruleGroupDtl/edit/dtlGroupCode/" + dtlGroupCode,
        end: function () {
            table && table.draw();
        }
    });
}


/**
 * 选择选票规则组明细
 */
function add() {
    var applyNo = $('#applyNo').val()
    layer.open({
        type: 2,
        content: basePath + "/bill/intellect/ruleGroup/selectRule",
        btn: ["确定", "关闭"],
        btnAlign: "c",
        area: ["900px","1200px"],
        yes: function (index, layero) {
            var iframeWin = window[layero.find('iframe')[0]['name']];
            var selectData = iframeWin.getSelectData();
            var ruleNames = [], ruleIds = []
            for (var i = 0; i < selectData.length; i++) {
                ruleNames.push(selectData[i].ruleName)
                ruleIds.push(selectData[i].id)
            }
            $.post(basePath + "/bill/intellect/ruleGroupDtl/save", {ruleNames:ruleNames.join(','), ruleIds:ruleIds.join(','),
                    applyNo: applyNo},
                function (result) {
                    datatable && datatable.draw();
                }, 'JSON');
            layer.close(index);
        },
        close: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 删除选票规则组明细
 * @param el
 */
function del(el){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        $.post(basePath + "/bill/intellect/ruleGroupDtl/del", {id: data.id, serialNo: data.serialNo, applyNo: $('#applyNo').val()}, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 选票规则详情页
 */
function view(id) {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.view"), basePath + "/bill/intellect/rule/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ["850px", "530px"],
        maxmin: true,
        content: basePath + "/bill/intellect/rule/view?id=" + id,
        end: function () {
            table && table.draw();
        }
    });
}

function initTools() {
    form.render();
    dicts.render();
    selectType.render();
    form.renderIcon()
}