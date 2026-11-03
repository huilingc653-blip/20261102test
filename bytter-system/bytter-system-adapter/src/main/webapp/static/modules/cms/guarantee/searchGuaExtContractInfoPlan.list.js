var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    mulselects=bt.mulselect.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        scrollY:'600px',
        order: [[9,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/guaExtContractInfo/searchdatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", className: 'center', bSortable: true},
            {data: "guaNo", className: 'center', bSortable: true},
            {data: "guaTypeName", className: 'center', bSortable: true},
            {data: "creditorPerson", className: 'center', bSortable: true},
            {data: "debtorPersonName", className: 'center', bSortable: true},
            {data: "curName", className: 'center', bSortable: true},
            {
                data: "guaMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.guaMoney);
                }
            },
            {data:"contractCode",className:'center',bSortable:true},
            {data:"startDate",className:'center',bSortable:true},
            {data:"endDate",className:'center',bSortable:true},
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("extContract.status",full.status);
                }
            },
            {data:"drawMoneyDate",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="repayLan(this)">'+bt.lang("cms.button.repayPlan")+'</a><span class="text-explode"></span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                    return html;
                }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7', //汇总列序号
                    dataName: 'guaMoney' //汇总列担保额度
                }
            ]
        }
    });
});

//担保方式占用轨迹
function listMsg(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.guaContract.detailInfo'),basePath + "/cms/guarantee/guaContractInfo/queryEmployDtl/" + data.id);
            return;
        }
    }
}


/**
 * 查看合同信息
 * @param el
 */
function conView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var conId = data.conId;
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/contract/conCommon/conView?id=" + conId
    });
}

/**判断字符 串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}


/**加载还款计划*/
function repayLan(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params = "id=" +data.id;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/guarantee/guaExtContractInfo/toRepayPlan?" + params);
    }
}

//查看担保合同信息
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaExtContractInfo/viewInfo/" +data.id
    });
}
