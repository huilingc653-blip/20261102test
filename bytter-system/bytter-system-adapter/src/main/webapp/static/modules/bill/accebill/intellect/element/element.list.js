var datatable,dicts;
bt.use(['form', 'dicts', 'element', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();


    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [{
            hide: bt.isnotResource("bill:intellect:element:add"),
            text: bt.lang("common.button.add"),
            action: function (e, dt, node, config) {
                add();
            }
        }],
        ajax: {
            'url': basePath + "/bill/intellect/element/datatable"
        }
        , columns: [
            {
                data: "elementCode", className: "center",

            },
            {data: "elementName", className: "center"},
            {data: "rmk", className: "center"},
            {
                data: "validSign", className: "center", render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", data);
                }
            },
            {
                data: null, className: "center", render: function (data, type, full, meta) {
                    var html = '';
                    if (full.createBy !== '' && full.createBy !== undefined && full.createBy !== null) {
                        if (bt.isResource("bill:intellect:element:edit")) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(\'' + data.id + '\')">' + bt.lang("common.button.edit") + '</a>';
                        }
                        if (bt.isResource("bill:intellect:element:del")) {
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="del(\'' + data.id + '\')">' + bt.lang("common.button.delete") + '</a>';
                        }
                        html += '<span class="text-explode">|</span>'
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(\'' + data.id + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

/**
 * 新增选票要素
 */
function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/bill/intellect/element/add');
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + '/bill/intellect/element/add',
        end: function () {
            table && table.draw();
        }
    });
}

/**
 * 删除选票要素
 * @param id
 */
function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/intellect/element/del", {id: id}, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 查看选票要素
 */
function view(id) {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.view"), basePath + "/bill/intellect/element/view?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ["850px", "530px"],
        maxmin: true,
        content: basePath + "/bill/intellect/element/view?id=" + id,
        end: function () {
            table && table.draw();
        }
    });
}


/**
 * 新增选票要素
 */
function edit(id) {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.edit"), basePath + "/bill/intellect/element/edit?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ["850px", "530px"],
        maxmin: true,
        content: basePath + "/bill/intellect/element/edit?id=" + id,
        end: function () {
            table && table.draw();
        }
    });
}