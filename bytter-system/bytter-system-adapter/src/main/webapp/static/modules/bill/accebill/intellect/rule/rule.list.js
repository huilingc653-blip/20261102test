var datatable,dicts;
bt.use(['form', 'dicts', 'element', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();


    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[0,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [{
            hide: bt.isnotResource("bill:intellect:rule:add"),
            text: bt.lang("common.button.add"),
            action: function (e, dt, node, config) {
                add();
            }
        }],
        ajax: {
            'url': basePath + "/bill/intellect/rule/datatable"
        }
        , columns: [
            {
                data: "ruleCode", className: "center",

            },
            {data: "ruleName", className: "center"},
            {data: "rmk", className: "center"},
            {
                data: "validSign", className: "center", render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", data);
                }
            },
            {
                data: null, className: "center", render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource("bill:intellect:rule:edit")) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(\'' + data.id + '\')">' + bt.lang("common.button.edit") + '</a>';
                    }
                    if (bt.isResource("bill:intellect:rule:del")) {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="del(\'' + data.id + '\')">' + bt.lang("common.button.delete") + '</a>';
                    }
                    html += '<span class="text-explode">|</span>'
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(\'' + data.id + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

/**
 * 新增选票规则
 */
function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/bill/intellect/rule/add');
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + '/bill/intellect/rule/add',
        end: function () {
            table && table.draw();
        }
    });
}

/**
 * 删除选票规则
 * @param id
 */
function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/intellect/rule/del/" + id, {}, function(result){
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
        content: basePath + "/bill/intellect/rule/view/" + id,
        end: function () {
            table && table.draw();
        }
    });
}

/**
 * 新增选票规则
 */
function edit(id) {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.edit"), basePath + "/bill/intellect/rule/edit/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ["850px", "530px"],
        maxmin: true,
        content: basePath + "/bill/intellect/rule/edit/" + id,
        end: function () {
            table && table.draw();
        }
    });
}