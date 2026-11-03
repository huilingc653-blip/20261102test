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
            'url': basePath + "/agreeddeposit/fcagreeddeposit/datatableCheck"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns:[
            {data: "netName", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "blnctrAcc", bSortable: true, className: 'center'},
            {data: "accName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true, className: 'center'},
            {data: "contractNo", bSortable: true, className: 'center'},
            {data: "beginDate", bSortable: true, className: 'center'},
            {data: "endDate", bSortable: true, className: 'center'},
            {data: "userName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center' , render: function (data, type, full, meta) {
                var html = '';
                if(bt.isResource('netbank:agreeddeposit:fcagreeddeposit:regCheck:apply',full.corpId) && full.status == 11){//通过和打回权限
                    /*html += '<a class="" href="#" data-row="' + meta.row + '" onclick="pass(this)">' + bt.lang("common.button.pass") +
                        '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="back(this)">' + bt.lang("common.button.back") +
                        '</a><span class="text-explode">|</span>';*/
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a><span class="text-explode">|</span>';
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
        var corpPoolChildUrl = basePath + "/agreeddeposit/fcagreeddeposit?listUserCorpPoolChilds&poolId=" + data.id+"&opType=0&checkSign=1";
        bt.selectType.get("eqs_corpId").attr("data-url", corpPoolChildUrl);
    }
});

function check(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.check.data'), basePath + "/agreeddeposit/fcagreeddeposit/check/"+data.busType+"/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/agreeddeposit/fcagreeddeposit/check/"+data.busType+"/" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}

function pass(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang("common.message.confirm.check"),function(){
        $.get(basePath + "/agreeddeposit/fcagreeddeposit/op/95/0/"+ data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function back(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang("common.message.confirm.back"),function(){
        $.get(basePath + "/agreeddeposit/fcagreeddeposit/op/-1/0/"+ data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/fcagreeddeposit/view/0/" + data.id
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