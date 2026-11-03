bt.use(['form', 'datatable','select','dicts','selectType'], function () {
    $ = bt.jquery, form = bt.form();

    var selectType = bt.selectType.init();

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
            'url': basePath + "/foreign/fextrangrouprule/datatable"
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
            {data: "ruleName", bSortable: true,className: 'center'},
            {data: "tranType", bSortable: true,className: 'center',render:function (data, type, full, meta) {
                    return bt.dict("fexCorp.tranType", full.tranType);
                }},
            {data: "proName", bSortable: true,className: 'center'},
            {data: "validSign", bSortable: true,className: 'center',render:function (data, type, full, meta) {
                    if (full.validSign == '1') {
                        return bt.lang("nis.sc_yn.yes");
                    } else {
                        return bt.lang("nis.sc_yn.no");
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
    /**初始化数据表  end*/

    /**产品回调begin*/
    bt.changePro = function(data){
        // selectType.get("proId").attr({"data-url":basePath+"/foreign/fexproduct/listProduct/"+data.value});
        var url = '';
        if (data.value == '9'){ //查询全部的产品
            url = basePath+"/foreign/fexproduct/listProduct/"+" ";
        } else if (bt.empty(data.value)){
            url = basePath+"/foreign/fexproduct/listProduct/-1";
        } else {
            url = basePath+"/foreign/fexproduct/listProduct/" + data.value;
        }
        selectType.get('proId').attr({"data-url":url,"data-defaultValue":""});
        form.render();
    };
    //重新渲染回调
    bt.dicts.init();
    /**产品回调end*/
});


/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextrangrouprule/edit");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextrangrouprule/edit",
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
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextrangrouprule/view/" + data.id
    });
}


/**
 * 更新
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fextrangrouprule/edit?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fextrangrouprule/edit?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}


/**
 * 删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fextrangrouprule/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
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