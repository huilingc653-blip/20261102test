var select;
bt.use(['form', 'datatable', 'dicts', 'btImport','selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    select = bt.selectType.init();
    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/agreeddeposit/fcagreeddeposit/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:agreeddeposit:fcagreeddeposit:reg:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        }
        , columns: [
        {data: "netName", bSortable: true, className: 'center'},
        {data: "corpName", bSortable: true, className: 'center'},
        {data: "blnctrAcc", bSortable: true, className: 'center'},
        {data: "accName", bSortable: true, className: 'center'},
        {data: "curName", bSortable: true, className: 'center'},
        {data: "contractNo", bSortable: true, className: 'center'},
        {data: "beginDate", bSortable: true, className: 'center'},
        {data: "endDate", bSortable: true, className: 'center'},
        {
            data: "status", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                return bt.dict("agreed.status", full.status);
            }
        },
        {
            data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
            var html = '';
            if(bt.isResource('netbank:agreeddeposit:fcagreeddeposit:reg:apply',full.corpId) && (full.status == 0 || full.status == -1)){//更新和删除权限
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
            }
            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
            return html;
            }
        }
    ]
    });

    /**
     * 资金池回调
     * @param data
     */
    netIdCallback = function (data) {
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/agreeddeposit/fcagreeddeposit?listUserCorpPoolChilds&poolId=" + data.id+"&opType=0&checkSign=0";
        bt.selectType.get("eqs_corpId").attr("data-url", corpPoolChildUrl);
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/agreeddeposit/fcagreeddeposit/add/");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/fcagreeddeposit/add/",
        end: function () {
            table && table.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/fcagreeddeposit/view/0/"+ data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/agreeddeposit/fcagreeddeposit/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: updateUrl + "id=" + data.id,
            end: function () {
                table && table.draw();
            }
        });
    }
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/agreeddeposit/fcagreeddeposit/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


//资金池回调
var getNetIdCallback = function (data) {
    netIdCallback(data);
}

//监听删除标志
$('body').on('click','.resetBtn',function () {
    select.get("eqs_corpId").attr("data-url","");
});