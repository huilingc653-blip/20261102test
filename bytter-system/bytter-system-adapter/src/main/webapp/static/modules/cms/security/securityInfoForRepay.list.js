bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp","dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间
    bt.moneyType.init();
    corp = bt.corp.init();//单位

    /**
     * 生成列表
     */
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        scrollY:'300px',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData:"false"
        },
        ajax: {
            'url': basePath + "/cms/security/repaySure/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data: "securityTypeName",className:'center', bSortable: true},
            {data: "corpName",className:'center', bSortable: true},
            {data:"securityCode",className:'center',bSortable:true},
            {data:"securitySname",className:'center',bSortable:true},
            {data: "securityName",className:'center', bSortable: true},
            {data:"curName",className:'center',bSortable:true},
            {data:"money",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.money);
                }},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("lease.status", full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('cms:security:repay:sure:op')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("还款确认") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("还款明细") + '</a>';

                    return html;
                }}
        ]
    });
});


function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('还款确认'),basePath + "/cms/security/repaySure/toRepay/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/security/repaySure/toRepay/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('还款明细'),basePath + "/cms/security/repaySure/viewRepay/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/security/repaySure/viewRepay/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}
