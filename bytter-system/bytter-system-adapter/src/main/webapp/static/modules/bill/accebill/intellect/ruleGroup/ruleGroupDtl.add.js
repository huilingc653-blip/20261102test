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
        ignoreButtons: ['colvis', 'print', 'state', 'output'],
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                text: bt.lang("common.button.add"),
                action: function (e, dt, node, config) {
                    add();
                }
            }
        ],
        ajax: {
            'url': basePath + "/bill/intellect/ruleGroupDtl/datatable"
        }
        , columns: [
            {data: "dtlGroupCode", className: "center"},
            {data: "ruleName", className: "center"},
            {data: "ruleRmk", className: "center"},
            {
                data: null, className: "center", render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(\'' + data.id + '\')">' + bt.lang("common.button.delete") + '</a>';
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="view(\'' + data.ruleId + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    form.on("submit(submit)", function (data) {
        bt.loading.show();
        $.post(basePath + "/bill/intellect/rule/update", data.field, function (result) {
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
 * 新增选票规则组明细
 */
function add() {
    var applyNo = $('#applyNo').val()
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/bill/intellect/ruleGroupDtl/add/' + applyNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + '/bill/intellect/ruleGroupDtl/add' + applyNo,
        end: function () {
            table && table.draw();
        }
    });
}

/**
 * 删除选票规则组明细
 * @param id
 */
function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/intellect/ruleGroupDtl/del", {id: id}, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 选票规则组明细详情页
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